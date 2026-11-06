# Build secrets

These are the secrets used to build and release the app. Change them in https://github.com/lydavid/MusicSearch/settings/secrets/actions.

- `APP_STORE_API_KEY_JSON_BASE64`: The base64 encoding of `app_store_api_key.json`
- `GOOGLE_SERVICE_INFO_PLIST_BASE64`: The base64 encoding of `GoogleService-Info.plist`
- `GOOGLE_SERVICES_DEBUG_JSON_BASE64`: The base64 encoding of the debug `google-services.json` used for debug build
- `GOOGLE_SERVICES_JSON_BASE64`: The base64 encoding of `google-services.json` used for Firebase
- `MATCH_PASSWORD`: [Fastlane Match](https://docs.fastlane.tools/actions/match/) password to decrypt the certificates and profiles
- `MUSICSEARCH_GHA_HELPER_APP_ID`: From GitHub Apps app page (e.g. https://github.com/settings/apps/musicsearch-gha-helper). Not used now.
- `MUSICSEARCH_GHA_HELPER_APP_PRIVATE_KEY`: Not used now.
- `ORG_GRADLE_PROJECT_MUSICBRAINZ_CLIENT_ID`: From `not_so_secret.properties`
- `ORG_GRADLE_PROJECT_MUSICBRAINZ_CLIENT_SECRET`: From `not_so_secret.properties`
- `ORG_GRADLE_PROJECT_SPOTIFY_CLIENT_ID`: From `secrets.properties`
- `ORG_GRADLE_PROJECT_SPOTIFY_CLIENT_SECRET`: From `secrets.properties`
- `PAT`: GitHub personal access token with push access to `lydavid/MusicSearch` and read access to `lydavid/musicsearch-fastlane-match` (from https://github.com/settings/personal-access-tokens)
- `RELEASE_KEY_PASSWORD`: The key password to `release-keystore.jks`
- `RELEASE_KEYSTORE_JKS_BASE64`: The base64 encoding of `release-keystore.jks` used for publishing our app
- `RELEASE_STORE_PASSWORD`: The store password to `release-keystore.jks`
- `SERVICE_ACCOUNT`: See https://github.com/google-github-actions/auth#authenticating-via-workload-identity-federation-1
- `SIGNING_KEY`: Conveyor signing key (https://github.com/hydraulic-software/conveyor/blob/master/actions/build/README.md)
- `WORKLOAD_IDENTITY_PROVIDER`: See https://github.com/google-github-actions/auth#authenticating-via-workload-identity-federation-1
