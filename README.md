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

Or you can download its APK from the top of [all GitHub Releases](https://github.com/lydavid/MusicSearch/releases/).

Desktop releases are packaged by [Conveyor](https://www.hydraulic.dev/) and available from this [download page](https://lydavid.github.io/MusicSearch/download.html).

## Features

- Search MusicBrainz's massive database for any information related to your favorite artist or song
- Offline-first; all data is cached on device after loading each page/tab
- Almost every tab allows you to filter its content instantaneously
- Aliases will be used when filtering to help find things in other languages
- See every page you've visited in the history screen, and quickly get back to them
- Save anything to a collection
- Login using your MusicBrainz account to add to your existing collections
- Listening on Spotify? Enable Device Broadcast Status to search the artist or song from the app
- Have a Pixel phone? Enable notification listener to record Now Playing history
- Scrobbling to ListenBrainz? Enter your username to explore your listens
- Customize the app's appearance with: Light/Dark theme, Material theme based on your wallpaper, or pick a custom color
- Is an artist's discography incomplete? Aliases missing? Other data missing? Contribute it to MusicBrainz: https://musicbrainz.org

See [all features here](./docs/all_features.md).

This is a music database/discovery app, not a music player.
There are external links to various streaming platforms which will open the album/song in their app if it's installed.

## Screenshots

| Search artist                                                                    | Artist details                                                                    | Release groups by artist                                                                 | Release groups by artist (filtered)                                                             |
|----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| ![](fastlane/metadata/android/en-US/images/phoneScreenshots/1_search_artist.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/2_artist_details.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/3_artist_release_groups.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/4_artist_release_groups_filter.png) | 

| Release details                                                                    | Release tracks                                                                    | All collections                                                                    | Collection management                                                                    |
|------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| ![](fastlane/metadata/android/en-US/images/phoneScreenshots/5_release_details.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/6_release_tracks.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/7_all_collections.png) | ![](fastlane/metadata/android/en-US/images/phoneScreenshots/8_collection_management.png) |

## Development

[See here](./docs/README.md) for notes on setting up the project, and some development processes.

You can help translate this project through Weblate by clicking this [link](https://hosted.weblate.org/engage/musicsearch/) or the image below:

<a href="https://hosted.weblate.org/engage/musicsearch/">
<img src="https://hosted.weblate.org/widget/musicsearch/translations/287x66-grey.png" alt="Translation status" />
</a>

## Privacy Policy

See [PRIVACY_POLICY.md](PRIVACY_POLICY.md) for Android and Desktop.
See [PRIVACY_POLICY_IOS.md](PRIVACY_POLICY_IOS.md) for iOS.
