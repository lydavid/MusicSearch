# Collections

While lookup history records every entity you've visited, collections will allow you to save
only the entities you want.

Search `collectableResources` to find all the entities that can be collected.

## Links

- https://wiki.musicbrainz.org/MusicBrainz_API#collections


## Prerequisites

- [x] [OAuth with MusicBrainz](oauth_musicbrainz.md)

## Implementation

### General
- [x] Can create new collections
  - [x] From collection tab via menu item
  - [x] From any collectable entity's page via "Add to collection" bottom sheet
- [x] Can add entities to a collection
  - [x] Many-to-many relationship between collection and collection_entity
  - [x] An entity can only appear once in any given collection
  - [x] A collection only holds one type of entity
- [x] Can filter all collections by their name
  - Filter by description (MB WS doesn't return description -> out of scope)
- [ ] show an indicator that an entity is already in the collection
  - does not work for remote collections before we get its content

### Pull collections from MB
- [x] Optional MusicBrainz id (mbid) for collection
- [x] Pull all collections into local database (DB)
  - [x] Will need profile's username to get their private collections
    - https://musicbrainz.org/ws/2/collection?editor={username}&inc=user-collections
  - [x] if username (and auth state) exists, query for user's collections
    - otherwise we don't query for any collections at all because we need their username
    - technically, if the user knew their or someone's username, they could input it to see their public collections
      - out of scope
  - [x] Insert all collections into DB (this is just the info about each collection, not its content)
- [ ] On clicking a MB collection, query for its content from MB
  - Start off with releases by collection
    - [x] cover art
    - [ ] scaffold
    - [x] Insert into DB
- What happens when the user logs out?
  - [x] Local collections should remain
  - [x] Any collections already downloaded by MB should remain
  - [x] If there are outstanding collections to fetch from MB, do not try to fetch them only to fail and show an error


### Push collection content to MB, synchronize
- XML payload not needed for collections!
  - Otherwise would need to try https://github.com/Tickaroo/tikxml
- [x] Add to collection
  - PUT /ws/2/collection/f4784850-3844-11e0-9e42-0800200c9a66/releases/455641ea-fff4-49f6-8fb4-49f961d8f1ad;c410a773-c6eb-4bc0-9df8-042fe6645c63?client=example.app-0.4.7
  - [ ] Do not crash on 401 or other exceptions
    - [ ] fail snackbar
    - [ ] show a snackbar saying whether it succeeded or it was already part of it
- [ ] Delete from collection
  - DELETE /ws/2/collection/f4784850-3844-11e0-9e42-0800200c9a66/releases/455641ea-fff4-49f6-8fb4-49f961d8f1ad;?client=example.app-0.4.7
  - You may submit up to ~400 entities in a single request, separated by a semicolon (;)
- [ ] Unidirectional sync where MB is source of truth
  - local additions/deletions will be overwritten
  - This will be supported the moment we copy/paste our paging code
  - [ ] An easier way to always keep remote up to date is to immediate PUT/DELETE an entity
    - Will work unless user does not have connection or is not logged in (they have to be logged in to fetch remote collections)
- Bidirectional sync (out of scope)
  - would need a timestamp field in MB and locally
- Cannot push local collections to MB cause webservice not implemented: https://tickets.metabrainz.org/browse/MBS-11914
  - So we can only update existing collections

