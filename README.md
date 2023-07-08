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
- History: See every page you've visited, and quickly get back to them
- Filter: Almost every tab allows you to search its content instantaneously
- Collections: Save anything to a collection
- Login using your MusicBrainz account to add to your existing collections
- Cover arts
- Dark theme
- Material You theme

## Screenshots

| Search artist       | Release groups by artist   | Release details     | Release tracks      |
|---------------------|----------------------------|---------------------|---------------------|
| ![](assets/1.png)   | ![](assets/2.png)          | ![](assets/3.png)   | ![](assets/4.png)   | 

| Artist stats      | Lookup history    | Collection of release groups |
|-------------------|-------------------|------------------------------|
| ![](assets/5.png) | ![](assets/6.png) | ![](assets/7.png)            |

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
  common --> test-data
  common --> screenshot
  app --> test-data
  app --> data-android
  app --> common
  app --> core
  app --> collections
  app --> history
  app --> image
  app --> settings
  app --> data-android
  app --> common
  app --> core
  app --> collections
  app --> history
  app --> image
  app --> settings
  app --> data-android
  app --> common
  app --> core
  app --> collections
  app --> history
  app --> image
  app --> settings
  app --> data-android
  app --> common
  app --> core
  app --> collections
  app --> history
  app --> image
  app --> settings
  app --> test-data
  coverart --> base
  history --> data
  history --> common
  history --> core
  history --> image
  history --> test-data
  history --> screenshot

```