# OAuth 2 with MusicBrainz

Authenticate user with MusicBrainz using OAuth 2.
This is completely optional for users.

## Implementation

- [x] [OAuth with MusicBrainz](https://musicbrainz.org/doc/Development/OAuth2) (MB) to access user's collections and profile
  - [x] Capture authorization redirect
    - [x] Save AuthState including Bearer token
    - [x] Pass bearer token to MB api request if it exists
      - Need to pass through `provideOkHttpClient`
      - We don't need it passed to `provideCoverArtArchiveApi`, so redo structure
- [ ] Login card
  - [ ] Profile name
  - [ ] Logout card

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
