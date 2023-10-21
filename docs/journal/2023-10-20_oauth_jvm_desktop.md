# Attempting OAuth 2.0 for a JVM Desktop App

This is for MusicBrainz.
For Spotify, we're using [Client Credentials Flow](https://developer.spotify.com/documentation/web-api/tutorials/client-credentials-flow),
which doesn't require input from the user.

Our current implementation uses [AppAuth-Android](https://github.com/openid/AppAuth-Android),
which as its name implies is for Android only.
Looking through openid's [repositories](https://github.com/openid?q=AppAuth&type=all&language=&sort=),
they have an iOS SDK as well, but no desktop SDK.

Browsing GitHub for `oauth2` and filtering for Java, we find https://github.com/scribejava/scribejava.
We quickly test it out in :desktop-app using a pre-bundled OAuth api implementation,
and it works.

## Generalizing MusicBrainz OAuth for Android and JVM

Move all musicbrainz authentication related code to `:data:musicbrainz:auth`.
Many of them were already previously under `auth` dir in `:data:musicbrainz`,
but some were in `:data-android`.
The ones from `:data-android` should go under `androidMain` source set.
The url endpoints should be extracted out to `commonMain`.

One of the first issue we ran into extracting out `:data:musicbrainz` is the reference to the
constant `MUSIC_BRAINZ_BASE_URL`.
We need to decide whether to have a `:data:musicbrainz:core` just to hold this so that
the two MusicBrainz data modules can depend on it.
Or to just keep auth under `:data:musicbrainz`.
In the end, it seems easier to just keep them together.
Various apis are restricted behind authentication, so we need to access auth from `:data:musicbrainz` anyways.

