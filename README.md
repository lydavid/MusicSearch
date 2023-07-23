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
- Almost every tab allows you to search its content instantaneously
- Save anything to a collection
- Login using your MusicBrainz account to add to your existing collections
- Have a Pixel phone? Enable notification listener to record Now Playing history
- Cover arts
- Dark theme
- Material You theme

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

## Dependency Diagram

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR

  subgraph data
    base
    coverart
    domain
    musicbrainz
    room
    spotify
  end

  subgraph test
    image
    screenshot
  end
  domain --> base
  domain --> coverart
  domain --> musicbrainz
  domain --> room
  room --> base
  room --> coverart
  room --> musicbrainz
  image --> base
  image --> core
  data --> base
  data --> coverart
  data --> domain
  data --> room
  data --> spotify
  data --> musicbrainz
  screenshot --> core
  settings --> data-android
  settings --> common
  settings --> core
  settings --> screenshot
  data-android --> data
  data-android --> test-data
  nowplaying --> data
  nowplaying --> common
  nowplaying --> core
  nowplaying --> image
  nowplaying --> image
  nowplaying --> screenshot
  test-data --> data
  musicbrainz --> base
  spotify --> base
  collections --> data
  collections --> common
  collections --> core
  collections --> screenshot
  common --> data-android
  common --> core
  common --> image
  common --> image
  common --> screenshot
  app --> test-data
  app --> data-android
  app --> common
  app --> core
  app --> collections
  app --> history
  app --> image
  app --> nowplaying
  app --> settings
  app --> data-android
  app --> common
  app --> core
  app --> collections
  app --> history
  app --> image
  app --> nowplaying
  app --> settings
  app --> data-android
  app --> common
  app --> core
  app --> collections
  app --> history
  app --> image
  app --> nowplaying
  app --> settings
  app --> data-android
  app --> common
  app --> core
  app --> collections
  app --> history
  app --> image
  app --> nowplaying
  app --> settings
  app --> test-data
  coverart --> base
  history --> data
  history --> common
  history --> core
  history --> image
  history --> image
  history --> screenshot

```