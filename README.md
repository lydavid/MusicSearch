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

[See here](./docs/README.md) for notes on upcoming features and some development processes.

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
    musicbrainz
  end
  musicbrainz --> base
  data --> base
  data --> coverart
  data --> musicbrainz
  data-android --> data
  data-android --> test-data
  app --> test-data
  app --> data-android
  app --> data-android
  app --> data-android
  app --> data-android
  app --> test-data
  coverart --> base
  test-data --> data

```