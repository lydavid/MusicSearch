# All Features

WIP

## Browse

The following table shows all resources you can browse for in an entity's details screen.
It was derived from https://wiki.musicbrainz.org/MusicBrainz_API#Browse.

An example of how to read this table.
When you click on an artist, you will see the artist's details screen, in it, there will be a tab to
browse all of their events, recordings, releases, and release groups.
Soon, there will be a tab to browse all of their works.

| Entity        | Browsing supported                                                                                       | Browsing not yet supported                       |
|---------------|----------------------------------------------------------------------------------------------------------|--------------------------------------------------|
| area          | artists, events, places, releases                                                                        | collections, labels                              |
| artist        | events, recordings, release, release groups                                                              | collections, works                               |
| collection    | areas, artists, events, instruments, labels, places, recordings, releases, release groups, series, works |                                                  |
| event         |                                                                                                          | collections                                      |
| instrument    |                                                                                                          | collections                                      |
| label         | releases                                                                                                 | collections                                      |
| place         | events                                                                                                   | collections                                      |
| recording     | releases                                                                                                 | artists, collections                             |
| release       |                                                                                                          | artists, collections, recordings, release groups |
| release group | releases                                                                                                 | artists, collections                             |
| series        |                                                                                                          | collections                                      |
| work          | artists, recordings                                                                                      | collections                                      |

- Note that MusicBrainz supports browsing some entities not supported by the API, such as works by area. See below for those supported by MusicBrainz's API
- There are no url details screen, clicking a url will just go to that url, either opening it in the browser, or deep linking to an app that supports it
- labels by release have been excluded, as we fetch all labels in the initial lookup with `inc=labels`, displaying it in the Details tab
- releases by track (which releases a given track is found in) is not supported as we don't have a track details screen. Clicking a track goes to its associated recording
- releases by track_artist may eventually be supported, but is not planned. This would be found in an artist's details screen


## Up navigation from subtitle

TODO: Insert an image/gif of a release with multiple artist credits

From the subtitle of the following screens, you can navigate "up" without having visited them first.

- Recording -> Artist
- Release -> Artist, Release Group
- Release Group -> Artist
