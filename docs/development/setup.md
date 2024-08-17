# How to setup and build the project

- Create `secrets.properties` file in root directory with this content:
```
MUSICBRAINZ_CLIENT_ID=<your MB client id>
MUSICBRAINZ_CLIENT_SECRET=<your MB client secret>
SPOTIFY_CLIENT_ID=<your Spotify client id>
SPOTIFY_CLIENT_SECRET=<your Spotify client secret>
```

## Firebase
- Go to https://console.firebase.google.com/
- Create 2 projects with analytics and crashlytics with the following package ids:
  - `io.github.lydavid.musicsearch.debug` for debug
  - `io.github.lydavid.musicsearch` for release
- Download and add `google-services.json` to `android/app/src/debug` and `android/app/src/release`

## MusicBrainz
- Go to https://musicbrainz.org/account/applications/register
- Enter any name
- Enter this callback url: `io.github.lydavid.musicsearch.debug://oauth2/redirect`
- Find the client id and secret from https://musicbrainz.org/account/applications and copy them into `secrets.properties`

## Spotify 
- Go to https://developer.spotify.com/dashboard
- Create an app
- Find the client id and secret from settings and copy them into `secrets.properties`
