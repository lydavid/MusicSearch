# All Features

## Feature Matrix

This represents whether a feature is available (✅) on a platform, planned (⬜), or not supported (❌).
Some ✅ features are still work-in-progress, and their progress are tracked under their respective sections.
Inspired by [Spotless' feature matrix](https://github.com/diffplug/spotless?tab=readme-ov-file#current-feature-matrix).

| Feature                                                     | Android | Desktop | iOS |
|-------------------------------------------------------------|---------|---------|-----|
| [Browse](#browse)                                           | ✅       | ✅       | ✅   |
| [Collections](#collections)                                 | ✅       | ✅       | ✅   |
| [Export](#export)                                           | ✅       | ✅       | ⬜   |
| [Images](#images)                                           | ✅       | ✅       | ✅   |
| [Graph](#graph)                                             | ✅       | ✅       | ✅   |
| [Local Database](#local-database)                           | ✅       | ✅       | ✅   |
| [MusicBrainz Login](#musicbrainz-login)                     | ✅       | ✅       | ✅   |
| Pixel Now Playing History                                   | ✅       | ❌       | ❌   |
| [Search MusicBrainz](#search-musicbrainz)                   | ✅       | ✅       | ✅   |
| [Spotify Playing History](#spotify-playing-history)         | ✅       | ⬜️      | ⬜️  |
| [Swipe to Refresh](#swipe-to-refresh)                       | ✅       | ✅       | ✅   |
| [Up navigation from subtitle](#up-navigation-from-subtitle) | ✅       | ✅       | ✅   |
| [Wikipedia Extract](#wikipedia-extract)                     | ✅       | ✅       | ✅   |

## Browse

The following table shows all resources you can browse for in an entity's details screen.
It was derived from https://wiki.musicbrainz.org/MusicBrainz_API#Browse.

Given an entity, when you click on them from anywhere in the app, you will land on their details page.
In separate tabs, you can see all related entities listed under browsing supported.
e.g. On an artist's page, you can see their events, recordings, releases, release groups, and works.

| Entity        | Browsing supported                                                                                       | Browsing not yet supported | Unsupported                        |
|---------------|----------------------------------------------------------------------------------------------------------|----------------------------|------------------------------------|
| area          | artists, events, labels, places, releases                                                                | collections                | recordings, works                  |
| artist        | events, recordings, releases, release groups, works                                                      | collections                |
| collection    | areas, artists, events, instruments, labels, places, recordings, releases, release groups, series, works |                            |
| event         |                                                                                                          | collections                |
| instrument    |                                                                                                          | collections                |
| label         | releases                                                                                                 | collections                |
| place         | events                                                                                                   | collections                |
| recording     | releases                                                                                                 | collections                | artists                            |
| release       | artists                                                                                                  | collections                | labels, recordings, release groups |
| release group | releases                                                                                                 | collections                | artists                            |
| series        |                                                                                                          | collections                |
| work          | artists, recordings                                                                                      | collections                |

- There are no url details screen, clicking a url will just go to that url, either opening it in the browser, or deep linking to an app that supports it
- labels by release have been excluded, as we fetch all labels in the initial lookup with `inc=labels`, displaying it in the Details tab
- releases by track (which releases a given track is found in) is not supported as we don't have a track details screen. Clicking a track goes to its associated recording
- releases by track_artist may eventually be supported, but is not planned. This would be found in an artist's details screen
- artists by recording have been excluded as it is already fetched with initial recording lookup and is displayed as artist credits in the top bar
- artists by release group have been excluded as it is already fetched with initial release group lookup and is displayed as artist credits in the top bar
- recordings by release have been excluded as it is the same as tracks by release which we already fetch with release lookup
- release groups by release have been excluded as it is already fetched with initial release lookup and is displayed in the top bar dropdown menu. There is only one such release group a release may belong to

## Collections

Your personal list of entities.
You can add to and delete items from them.

New collections can be created but will only exist locally due to technical limitations.

[Login](#musicbrainz-login) to see your MusicBrainz collections.
Additions and deletions will be synced to MusicBrainz's server.

A collection can only store one type of entity.
See below for the supported types of collections.

| Entity        | Supported |
|---------------|-----------|
| area          | ✅         |
| artist        | ✅         |
| collection    | ❌         |
| event         | ✅         |
| instrument    | ✅         |
| label         | ✅         |
| place         | ✅         |
| recording     | ✅         |
| release       | ✅         |
| release group | ✅         |
| series        | ✅         |
| work          | ✅         |


## Export

Exporting the app's database as a sqlite database is supported.

You will need to use a tool such as [SQLite's CLI](https://www.sqlite.org/cli.html) to interact with it.


## Images

| Entity        | Supported | Source                                                                                 |
|---------------|-----------|----------------------------------------------------------------------------------------|
| area          | ⬜️        | wikidata?                                                                              |
| artist        | ✅         | [Spotify](https://developer.spotify.com/documentation/web-api/reference/get-an-artist) |
| collection    | ⬜️        |                                                                                        |
| event         | ✅️        | [Event Art Archive](https://musicbrainz.org/doc/Event_Art_Archive)                     |
| instrument    | ❌         |                                                                                        |
| label         | ⬜️        | wikidata?                                                                              |
| place         | ❌         |                                                                                        |
| recording     | ❌         |                                                                                        |
| release       | ✅         | [Cover Art Archive](https://musicbrainz.org/doc/Cover_Art_Archive)                     |
| release group | ✅         | [Cover Art Archive](https://musicbrainz.org/doc/Cover_Art_Archive)                     |
| series        | ❌         |                                                                                        |
| work          | ❌         |                                                                                        |

## Graph

Artist/recording graph showing which artists and recordings a given artist collaborated with and on.
This is currently limited to artists credited on the recording, which are primarily performers.

Inspired by [Obsidian](https://github.com/obsidianmd/obsidian-releases)'s graph view.

## Local Database

Browse all entities that have been cached locally in the app's database while you were searching.

### Browse local database images

All [images](#images) that you've come across are stored in the app's local database.
This feature allows you to browse them in a grid.
You can filter and sort based on a few metadata such as its type (e.g. Front, Spine, Back), comment, and the linked entity's name.


## MusicBrainz Login

From the app's settings, you can login to your MusicBrianz account.
This allows the app to fetch and display all of your [collections](#collections).

## Search MusicBrainz

| Entity        | Supported |
|---------------|-----------|
| area          | ✅         |
| artist        | ✅         |
| collection    | ❌         |
| event         | ✅         |
| instrument    | ✅         |
| label         | ✅         |
| place         | ✅         |
| recording     | ✅         |
| release       | ✅         |
| release group | ✅         |
| series        | ✅         |
| work          | ✅         |

## Spotify Playing History

You must enable "Device Broadcast Status" from Spotify's settings to allow this app to receive
broadcasts from Spotify.
Afterwards, with both Spotify and MusicSearch open, your listening history will be recorded,
allowing you to search for recently played tracks' artists, albums (release groups),
and tracks (recordings) in MusicBrianz.

## Swipe to Refresh

TODO: screenshot of details screen
TODO: screenshot of list screen

| Entity        | Details | List Screen |
|---------------|---------|-------------|
| area          | ✅       | ✅           |
| artist        | ✅       | ✅           |
| collection    | ❌       | ✅           |
| event         | ✅️      | ✅           |
| instrument    | ✅       | ✅           |
| label         | ✅️      | ✅           |
| place         | ✅       | ✅           |
| recording     | ✅       | ✅           |
| release       | ✅       | ✅           |
| release group | ✅️      | ✅           |
| series        | ✅       | ✅           |
| work          | ✅       | ✅           |

- A collection does not have a details screen. It only has one tab which lists all of its contents. e.g. An artist collection lists all of its artists
- List screens are analogous to those listed under "Browsing supported" in the [browse](#browse) feature section.

## Up navigation from subtitle

TODO: Insert an image/gif of a release with multiple artist credits

From the subtitle of the following screens, you can navigate "up" without having visited them first.

- Recording -> Artist
- Release -> Artist, Release Group
- Release Group -> Artist

## Wikipedia extract

Displays a Wikipedia excerpt of the entity along with a link to deep link into Wikipedia to read more.
This depends on the existence of a [Wikidata link](https://musicbrainz.org/doc/Wikidata).
If an entity is missing one, consider adding it to [MusicBrainz](https://musicbrainz.org/), and/or to [Wikidata](https://www.wikidata.org).

At the moment, we always get the English Wikipedia article's extract.

| Entity        | Details |
|---------------|---------|
| area          | ✅️      |
| artist        | ✅       |
| event         | ✅️      |
| instrument    | ✅       |
| label         | ✅️      |
| place         | ✅       |
| recording     | ✅       |
| release       | ✅       |
| release group | ✅       |
| series        | ✅       |
| work          | ✅       |
