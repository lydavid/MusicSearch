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
- [ ] Optional MusicBrainz id (mbid) for collection
  - [ ] When pulling collections from MusicBrainz, we will have store its mbid
  - [ ] Collections created in our app do not have an mbid
    - [ ] After pushing our collections to MB, we can update it with the newly created mbid
- [x] Pull all collections into local database (DB)
  - [x] Will need profile's username to get their private collections
    - https://musicbrainz.org/ws/2/collection?editor={username}&inc=user-collections
  - [x] if username (and auth state) exists, query for user's collections
    - otherwise we don't query for any collections at all because we need their username
    - technically, if the user knew their or someone's username, they could input it to see their public collections
      - out of scope
  - [x] Insert all collections into DB (this is just the info about each collection, not its content)
- [ ] On clicking a MB collection, query for its content from MB
  - Should we query for their content as the user scrolls?
    - We currently show a count in collections list, which is based on sum of its linked entities but because we don't insert all its entities on initial pull, we don't have that info
      - We could get it from `area-count` etc -> would mean we need to store/update its count in `collection` 
  - [ ] Insert into DB
- What happens when the user logs out?
  - [x] Local collections should remain
  - [x] Any collections already downloaded by MB should remain
  - [x] If there are outstanding collections to fetch from MB, do not try to fetch them only to fail and show an error


### Push collections to MB
- [ ] POST with XML data

### Sync between MB and DB
- What happens when there are changes in both remote and local?
- How do we detect changes in remote?
- Research ANKI
