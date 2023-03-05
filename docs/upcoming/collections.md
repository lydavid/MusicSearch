# Collections

While lookup history records every entity you've visited, collections will allow you to save
only the entities you want.

Search `collectableResources` to find all the entities that can be collected.

- [x] Can create new collections
  - [x] From collection tab via menu item
  - [x] From any collectable entity's page via "Add to collection" bottom sheet
- [x] Can add entities to a collection
  - [x] Many-to-many relationship between collection and collection_entity
  - [x] An entity can only appear once in any given collection
  - [x] A collection only holds one type of entity
- [x] Can filter all collections by their name
  - [ ] Filter by description
- [ ] Oauth with MusicBrainz (MB) to access user's collections and profile: https://musicbrainz.org/doc/Development/OAuth2
  - [ ] Pull all collections into local database. Will need profile's username to get their private collections
    - https://musicbrainz.org/ws/2/collection?editor={username}&inc=user-collections
- [ ] Optional MusicBrainz id (mbid) for collection
  - [ ] When pulling collections from MusicBrainz, we will have store its mbid
  - [ ] Collections created in our app do not have an mbid
    - [ ] After pushing our collections to MB, we can update it with the newly created mbid



- [ ] After adding an entity to a collection, show a snackbar saying whether it succeeded or it was already part of it
- 
