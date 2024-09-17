# MusicSearch

An Android app for browsing songs, artists, and anything related to them
using [MusicBrainz's API](https://wiki.musicbrainz.org/MusicBrainz_API).

Multiplatform distributions are heavily WIP. Only the Android app is considered stable.

## Releases

The [master branch](https://github.com/lydavid/MusicSearch/tree/master) Android app is available on Google Play.
Or you can you can download its APK from the [latest GitHub Release](https://github.com/lydavid/MusicSearch/releases/latest/).

The [beta branch](https://github.com/lydavid/MusicSearch) Android app is available on Google Play as a beta tester.
Or you can you can download its APK from the top of [all GitHub Releases](https://github.com/lydavid/MusicSearch/releases/).

<a href="https://play.google.com/store/apps/details?id=io.github.lydavid.musicsearch">
    <img alt="Get it on Google Play" height="80"
        src="https://play.google.com/intl/en_ca/badges/static/images/badges/en_badge_web_generic.png" />
</a>

Desktop releases are packaged by [Conveyor](https://www.hydraulic.dev/) and available from this [download page](https://lydavid.github.io/MusicSearch/download.html).

## Features

- Search MusicBrainz's massive database for any information related to your favorite artist or song
- Offline-first; all data is cached on device after loading each page/tab
- See every page you've visited in the history screen, and quickly get back to them
- Almost every tab allows you to filter its content instantaneously
- Save anything to a collection
- Login using your MusicBrainz account to add to your existing collections
- Listening on Spotify? Enable Device Broadcast Status to search the artist or song from the app
- Have a Pixel phone? Enable notification listener to record Now Playing history
- Dark theme
- Material You theme

See [all features here](./docs/all_features.md).

## Screenshots

| Search artist                             | Artist details                             | Release groups by artist                          | Release details                             |
|-------------------------------------------|--------------------------------------------|---------------------------------------------------|---------------------------------------------|
| ![](fastlane/metadata/android/en-US/images/phoneScreenshots/1_search_artist.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/2_artist_details.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/3_artist_release_groups.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/4_release_details.png) | 

| Release tracks                             | All collections                             | Collection of releases                 | Lookup history                             |
|--------------------------------------------|---------------------------------------------|----------------------------------------|--------------------------------------------|
| ![](fastlane/metadata/android/en-US/images/phoneScreenshots/5_release_tracks.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/6_all_collections.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/7_collection.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/8_lookup_history.png) |

## Development

[See here](./docs/README.md) for notes on setting up the project, and some development processes.

## Privacy Policy

See [PRIVACY_POLICY.md](PRIVACY_POLICY.md)

## Database schema

![](assets/musicsearch_db_schema.svg)
