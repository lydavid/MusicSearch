#!/usr/bin/env node
// Local test for upload-desktop-assets.mjs. Spins up a mock "GitHub" server that
// mimics the release-asset multipart protocol, then runs the real uploader
// against it and asserts the parts were received and the upload completed.
import http from 'node:http';
import { mkdtemp, writeFile, mkdir } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import path from 'node:path';
import { fileURLToPath, pathToFileURL } from 'node:url';
import { spawn } from 'node:child_process';

const here = path.dirname(fileURLToPath(import.meta.url));
const UPLOADER = path.join(here, 'upload-desktop-assets.mjs');

// --- Build a fake output dir -----------------------------------------------
const outDir = await mkdtemp(path.join(tmpdir(), 'dsa-'));
await writeFile(path.join(outDir, 'tiny.txt'), 'hello');
await writeFile(path.join(outDir, 'big.zip'), Buffer.alloc(12 * 1024 * 1024, 7)); // 12 MB, 3 parts @ 5MiB

// --- Mock GitHub server ----------------------------------------------------
const receivedParts = {}; // filename -> { partNumber: etag }
let completed = {};

const server = http.createServer((req, res) => {
  const u = new URL(req.url, 'http://localhost');
  const seg = u.pathname.split('/');

  if (req.method === 'GET' && seg.at(-2) === 'tags') {
    return json(res, 200, { id: 123, tag_name: 'vTEST' });
  }
  if (req.method === 'POST' && /\/releases\/123\/uploads$/.test(u.pathname) && u.searchParams.has('name')) {
    const name = u.searchParams.get('name');
    // Pre-signed URL the uploader fills in and re-PUTs to; embeds an UploadId in
    // the "/uploads/{uuid}" path segment and {name}/{filename} placeholders.
    const base = `http://localhost:${server.address().port}/releases/123/uploads/UPLOADID-123`;
    return json(res, 201, { url: `${base}?name={name}&filename={filename}`, content_type: 'application/octet-stream' });
  }
  if (req.method === 'PUT' && u.searchParams.has('partNumber')) {
    let data = '';
    req.on('data', (c) => (data += c));
    req.on('end', () => {
      const fn = decodeURIComponent(u.searchParams.get('name'));
      receivedParts[fn] = receivedParts[fn] || {};
      receivedParts[fn][u.searchParams.get('partNumber')] = `"<etag:${data.length}>"`;
      res.setHeader('ETag', receivedParts[fn][u.searchParams.get('partNumber')]);
      json(res, 200, {});
    });
    return;
  }
  if (req.method === 'PUT' && u.searchParams.has('UploadId') && !u.searchParams.has('partNumber')) {
    let data = '';
    req.on('data', (c) => (data += c));
    req.on('end', () => {
      const fn = decodeURIComponent(u.searchParams.get('name'));
      const m = data.match(/<PartNumber>(\d+)<\/PartNumber>/g);
      completed[fn] = { body: data, parts: m ? m.length : 0 };
      json(res, 200, {});
    });
    return;
  }
  json(res, 404, { message: 'unexpected: ' + req.method + ' ' + u.pathname });
});

function json(res, status, obj) {
  res.writeHead(status, { 'Content-Type': 'application/json' });
  res.end(JSON.stringify(obj));
}

await new Promise((r) => server.listen(0, r));
const port = server.address().port;

// --- Run the real uploader against the mock --------------------------------
const env = {
  ...process.env,
  GITHUB_REPOSITORY: 'lydavid/MusicSearch',
  GITHUB_UPLOAD_TOKEN: 'test-token',
  RELEASE_TAG: 'vTEST',
  OUTPUT_DIR: outDir,
  GITHUB_API_URL: `http://localhost:${port}`,
  MIN_PART_SIZE: String(5 * 1024 * 1024),
  MAX_RETRIES: '2',
};

const exitCode = await new Promise((resolve) => {
  const child = spawn(process.execPath, [UPLOADER], { env, stdio: 'inherit' });
  child.on('close', resolve);
});

server.close();

// --- Asserts ---------------------------------------------------------------
let failures = 0;
function check(cond, msg) {
  console.log(`${cond ? 'PASS' : 'FAIL'}: ${msg}`);
  if (!cond) failures++;
}

check(exitCode === 0, `uploader exited 0 (got ${exitCode})`);
check(!!receivedParts['big.zip'], 'big.zip parts were received');
check((receivedParts['big.zip']?.['1'] ?? null) != null, 'big.zip part 1 received');
check((receivedParts['big.zip']?.['3'] ?? null) != null, 'big.zip part 3 received (12MB => 3 parts @ 5MiB)');
check((receivedParts['tiny.txt']?.['1'] ?? null) != null, 'tiny.txt single part received');
check(!!completed['big.zip']?.body && completed['big.zip'].parts === 3, 'big.zip completion XML has 3 parts');
check(!!completed['tiny.txt']?.body && completed['tiny.txt'].parts === 1, 'tiny.txt completion XML has 1 part');
check(completed['big.zip'].body.startsWith('<CompleteMultipartUpload>'), 'completion body starts with CompleteMultipartUpload');
check(completed['big.zip'].body.includes('<ETag>'), 'completion body carries ETags');

console.log(failures === 0 ? '\nALL TESTS PASSED' : `\n${failures} TEST(S) FAILED`);
process.exit(failures === 0 ? 0 : 1);
