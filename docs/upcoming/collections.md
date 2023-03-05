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
- [x] Oauth with MusicBrainz (MB) to access user's collections and profile: https://musicbrainz.org/doc/Development/OAuth2
  - [x] Capture authorization redirect
    - [x] Save AuthState including Bearer token
    - [ ] Pass bearer token to MB api request if it exists
      - Need to pass through `provideOkHttpClient`
      - We don't need it passed to `provideCoverArtArchiveApi`, so redo structure
  - [ ] Pull all collections into local database. Will need profile's username to get their private collections
    - https://musicbrainz.org/ws/2/collection?editor={username}&inc=user-collections
- [ ] Optional MusicBrainz id (mbid) for collection
  - [ ] When pulling collections from MusicBrainz, we will have store its mbid
  - [ ] Collections created in our app do not have an mbid
    - [ ] After pushing our collections to MB, we can update it with the newly created mbid
- [ ] After adding an entity to a collection, show a snackbar saying whether it succeeded or it was already part of it
- 

## CClear web browser from backstack after OAuth flow while keeping deeplinking ability

When we make an OAuth request, it launches the intent in a browser.
After completing the flow, it will follow the redirect uri back to our app.

Normally, after a back press, it will bring you back to the OAuth page in the webview.
Clicking "Allow access" again from here will fail as the request has already been used.
So we want to avoid resurfacing this page again to users.

The solution is to use [AppAuth](https://github.com/openid/AppAuth-Android)'s `RedirectUriReceiverActivity`
together with `android:launchMode="singleTask"` in the Manifest.

To make sure we only have one activity accept the redirect uri, we need to make sure our app's
Activity has a different `host` from `RedirectUriReceiverActivity`.
This also means we can keep our app's activity's launchMode as the default, allowing us to
deeplink test any of our screens without stopping our app.
