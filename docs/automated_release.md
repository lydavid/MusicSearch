# Automated Release

## Beta
Every night, we have a [workflow](../.github/workflows/publish-beta.yml) that tries to publish the app to Google Play's beta track and to GitHub as a pre-release.
This workflow has two jobs. The first runs a check to determine whether to exit early, and the second actually does the publishing.

### `check`

Because GitHub Actions (GHA) does not support early exit at the time of writing, we use this workaround to exit early: https://github.com/actions/runner/issues/662#issuecomment-1372855726.

This way, the workflow can determine whether to run the second job that does the heavy lifting based on the output of the first job. In our case, we check whether the previous git commit has the special commit message that this workflow will commit if it runs the publishing job. That is, bumping the version.

### `publish`

We use [semantic release](https://github.com/semantic-release/semantic-release) to determine our version name (eg. 0.11.2) and to generate our GitHub release notes (eg. https://github.com/lydavid/MusicSearch/releases/tag/v0.7.2).
Our version code is just incremented with each run of this job.
These are the only changes that we commit.
We commit them in this job so that we guarantee a unique version, and ensure that the app's version matches the release tag on GitHub (eg. v0.7.2).

#### Secrets

- `PAT`: Since we may git commit with this job, we need to pass in a GitHub Personal Access Token (PAT) that has push access
- `RELEASE_STORE_PASSWORD`: The store password to `release-keystore.jks`
- `RELEASE_KEY_PASSWORD`: The key password to `release-keystore.jks`
- `GOOGLE_SERVICES_JSON_BASE64`: The base64 encoding of `google-services.json` used for Firebase
- `RELEASE_KEYSTORE_JKS_BASE64`: The base64 encoding of `release-keystore.jks` used for publishing our app
- `WORKLOAD_IDENTITY_PROVIDER`: See https://github.com/google-github-actions/auth#authenticating-via-workload-identity-federation-1
- `SERVICE_ACCOUNT`: See https://github.com/google-github-actions/auth#authenticating-via-workload-identity-federation-1

## Production

This step isn't actually completely automated.
We need to manually merge `beta` into `master`.
We intended for [publish_production.yml](../.github/workflows/publish-production.yml) to run, but it does not seem to if we rebase and push.
So, we need to manually run this workflow through https://github.com/lydavid/MusicSearch/actions/workflows/publish-production.yml.
This workflow will publish a new build from master directly to production.
We publish again rather than promote the beta build so that we can change `VERSION_NAME` to match our semantic release version/tag,
which on production will not include `-beta.x` suffix.

Rather than using a GitHub PR, we should rebase `beta` onto `master` using our command line.
Using a PR will either:
- create an unnecessary merge commit when using the "Create a merge commit" option
- hide our commits from `beta` when using "Squash and merge"
- create duplicate commits when using "Rebase and merge"
