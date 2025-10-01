# Build secrets

These are the secrets used to build and release the app.

- `MUSICSEARCH_GHA_HELPER_APP_ID`: From GitHub Apps app page (e.g. https://github.com/settings/apps/musicsearch-gha-helper)
- `MUSICSEARCH_GHA_HELPER_APP_PRIVATE_KEY`:
- `ORG_GRADLE_PROJECT_MUSICBRAINZ_CLIENT_ID`: 
- `ORG_GRADLE_PROJECT_MUSICBRAINZ_CLIENT_SECRET`: 
- `ORG_GRADLE_PROJECT_SPOTIFY_CLIENT_ID`: 
- `ORG_GRADLE_PROJECT_SPOTIFY_CLIENT_SECRET`:
- `PAT`: GitHub personal access token with push access (from https://github.com/settings/personal-access-tokens)
- `RELEASE_KEYSTORE_JKS_BASE64`: The base64 encoding of `release-keystore.jks` used for publishing our app
- `RELEASE_STORE_PASSWORD`: The store password to `release-keystore.jks`
- `RELEASE_KEY_PASSWORD`: The key password to `release-keystore.jks`
- `GOOGLE_SERVICES_JSON_BASE64`: The base64 encoding of `google-services.json` used for Firebase
- `WORKLOAD_IDENTITY_PROVIDER`: See https://github.com/google-github-actions/auth#authenticating-via-workload-identity-federation-1
- `SERVICE_ACCOUNT`: See https://github.com/google-github-actions/auth#authenticating-via-workload-identity-federation-1
- `SIGNING_KEY`: Conveyor signing key (https://github.com/hydraulic-software/conveyor/blob/master/actions/build/README.md)
- `APP_STORE_API_KEY_JSON_BASE64`: The base64 encoding of `app_store_api_key.json`
- `GOOGLE_SERVICE_INFO_PLIST_BASE64`: The base64 encoding of `GoogleService-Info.plist`
- `MATCH_PASSWORD`: [Fastlane Match](https://docs.fastlane.tools/actions/match/) password to access private repository with iOS distribution certificates and provisioning profiles