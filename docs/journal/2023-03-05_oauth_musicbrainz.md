# OAuth 2 with MusicBrainz

Authenticate user with MusicBrainz using OAuth 2.
This is completely optional for users.

## Implementation

- [x] [OAuth with MusicBrainz](https://musicbrainz.org/doc/Development/OAuth2) (MB) to access user's collections and profile
  - [x] Capture authorization redirect
    - [x] Save AuthState including Bearer token
    - [x] Pass bearer token to MB api request if it exists
      - Need to pass through `provideOkHttpClient`
      - We don't need it passed to `provideCoverArtArchiveApi`
- [x] Login card
  - [x] Profile name
  - [x] Logout card
- [x] Reactive authentication works fine for us for now
  - Solution from: https://github.com/MrNtlu/Token-Authentication
    - search `.authenticator` to learn more from OkHttp's docs
  - Downside: calls to user and collections will be done twice, the first time without authorization header
  - Upside: The majority of our calls don't require authorization so we don't needlessly send it in the header


## Clear web browser from backstack after OAuth flow while keeping deeplinking ability

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


## OAuth 2 vs OpenID

OpenID providers must support GET and POST: https://github.com/openid/AppAuth-Android/issues/801
But OAuth 2 on its own does not.
MusicBrainz has OAuth 2 but not OpenID.


## Refresh experience

It does not seem like we can automatically refresh the user token.

We currently support two ways to re-authenticate:
1. Log out from Settings, then log back in
2. When trying to add to a collection and it fails due with a 401 code, we show a snackbar with
a button to login again

Both of these will just bring the user to MusicBrainz's OAuth page in a browser.
