# How to setup the project

## Firebase
- Go to https://console.firebase.google.com/
- Create 2 projects with analytics and crashlytics with the following package ids:
  - `io.github.lydavid.musicsearch.debug` for debug
  - `io.github.lydavid.musicsearch` for release
- Download and add `google-services.json` to `app/src/debug` and `app/src/release` directory

## MusicBrainz
- Go to https://musicbrainz.org/account/applications

## Spotify 
- Go to https://developer.spotify.com/dashboard
- Create an app
- Find the client id and secret from settings
- Create `secrets.properties` file in root directory with this content:

```
SPOTIFY_CLIENT_ID=<your client id>
SPOTIFY_CLIENT_SECRET=<your client secret>
```
