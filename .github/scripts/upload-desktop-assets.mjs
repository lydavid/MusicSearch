#!/usr/bin/env node
// Upload desktop distribution assets to a GitHub Release using the release-asset
// *multipart* upload API.
//
// Why: semantic-release's @semantic-release/github uploads every asset in one
// shot (octokit's `upload-release-asset`). GitHub's `uploads.github.com` returns
// HTTP 500 / HTTP-2 stream resets for large binaries (e.g. the ~85 MB macOS zip),
// which fails the whole release. Uploading the heavy archives through GitHub's
// documented multipart flow avoids that single-shot path.
//
// Env:
//   GITHUB_REPOSITORY   owner/repo               (default: env.GITHUB_REPOSITORY)
//   GITHUB_UPLOAD_TOKEN contents:write token     (default: env.GITHUB_TOKEN)
//   RELEASE_TAG         e.g. v1.135.0            (required)
//   OUTPUT_DIR          artifacts directory       (default: ./output)
//   MIN_PART_SIZE       bytes per part            (default: 5242880)
//   MAX_RETRIES         per-attempt retries       (default: 6)
//
// Exits non-zero on any failure.

import { readdir, stat, open } from 'node:fs/promises';
import { existsSync } from 'node:fs';
import path from 'node:path';

const repo = process.env.GITHUB_REPOSITORY;
const token = process.env.GITHUB_UPLOAD_TOKEN ?? process.env.GITHUB_TOKEN;
const tag = process.env.RELEASE_TAG;
const outputDir = process.env.OUTPUT_DIR ? path.resolve(process.env.OUTPUT_DIR) : path.resolve('output');
const minPartSize = Math.max(1, Number(process.env.MIN_PART_SIZE || 5 * 1024 * 1024) | 0);
const maxRetries = Math.max(1, Number(process.env.MAX_RETRIES || 6) | 0);
const apiBase = (process.env.GITHUB_API_URL || 'https://api.github.com').replace(/\/$/, '');

function die(msg, code = 2) {
  console.error(`ERROR: ${msg}`);
  process.exit(code);
}
if (!repo || !token) die('set GITHUB_REPOSITORY and GITHUB_UPLOAD_TOKEN (or GITHUB_TOKEN).');
if (!tag) die('set RELEASE_TAG (e.g. v1.135.0).');
if (!existsSync(outputDir)) die(`output dir not found: ${outputDir}`);

const [owner, name] = repo.split('/');

async function withRetry(fn, label) {
  let lastErr;
  for (let attempt = 1; attempt <= maxRetries; attempt++) {
    try {
      return await fn();
    } catch (err) {
      lastErr = err;
      console.error(`${label}: attempt ${attempt}/${maxRetries} failed: ${err.message ?? String(err)}`);
      await new Promise((r) => setTimeout(() => r(), Math.min(1000 * attempt, 15000)));
    }
  }
  throw lastErr;
}

function ghHeaders(extra = {}) {
  return {
    Authorization: `Bearer ${token}`,
    Accept: 'application/vnd.github+json',
    'X-GitHub-Api-Version': '2022-11-28',
    ...extra,
  };
}

// 1. Resolve the release id from the tag.
const releaseId = await withRetry(async () => {
  const url = `${apiBase}/repos/${owner}/${name}/releases/tags/${encodeURIComponent(tag)}`;
  const res = await fetch(url, { headers: ghHeaders(), signal: AbortSignal.timeout(120000) });
  if (!res.ok) throw new Error(`GET release by tag ${tag} -> ${res.status}`);
  const data = await res.json();
  if (!data || data.id == null) throw new Error('release id missing in response');
  return data.id;
}, 'GET release by tag');

// 2. Create a pre-signed upload for one file.
function createUpload(releaseId, filename) {
  const url = `${apiBase}/repos/${owner}/${name}/releases/${releaseId}/uploads?name=${encodeURIComponent(filename)}`;
  return withRetry(async () => {
    const res = await fetch(url, {
      method: 'POST',
      headers: ghHeaders({ 'Content-Length': '0' }),
      signal: AbortSignal.timeout(120000),
    });
    if (!res.ok) throw new Error(`POST create upload -> ${res.status}`);
    const data = await res.json();
    if (!data || !data.url) throw new Error('create upload returned no url');
    return data;
  }, `create upload ${filename}`);
}

// GitHub returns a URL like:
//   https://uploads.github.com/releases/{id}/uploads/{uuid}?{s3signed}&name={name}&filename={filename}
// Extract the S3 upload id from the "/uploads/{uuid}" path segment.
function extractUploadId(uploadUrl) {
  try {
    const parts = new URL(uploadUrl).pathname.split('/');
    const i = parts.indexOf('uploads');
    if (i !== -1 && parts[i + 1]) return parts[i + 1];
  } catch { /* ignore */ }
  return null;
}

function fillUrl(uploadUrl, filename) {
  const enc = encodeURIComponent(filename);
  return uploadUrl
    .replace(/%7Bname%7D/g, enc)
    .replace(/{name}/g, enc)
    .replace(/%7Bfilename%7D/g, enc)
    .replace(/{filename}/g, enc);
}

// 3. PUT one part. Returns the raw (quoted) ETag.
async function uploadPart(uploadUrl, filename, buffer, contentType, partNumber) {
  const u = new URL(fillUrl(uploadUrl, filename));
  u.searchParams.set('partNumber', String(partNumber));
  return withRetry(async () => {
    const res = await fetch(u.toString(), {
      method: 'PUT',
      headers: { 'Content-Type': contentType, 'Content-Length': String(buffer.length) },
      body: buffer,
      signal: AbortSignal.timeout(120000),
    });
    if (!res.ok) {
      const body = await res.text().catch(() => '');
      throw new Error(`${res.status}: ${body.slice(0, 200)}`);
    }
    const etag = res.headers.get('etag');
    if (!etag) throw new Error('missing ETag on part upload');
    return etag;
  }, `part ${partNumber} ${path.basename(filename)}`);
}

// 4. Complete the multipart upload.
async function completeUpload(uploadUrl, filename, parts) {
  const u = new URL(fillUrl(uploadUrl, filename));
  if (!u.searchParams.has('UploadId')) {
    const uploadId = extractUploadId(uploadUrl);
    if (uploadId) u.searchParams.set('UploadId', uploadId);
  }
  const body = '<CompleteMultipartUpload>\n' +
    parts.map((p) => `  <Part>\n    <PartNumber>${p.partNumber}</PartNumber>\n    <ETag>${p.etag}</ETag>\n  </Part>`).join('\n') +
    '\n</CompleteMultipartUpload>';
  await withRetry(async () => {
    const res = await fetch(u.toString(), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/xml', 'Content-Length': String(Buffer.byteLength(body)) },
      body,
      signal: AbortSignal.timeout(120000),
    });
    if (!res.ok) {
      const text = await res.text().catch(() => '');
      throw new Error(`${res.status}: ${text.slice(0, 400)}`);
    }
  }, `complete upload ${path.basename(filename)}`);
}

async function uploadFile(releaseId, filePath) {
  const filename = path.basename(filePath);
  const meta = await createUpload(releaseId, filename);
  const contentType = meta.content_type || 'application/octet-stream';
  const st = await stat(filePath);
  const handle = await open(filePath, 'r');
  try {
    const parts = [];
    let offset = 0;
    let partNumber = 1;
    const buffer = Buffer.alloc(minPartSize);
    while (offset < st.size) {
      const { buffer: buf, bytesRead } = await handle.read(buffer, 0, Math.min(minPartSize, st.size - offset), offset);
      const etag = await uploadPart(meta.url, filename, buf, contentType, partNumber);
      parts.push({ partNumber, etag });
      offset += bytesRead;
      partNumber++;
    }
    if (st.size === 0) {
      // GitHub still requires at least one part for an empty object.
      await uploadPart(meta.url, filename, Buffer.alloc(0), contentType, 1);
      parts.push({ partNumber: 1, etag: '""' });
    }
    await completeUpload(meta.url, filename, parts);
    console.log(`uploaded ${filename} (${st.size} bytes, ${parts.length} part(s))`);
  } finally {
    await handle.close();
  }
}

const files = [];
for (const entry of await readdir(outputDir, { withFileTypes: true })) {
  if (entry.isFile()) files.push(path.join(outputDir, entry.name));
}
if (files.length === 0) {
  console.error(`ERROR: no files to upload in ${outputDir}`);
  process.exit(2);
}

let ok = 0;
for (const f of files) {
  try {
    await uploadFile(releaseId, f);
    ok++;
  } catch (err) {
    console.error(`FAILED to upload ${path.basename(f)}: ${err.message ?? String(err)}`);
    process.exit(1);
  }
}
console.log(`Desktop upload complete: ${ok}/${files.length} file(s).`);
