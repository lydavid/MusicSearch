# MusicSearch

<p>
    <img alt="F-Droid Version" src="https://img.shields.io/f-droid/v/io.github.lydavid.musicsearch?label=F-Droid">
    <img alt="GitHub Release Version" src="https://img.shields.io/github/v/release/lydavid/MusicSearch?label=GitHub">
    <img alt="Custom badge" src="https://img.shields.io/endpoint?color=green&logo=google-play&logoColor=green&url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Dio.github.lydavid.musicsearch%26gl%3DUS%26hl%3Den%26l%3DGoogle%2520Play%26m%3Dv%24version">
</p>

An Android app for browsing songs, artists, and anything related to them
using [MusicBrainz's API](https://wiki.musicbrainz.org/MusicBrainz_API).

Multiplatform distributions are heavily WIP. Only the Android app is considered stable.

## Releases

<a href="https://www.androidfreeware.net/download-musicsearch-apk.html">
    <img alt="Get it on AndroidFreeware" height="80"
        src="https://www.androidfreeware.net/images/androidfreeware-badge.png" />
</a>

<a href="https://f-droid.org/packages/io.github.lydavid.musicsearch">
    <img alt="Get it on F-Droid" height="80"
        src="https://f-droid.org/badge/get-it-on.png" />
</a>

<a href="https://play.google.com/store/apps/details?id=io.github.lydavid.musicsearch">
    <img alt="Get it on Google Play" height="80"
        src="https://play.google.com/intl/en_ca/badges/static/images/badges/en_badge_web_generic.png" />
</a>

<a href="https://www.openapk.net/musicsearch/io.github.lydavid.musicsearch/">
    <img alt="Get it on OpenAPK" height="80"
        src="https://www.openapk.net/images/openapk-badge.png" />
</a>

Or you can you can download its APK from the top of [all GitHub Releases](https://github.com/lydavid/MusicSearch/releases/).

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
