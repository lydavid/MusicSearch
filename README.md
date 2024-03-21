# MusicSearch

An Android app for browsing songs, artists, and anything related to them
using [MusicBrainz's API](https://wiki.musicbrainz.org/MusicBrainz_API).

<a href="https://play.google.com/store/apps/details?id=io.github.lydavid.musicsearch">
    <img alt="Get it on Google Play" height="80"
        src="https://play.google.com/intl/en_ca/badges/static/images/badges/en_badge_web_generic.png" />
</a>

## Features

- Search MusicBrainz's massive database for any information related to your favorite artist or song
- All data is cached on device after loading each page/tab
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
| ![](assets/screenshots/search_artist.png) | ![](assets/screenshots/artist_details.png) | ![](assets/screenshots/artist_release_groups.png) | ![](assets/screenshots/release_details.png) | 

| Release tracks                             | All collections                             | Collection of releases                 | Lookup history                             |
|--------------------------------------------|---------------------------------------------|----------------------------------------|--------------------------------------------|
| ![](assets/screenshots/release_tracks.png) | ![](assets/screenshots/all_collections.png) | ![](assets/screenshots/collection.png) | ![](assets/screenshots/lookup_history.png) |

## Development

[See here](./docs/README.md) for notes on setting up the project, and some development processes.

## Privacy Policy

See [PRIVACY_POLICY.md](PRIVACY_POLICY.md)

## Module Dependency Graph

Work in progress. Red arrows represents dependencies I wish to remove.

![](assets/module_dependency_graph.svg)

## Database schema

![](assets/musicsearch_db_schema.svg)
