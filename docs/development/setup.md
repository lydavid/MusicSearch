# How to setup and build the project

- Create `secrets.properties` file in root directory with this content:
```
SPOTIFY_CLIENT_ID=<your Spotify client id>
SPOTIFY_CLIENT_SECRET=<your Spotify client secret>
```

## Firebase
- Go to https://console.firebase.google.com/
- Create 2 projects with analytics and crashlytics with the following package ids:
  - `io.github.lydavid.musicsearch.debug` for debug
  - `io.github.lydavid.musicsearch` for release
- Download and add `google-services.json` to `android/app/src/debug` and `android/app/src/release`

## Spotify 
- Go to https://developer.spotify.com/dashboard
- Create an app
- Find the client id and secret from settings and copy them into `secrets.properties`

## iOS
- Create a [Fastlane JSON API key file](https://docs.fastlane.tools/app-store-connect-api/#using-fastlane-api-key-json-file) using data from https://appstoreconnect.apple.com/access/integrations/api 
- Use [Fastlane Match](https://docs.fastlane.tools/actions/match/) to store certificates and profiles in a private repository: e.g. https://github.com/lydavid/musicsearch-fastlane-match
