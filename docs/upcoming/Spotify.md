# Search Spotify artists, albums, and tracks

## Motivation

I want to be able to quickly lookup MusicBrainz for artists/albums/tracks from Spotify. And those names might that be in another language I can't type.
Spotify doesn't offer a way to copy/paste any of these on mobile.

## Spotify broadcast notification

Fortunately, Spotify provides a way to broadcast metadata that includes artist/album/track name to other apps on your Android device.

You will have to turn this feature on from inside Spotify's settings: https://developer.spotify.com/documentation/android/guides/android-media-notifications/#enabling-media-notifications


## Implementation

- [x] Use a broadcast receiver to listen to Spotify's broadcast notifications
- [ ] Find a place to put this flow
  - [ ] Inside [advanced search](advanced_search.md)?
- [x] Support deeplinking search
  - [x] artist: Artist
  - [x] album: Release Group, Artist
  - [x] track: Recording, Artist
- [ ] A disclaimer/warning to user to enable `Device Broadcast Status` in their Spotify app
  - Where should this be?
  - If this is a hightlight feature, we can have a pop-up on first app launch advertising it

## Open questions

- Is there any way for us to get the artist/album/track name in its original language?
  - If they are transliterated, we will not find any results from MusicBrainz
