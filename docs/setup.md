# How to setup the project

## Firebase
- Go to https://console.firebase.google.com/
- Create project with analytics and crashlytics
- Add google-services.json to `app` directory

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
