# All Features

WIP

## Browse

The following table shows all resources you can browse for in an entity's details screen.
It was derived from https://wiki.musicbrainz.org/MusicBrainz_API#Browse.

Given an entity, when you click on them from anywhere in the app, you will land on their details page.
In separate tabs, you can see all related entities listed under browsing supported.
e.g. On an artist's page, you can see their events, recordings, releases, and release groups.

| Entity        | Browsing supported                                                                                       | Browsing not yet supported | Unsupported                         |
|---------------|----------------------------------------------------------------------------------------------------------|----------------------------|-------------------------------------|
| area          | artists, events, places, releases                                                                        | collections, labels        | recordings, works                   |
| artist        | events, recordings, release, release groups, works                                                       | collections                |
| collection    | areas, artists, events, instruments, labels, places, recordings, releases, release groups, series, works |                            |
| event         |                                                                                                          | collections                |
| instrument    |                                                                                                          | collections                |
| label         | releases                                                                                                 | collections                |
| place         | events                                                                                                   | collections                |
| recording     | releases                                                                                                 | collections                | artists                             |
| release       | artists                                                                                                  | collections                | labels, recordings, release groups  |
| release group | releases                                                                                                 | collections                | artists                             |
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


## Up navigation from subtitle

TODO: Insert an image/gif of a release with multiple artist credits

From the subtitle of the following screens, you can navigate "up" without having visited them first.

- Recording -> Artist
- Release -> Artist, Release Group
- Release Group -> Artist
