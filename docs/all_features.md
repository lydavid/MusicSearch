# All Features

WIP

## Browse

The following table shows all resources you can browse for in an entity's details screen.

An example of how to read this table.
When you click on an artist, you will see the artist's details screen, in it, there will be a tab to
browse all of their release groups, and releases.
Soon, there will be a tab to browse all of their events, recordings, and works.

| Entity        | Browsing supported                                                                                       | Browsing not yet supported                       |
|---------------|----------------------------------------------------------------------------------------------------------|--------------------------------------------------|
| area          | events, places, releases                                                                                 | artists, collections, labels                     |
| artist        | release, release groups, events                                                                          | collections, recordings, works                   |
| collection    | areas, artists, events, instruments, labels, places, recordings, releases, release groups, series, works |                                                  |
| event         |                                                                                                          | collections                                      |
| instrument    |                                                                                                          | collections                                      |
| label         | releases                                                                                                 | collections                                      |
| place         | events                                                                                                   | collections                                      |
| recording     | releases                                                                                                 | artists, collections                             |
| release       |                                                                                                          | artists, collections, recordings, release groups |
| release group | releases                                                                                                 | artists, collections                             |
| series        |                                                                                                          | collections                                      |
| work          | recordings                                                                                               | artists, collections                             |

- Note that MusicBrainz supports browsing some entities not supported by the API, such as works by area. See below for those supported by MusicBrainz's API
- There are no url details screen, clicking a url will just go to that url, either opening it in the browser, or deep linking to an app that supports it
- labels by release have been excluded, as we fetch all labels in the initial lookup with `inc=labels`, displaying it in the Details tab
- releases by track (which releases a given track is found in) is not supported as we don't have a track details screen. Clicking a track goes to its associated recording
- releases by track_artist may eventually be supported, but is not planned. This would be found in an artist's details screen

Below is a chart extracted from https://wiki.musicbrainz.org/MusicBrainz_API#Browse.
It shows the supported resources we can browse on the left column, and the entity whose screen we will browse for that resource from.
Square brackets indicates those supported by the app. Tildes represents those that are not planned to be supported. The rest will eventually be supported.
eg. The first row means we can browse areas from a collection's screen.

```
 /ws/2/area            [collection]
 /ws/2/artist          area, [collection], recording, release, release-group, work
 /ws/2/collection      area, artist, [editor], event, label, place, recording, release, release-group, work
 /ws/2/event           area, artist, [collection], [place]
 /ws/2/instrument      [collection]
 /ws/2/label           area, [collection], ~~release~~
 /ws/2/place           [area], [collection]
 /ws/2/recording       artist, [collection], release, [work]
 /ws/2/release         [area], [artist], [collection], [label], ~~track~~, ~~track_artist~~, [recording], [release-group]
 /ws/2/release-group   [artist], [collection], release
 /ws/2/series          [collection]
 /ws/2/work            artist, [collection]
 /ws/2/url             ~~resource~~
```

## Up navigation from subtitle

TODO: Insert an image/gif of a release with multiple artist credits

From the subtitle of the following screens, you can navigate "up" without having visited them first.

- Recording -> Artist
- Release -> Artist, Release Group
- Release Group -> Artist
