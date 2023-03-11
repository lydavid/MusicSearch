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
  - [ ] Filter by description
- [ ] After adding an entity to a collection, show a snackbar saying whether it succeeded or it was already part of it


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
  - [ ] Insert into DB
- What happens when the user logs out?
  - [x] Local collections should remain
  - [x] Any collections already downloaded by MB should remain
  - [x] If there are outstanding collections to fetch from MB, do not try to fetch them only to fail and show an error


### Push collection content to MB, synchronize
- [ ] POST with XML data
  - PUT /ws/2/collection/f4784850-3844-11e0-9e42-0800200c9a66/releases/455641ea-fff4-49f6-8fb4-49f961d8f1ad;c410a773-c6eb-4bc0-9df8-042fe6645c63?client=example.app-0.4.7
  - DELETE /ws/2/collection/f4784850-3844-11e0-9e42-0800200c9a66/releases/455641ea-fff4-49f6-8fb4-49f961d8f1ad;?client=example.app-0.4.7
  - You may submit up to ~400 entities in a single request, separated by a semicolon (;)
- [ ] Unidirectional sync where MB is source of truth
  - local additions/deletions will be overwritten
  - This will be supported the moment we copy/paste our paging code
  - [ ] An easier way to always keep remote up to date is to immediate PUT/DELETE an entity
    - Will work unless user does not have connection or is not logged in (they have to be logged in to fetch remote collections)
- [ ] Bidirectional sync
  - Research ANKI
  - `collection` scenarios (this is just unidirectional)
    - not in remote, not in local -> do nothing
    - in remote, in local -> do nothing
    - not in remote, in local -> delete
      - Normally if it was added to local at a later timestamp, local should be pushed to remote but POST collection is not supported
    - in remote, not in local -> insert
  - `collection_entity` scenarios
    - not in remote, not in local -> do nothing
    - in remote, in local -> do nothing
    - not in remote, in local -> ?
    - in remote, not in local -> ?
  - What happens when there are changes in both remote and local?
    - How do we detect changes in remote? MB's schema would need to support it
    - global last_sync_time
      - and in `collection` table
  - deletions may need to be soft (with a flag)
- Cannot push local collections to MB cause webservice not implemented: https://tickets.metabrainz.org/browse/MBS-11914
  - So we can only update existing collections

