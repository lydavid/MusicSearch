# Changelog

## [1.121.0](https://github.com/lydavid/MusicSearch/compare/v1.120.0...v1.121.0) (2026-06-11)


### Features

* save listen's disc/track number and ISRC and show them when Show originally submitted data icon button is toggled to help with mapping listens; update database version to 80 ([#2249](https://github.com/lydavid/MusicSearch/issues/2249)) ([463b0b9](https://github.com/lydavid/MusicSearch/commit/463b0b9eaf7215aec8583475c3f05081d4d54d0d))

## [1.120.0](https://github.com/lydavid/MusicSearch/compare/v1.119.1...v1.120.0) (2026-06-07)


### Features

* support changing ListenBrainz instance ([c3b2629](https://github.com/lydavid/MusicSearch/commit/c3b2629269c7cdc0d12f5c24e233fab045ba171d))
* support changing MusicBrainz instance (such as to a self-hosted one) ([ba12783](https://github.com/lydavid/MusicSearch/commit/ba1278353b4d4ee340890b3ee07af6de40cdd7d0))

## [1.119.1](https://github.com/lydavid/MusicSearch/compare/v1.119.0...v1.119.1) (2026-06-01)


### Bug Fixes

* include ISRCs when fetching a release's tracks' recording, so we do not remove a visited recording's ISRCs ([877947f](https://github.com/lydavid/MusicSearch/commit/877947f7de230e69643d18b5154a5f5e77cccb93))

## [1.119.0](https://github.com/lydavid/MusicSearch/compare/v1.118.0...v1.119.0) (2026-05-29)


### Features

* show number of items shown out of total as a header list item when filtering Tracks ([18e7985](https://github.com/lydavid/MusicSearch/commit/18e79852bc329a5d969322c31dcd6430e2711a37))

## [1.118.0](https://github.com/lydavid/MusicSearch/compare/v1.117.0...v1.118.0) (2026-05-27)


### Bug Fixes

* increase genres/tags overflow dropdown icon touch area ([16b1426](https://github.com/lydavid/MusicSearch/commit/16b14269fffac9b21ce9da8507e87765d623e03e))
* move listen count and complete listen count to list separator; embiggen list separator vertical padding; shrink icons in list separators a bit to not dominate the text ([5a46bc8](https://github.com/lydavid/MusicSearch/commit/5a46bc8061eb88bafe93a93e624fbc785e821833))


### Features

* make new collapsible section headers sticky ([de061c8](https://github.com/lydavid/MusicSearch/commit/de061c85245bda015ad1fe9d6c0cb8bbc6806098))
* support collapsing all sections in Details tab ([c1f5a89](https://github.com/lydavid/MusicSearch/commit/c1f5a89d5c6f1b29fb8e3eff6a1c214283075bbb))

## [1.117.0](https://github.com/lydavid/MusicSearch/compare/v1.116.0...v1.117.0) (2026-05-25)


### Bug Fixes

* filter out blank attributes so that successive commas are not displayed in Relationships screen ([a64ad2b](https://github.com/lydavid/MusicSearch/commit/a64ad2b4c7ad42924a53b263928c85b98b7fb4d9))


### Features

* support more relationship types ([72f3745](https://github.com/lydavid/MusicSearch/commit/72f37453d899b0b07d3fb8ece298d12429822f1c))

## [1.116.0](https://github.com/lydavid/MusicSearch/compare/v1.115.0...v1.116.0) (2026-05-24)


### Features

* fetch user genres/tags to prepopulate upvotes ([419eb80](https://github.com/lydavid/MusicSearch/commit/419eb808b539bd56a2b3f5f5326df4f1073a4bac))

## [1.115.0](https://github.com/lydavid/MusicSearch/compare/v1.114.1...v1.115.0) (2026-05-20)


### Bug Fixes

* show all relationship attributes deduplicated ([7f3d530](https://github.com/lydavid/MusicSearch/commit/7f3d5305d71fa6c1a6ce404e264a70d85237499f))


### Features

* persist options in Lookup URL screen ([1a4c1b9](https://github.com/lydavid/MusicSearch/commit/1a4c1b9022a42db775969d1bfd9c5151b4b72e23))

## [1.114.1](https://github.com/lydavid/MusicSearch/compare/v1.114.0...v1.114.1) (2026-05-19)


### Bug Fixes

* handle more relationship types properly (including members vs members of) ([b16da1f](https://github.com/lydavid/MusicSearch/commit/b16da1f4a1e9b4558aa9d7fa90fef397f89f241f))
* map area type to localized text ([75b069b](https://github.com/lydavid/MusicSearch/commit/75b069b32f5b07fd26fe6f81c6f2e3d8136fc978))
* map event type to localized text ([34a0fc3](https://github.com/lydavid/MusicSearch/commit/34a0fc335cf3facd4fc410e4e0eee317374c4197))
* map instrument type to localized text ([e7b0311](https://github.com/lydavid/MusicSearch/commit/e7b031197f50c3cce22958a2ecf61b2cfa7c3dcf))
* map label type to localized text ([fc86fd2](https://github.com/lydavid/MusicSearch/commit/fc86fd295ae4c8038ce3a2b46483e775619bc101))
* map place type to localized text ([87dd70b](https://github.com/lydavid/MusicSearch/commit/87dd70b20898d6778694ab0c341591d8d8eff020))
* map series type to localized text ([a6a014a](https://github.com/lydavid/MusicSearch/commit/a6a014a7b795b0ff179dd5fd98d2431c08f53f92))

## [1.114.0](https://github.com/lydavid/MusicSearch/compare/v1.113.0...v1.114.0) (2026-05-17)


### Bug Fixes

* **desktop:** login from snackbar from details will show a dialog for you to input auth token ([bd5d64b](https://github.com/lydavid/MusicSearch/commit/bd5d64b452b714de0bf3d968a43548a768b41747))


### Features

* hide downvoted genres/tags; add more actions menu to genres/tags headers to show/hide downvoted genres/tags ([834e9e5](https://github.com/lydavid/MusicSearch/commit/834e9e59698c765a810e717a8284609a593895ba))
* support voting on tags (requires re-authenticating with MusicBrainz for tag permissions); update database version to 79 ([5fa1a3a](https://github.com/lydavid/MusicSearch/commit/5fa1a3aaf39574a56fd4b2511e7891bf1061bb5a))

## [1.113.0](https://github.com/lydavid/MusicSearch/compare/v1.112.1...v1.113.0) (2026-05-13)


### Features

* show genres/tags in details screens (cached screens will require refreshing to see these); click a tag to quickly search for it, or go to the genre's screen  ([#2198](https://github.com/lydavid/MusicSearch/issues/2198)) ([c9d7609](https://github.com/lydavid/MusicSearch/commit/c9d7609714430a2e3c2fc77e21a9e496dfa920c4)), closes [#2046](https://github.com/lydavid/MusicSearch/issues/2046)

## [1.112.1](https://github.com/lydavid/MusicSearch/compare/v1.112.0...v1.112.1) (2026-05-12)


### Bug Fixes

* remove dedicated Additional Details section for releases and put its details under Release information ([c6d40e5](https://github.com/lydavid/MusicSearch/commit/c6d40e5aa9e17965a8eb9afba38046d70808f567))

## [1.112.0](https://github.com/lydavid/MusicSearch/compare/v1.111.0...v1.112.0) (2026-05-10)


### Bug Fixes

* change default number of latest listens to show in details back to 3 so other content is still discoverable ([c2d7a51](https://github.com/lydavid/MusicSearch/commit/c2d7a513f2d5d8d8bc03bf9a10f4c572c02d0081))
* show just the listen count, but content description reads "X listen(s)" ([8f2636c](https://github.com/lydavid/MusicSearch/commit/8f2636c12d4d7aed268e0b07916e960ede75bce5))
* use system date/time regional format throughout app ([6775d8d](https://github.com/lydavid/MusicSearch/commit/6775d8d5b0c7301b7b24a2fbbeadd7cf05c769cc))


### Features

* add (opt-in) option to submit client name and version with listens ([68fde3b](https://github.com/lydavid/MusicSearch/commit/68fde3b007475a79ef071e523d01cbaaf79b6ec9))
* add navigation to recording/listen from listens in artist/recording/release/work details screens; allow filtering listens in details; add a setting to listens screen to adjust how many listens to show in details screens ([45cb0ed](https://github.com/lydavid/MusicSearch/commit/45cb0ed4618825c4765efb9be5886faa2c7e4791))
* show ISRCs in recording list item and make them filterable by text ([1415257](https://github.com/lydavid/MusicSearch/commit/1415257ddbc1910eb6b09204ee68cf1ea4ef7ac3))

## [1.111.0](https://github.com/lydavid/MusicSearch/compare/v1.110.0...v1.111.0) (2026-05-07)


### Features

* add toggle icon button to listen additional actions sheet to show/hide originally submitted data to help debug MusicBrainz mapping ([b8b1d7d](https://github.com/lydavid/MusicSearch/commit/b8b1d7d802f5850cd178b78273367ef0cbbe8e10))

## [1.110.0](https://github.com/lydavid/MusicSearch/compare/v1.109.0...v1.110.0) (2026-05-03)


### Bug Fixes

* do not hide details section if it exists but is currently filtered out because showing (0 / X) is more meaningful ([e351500](https://github.com/lydavid/MusicSearch/commit/e3515008970fbfd3f69f25a7194279e59345904c))


### Features

* count number of shown release labels and events (similar to external links and aliases) ([4a2a8d1](https://github.com/lydavid/MusicSearch/commit/4a2a8d1c2acc6b78ae4a747749264823f9d05d4d))

## [1.109.0](https://github.com/lydavid/MusicSearch/compare/v1.108.0...v1.109.0) (2026-05-01)


### Features

* add date filter to listens screen which lets you jump to a date in the past ([d913efd](https://github.com/lydavid/MusicSearch/commit/d913efdc0fcab91485ff1e3fdbbcdf349dc9bf09))
* add icons next to collections in "Add to collection" sheet to represent that none/some of the selected items are in the collection already ([64dea7f](https://github.com/lydavid/MusicSearch/commit/64dea7f1ddf249c95a3db1eba20f24debb1bcc44))
* from a listen item, add ability to filter starting from its listen date ([72eb1b2](https://github.com/lydavid/MusicSearch/commit/72eb1b2c926aae542754e10ee4f8c3b92f29508e))

## [1.108.0](https://github.com/lydavid/MusicSearch/compare/v1.107.0...v1.108.0) (2026-04-27)


### Features

* show label begin/end date in label lists ([b55f4d7](https://github.com/lydavid/MusicSearch/commit/b55f4d70fcd7edfb3cc6957ebd4e2ad51ccb570d))
* support sorting areas by cached order, name, or begin date, ascending/descending; default sort order is now cached order ascending; previous default was begin date ascending, then name ascending ([d0ade97](https://github.com/lydavid/MusicSearch/commit/d0ade97a5b2554e3f2e6eefc43bf485831d238fb))
* support sorting events by cached order, name, or start date, ascending/descending; default sort order is now cached order ascending; previous default was start date ascending ([c53e37c](https://github.com/lydavid/MusicSearch/commit/c53e37c7b933b50480ae5b2d098e4c5b8c6f3620))
* support sorting labels by cached order, name, begin date, or label code ascending/descending; default sort order is now cached order ascending; previous default was GUID ([efc3539](https://github.com/lydavid/MusicSearch/commit/efc35391a7a316af6773995a7258a79444e8111d))
* support sorting places by cached order, name, address or open date, ascending/descending; default sort order is now cached order ascending; previous default was name, then address ascending ([1072ade](https://github.com/lydavid/MusicSearch/commit/1072ade3a3b01e13aebc60995d5598d469af91e2))

## [1.107.0](https://github.com/lydavid/MusicSearch/compare/v1.106.1...v1.107.0) (2026-04-26)


### Bug Fixes

* don't show filtered count header when total count is 0 because it's an intermediate state ([f95aeda](https://github.com/lydavid/MusicSearch/commit/f95aedadf94415ef3e8e9b81555d28a66786aab3))
* get name from work type rather than type id because search does not give us its type id ([cd0c738](https://github.com/lydavid/MusicSearch/commit/cd0c7381868c43d78899b334227122399a636587))
* map work types to localizable text ([9eab6d4](https://github.com/lydavid/MusicSearch/commit/9eab6d4f25425ec77154259fa429b81c1c4e9888))


### Features

* add tri-state checkbox to (de)select all release statuses to filter ([0be5701](https://github.com/lydavid/MusicSearch/commit/0be5701faf1c23d6193fb8cb740ebf17aa433731))
* support sorting artists by cache order (new default), alphabetically, or begin date (previous default), ascending/descending ([b99572a](https://github.com/lydavid/MusicSearch/commit/b99572a2664ddb0889b7e468fd665853e87862d9))
* support sorting works by cached order, name, or listens, ascending/descending; default sort order is now cached order ascending; previous default was based on its GUID, which was nonsensical ([2191b50](https://github.com/lydavid/MusicSearch/commit/2191b50f3e5541012730d1dedb37d1f4ebb6a338))

## [1.106.1](https://github.com/lydavid/MusicSearch/compare/v1.106.0...v1.106.1) (2026-04-25)


### Bug Fixes

* do not show "Showing X of Y" header when filtered count (X) is 0, which is an intermediate state that would shortly change ([6c02931](https://github.com/lydavid/MusicSearch/commit/6c02931e483ebdc1625365bf81ea06d39cb44f5e))

## [1.106.0](https://github.com/lydavid/MusicSearch/compare/v1.105.0...v1.106.0) (2026-04-23)


### Bug Fixes

* store release status as a 1 byte INTEGER rather than 36 bytes TEXT; migrate database version to 76 ([fb2bff3](https://github.com/lydavid/MusicSearch/commit/fb2bff3d5ef2865738cddf4d17a44d8364eb82a0))


### Features

* support filtering releases by its status (e.g. official, promotion) ([2d0e42d](https://github.com/lydavid/MusicSearch/commit/2d0e42d26995676756989ae5f9de1c37cf03b0bb))

## [1.105.0](https://github.com/lydavid/MusicSearch/compare/v1.104.0...v1.105.0) (2026-04-20)


### Features

* add buttons to switch between looking up URL in MusicBrainz or local database ([8f9c0f7](https://github.com/lydavid/MusicSearch/commit/8f9c0f7d08e85c28ab05d98ef06f8632a3b71c9b))
* add screen to Search screen's additional actions to lookup a URL in MusicBrainz and show list items if it exists ([bbf73dd](https://github.com/lydavid/MusicSearch/commit/bbf73dd425dd19941b02b8b2e25589e6c0d53e58))

## [1.104.0](https://github.com/lydavid/MusicSearch/compare/v1.103.2...v1.104.0) (2026-04-17)


### Features

* add release status stats ([398db62](https://github.com/lydavid/MusicSearch/commit/398db620d1083ed77bd2fef753a749937749594b))
* show number of items shown out of total as a header list item when filtering ([d82c40d](https://github.com/lydavid/MusicSearch/commit/d82c40d0c4d73769464e6ee180cb8cbe367c30e4))
* support adding items to collections from search screen ([949f4d3](https://github.com/lydavid/MusicSearch/commit/949f4d32cbc7b9b86717576cf652d13b066a7c17))

## [1.103.2](https://github.com/lydavid/MusicSearch/compare/v1.103.1...v1.103.2) (2026-04-15)


### Bug Fixes

* sometimes the count of artists by collection is off due to not filtering by artist aliases ([78e2caf](https://github.com/lydavid/MusicSearch/commit/78e2caf44155d6b4627d5dd22142c5c7962c4438))

## [1.103.1](https://github.com/lydavid/MusicSearch/compare/v1.103.0...v1.103.1) (2026-04-14)


### Bug Fixes

* bring back sticky header for release group types ([32824d2](https://github.com/lydavid/MusicSearch/commit/32824d20db0d2fb23edfb29c242db78cf3aa72e6))

## [1.103.0](https://github.com/lydavid/MusicSearch/compare/v1.102.1...v1.103.0) (2026-04-13)


### Bug Fixes

* scale color wheel properly in landscape mode ([e97fce9](https://github.com/lydavid/MusicSearch/commit/e97fce919cd68f4a94c50dbbfe80feb5dd77b7c3))
* standardize datastore preferences names; use an enum to make sure we don't reuse the same name ([87706f0](https://github.com/lydavid/MusicSearch/commit/87706f058a71f9e45b210e6308eb4dd6ac09acbd))


### Features

* add option under Appearance to enable/disable scroll to hide top app bar; support this in more screens; default is now disable ([77cc903](https://github.com/lydavid/MusicSearch/commit/77cc903e7422f8f115d491be8157d6502e82a000))

## [1.102.1](https://github.com/lydavid/MusicSearch/compare/v1.102.0...v1.102.1) (2026-04-08)


### Bug Fixes

* do not lowercase German tab name in "Refresh X" ([00b20a9](https://github.com/lydavid/MusicSearch/commit/00b20a91c8e36303e607e1536a0764ba84bc2c98))

## [1.102.0](https://github.com/lydavid/MusicSearch/compare/v1.101.0...v1.102.0) (2026-04-07)


### Features

* **ios:** support exporting database to app Documents folder and expose this folder to Files app ([303762d](https://github.com/lydavid/MusicSearch/commit/303762de4cff141099d82d5970826520b0ce954f))

## [1.101.0](https://github.com/lydavid/MusicSearch/compare/v1.100.0...v1.101.0) (2026-04-06)


### Bug Fixes

* map release group types to localizable text ([67f1410](https://github.com/lydavid/MusicSearch/commit/67f14108cd47db2242047881d345551adc812767))


### Features

* add action to open in browser at listen timestamp from a listen ([aa4171d](https://github.com/lydavid/MusicSearch/commit/aa4171dca992a34a4586ff5b28c46ed5dc5c2ebb))

## [1.100.0](https://github.com/lydavid/MusicSearch/compare/v1.99.0...v1.100.0) (2026-04-05)


### Bug Fixes

* fix crash when clicking a listen's more action icon button due to image transition ([7ecf13e](https://github.com/lydavid/MusicSearch/commit/7ecf13e2b87d03b8a816ef8b552e86e97dc2923b))
* reduce excessive network requests to fetch data from Spotify/Wikimedia when refreshing a details screen ([d404ebf](https://github.com/lydavid/MusicSearch/commit/d404ebf1189b781cd22e2ed9de391473123e0b17))


### Features

* support getting artist image from Wikimedia if wikidata url exists as fallback if Spotify secrets or url does not exist ([571c3dd](https://github.com/lydavid/MusicSearch/commit/571c3ddfe39a744d13f5b9d09c5f3487b43fafb8))
* support opening wikimedia image url in wikimedia commons page that shows license; restructure image models; update database version to 76 ([0170a5b](https://github.com/lydavid/MusicSearch/commit/0170a5b8821b059675b3796c1456eabf6b2a772f))

## [1.99.0](https://github.com/lydavid/MusicSearch/compare/v1.98.0...v1.99.0) (2026-04-01)


### Bug Fixes

* show decoded Wikipedia url (without percent-encoding) ([17f0133](https://github.com/lydavid/MusicSearch/commit/17f01337302025d0a871629ed98c2289672acbdc))


### Features

* show highlight over filtered text in list screens ([0ff94ad](https://github.com/lydavid/MusicSearch/commit/0ff94adf37b6a2952926b0d70a74a1108d0f7805))

## [1.98.0](https://github.com/lydavid/MusicSearch/compare/v1.97.1...v1.98.0) (2026-03-31)


### Features

* get wikipedia extract based on device language setting ([#2107](https://github.com/lydavid/MusicSearch/issues/2107)) ([29c8581](https://github.com/lydavid/MusicSearch/commit/29c8581f792bd647e929a0fe6c4e87a51db0fa4e))

## [1.97.1](https://github.com/lydavid/MusicSearch/compare/v1.97.0...v1.97.1) (2026-03-30)


### Bug Fixes

* improve add to collection messages and make them localizable; show syncing message when adding to or deleting from a remote collection; use colored snackbar in all details screens ([674ad36](https://github.com/lydavid/MusicSearch/commit/674ad36b64a55ce2d06f582c4b383d8c7ffa40c5))
* only bold unvisited item's name without bolding its disambiguation and aliases ([d9ca1a1](https://github.com/lydavid/MusicSearch/commit/d9ca1a1a3868a8f351a510e43fc3fe55cef529e4))
* show " + ?" after remote collection count if you have not fetched all items in it (or if it's out of sync with the remote count from adding/deleting) ([0428f9a](https://github.com/lydavid/MusicSearch/commit/0428f9a7f62e7573cf216299ed9a17654bfa5bc8))
* support adding more than 400 entities at a time to a remote collection ([ff80759](https://github.com/lydavid/MusicSearch/commit/ff80759c9c02b1d982e51e1c19293fddb13fad6b))
* support deleting more than 400 entities at a time from a remote collection ([b2d8dff](https://github.com/lydavid/MusicSearch/commit/b2d8dff5e67b5d9c0dff05e4b8bcce66fac53083))

## [1.97.0](https://github.com/lydavid/MusicSearch/compare/v1.96.0...v1.97.0) (2026-03-29)


### Bug Fixes

* don't crash when visiting too many releases with many tracks ([1785429](https://github.com/lydavid/MusicSearch/commit/1785429317789a504191ebc76b05fd9764c00cf5))
* map artist gender to localized text ([9d73b36](https://github.com/lydavid/MusicSearch/commit/9d73b361ab31bfc826a1e45311f1c9c59f2e8acf))
* map artist type to localized text ([5c52386](https://github.com/lydavid/MusicSearch/commit/5c523865acdcd8bdac7e297716d9f1e8fbeeab6c))
* no more crashes when selecting 1000s of tracks, then putting the app in the background ([38c3417](https://github.com/lydavid/MusicSearch/commit/38c341799c27eaba5f13ec4985e9929898ec2f5e))
* only bold an unvisited entity's name/disambiguation/aliases instead of everything ([a130a88](https://github.com/lydavid/MusicSearch/commit/a130a884e69e7ec0415dc1c69666f3d2ba4eed6d))
* possibly speed up counts of entity in local database screen; show "?" instead of "0" while loading ([91fed26](https://github.com/lydavid/MusicSearch/commit/91fed265d382eb23a915c181894b727c610ebb19))


### Features

* enable selecting app language (English, German, French, or Estonian) through App Info for Android 13+ ([493a3b8](https://github.com/lydavid/MusicSearch/commit/493a3b8bea47a145dab57334b73528c29ea04a7c))
* show highlight over filtered text in Details screens ([070a166](https://github.com/lydavid/MusicSearch/commit/070a166ab983d4d7ad52218fc7271132ff0d32d8))

## [1.96.0](https://github.com/lydavid/MusicSearch/compare/v1.95.1...v1.96.0) (2026-03-27)


### Features

* support selecting tracks from a release to submit to ListenBrainz ([1d940f0](https://github.com/lydavid/MusicSearch/commit/1d940f0597a63018486d73b7dfe1e5ccb6eb8b0b))

## [1.95.1](https://github.com/lydavid/MusicSearch/compare/v1.95.0...v1.95.1) (2026-03-26)


### Bug Fixes

* fix crash when app is put in background while a release screen is part of the backstack (Parcel IllegalArgumentException) ([869edb7](https://github.com/lydavid/MusicSearch/commit/869edb7ee430d8f1b5b331fe7b3dad8db903b039))

## [1.95.0](https://github.com/lydavid/MusicSearch/compare/v1.94.0...v1.95.0) (2026-03-25)


### Bug Fixes

* "Select all" for tracks actually selects all (even when there are over hundreds) ([715477c](https://github.com/lydavid/MusicSearch/commit/715477c591aa7ee5960876f6a86ea16581fd7da0))
* standardize all dialogs' background color and corner radius ([15be3f7](https://github.com/lydavid/MusicSearch/commit/15be3f7b16251963e0f54215d3862db83345639f))


### Features

* **desktop:** support click to paste MusicBrainz authorization code ([caab152](https://github.com/lydavid/MusicSearch/commit/caab15246ee2acb25f6ec015ca2248c8949455e6))

## [1.94.0](https://github.com/lydavid/MusicSearch/compare/v1.93.1...v1.94.0) (2026-03-24)


### Features

* allow selecting all tracks to add their recordings to a collection; use a list instead of set when adding to a collection to preserve order ([35951dc](https://github.com/lydavid/MusicSearch/commit/35951dc5bf7d549006b7a63f56cec46dd5e6951e))

## [1.93.1](https://github.com/lydavid/MusicSearch/compare/v1.93.0...v1.93.1) (2026-03-23)


### Bug Fixes

* relocate "Show more info in release list items" setting to the release list screens ([cf6368c](https://github.com/lydavid/MusicSearch/commit/cf6368cf3890a9911698aab0e8f9d20973ba84cd))

## [1.93.0](https://github.com/lydavid/MusicSearch/compare/v1.92.0...v1.93.0) (2026-03-22)


### Bug Fixes

* fix IndexOutOfBoundsException crash in tracks list ([2762bfd](https://github.com/lydavid/MusicSearch/commit/2762bfda5e6fc75532fd94a4451f3f1768e1096e))


### Features

* support login from a collection's fullscreen error screen; add better error handling for MusicBrainz login for all platforms ([d55d4b9](https://github.com/lydavid/MusicSearch/commit/d55d4b9dbdc4c42da554723a5e2f364a97b814d2))

## [1.92.0](https://github.com/lydavid/MusicSearch/compare/v1.91.0...v1.92.0) (2026-03-19)


### Bug Fixes

* make sure artist credits still show up on tracks on older SDKs ([bbca559](https://github.com/lydavid/MusicSearch/commit/bbca5597a6a111f51dd2ad904afed1ca369e76dc))


### Features

* add more actions button to tracks list which supports adding the track's recording to a collection or submitting a listen of the track to ListenBrainz; show whether a track is part of a collection with a small icon ([61b8c65](https://github.com/lydavid/MusicSearch/commit/61b8c6509cdaddf9df680700b5ff623571c9ebb1))

## [1.91.0](https://github.com/lydavid/MusicSearch/compare/v1.90.3...v1.91.0) (2026-03-13)


### Bug Fixes

* limit date picker to valid dates ([d001505](https://github.com/lydavid/MusicSearch/commit/d001505c2cd461d7120ad23964ec497495929720))
* refresh listen count in recording screen after submitting listen ([dc041a6](https://github.com/lydavid/MusicSearch/commit/dc041a6f0af4b8194297f44ef73257e78ed26936))
* show non-soft-deleted listens and count in details screens (these are eventually actually deleted after leaving the listens screen) ([6c6e233](https://github.com/lydavid/MusicSearch/commit/6c6e233696f58405aafd51cdc2a59cba185fe0b0))


### Features

* add menu option in recording screen to manually submit listen to ListenBrainz ([#2057](https://github.com/lydavid/MusicSearch/issues/2057)) ([e0a0ccc](https://github.com/lydavid/MusicSearch/commit/e0a0ccc0cd7af4a4504002279b3c38d849eeaecd))

## [1.90.3](https://github.com/lydavid/MusicSearch/compare/v1.90.2...v1.90.3) (2026-03-05)


### Bug Fixes

* prefer MusicBrainz release ID that was submitted if it exists over the automatically linked release ID because it is usually more accurate ([2a60b07](https://github.com/lydavid/MusicSearch/commit/2a60b072b1a2ae3fa6224546d290ea15d35d98f6))

## [1.90.2](https://github.com/lydavid/MusicSearch/compare/v1.90.1...v1.90.2) (2026-03-03)


### Bug Fixes

* include UUID in the "no recording found" error message ([d2edf6f](https://github.com/lydavid/MusicSearch/commit/d2edf6f790f29b5756aa49d39dda3b36c05c2c84))

## [1.90.1](https://github.com/lydavid/MusicSearch/compare/v1.90.0...v1.90.1) (2026-03-01)


### Bug Fixes

* don't crash on somewhat common exceptions ([642c091](https://github.com/lydavid/MusicSearch/commit/642c09104414c527b3021dfbf706a4659bf35d97))
* resolve memory leak ([c619b84](https://github.com/lydavid/MusicSearch/commit/c619b84657660b57292878136b4683bc3e4122ef))

## [1.90.0](https://github.com/lydavid/MusicSearch/compare/v1.89.0...v1.90.0) (2026-02-25)


### Features

* show error snackbar when you try to link a listen to a non-recording UUID ([9f3e8b5](https://github.com/lydavid/MusicSearch/commit/9f3e8b51d6a403083db0980b8b06cba9dc16600f))

## [1.89.0](https://github.com/lydavid/MusicSearch/compare/v1.88.1...v1.89.0) (2026-02-24)


### Features

* show latest times you've listened to a work with a button to go to all listens with the work faceted; show all works with linked listens in Listen screen's facet bottom sheet ([73695d9](https://github.com/lydavid/MusicSearch/commit/73695d996152db7ca9b0cb0ff9e3ee878ace00ef))

## [1.88.1](https://github.com/lydavid/MusicSearch/compare/v1.88.0...v1.88.1) (2026-02-20)


### Bug Fixes

* fix licenses/libraries screen crash ([e246197](https://github.com/lydavid/MusicSearch/commit/e2461978267052dd288c9640a79325e7d6d7558f))

## [1.88.0](https://github.com/lydavid/MusicSearch/compare/v1.87.0...v1.88.0) (2026-02-19)


### Bug Fixes

* don't crash when selecting title to copy then navigating back ([9660726](https://github.com/lydavid/MusicSearch/commit/966072697242b316a3b7523d70be627b4b6fba23)), closes [#1673](https://github.com/lydavid/MusicSearch/issues/1673)


### Features

* support translations through Weblate ([5a19e30](https://github.com/lydavid/MusicSearch/commit/5a19e30f86b7003460ea0dcbb8c76233aa171e6c))




## [1.87.0](https://github.com/lydavid/MusicSearch/compare/v1.86.0...v1.87.0) (2026-01-16)


### Features

* show number of times user has listened to any recordings of a visited work in list views ([6d9acf0](https://github.com/lydavid/MusicSearch/commit/6d9acf0265bd5e81787c13eb71a226c98fd41448))

## [1.86.0](https://github.com/lydavid/MusicSearch/compare/v1.85.0...v1.86.0) (2026-01-14)


### Bug Fixes

* correctly parse title and artist from Android Now Playing notification for some more languages ([8bed10a](https://github.com/lydavid/MusicSearch/commit/8bed10a37b6a4292134ee4c4dc3f2164f6b0f3ce))


### Features

* show number of times you have listened to recordings of a work in work details screen ([b3f7366](https://github.com/lydavid/MusicSearch/commit/b3f7366a9e867bdf7a71096c3551515b08c8b0b8))

## [1.85.0](https://github.com/lydavid/MusicSearch/compare/v1.84.1...v1.85.0) (2026-01-10)


### Features

* show different color snackbar for deleting items from collection states (soft delete, hard delete loading/error/success) ([ca5c888](https://github.com/lydavid/MusicSearch/commit/ca5c888caad1e782bb38b0e446d5ef8e6f0a22aa))

## [1.84.1](https://github.com/lydavid/MusicSearch/compare/v1.84.0...v1.84.1) (2025-12-28)


### Bug Fixes

* show aliases for tracks when loading a release without having to click into a track first ([7f6b679](https://github.com/lydavid/MusicSearch/commit/7f6b679d22b2cb70c86c31b6baa62e7b04838d24))

## [1.84.0](https://github.com/lydavid/MusicSearch/compare/v1.83.0...v1.84.0) (2025-12-22)


### Features

* support deleting listens ([#1963](https://github.com/lydavid/MusicSearch/issues/1963)) ([6f18fd3](https://github.com/lydavid/MusicSearch/commit/6f18fd3ced94eb1df4914e465f53cc96d75716cb))

## [1.83.0](https://github.com/lydavid/MusicSearch/compare/v1.82.1...v1.83.0) (2025-12-09)


### Features

* show release group type if not sorting by types which show the type as a sticky separator ([c995e0d](https://github.com/lydavid/MusicSearch/commit/c995e0d0e0dfce2b9581e9ccf9243572187f7c55))

## [1.82.1](https://github.com/lydavid/MusicSearch/compare/v1.82.0...v1.82.1) (2025-12-05)


### Bug Fixes

* aliases no longer multiplies listen count ([c262909](https://github.com/lydavid/MusicSearch/commit/c2629093957af10716f29c20cd6e6bcfd95f8553))

## [1.82.0](https://github.com/lydavid/MusicSearch/compare/v1.81.0...v1.82.0) (2025-12-03)


### Bug Fixes

* constrain single line text fields' hint/label to one line ([a5e2b68](https://github.com/lydavid/MusicSearch/commit/a5e2b6830cda3b6f609bf12097e23f807044c144))


### Features

* add "paste from clipboard" button to listens screens to simplify pasting urls, mbids, user token, and username ([03739ef](https://github.com/lydavid/MusicSearch/commit/03739efcbc060e97834d36f60b9618c2ae48a61e))

## [1.81.0](https://github.com/lydavid/MusicSearch/compare/v1.80.0...v1.81.0) (2025-10-20)


### Bug Fixes

* always show the first release image as thumbnail; order an entity's images in same order from MusicBrainz ([157f558](https://github.com/lydavid/MusicSearch/commit/157f55848a00904d94b84c143acbc557cad6ab65))
* correct releases listen count in label screen ([30a3ebe](https://github.com/lydavid/MusicSearch/commit/30a3ebe6ef48b25dd9a1d9d6fedc0e7f06f3795d))
* fix crash when querying all releases ([a8c26b3](https://github.com/lydavid/MusicSearch/commit/a8c26b3710331b6c1bc7f14444ee6b91b59c2d64))


### Features

* support more sort options for release groups; default to sorting by insertion order; replace existing toggle sort ([18eb3c6](https://github.com/lydavid/MusicSearch/commit/18eb3c691a33ea01ceb4f293635dadfb662fbe3c))
* support sorting recordings by insertion (cache) order or the reverse ([4f51ccd](https://github.com/lydavid/MusicSearch/commit/4f51ccd92917f6da67808bb858f3b4f0156c9d8e))

## [1.80.0](https://github.com/lydavid/MusicSearch/compare/v1.79.2...v1.80.0) (2025-10-19)


### Bug Fixes

* drop unused view; update database version to 73 ([#1870](https://github.com/lydavid/MusicSearch/issues/1870)) ([9f6dc6d](https://github.com/lydavid/MusicSearch/commit/9f6dc6d191a3105a6f2fa231343dfd78400aab13))
* speed up browsing release groups by collection ([41d8511](https://github.com/lydavid/MusicSearch/commit/41d8511b3fe49a64b0339c0b0e2158e659dcca25))
* speed up searching releases (by not joining all releases) ([6972124](https://github.com/lydavid/MusicSearch/commit/6972124b8ca5e5f6d191f13f86c56061603abf6f))


### Features

* support more options for sorting releases (including by listens and complete listens); default to no sorting and replaces existing toggle sort ([28ccf4c](https://github.com/lydavid/MusicSearch/commit/28ccf4c1b11d6b4398af2c38a1d1151b6b50a7c0))

## [1.79.2](https://github.com/lydavid/MusicSearch/compare/v1.79.1...v1.79.2) (2025-10-17)


### Bug Fixes

* correct track listen count so that they are no longer multiplied by aliases ([d28a485](https://github.com/lydavid/MusicSearch/commit/d28a485eea441111cf8c463501033feac65b79ed))
* improve speed browsing release groups by entity by not scanning the entire release group table ([d3869e6](https://github.com/lydavid/MusicSearch/commit/d3869e60a79c7258a90f54fcdca282f0cfc6621c))
* significantly improve speed browsing releases by label or collection ([2e0cb67](https://github.com/lydavid/MusicSearch/commit/2e0cb67477716e4ab8a4ad8662430ae8f2990f18))

## [1.79.1](https://github.com/lydavid/MusicSearch/compare/v1.79.0...v1.79.1) (2025-10-16)


### Bug Fixes

* significantly improve speed browsing releases by entity ([3854fdb](https://github.com/lydavid/MusicSearch/commit/3854fdb581817a6ccc4bcb7248ab9f5dd4c0fdcb))

## [1.79.0](https://github.com/lydavid/MusicSearch/compare/v1.78.1...v1.79.0) (2025-10-14)


### Features

* show number of listens and complete listens for each release in list screens; fix paging releases in some screens (such as labels); update database version to 72 ([#1860](https://github.com/lydavid/MusicSearch/issues/1860)) ([5481e6c](https://github.com/lydavid/MusicSearch/commit/5481e6c049fee6088e8766d3bdebe6a3f95577fe))

## [1.78.1](https://github.com/lydavid/MusicSearch/compare/v1.78.0...v1.78.1) (2025-10-13)


### Bug Fixes

* show dismissable snackbar on receiving error from Wikidata/Wikipedia (for all details screens) ([d3982b6](https://github.com/lydavid/MusicSearch/commit/d3982b6694e50fe89c8c9ca6b2aff31b993de64d))

## [1.78.0](https://github.com/lydavid/MusicSearch/compare/v1.77.0...v1.78.0) (2025-10-12)


### Bug Fixes

* move "See collaborators" above the dropdown menu items that may change between tabs ([16dbdb4](https://github.com/lydavid/MusicSearch/commit/16dbdb402e274d824a703f128cd28c29fedbe6b4))
* support scrolling recording sort options ([78cd7d1](https://github.com/lydavid/MusicSearch/commit/78cd7d14c85541dc5ba182f4e442cb0eabb37a24))


### Features

* move all details screen stats from tab to dropdown menu (to align with stats for other screens) ([d104d1e](https://github.com/lydavid/MusicSearch/commit/d104d1e9649b779f0f6f57fa3d8df02493055948))
* move artist's stats from tab to dropdown menu (to align with stats for other screens); try screenshot tests with `ContentWithOverlays` ([33863d3](https://github.com/lydavid/MusicSearch/commit/33863d3840f63dbb2d05b487814383fda8375fcf))

## [1.77.0](https://github.com/lydavid/MusicSearch/compare/v1.76.1...v1.77.0) (2025-10-11)


### Features

* support sorting recordings by name, date, or listens (ascending or descending); default to no sorting (will be in order that MusicBrainz API gives us) ([c92473b](https://github.com/lydavid/MusicSearch/commit/c92473bcda864605e34ec6e108e4fd1493b954a7))

## [1.76.1](https://github.com/lydavid/MusicSearch/compare/v1.76.0...v1.76.1) (2025-10-10)


### Bug Fixes

* lookup and cache details data in the background to prevent UI from freezing ([75aca4a](https://github.com/lydavid/MusicSearch/commit/75aca4ada2902b33d5b1f7195896c2680c15891f))

## [1.76.0](https://github.com/lydavid/MusicSearch/compare/v1.75.0...v1.76.0) (2025-10-07)


### Bug Fixes

* show recording collections to add to from recording screen instead of area collections ([5ba7390](https://github.com/lydavid/MusicSearch/commit/5ba739090e83565e4c42b4f838fc5dea2643caf4))


### Features

* show listens in recordings list screens (if ListenBrainz username is set) ([a7335c2](https://github.com/lydavid/MusicSearch/commit/a7335c2ff0adaf5f17fa8718bf20d6565a2c5c16))

## [1.75.0](https://github.com/lydavid/MusicSearch/compare/v1.74.0...v1.75.0) (2025-10-06)


### Bug Fixes

* do not delete locally cached data if network call fails while refreshing ([d6140d1](https://github.com/lydavid/MusicSearch/commit/d6140d192963e4f9764ee687576c14af63472895))
* remove duplicate edges in collaboration graph ([becef4b](https://github.com/lydavid/MusicSearch/commit/becef4b33a40604798a300a4b5bd1489d9b6c9c5))


### Features

* show label's area in details screen ([#1847](https://github.com/lydavid/MusicSearch/issues/1847)) ([4e4c4ac](https://github.com/lydavid/MusicSearch/commit/4e4c4ac530f863db83a1b7034738e489fcbf0e06))
* show number of times user has listened to an album completely with a star icon (in release details) ([0c50163](https://github.com/lydavid/MusicSearch/commit/0c5016385487b832117350a85154540761c7ec00))

## [1.74.0](https://github.com/lydavid/MusicSearch/compare/v1.73.0...v1.74.0) (2025-10-05)


### Bug Fixes

* preserve facets query when dismissing bottom sheet ([6dddf00](https://github.com/lydavid/MusicSearch/commit/6dddf00c7f8cd9e8b6f7a4ccf6d29227a9149e33))


### Features

* show button to clear facets if are selected ([dd72fd4](https://github.com/lydavid/MusicSearch/commit/dd72fd4b4bfcb58b4612dbab073e516347899d75))
* support filtering listens by a specific artist (on the listen's linked recording); show all listens from an artist screen goes to listens screen with this filter pre-applied ([bb7a633](https://github.com/lydavid/MusicSearch/commit/bb7a63306a3c7b81895e8d5e45633cb4da271024))

## [1.73.0](https://github.com/lydavid/MusicSearch/compare/v1.72.2...v1.73.0) (2025-10-04)


### Bug Fixes

* align color for disambiguation and alias in facets to rest of app ([2288221](https://github.com/lydavid/MusicSearch/commit/2288221aed573dc5c575a393451df3501d6c47f6))
* show recording disambiguation and alias in facets list ([09e5402](https://github.com/lydavid/MusicSearch/commit/09e540252355d31fc04ffb81c130a1f6a068909c))


### Features

* show the latest 3 listens for an artist ([7411814](https://github.com/lydavid/MusicSearch/commit/7411814a0681cd48937a445bef8fe54214209ce6))

## [1.72.2](https://github.com/lydavid/MusicSearch/compare/v1.72.1...v1.72.2) (2025-10-03)


### Bug Fixes

* support scrolling listen's additional actions bottom sheet ([8e29a45](https://github.com/lydavid/MusicSearch/commit/8e29a45126c280ac01914d8a62259ba871ee264f))

## [1.72.1](https://github.com/lydavid/MusicSearch/compare/v1.72.0...v1.72.1) (2025-10-01)


### Bug Fixes

* count distinct collected entities, not the container entity ([096cf33](https://github.com/lydavid/MusicSearch/commit/096cf33e08079802c5bf9d0912db707100f6165d))

## [1.72.0](https://github.com/lydavid/MusicSearch/compare/v1.71.0...v1.72.0) (2025-09-27)


### Bug Fixes

* update scrollable tab row to material 3 secondary scrollable tab row ([f2f9423](https://github.com/lydavid/MusicSearch/commit/f2f94234c8523e67a60f51e4225bbeccdff7c8a8))


### Features

* support filtering listens by a specific release; show all listens from a release screen goes to listens screen with this filter pre-applied ([e8e2b4c](https://github.com/lydavid/MusicSearch/commit/e8e2b4cf0e27b7f5d3ffd0b7436f0218b987122d))

## [1.71.0](https://github.com/lydavid/MusicSearch/compare/v1.70.1...v1.71.0) (2025-09-23)


### Features

* support manually linking a listen with a MusicBrainz recording by entering its id or url ([e64963c](https://github.com/lydavid/MusicSearch/commit/e64963c201716289bad11e51bf071c3179f7be64))

## [1.70.1](https://github.com/lydavid/MusicSearch/compare/v1.70.0...v1.70.1) (2025-09-22)


### Bug Fixes

* link listen's album to release id rather than its image's release id if it exists, fallback to image release id otherwise; update database version to 69 ([ba3e2d8](https://github.com/lydavid/MusicSearch/commit/ba3e2d8f3c9b02ff5be9f406c041f7ba0b758bc6))
* only count distinct entities in collected stats to prevent overcounting those that are part of multiple collections ([12e1b2f](https://github.com/lydavid/MusicSearch/commit/12e1b2fafd377c702e59b0eae4708ea63a30e256))
* remove duplicate images based on url with http/https and .jpg stripped; update database version to 70 ([#1827](https://github.com/lydavid/MusicSearch/issues/1827)) ([20eaa60](https://github.com/lydavid/MusicSearch/commit/20eaa60c2f5e9dbf9ae5297a7dd565faf970d560))
* use more consistent sentence-casing for titles and menu items (e.g. "Refresh details") ([be8f8e3](https://github.com/lydavid/MusicSearch/commit/be8f8e30e4df8a00839cd46680b8db9120292239))

## [1.70.0](https://github.com/lydavid/MusicSearch/compare/v1.69.0...v1.70.0) (2025-09-20)


### Bug Fixes

* clear ListenBrainz token from text field after logging in ([a0515cc](https://github.com/lydavid/MusicSearch/commit/a0515cc35b4e01138178df2aafe0c1945882d267))


### Features

* support refreshing the metadata mapping of a user's listen if you are logged in as the user ([6450085](https://github.com/lydavid/MusicSearch/commit/6450085b6e1c7f31df71ffc61d8f1063a526fb2a))

## [1.69.0](https://github.com/lydavid/MusicSearch/compare/v1.68.0...v1.69.0) (2025-09-19)


### Bug Fixes

* record latest entity id in history, and merge old history record with it ([04b91bf](https://github.com/lydavid/MusicSearch/commit/04b91bfb36d6ff15b01a710ec7cddab699eb1082))
* update recording id on tracks/listens when a recording id has changed upon visiting the recording ([924758d](https://github.com/lydavid/MusicSearch/commit/924758de8f70aa6ef12b1cb5a5c2afe56e1f4af4))


### Features

* support filtering listens by those with unlinked recordings; update database version to 68 ([#1815](https://github.com/lydavid/MusicSearch/issues/1815)) ([c5e1758](https://github.com/lydavid/MusicSearch/commit/c5e1758a3e83f371366ad3aa38f23c443c110efc))

## [1.68.0](https://github.com/lydavid/MusicSearch/compare/v1.67.0...v1.68.0) (2025-09-17)


### Bug Fixes

* do not show filters when no username is set ([8392888](https://github.com/lydavid/MusicSearch/commit/839288821cef03c1e233c176dfbdde7a9db5f5e6))


### Features

* support filtering on a song via more actions bottom sheet or dismissing the filter if it exists ([cf5cd04](https://github.com/lydavid/MusicSearch/commit/cf5cd04ad6abc4fac32022ced7dd71469ba5a02f))

## [1.67.0](https://github.com/lydavid/MusicSearch/compare/v1.66.0...v1.67.0) (2025-09-16)


### Features

* allow filtering listens by a recording (via a facet); see all listens from recording details goes to this screen with the facet applied ([3504e53](https://github.com/lydavid/MusicSearch/commit/3504e538d0758a3178d487abe65a7d7838c7a8e8))
* show icon on recording list items that it's a video; show whether a recording is a video in its details screen ([acea1c2](https://github.com/lydavid/MusicSearch/commit/acea1c2f97dc7aef7c54326c309deb73cf4515c6))

## [1.66.0](https://github.com/lydavid/MusicSearch/compare/v1.65.0...v1.66.0) (2025-09-14)


### Bug Fixes

* "Refresh Tracks" menu item actually deletes local tracks and inserts new tracks from MusicBrainz ([1aeb9e6](https://github.com/lydavid/MusicSearch/commit/1aeb9e67203539abd049da4c18b1528fe414b3bb))
* fix crash when clicking on an item whose id has changed (usually due to it being merged into another entity in MusicBrainz) ([3648147](https://github.com/lydavid/MusicSearch/commit/3648147a6290545ec83f488dbc00468a9882f854))


### Features

* show number of times user has listened to an artist ([5f294c2](https://github.com/lydavid/MusicSearch/commit/5f294c2fdc2317008217296eef89d964e9eb0548))

## [1.65.0](https://github.com/lydavid/MusicSearch/compare/v1.64.2...v1.65.0) (2025-09-13)


### Bug Fixes

* only show option to sort images in the images grid screen from Database because it does not affect the other screens ([4e54051](https://github.com/lydavid/MusicSearch/commit/4e54051c75c6c9740c96ed1ab66d95712248901d))


### Features

* if there is only one image, open the single image in zoomable pager view ([3cdf83a](https://github.com/lydavid/MusicSearch/commit/3cdf83a262386c5e7b9018072d3a9777aeebaf0e))
* support clicking artist image to expand it ([1384dc1](https://github.com/lydavid/MusicSearch/commit/1384dc185ba6b8db3cffdeeae32c0948202d0edc))

## [1.64.2](https://github.com/lydavid/MusicSearch/compare/v1.64.1...v1.64.2) (2025-09-11)


### Bug Fixes

* store relation type id and attribute ids; also store all attribute values and display them; update database version to 67 ([#1784](https://github.com/lydavid/MusicSearch/issues/1784)) ([a510c77](https://github.com/lydavid/MusicSearch/commit/a510c775619fc9e57874c2288375d19254b41630))

## [1.64.1](https://github.com/lydavid/MusicSearch/compare/v1.64.0...v1.64.1) (2025-09-10)


### Bug Fixes

* replace with composite index because we always join with both listen's username and recording_musicbrainz_id ([3739621](https://github.com/lydavid/MusicSearch/commit/373962166b9a55e790ef13c80efc96f7e5f368cd))

## [1.64.0](https://github.com/lydavid/MusicSearch/compare/v1.63.1...v1.64.0) (2025-09-08)


### Bug Fixes

* add index on track's recording_id because it's used for joining with aliases; update database version to 65 ([0809872](https://github.com/lydavid/MusicSearch/commit/08098721ffe225223539164ee469047310bd26a8))
* do not use tooltip because it's crashing on desktop ([70a649f](https://github.com/lydavid/MusicSearch/commit/70a649fb6b9aa18b0c64cd0a24fc57b7f622878f))


### Features

* show number of times the user has listened to tracks (recordings) on a release if ListenBrainz username is set; show relatively how often you've listened to tracks on a release with a bar (the most listened tracks will be full) ([c4bec74](https://github.com/lydavid/MusicSearch/commit/c4bec74732892e93a193ceaf0f9d34fe8e116d51))

## [1.63.1](https://github.com/lydavid/MusicSearch/compare/v1.63.0...v1.63.1) (2025-09-07)


### Bug Fixes

* fix desktop app crash when migrating to database version 59+ ([eb8fadb](https://github.com/lydavid/MusicSearch/commit/eb8fadbeb0ead2b7618341c97f94e409de02cce8))

## [1.63.0](https://github.com/lydavid/MusicSearch/compare/v1.62.1...v1.63.0) (2025-09-06)


### Bug Fixes

* use 24 hour format for timestamp for now) ([cc5340f](https://github.com/lydavid/MusicSearch/commit/cc5340fdb0305d99a379a9b924a5932d16ee30dc))


### Features

* show number of times the user has listened to a recording if ListenBrainz username is set ([527bafd](https://github.com/lydavid/MusicSearch/commit/527bafd0955b35c0e2102e9bebde8c57076025ed))

## [1.62.1](https://github.com/lydavid/MusicSearch/compare/v1.62.0...v1.62.1) (2025-09-05)


### Bug Fixes

* fix last updated footer pushing content up for all list screens; do not show footer when empty (you can still see last updated in stats) ([70d117e](https://github.com/lydavid/MusicSearch/commit/70d117ea8b03ef5c51666b4c57ac4acf7cd1c586))

## [1.62.0](https://github.com/lydavid/MusicSearch/compare/v1.61.1...v1.62.0) (2025-09-03)


### Bug Fixes

* fix footer pushing content for releases; create view for release; update database version to 64 ([#1766](https://github.com/lydavid/MusicSearch/issues/1766)) ([a2667aa](https://github.com/lydavid/MusicSearch/commit/a2667aab0a25b95ea713b4fe54eeafbebb21f0bf))


### Features

* add toggle option to sort/unsort releases by date (unsorted by default); change release groups to be unsorted by default ([7fcd344](https://github.com/lydavid/MusicSearch/commit/7fcd3441c5cb3f48b9e637dc9242c680193ce5de))

## [1.61.1](https://github.com/lydavid/MusicSearch/compare/v1.61.0...v1.61.1) (2025-09-02)


### Bug Fixes

* store empty string instead of null when values don't exist for recording/series/work for consistency; update database version to 62 ([#1764](https://github.com/lydavid/MusicSearch/issues/1764)) ([460075d](https://github.com/lydavid/MusicSearch/commit/460075db73259fc1cc7e46cd2156fb0d425dcc02))
* store empty string instead of null when values don't exist for release for consistency; drop unused cover_art_count; drop redundant status and map it from status_id; update database version to 63 ([#1765](https://github.com/lydavid/MusicSearch/issues/1765)) ([d29294a](https://github.com/lydavid/MusicSearch/commit/d29294a7e4a0a73371b34969038dd07976b63313))

## [1.61.0](https://github.com/lydavid/MusicSearch/compare/v1.60.2...v1.61.0) (2025-09-01)


### Bug Fixes

* fix footer pushing content up by joining release groups with browse metadata to add last updated footer; add release group view; update database version to 61 ([#1762](https://github.com/lydavid/MusicSearch/issues/1762)) ([b2767ad](https://github.com/lydavid/MusicSearch/commit/b2767ad81d4f071ce67c6b02c74e1d399292c040))
* store empty string instead of null when values don't exist for area/genre/instrument/label/place for consistency; update database version to 60 ([#1759](https://github.com/lydavid/MusicSearch/issues/1759)) ([3e3ca27](https://github.com/lydavid/MusicSearch/commit/3e3ca27f3a1843f5345967e47f941cad19e421f5))


### Features

* show listen's recording disambiguation and primary alias if they exist ([#1761](https://github.com/lydavid/MusicSearch/issues/1761)) ([acaa508](https://github.com/lydavid/MusicSearch/commit/acaa508ca6a8bf3e57c5d598d693144e2791d2b3))
* show when label was founded/defunct under Details ([c173bb8](https://github.com/lydavid/MusicSearch/commit/c173bb8d471f28292e3abfa0a013b1fad8896ac9))

## [1.60.2](https://github.com/lydavid/MusicSearch/compare/v1.60.1...v1.60.2) (2025-08-31)


### Bug Fixes

* store empty string instead of null when values don't exist for event for consistency; insert 0 instead of empty string for booleans; update database version to 59 ([#1758](https://github.com/lydavid/MusicSearch/issues/1758)) ([1fe6acc](https://github.com/lydavid/MusicSearch/commit/1fe6acc353c21564026b6d7227ee672ab5494914))

## [1.60.1](https://github.com/lydavid/MusicSearch/compare/v1.60.0...v1.60.1) (2025-08-30)


### Bug Fixes

* store empty string instead of null when values don't exist for artist for consistency; update database version to 58 ([#1757](https://github.com/lydavid/MusicSearch/issues/1757)) ([dcae89f](https://github.com/lydavid/MusicSearch/commit/dcae89f87a50ba9abf3e29e31f1970736f8d0835))

## [1.60.0](https://github.com/lydavid/MusicSearch/compare/v1.59.2...v1.60.0) (2025-08-29)


### Bug Fixes

* remove repeated artist credits and ensure an entity can be linked to at most one `artist_credit` through `artist_credit_entity`; update database version to 57 ([#1755](https://github.com/lydavid/MusicSearch/issues/1755)) ([0966e48](https://github.com/lydavid/MusicSearch/commit/0966e48c8242fab301122d89994aec5f34c5aa11))


### Features

* support ListenBrainz authentication by inputting a user token ([d26de24](https://github.com/lydavid/MusicSearch/commit/d26de24de16c11390d293a40f792179e0ae90128))

## [1.59.2](https://github.com/lydavid/MusicSearch/compare/v1.59.1...v1.59.2) (2025-08-27)


### Bug Fixes

* include isrcs when browsing or visiting recording ([1b5ae47](https://github.com/lydavid/MusicSearch/commit/1b5ae4704b5c161009cb9caa8c098c2fcafe43fb))
* update recording when visiting it for the first time (so when coming from Listens, it will likely add info like the first release date) ([f4f53b7](https://github.com/lydavid/MusicSearch/commit/f4f53b79eb060873db3639f3bb1123e91bc6749d))
* update release when visiting it for the first time (so when coming from Listens, it will likely add info like barcode and release date) ([09aaf4d](https://github.com/lydavid/MusicSearch/commit/09aaf4de3621016fda7c5b27da47d3763dc8425c))

## [1.59.1](https://github.com/lydavid/MusicSearch/compare/v1.59.0...v1.59.1) (2025-08-26)


### Bug Fixes

* add checks for reaching latest or oldest listen and stop trying to get more listens temporarily (until returning) ([321f7ae](https://github.com/lydavid/MusicSearch/commit/321f7aeb2928fdc83def49a918a978ff753d961f))
* export app database on IO thread so that UI does not freeze up ([96504fe](https://github.com/lydavid/MusicSearch/commit/96504fe21864583253dda64738b028a34103b0ae))
* go back to default 10s timeout now that we know there's an existing issue with getting listens spaced out far apart in time ([17189ef](https://github.com/lydavid/MusicSearch/commit/17189ef67a1e2a31bd65e6ea248f2536c80c8137))
* keep listen list item position when returning to screen from back stack even when there is a filter query ([aa10e9e](https://github.com/lydavid/MusicSearch/commit/aa10e9eb65a7862f1ba1fc412ab5ba3efc9b3fc5))
* use list item key to keep its scroll position which should result in less jumps when loading images for the first time ([a0b25b6](https://github.com/lydavid/MusicSearch/commit/a0b25b64866ab6a6770ed05d8426ebeb3622d904))

## [1.59.0](https://github.com/lydavid/MusicSearch/compare/v1.58.0...v1.59.0) (2025-08-25)


### Bug Fixes

* downgrade AGP to 8.11.1 ([2261fca](https://github.com/lydavid/MusicSearch/commit/2261fca3ed7840356d162ecfd6499657cc422d5c))


### Features

* add "open in browser" menu item that opens the user's listens in ListenBrainz ([812b214](https://github.com/lydavid/MusicSearch/commit/812b214548ec6c40f0388366e881a755524e60bf))
* group listens by day, showing the formatted date in a sticky header and formatted time on each listen ([cc7938b](https://github.com/lydavid/MusicSearch/commit/cc7938ba9c5178d6af8d4df3d5fd16469b1642af))

## [1.58.0](https://github.com/lydavid/MusicSearch/compare/v1.57.0...v1.58.0) (2025-08-24)


### Bug Fixes

* retain listens scroll position when returning from back stack ([ff51d53](https://github.com/lydavid/MusicSearch/commit/ff51d53eb88e756700450f6f1466656891f1772f))
* use a sticky header for all list separators (e.g. mediums, release group types, and dates) ([815093d](https://github.com/lydavid/MusicSearch/commit/815093d5a9303d522aa2ea363cf3cfc11bf9d542))


### Features

* add more actions button to ListenBrainz listens that opens bottom sheet; can navigate to album (release) directly if its mapping exists ([60c6155](https://github.com/lydavid/MusicSearch/commit/60c6155171e34e448fe87fe4dfe09dac9878a119))
* allow selecting track name, artist credits, and release name in bottom sheet header ([39c27e2](https://github.com/lydavid/MusicSearch/commit/39c27e21490639c84111db3995549c735aaa1401))
* bold recording name and release name in listens screen if unvisited ([25ad76b](https://github.com/lydavid/MusicSearch/commit/25ad76bc3750575b92ff3649f7cfc421d51ed918))
* show listen track duration ([2a91f31](https://github.com/lydavid/MusicSearch/commit/2a91f31e7fa7640b2d2b27c0a147a6416fc466ab))
* show total number of listens downloaded ([bb85ce5](https://github.com/lydavid/MusicSearch/commit/bb85ce5b3a738b02853edd58a78ebd90b5a94452))

## [1.57.0](https://github.com/lydavid/MusicSearch/compare/v1.56.0...v1.57.0) (2025-08-23)


### Bug Fixes

* deduplicate images with the same thumbnail url and prevent them from being added; update database version to 56 ([ae47429](https://github.com/lydavid/MusicSearch/commit/ae47429b748188cc1f53e84a34bc17c80b481339))


### Features

* add ability to download/browse ListenBrainz listens by entering a username; update database version to 55 ([#1736](https://github.com/lydavid/MusicSearch/issues/1736)) ([2505d0d](https://github.com/lydavid/MusicSearch/commit/2505d0d1c551770a0396cb9a39c5d54fcff437b1)), closes [#1733](https://github.com/lydavid/MusicSearch/issues/1733)

## [1.56.0](https://github.com/lydavid/MusicSearch/compare/v1.55.2...v1.56.0) (2025-08-18)


### Bug Fixes

* make search's resource dropdown's text field fill half the screen ([cdabaae](https://github.com/lydavid/MusicSearch/commit/cdabaaed682f35f012ffa9ad835df6c5e61f48c0))


### Features

* highlight filter text for collections ([0ac9ceb](https://github.com/lydavid/MusicSearch/commit/0ac9ceb484f3ab1a5de9221d6c82dc69d8452641))

## [1.55.2](https://github.com/lydavid/MusicSearch/compare/v1.55.1...v1.55.2) (2025-08-11)


### Bug Fixes

* downgrade AGP to 8.11.1 to allow building on F-Droid; prevent renovate from upgrading for now ([552ec89](https://github.com/lydavid/MusicSearch/commit/552ec8955fdd8797e291ad3d38cebf7fe96a76f2))

## [1.55.1](https://github.com/lydavid/MusicSearch/compare/v1.55.0...v1.55.1) (2025-08-05)


### Bug Fixes

* drop unused work `language` from database, copying it over to `languages` if not set; update database version to 54 ([#1705](https://github.com/lydavid/MusicSearch/issues/1705)) ([9e43877](https://github.com/lydavid/MusicSearch/commit/9e43877a6823bc27d04f16b484c9cda99faf7d95))

## [1.55.0](https://github.com/lydavid/MusicSearch/compare/v1.54.0...v1.55.0) (2025-08-04)


### Bug Fixes

* can load more than 100 tracks again ([3a852f6](https://github.com/lydavid/MusicSearch/commit/3a852f6d353b5a5310253f6cf4b47b68cddfba49))
* move medium position before the medium format and name to avoid confusing it for counting the number of a format in a release ([1030d55](https://github.com/lydavid/MusicSearch/commit/1030d552a451427eff163a88149a2226ac30dab2))


### Features

* show the display language of aliases in details and relocate the locale so that it doesn't get cut off by a long alias name ([73b0c95](https://github.com/lydavid/MusicSearch/commit/73b0c952b38b0270686bbfadeee91d7a9e62b5a6))

## [1.54.0](https://github.com/lydavid/MusicSearch/compare/v1.53.0...v1.54.0) (2025-08-04)


### Bug Fixes

* correctly save alias start and end date ([035a33a](https://github.com/lydavid/MusicSearch/commit/035a33a2263a65872acf48e946db7837644a0666))


### Features

* make collapsible headers in details tab sticky ([aa39e61](https://github.com/lydavid/MusicSearch/commit/aa39e6102657fa06f6b9fe0089dd40c6292f650b))
* show all aliases in details screen under external links in a collapsible section ([7fee157](https://github.com/lydavid/MusicSearch/commit/7fee157b5dc3bf0c7f5b295971f01d22a5b00637))
* show number of urls and aliases; when filtering, show currently shown out of total ([bf015ad](https://github.com/lydavid/MusicSearch/commit/bf015ad3cfc9a465c660b38d21dc2cdbf497156f))

## [1.53.0](https://github.com/lydavid/MusicSearch/compare/v1.52.0...v1.53.0) (2025-08-03)


### Features

* show primary alias based on device locale for relationships; update database version to 53 ([#1699](https://github.com/lydavid/MusicSearch/issues/1699)) ([e552d49](https://github.com/lydavid/MusicSearch/commit/e552d49ca9d68b0c581003f7a4699786cc22c839))
* show relationships last updated in stats ([3e8dd06](https://github.com/lydavid/MusicSearch/commit/3e8dd0625b3bc57571f5938dad19fac657ed1772))

## [1.52.0](https://github.com/lydavid/MusicSearch/compare/v1.51.0...v1.52.0) (2025-08-02)


### Features

* show primary alias based on device locale for tracks (from its recording's aliases) ([4b3c7c5](https://github.com/lydavid/MusicSearch/commit/4b3c7c5a356b4e0d6b3c07d7c302ec6d45123f2d))

## [1.51.0](https://github.com/lydavid/MusicSearch/compare/v1.50.0...v1.51.0) (2025-07-31)


### Bug Fixes

* add index for joining search result with entity tables; update database version to 52 ([#1691](https://github.com/lydavid/MusicSearch/issues/1691)) ([32d10c3](https://github.com/lydavid/MusicSearch/commit/32d10c3c26ef752d92581b5836a323f57cfc3359))


### Features

* show primary aliases based on device locale in list items (except for tracks and relationships) ([48b0b5f](https://github.com/lydavid/MusicSearch/commit/48b0b5fb9cda50d0d7a5ef2386668264afc7144b))

## [1.50.0](https://github.com/lydavid/MusicSearch/compare/v1.49.5...v1.50.0) (2025-07-28)


### Bug Fixes

* do not crash when refreshing details (due to foreign key constraint failure when entity is deleted but aliases remain) ([#1684](https://github.com/lydavid/MusicSearch/issues/1684)) ([4e0506e](https://github.com/lydavid/MusicSearch/commit/4e0506ec5edff8ef9adccc0d060de100e38fe453))


### Features

* show primary alias based on device locale in details title and in history; update database version to 51 ([#1685](https://github.com/lydavid/MusicSearch/issues/1685)) ([cc9bb43](https://github.com/lydavid/MusicSearch/commit/cc9bb43e830ec4df7f8f3c0f9b1bda4cd8833bc3))

## [1.49.5](https://github.com/lydavid/MusicSearch/compare/v1.49.4...v1.49.5) (2025-07-27)


### Bug Fixes

* split alias tables by entity type so that we join smaller tables; update database version to 49 ([#1683](https://github.com/lydavid/MusicSearch/issues/1683)) ([3394139](https://github.com/lydavid/MusicSearch/commit/3394139053057c08cfbc6715ef1bbb963aff79de))

## [1.49.4](https://github.com/lydavid/MusicSearch/compare/v1.49.3...v1.49.4) (2025-07-26)


### Bug Fixes

* migrate pull-to-refresh to material3; add latest material 3 dependency; remove material dependency ([1427ea0](https://github.com/lydavid/MusicSearch/commit/1427ea09f45862bec25570b7a01b3348aa93bb97))

## [1.49.3](https://github.com/lydavid/MusicSearch/compare/v1.49.2...v1.49.3) (2025-07-25)


### Bug Fixes

* cancel coroutine scope when destroying notification listener to fix memory leak ([f705378](https://github.com/lydavid/MusicSearch/commit/f70537816c94ed322f8ed42f39765ebe756a7f16))
* **desktop:** call login from a coroutine scope from presenter while putting network calls on IO thread ([6e6d91f](https://github.com/lydavid/MusicSearch/commit/6e6d91f8016da55c0e98735d70a10697094b19a8))
* **desktop:** fix crash related to missing ExposedDropdownMenuBox ([0bc3188](https://github.com/lydavid/MusicSearch/commit/0bc3188c8c68ddca79ca58594364912e80b76346))
* save images on IO dispatcher but continue to cache image list on main dispatcher ([24f1623](https://github.com/lydavid/MusicSearch/commit/24f1623d28300c21ea00b43241fbeb6eddb92798))

## [1.49.2](https://github.com/lydavid/MusicSearch/compare/v1.49.1...v1.49.2) (2025-07-18)


### Bug Fixes

* catch UnknownHostException instead of crashing ([52769b9](https://github.com/lydavid/MusicSearch/commit/52769b940766d4267dc72be78fee13b4726dc07b))

## [1.49.1](https://github.com/lydavid/MusicSearch/compare/v1.49.0...v1.49.1) (2025-07-17)


### Bug Fixes

* coalesce null release group types to empty so that when we group we do not distinguish between null and empty ([#1670](https://github.com/lydavid/MusicSearch/issues/1670)) ([054f730](https://github.com/lydavid/MusicSearch/commit/054f730a74bc79cab1d43ecbc70c040f2d2cdbeb))

## [1.49.0](https://github.com/lydavid/MusicSearch/compare/v1.48.0...v1.49.0) (2025-07-16)


### Features

* add collected stats ([55f2451](https://github.com/lydavid/MusicSearch/commit/55f2451764859f8e1faa90e801314a496fa08046))
* show cached/visited stats in local database ([9cb6951](https://github.com/lydavid/MusicSearch/commit/9cb6951123aec0d86194699d34d038e234451197))

## [1.48.0](https://github.com/lydavid/MusicSearch/compare/v1.47.0...v1.48.0) (2025-07-15)


### Features

* add visited count stats for all entity types ([5b9b6a0](https://github.com/lydavid/MusicSearch/commit/5b9b6a0df7804a1e3f3d8fefb85064fb5ab577b7))

## [1.47.0](https://github.com/lydavid/MusicSearch/compare/v1.46.0...v1.47.0) (2025-07-14)


### Bug Fixes

* distinguish between 0 and unknown amount of remote entities; simplify stats to use "/" instead of "of" ([923b5f9](https://github.com/lydavid/MusicSearch/commit/923b5f9db0d68455bbec03ac195d60b3133cf2d4))
* local collections' stats should always say everything has been cached ([9c82e06](https://github.com/lydavid/MusicSearch/commit/9c82e06610801dfa346cb19124dfc0b3edcbd4ab))
* update compose multiplatform to 1.9.0-alpha03, fixing a crash with TextField on iOS ([1dc0cbb](https://github.com/lydavid/MusicSearch/commit/1dc0cbbcf26132b9108332217cba649518b2adca))


### Features

* add visited count stats to release groups lists ([860e1ae](https://github.com/lydavid/MusicSearch/commit/860e1aeea7b678d4414694dd37d5ed2739667e30))

## [1.46.0](https://github.com/lydavid/MusicSearch/compare/v1.45.0...v1.46.0) (2025-07-07)


### Features

* support batch adding to collection from collections and database screens ([0871e51](https://github.com/lydavid/MusicSearch/commit/0871e51af3344ba69e61a21f0e49b98434c8f95c))
* support showing all languages used in the lyrics of a work instead of "Multiple languages" (requires refreshing list or details) ([#1655](https://github.com/lydavid/MusicSearch/issues/1655)) ([54dc5a1](https://github.com/lydavid/MusicSearch/commit/54dc5a161115d62b041c0b4cc8e8f5fa858a34a9))

## [1.45.0](https://github.com/lydavid/MusicSearch/compare/v1.44.0...v1.45.0) (2025-07-06)


### Bug Fixes

* do not allow selecting remote collections for deletion ([b88ede4](https://github.com/lydavid/MusicSearch/commit/b88ede46ca0694da702b36d206fd49772ae057da))


### Features

* support undoing deleting a local collection; update database version to 46 ([#1652](https://github.com/lydavid/MusicSearch/issues/1652)) ([a6f283b](https://github.com/lydavid/MusicSearch/commit/a6f283b8f5b3ce23cfdd1b719f9d5ef25de8e366))

## [1.44.0](https://github.com/lydavid/MusicSearch/compare/v1.43.0...v1.44.0) (2025-07-05)


### Features

* add "Select all"/"Deselect all" button that appears after selecting an item in collection/details screens; show number of selected items out of number of loaded items in top bar; prevent selecting remote collections for deletion ([a188d55](https://github.com/lydavid/MusicSearch/commit/a188d5553e6d6207573b925a0da06934f1b4179b))

## [1.43.0](https://github.com/lydavid/MusicSearch/compare/v1.42.0...v1.43.0) (2025-07-02)


### Features

* show whether an item is in a collection in list views; support adding to collection by clicking bookmark icon next to item; update database version to 45 ([#1637](https://github.com/lydavid/MusicSearch/issues/1637)) ([9dc1aa6](https://github.com/lydavid/MusicSearch/commit/9dc1aa63a589681b7895dd16ee382aaec459f0f5))

## [1.42.0](https://github.com/lydavid/MusicSearch/compare/v1.41.0...v1.42.0) (2025-06-29)


### Bug Fixes

* remote series collection not showing last updated ([c6f8913](https://github.com/lydavid/MusicSearch/commit/c6f891362cb8bbae99d01235e27305f2d8bcd0a1))


### Features

* add stats menu item in collections ([dbea8cb](https://github.com/lydavid/MusicSearch/commit/dbea8cb866b5520644eadf50a89115ff5789dd8e))

## [1.41.0](https://github.com/lydavid/MusicSearch/compare/v1.40.2...v1.41.0) (2025-06-26)


### Bug Fixes

* **ios:** decode urls in details ([090f156](https://github.com/lydavid/MusicSearch/commit/090f15659c9786a3ee0866653fccb97507ec276e))


### Features

* add icons to menu items ([32d184c](https://github.com/lydavid/MusicSearch/commit/32d184c372b6f9d68fcc06ef9d82dd6e98e7ee68))
* support adding multiple items to collection from details (click image/icon then add from "More" action); move "add [this] to collection" to its own action toggle (which is filled when this entity is part of a collection) ([849b05b](https://github.com/lydavid/MusicSearch/commit/849b05b7754efccbbaadbe5a789e570aa9a30661))

## [1.40.2](https://github.com/lydavid/MusicSearch/compare/v1.40.1...v1.40.2) (2025-06-24)


### Bug Fixes

* always use Compose Multiplatform libraries to prevent using multiple versions of a compose library ([#1619](https://github.com/lydavid/MusicSearch/issues/1619)) ([3b003ce](https://github.com/lydavid/MusicSearch/commit/3b003cef587b7ec41d5ae8789463734c495c1cd4))

## [1.40.1](https://github.com/lydavid/MusicSearch/compare/v1.40.0...v1.40.1) (2025-06-20)


### Bug Fixes

* do not display duplicate labels for each image in relationships list ([8f5cac1](https://github.com/lydavid/MusicSearch/commit/8f5cac1802fdde93fad68f03627345b3d5d7d734))

## [1.40.0](https://github.com/lydavid/MusicSearch/compare/v1.39.0...v1.40.0) (2025-06-14)


### Features

* add aliases when browsing lists, which will be used when filtering ([d9b630f](https://github.com/lydavid/MusicSearch/commit/d9b630fdabb604f8be59678729de1157a7eb3dd4))

## [1.39.0](https://github.com/lydavid/MusicSearch/compare/v1.38.1...v1.39.0) (2025-06-13)


### Features

* add aliases when searching to make filtering lists easier ([#1600](https://github.com/lydavid/MusicSearch/issues/1600)) ([2eb6d3c](https://github.com/lydavid/MusicSearch/commit/2eb6d3c75913fa2e1238f5dddb44b4fb64393426))

## [1.38.1](https://github.com/lydavid/MusicSearch/compare/v1.38.0...v1.38.1) (2025-06-10)


### Bug Fixes

* center title icon even when there is a subtitle and fix title horizontal scroll ([f43ef43](https://github.com/lydavid/MusicSearch/commit/f43ef430f23af48682e933f52f83239c70702f74))
* group relationships that link to the same page together as a single list item (e.g. a single entry for "compose, lyricist" if it is the same artist) ([4d2cf8d](https://github.com/lydavid/MusicSearch/commit/4d2cf8d280c1a91c4862f3e947c2887f60d2d4e4))

## [1.38.0](https://github.com/lydavid/MusicSearch/compare/v1.37.0...v1.38.0) (2025-06-08)


### Bug Fixes

* collection not being retained in backstack ([0255e57](https://github.com/lydavid/MusicSearch/commit/0255e57959030db771f390cd9a5aac80aee96618))
* correctly count the number of actual artist images (excluding placeholders) ([4a412ef](https://github.com/lydavid/MusicSearch/commit/4a412efbfc41daf029dbf71594a39f02a38e604b))


### Features

* add shared image transition between thumbnail and full-size images when navigating between screens ([f3543b0](https://github.com/lydavid/MusicSearch/commit/f3543b07967336424b1f35ae918289531a52bacc))
* show event thumbnail image if it exists (by visiting details) in search results and events list ([e4e19d4](https://github.com/lydavid/MusicSearch/commit/e4e19d4d8b461dc2e15c720818d95c68bd3cc80e))
* show the number of images in details screen next to information header ([048c32e](https://github.com/lydavid/MusicSearch/commit/048c32ea4315bcc2c733d77e672c5c6bd88fe9d6))

## [1.37.0](https://github.com/lydavid/MusicSearch/compare/v1.36.0...v1.37.0) (2025-06-07)


### Bug Fixes

* separate filtering out Wikipedia extract and url ([289a4ba](https://github.com/lydavid/MusicSearch/commit/289a4bab9b38a7c914ae8bf0dde30206ebe3787f))


### Features

* generalize details tab UI; support collapsing external links for all details screens; separate Wikipedia section with its own header ([eb7071b](https://github.com/lydavid/MusicSearch/commit/eb7071bf2bfb643fae300a3a47f4c54d1d124448)), closes [#1549](https://github.com/lydavid/MusicSearch/issues/1549)
* say what tab the refresh menu item will refresh (e.g. Refresh Details) ([a765ed5](https://github.com/lydavid/MusicSearch/commit/a765ed58f99ddda4651b2cd8348c43af6b061091))
* show last updated in details tab ([fcafea6](https://github.com/lydavid/MusicSearch/commit/fcafea657d4304821f6ea4a9b192160d41a3f4a1)), closes [#1490](https://github.com/lydavid/MusicSearch/issues/1490)
* show last updated in relationships tab; update database to version 43 ([#1577](https://github.com/lydavid/MusicSearch/issues/1577)) ([0764727](https://github.com/lydavid/MusicSearch/commit/07647276cc7efc2d2cf2dfb9d27c8b23ca4e6fe4)), closes [#1491](https://github.com/lydavid/MusicSearch/issues/1491)
* show loading when refreshing details ([8473302](https://github.com/lydavid/MusicSearch/commit/84733024499c0507446512078da6ac3622f829c8))
* support collapsing event external links ([67910aa](https://github.com/lydavid/MusicSearch/commit/67910aaf811724b4effc0943a05f325d361766b9))

## [1.36.0](https://github.com/lydavid/MusicSearch/compare/v1.35.0...v1.36.0) (2025-06-04)


### Bug Fixes

* artist sort name not being used in history; artist image not loading ([1a82552](https://github.com/lydavid/MusicSearch/commit/1a8255209c4ebb6018b0a0a78d35b6950762b8ff))


### Features

* support collapsing area external links ([87b413f](https://github.com/lydavid/MusicSearch/commit/87b413f3f01c8542da3a4e2f1fe9b300f0830b4d))
* support collapsing artist external links ([4326c06](https://github.com/lydavid/MusicSearch/commit/4326c065f2bfe3b4f8a792a69223ddbd842a144a))
* support collapsing release and release group external links ([d87bda3](https://github.com/lydavid/MusicSearch/commit/d87bda38540b9da714787d0a0e919e628056f262)), closes [#1549](https://github.com/lydavid/MusicSearch/issues/1549)

## [1.35.0](https://github.com/lydavid/MusicSearch/compare/v1.34.1...v1.35.0) (2025-06-02)


### Bug Fixes

* do not infinitely try to fetch event cover arts when refreshing ([2865a23](https://github.com/lydavid/MusicSearch/commit/2865a23c5d59455c8244b9e1b89494046bc7639e))


### Features

* add refresh menu item for area screen ([6f145b4](https://github.com/lydavid/MusicSearch/commit/6f145b492e3eb7f62a5c7f9d12b9717c8b4ef334)), closes [#1031](https://github.com/lydavid/MusicSearch/issues/1031)
* add refresh menu item to all collection list screens as an alternative way to refresh data ([38e772e](https://github.com/lydavid/MusicSearch/commit/38e772ef6574b33dc37ed233b2d9695efb2c8a70))
* add refresh menu item to all details screens as an alternative way to refresh each tab's data (and the only way for desktop) ([5bb464f](https://github.com/lydavid/MusicSearch/commit/5bb464fdb8e15d3089ec6ad7fc1b57a366de423f))

## [1.34.1](https://github.com/lydavid/MusicSearch/compare/v1.34.0...v1.34.1) (2025-06-01)


### Bug Fixes

* allow filtering by artist sort name in history again ([c6bf7c3](https://github.com/lydavid/MusicSearch/commit/c6bf7c3ec484e2398a4ae76afb30e7de98b7d2b9))

## [1.34.0](https://github.com/lydavid/MusicSearch/compare/v1.33.0...v1.34.0) (2025-05-29)


### Features

* bundle the latest version of SQLite to avoid discrepancies between different Android versions ([#1558](https://github.com/lydavid/MusicSearch/issues/1558)) ([1024a37](https://github.com/lydavid/MusicSearch/commit/1024a37f91d3478383a05b11ac9a3c05bd0c355b)), closes [#1556](https://github.com/lydavid/MusicSearch/issues/1556)




## [1.33.0](https://github.com/lydavid/MusicSearch/compare/v1.32.4...v1.33.0) (2025-05-29)


### Features

* convert visited table to details_metadata; track last updated on refresh; update database version to 42 ([#1554](https://github.com/lydavid/MusicSearch/issues/1554)) ([24cbaaa](https://github.com/lydavid/MusicSearch/commit/24cbaaa329bda106084dd8eb65da6e9306104024)), closes [#1490](https://github.com/lydavid/MusicSearch/issues/1490)
* show SQLite version in developer mode ([4eeea4f](https://github.com/lydavid/MusicSearch/commit/4eeea4f8fa02e6ebf9e1df5acc19b4e745ad97e1))

## [1.32.4](https://github.com/lydavid/MusicSearch/compare/v1.32.3...v1.32.4) (2025-05-28)


### Bug Fixes

* do not try to commit download folder which no longer exists ([205bcf2](https://github.com/lydavid/MusicSearch/commit/205bcf2ac64f186bdd73e36582c080a64c7aa49b))

## [1.32.3](https://github.com/lydavid/MusicSearch/compare/v1.32.2...v1.32.3) (2025-05-28)


### Bug Fixes

* copy recursively the contents of output to download ([1863708](https://github.com/lydavid/MusicSearch/commit/186370845aa24b419cc322044d7f8aedb3d47e5a))
* go back to using GitHub releases as the download site ([5c20a1e](https://github.com/lydavid/MusicSearch/commit/5c20a1e31634bc47acb6d252c7902e8a3bf3b03c))

## [1.32.2](https://github.com/lydavid/MusicSearch/compare/v1.32.1...v1.32.2) (2025-05-27)


### Bug Fixes

* copy recursively from output to download ([14fa800](https://github.com/lydavid/MusicSearch/commit/14fa800f86b57b11bc9cfb5cf8e8ecd98287da83))

## [1.32.1](https://github.com/lydavid/MusicSearch/compare/v1.32.0...v1.32.1) (2025-05-27)


### Bug Fixes

* remove http cache so refreshing will get latest result from MusicBrainz ([5de6c53](https://github.com/lydavid/MusicSearch/commit/5de6c5325744d8e0bcd1abfc2d5ec813a98a882a)), closes [#1550](https://github.com/lydavid/MusicSearch/issues/1550)

## [1.32.0](https://github.com/lydavid/MusicSearch/compare/v1.31.5...v1.32.0) (2025-05-26)


### Bug Fixes

* "open in browser" from an event or release group's image grid will open appropriate page in MusicBrainz ([99f9bdc](https://github.com/lydavid/MusicSearch/commit/99f9bdc905899620efb9fbf6ccbd494f9d5fe0ef))
* delete artist credits link when refreshing recording/release/release group so that old artist credits that were edited will not continue to show ([368ae24](https://github.com/lydavid/MusicSearch/commit/368ae24a028e1f5c61a71e885fd04a8cc17e34fb)), closes [#1523](https://github.com/lydavid/MusicSearch/issues/1523)


### Features

* show the number of cover art images in release group and event details tab ([ad388c7](https://github.com/lydavid/MusicSearch/commit/ad388c7e956f0ef4e43527af6a42a1d35f2bd25e)), closes [#1471](https://github.com/lydavid/MusicSearch/issues/1471)
* support clicking on event and release group image in details tab to see all of its images ([5cfb5bf](https://github.com/lydavid/MusicSearch/commit/5cfb5bf45307476a5f0a3ddae43c3df5e0d2ffac)), closes [#1471](https://github.com/lydavid/MusicSearch/issues/1471)

## [1.31.5](https://github.com/lydavid/MusicSearch/compare/v1.31.4...v1.31.5) (2025-05-25)


### Bug Fixes

* do not crash when there's no internet connection in details tab ([54252e9](https://github.com/lydavid/MusicSearch/commit/54252e94355702677e9f1553bd50a3535f41982a))
* do not try to move files not generated by conveyor for desktop release ([e932b97](https://github.com/lydavid/MusicSearch/commit/e932b979b541678f046e6528a0304bca168a3568))
* **ios:** handle common exceptions when making API calls and retry request up to 3 times ([5cb4ee9](https://github.com/lydavid/MusicSearch/commit/5cb4ee929517b3e7fc966a0aa5b1b7d50631e45f))
* pass handled exception to all details retry screen to display a less generic error message ([2092770](https://github.com/lydavid/MusicSearch/commit/2092770c2373458a30633a3201f24896e14d30d1))
* pass handled exception to area details retry screen to display a less generic error message ([3b2924b](https://github.com/lydavid/MusicSearch/commit/3b2924b1433ebd4001230265b43338451098cbdf))

## [1.31.4](https://github.com/lydavid/MusicSearch/compare/v1.31.3...v1.31.4) (2025-05-24)


### Bug Fixes

* do not remove download directory ([15ae291](https://github.com/lydavid/MusicSearch/commit/15ae291fa4d79d7c31e83f20a86eaa5ee6752ba2))

## [1.31.3](https://github.com/lydavid/MusicSearch/compare/v1.31.2...v1.31.3) (2025-05-24)


### Bug Fixes

* app updates for desktop apps from GitHub pages download site by making sure the relevant files are uploaded (committed) ([71403a6](https://github.com/lydavid/MusicSearch/commit/71403a62e8c949275f31c5cc452e8edb0533f0ab))

## [1.31.2](https://github.com/lydavid/MusicSearch/compare/v1.31.1...v1.31.2) (2025-05-24)


### Bug Fixes

* drop unused `additional_info` column from `relation`; update database version to 41 ([fa24ffb](https://github.com/lydavid/MusicSearch/commit/fa24ffb515433f8344defa958227f7b543f3e681))

## [1.31.1](https://github.com/lydavid/MusicSearch/compare/v1.31.0...v1.31.1) (2025-05-23)


### Bug Fixes

* correctly get recordings with collaborations (rather than release groups) ([4af44d1](https://github.com/lydavid/MusicSearch/commit/4af44d1f27fa6cc35f834ddbe7264140fcf127a0))

## [1.31.0](https://github.com/lydavid/MusicSearch/compare/v1.30.0...v1.31.0) (2025-05-22)


### Features

* add option to switch between artist collaborations on recordings/releases/release groups in graph view ([f60cd29](https://github.com/lydavid/MusicSearch/commit/f60cd29989c525fa101d274fcbf5a5579643b78b))

## [1.30.0](https://github.com/lydavid/MusicSearch/compare/v1.29.2...v1.30.0) (2025-05-16)


### Features

* add settings to adjust number of images per row between 2-10, and adjust images grid padding between 0-16 ([9c6c10b](https://github.com/lydavid/MusicSearch/commit/9c6c10b09cbe10ab3cdc739dcf4527eb07021144))

## [1.29.2](https://github.com/lydavid/MusicSearch/compare/v1.29.1...v1.29.2) (2025-05-14)


### Bug Fixes

* details image not appearing until user scrolls up ([a27a61d](https://github.com/lydavid/MusicSearch/commit/a27a61d2e815d7ea3b254e1e19a0df6728e7660d))
* only batch write image metadata to database when there are more than 200 list items ([1af494e](https://github.com/lydavid/MusicSearch/commit/1af494e4feb83cfa704eea7ae49b61385e7ade74)), closes [#1465](https://github.com/lydavid/MusicSearch/issues/1465)

## [1.29.1](https://github.com/lydavid/MusicSearch/compare/v1.29.0...v1.29.1) (2025-05-12)


### Bug Fixes

* hide external links header when there are none or all have been filtered out ([0c6c9b1](https://github.com/lydavid/MusicSearch/commit/0c6c9b11bb4fabc92bbb3940b057fc18160d137f))

## [1.29.0](https://github.com/lydavid/MusicSearch/compare/v1.28.0...v1.29.0) (2025-05-11)


### Features

* adjust pan speed based on zoom level ([d47bb9e](https://github.com/lydavid/MusicSearch/commit/d47bb9e9aa27258bebc9c693e447da6a830568c9)), closes [#1525](https://github.com/lydavid/MusicSearch/issues/1525)
* support zooming in/out of artist/recording collaboration graph ([#1524](https://github.com/lydavid/MusicSearch/issues/1524)) ([d3240e9](https://github.com/lydavid/MusicSearch/commit/d3240e9a90469ae82a802e4e081e7067e3af5418))

## [1.28.0](https://github.com/lydavid/MusicSearch/compare/v1.27.1...v1.28.0) (2025-05-10)


### Features

* add color picker to choose theme color when not using Material You ([#1521](https://github.com/lydavid/MusicSearch/issues/1521)) ([ca669d3](https://github.com/lydavid/MusicSearch/commit/ca669d3b8fb635d38cb885dae95a0af325a9cbc9)), closes [#1516](https://github.com/lydavid/MusicSearch/issues/1516)

## [1.27.1](https://github.com/lydavid/MusicSearch/compare/v1.27.0...v1.27.1) (2025-05-07)


### Bug Fixes

* screen stutter when trying to load artist image on iOS ([ad49493](https://github.com/lydavid/MusicSearch/commit/ad49493dd0caabb36c53ab6c7313545abd0cdcef))

## [1.27.0](https://github.com/lydavid/MusicSearch/compare/v1.26.1...v1.27.0) (2025-05-05)


### Features

* support batch deleting, undoing delete from collection ([#1505](https://github.com/lydavid/MusicSearch/issues/1505)) ([07ecf25](https://github.com/lydavid/MusicSearch/commit/07ecf25a87aa66b24dc5c552a5b20cffbd628916))

## [1.26.1](https://github.com/lydavid/MusicSearch/compare/v1.26.0...v1.26.1) (2025-05-04)


### Bug Fixes

* artist's release groups refresh menu item; generalize release groups list ([a20f49b](https://github.com/lydavid/MusicSearch/commit/a20f49bc6868f4c8e28df6c910270fb8d670964b))

## [1.26.0](https://github.com/lydavid/MusicSearch/compare/v1.25.0...v1.26.0) (2025-05-02)


### Bug Fixes

* apply debounce to search input instead of results to make sure only the most recent results are shown ([b5404f5](https://github.com/lydavid/MusicSearch/commit/b5404f543b4d5910b407b1f481af4dc8dd425e1f)), closes [#1419](https://github.com/lydavid/MusicSearch/issues/1419)
* relationship time not showing up ([4bc5d95](https://github.com/lydavid/MusicSearch/commit/4bc5d95d70447e9dcbc44248999606ea1433f77d)), closes [#1464](https://github.com/lydavid/MusicSearch/issues/1464)
* stop setting additional_info with life span; format lifespan without English words; handle lifespan without end ([8e0758c](https://github.com/lydavid/MusicSearch/commit/8e0758cb2e431df150103b434ddbac2a20e83c91))


### Features

* animate collapsible list separators (seen under Tracks), rotating 90 degrees counterclockwise when collapsed ([73a8a9a](https://github.com/lydavid/MusicSearch/commit/73a8a9a29e8af0888c6758e6bec70ae6d6080bca))
* show Wikipedia and Wikidata logos ([4170206](https://github.com/lydavid/MusicSearch/commit/417020649839c5efb15e02a38e660b7f61cbf940))
* support collapsing release events in release details ([ab4ddaa](https://github.com/lydavid/MusicSearch/commit/ab4ddaab071f3c799e67701faa5c6c04f2b8560d)), closes [#1294](https://github.com/lydavid/MusicSearch/issues/1294)

## [1.25.0](https://github.com/lydavid/MusicSearch/compare/v1.24.4...v1.25.0) (2025-04-30)


### Bug Fixes

* crash when navigating to "Open source licenses" ([8a211b9](https://github.com/lydavid/MusicSearch/commit/8a211b94b9f3f3cd3e582060233e050335183583))
* ios build issue after updating compose ([f4daafb](https://github.com/lydavid/MusicSearch/commit/f4daafbe563d0afc87d4536291cd4e321d6aabe6))
* update compose multiplatform to 1.8.0-rc01 to fix desktop not building; use ImageVectors created through Valkyrie now that material icons are not shipped with CMP; merge ui:image into ui:common ([bfd6f94](https://github.com/lydavid/MusicSearch/commit/bfd6f94cf23cca5782591543e9ead22016866104))


### Features

* add last updated to the end of list/stats screens; previously fetched data will say it was last updated on the install date of this patch; bump database version to 37 ([#1485](https://github.com/lydavid/MusicSearch/issues/1485)) ([b43b971](https://github.com/lydavid/MusicSearch/commit/b43b971ab05247487ad7242e8c814a724cdd1d75))

## [1.24.4](https://github.com/lydavid/MusicSearch/compare/v1.24.3...v1.24.4) (2025-04-28)


### Bug Fixes

* clicking back on filter bar will always clear the query ([2788b85](https://github.com/lydavid/MusicSearch/commit/2788b852ba9f24b8419d30a81eb531b549c87805))
* genre list crash ([08e29a8](https://github.com/lydavid/MusicSearch/commit/08e29a8d25398328089fb04cc8bc334b5b66ab23))

## [1.24.3](https://github.com/lydavid/MusicSearch/compare/v1.24.2...v1.24.3) (2025-04-26)


### Bug Fixes

* do not delete entities when refreshing list screens ([34039b5](https://github.com/lydavid/MusicSearch/commit/34039b504d607f7089cb98eed0f7b0c0bb2d0634)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)

## [1.24.2](https://github.com/lydavid/MusicSearch/compare/v1.24.1...v1.24.2) (2025-04-23)


### Bug Fixes

* commit aboutlibraries.json for F-Droid release variant on dependency change; change this file for Google Play and desktop releases ([6e56f1e](https://github.com/lydavid/MusicSearch/commit/6e56f1e41fe4b7195aefad1f56c50565bff3ecdd)), closes [#1331](https://github.com/lydavid/MusicSearch/issues/1331)

## [1.24.1](https://github.com/lydavid/MusicSearch/compare/v1.24.0...v1.24.1) (2025-04-22)


### Bug Fixes

* **google play:** disable analytics and make crash reporting opt-in ([726b76f](https://github.com/lydavid/MusicSearch/commit/726b76fd453d0773e1c48144f87f6760ef115c96))
* insert or ignore artist credits instead of insert or fail which slowed iOS simulator ([9f3b502](https://github.com/lydavid/MusicSearch/commit/9f3b5024833cfc8013d02b60b2923bc3e4687eff)), closes [#1235](https://github.com/lydavid/MusicSearch/issues/1235)

## [1.24.0](https://github.com/lydavid/MusicSearch/compare/v1.23.0...v1.24.0) (2025-04-21)


### Bug Fixes

* update database version to 36: rename table and drop unused local_count ([#1470](https://github.com/lydavid/MusicSearch/issues/1470)) ([13c76de](https://github.com/lydavid/MusicSearch/commit/13c76de27f56138b81089e2ac87ed696ae6509eb))


### Features

* when adding to a collection, show whether it is already in it with a checkmark ([1a6ca24](https://github.com/lydavid/MusicSearch/commit/1a6ca24a48537e1a1f1ea0d57e5fadc775b1ba1a)), closes [#1469](https://github.com/lydavid/MusicSearch/issues/1469)

## [1.23.0](https://github.com/lydavid/MusicSearch/compare/v1.22.0...v1.23.0) (2025-04-20)


### Bug Fixes

* add padding between images in a grid ([8c7f12b](https://github.com/lydavid/MusicSearch/commit/8c7f12ba2fe84a97f4a3d0d3d2c48e643f57eba7))
* fill thumbnail images to its given size ([5911df2](https://github.com/lydavid/MusicSearch/commit/5911df2c23fad3a7808c964f55571bbf5a369578))
* show country flag emoji in list items; fix missing country type when searching areas that were previously seen first in a release event ([e73d5cc](https://github.com/lydavid/MusicSearch/commit/e73d5cc4d173763351e6b316f99af50b19c7b02d))
* use entity icon as placeholder icon in image grid ([d172cc0](https://github.com/lydavid/MusicSearch/commit/d172cc01076ce6acc7827299fe4c504564c79b66))


### Features

* color the selected tab's text with the primary color while leaving unselected tabs as black/white ([50a1525](https://github.com/lydavid/MusicSearch/commit/50a15255fb6d003e945bf4e8bcd94092af32f6ad))
* show country flag emoji (regional indicator symbol) in details tab ([96f21a8](https://github.com/lydavid/MusicSearch/commit/96f21a84bdd0ee114df7a4d211b63a30a3e4be1f))

## [1.22.0](https://github.com/lydavid/MusicSearch/compare/v1.21.3...v1.22.0) (2025-04-19)


### Features

* **ios:** add OAuth login ([f5bc72d](https://github.com/lydavid/MusicSearch/commit/f5bc72de3ad593ef656f56a6c0f0545e06c5b86a))
* **ios:** show flag emoji ([eec3496](https://github.com/lydavid/MusicSearch/commit/eec34967b685a63ea2e5d3de1e1b183119ee2486))

## [1.21.3](https://github.com/lydavid/MusicSearch/compare/v1.21.2...v1.21.3) (2025-04-18)


### Bug Fixes

* make desktop OAuth requests without library; use a Kotlin constant for the application id that will be changed for releases ([a6e2cda](https://github.com/lydavid/MusicSearch/commit/a6e2cdaf1d4ad5dd6ed149eb85536b8c1996647a))

## [1.21.2](https://github.com/lydavid/MusicSearch/compare/v1.21.1...v1.21.2) (2025-04-16)


### Bug Fixes

* synchronize writes to hash map that tracks pending image metadata to insert into our database ([1e33566](https://github.com/lydavid/MusicSearch/commit/1e33566ca1d3a63fd70d88c22b28b1eee48d0666))

## [1.21.1](https://github.com/lydavid/MusicSearch/compare/v1.21.0...v1.21.1) (2025-04-15)


### Bug Fixes

* less UI reloads when browsing large lists of releases and release groups by batch writing image metadata to database ([#1457](https://github.com/lydavid/MusicSearch/issues/1457)) ([1549ed3](https://github.com/lydavid/MusicSearch/commit/1549ed3a02e9e9173e6f3fe5ceb0e74be990de1d)), closes [#1413](https://github.com/lydavid/MusicSearch/issues/1413)

## [1.21.0](https://github.com/lydavid/MusicSearch/compare/v1.20.0...v1.21.0) (2025-04-10)


### Bug Fixes

* **desktop:** correct database and export destination path ([9c9b5f0](https://github.com/lydavid/MusicSearch/commit/9c9b5f081293c40bb48eb82ad6524bdb85dbffb5))


### Features

* show database version and copy to clipboard on click for easier debugging ([b665195](https://github.com/lydavid/MusicSearch/commit/b665195900d8544f6341dded476df9cabfcad3d8))

## [1.20.0](https://github.com/lydavid/MusicSearch/compare/v1.19.0...v1.20.0) (2025-04-09)


### Features

* add icon next to each Database item ([52aa4ce](https://github.com/lydavid/MusicSearch/commit/52aa4ce940ab2c9698e529e50bfed4fd00beabab))


### Reverts

* Revert "chore: move new download page and commit before desktop distribution" ([fe46b8a](https://github.com/lydavid/MusicSearch/commit/fe46b8aedd2a1590b2082dd654cc965dac3edd85))

## [1.19.0](https://github.com/lydavid/MusicSearch/compare/v1.18.0...v1.19.0) (2025-04-08)


### Bug Fixes

* **android:** say the database was saved to Downloads ([c65fbf6](https://github.com/lydavid/MusicSearch/commit/c65fbf60018b0bfd7afcf4f0635ddb223740f4d7))


### Features

* **desktop:** support exporting database (to same directory where the app is installed) ([f963635](https://github.com/lydavid/MusicSearch/commit/f963635f5542730da27d35f07ec0995bcca1511d))
* **ios:** add settings screen (oauth and licenses not supported yet) ([5aa6a95](https://github.com/lydavid/MusicSearch/commit/5aa6a959f9b7dce2debc0247eadfaaedf7d3eaef))

## [1.18.0](https://github.com/lydavid/MusicSearch/compare/v1.17.0...v1.18.0) (2025-04-07)


### Bug Fixes

* **desktop:** fix broken migration by using overloaded JdbcSqliteDriver method that accepts a schema ([e1332ec](https://github.com/lydavid/MusicSearch/commit/e1332ec0d7d797c1dfacd58cb6511ac568ac8b4d)), closes [#1286](https://github.com/lydavid/MusicSearch/issues/1286)
* increase prefetch distance for list screens so that it is less likely to get stuck trying to load many pages ahead ([26d3a50](https://github.com/lydavid/MusicSearch/commit/26d3a501dcf6c86152339cd7c74a17ebebcc1d3d)), closes [#1413](https://github.com/lydavid/MusicSearch/issues/1413)
* **iOS, desktop:** align database name with Android ([80198ba](https://github.com/lydavid/MusicSearch/commit/80198ba5adf38d6bc62d6e8d3d791efdab22eeb0))


### Features

* move image browser under Database tab ([371c4b1](https://github.com/lydavid/MusicSearch/commit/371c4b145b9e9a8e57393c61c54566b2c26d8c82))
* show how many of each entities we have in database screen ([84c4e11](https://github.com/lydavid/MusicSearch/commit/84c4e11f87c2a70600709f6c16e0e12b4866453d)), closes [#1435](https://github.com/lydavid/MusicSearch/issues/1435)
* show number of images in Database tab ([68e013a](https://github.com/lydavid/MusicSearch/commit/68e013a1ceb72f8fd8b1e8b25ad0b018065d28ff))

## [1.17.0](https://github.com/lydavid/MusicSearch/compare/v1.16.10...v1.17.0) (2025-04-06)


### Bug Fixes

* do not add duplicate images while scrolling through local database ([8104d04](https://github.com/lydavid/MusicSearch/commit/8104d04482e84f137a55c363e5477524bd0c6e0c)), closes [#1413](https://github.com/lydavid/MusicSearch/issues/1413)


### Features

* add local database to allow browsing all cached entities; move history under this screen ([#1433](https://github.com/lydavid/MusicSearch/issues/1433)) ([41f59d4](https://github.com/lydavid/MusicSearch/commit/41f59d49c3d3195c33473b0b4872b60d991f5469)), closes [#1434](https://github.com/lydavid/MusicSearch/issues/1434)

## [1.16.10](https://github.com/lydavid/MusicSearch/compare/v1.16.9...v1.16.10) (2025-03-30)


### Bug Fixes

* show release group first release date ([#1420](https://github.com/lydavid/MusicSearch/issues/1420)) ([7d81a0a](https://github.com/lydavid/MusicSearch/commit/7d81a0aeadcdcc46c353e9f1c37d57493f8d371f))
* when refreshing recording's releases, do not delete the ones linked to multiple other artist/area/label/recording ([d1251d4](https://github.com/lydavid/MusicSearch/commit/d1251d46e0d2c26f57582a4a0ddca47a727f1bfe)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)
* when refreshing release group's releases, do not delete the ones linked to other artist/area/label/recording/release group ([911e613](https://github.com/lydavid/MusicSearch/commit/911e613634a9beafc9affcc1994dcdb6bea211b1)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)

## [1.16.9](https://github.com/lydavid/MusicSearch/compare/v1.16.8...v1.16.9) (2025-03-29)


### Bug Fixes

* when refreshing artist's releases, do not delete the ones linked to multiple other artist/area/label ([9fd565f](https://github.com/lydavid/MusicSearch/commit/9fd565f0790d2ccbe5fe5b50891c6e8c3e00419f)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)

## [1.16.8](https://github.com/lydavid/MusicSearch/compare/v1.16.7...v1.16.8) (2025-03-26)


### Bug Fixes

* do not delete release groups that appears in more than one artist's release groups tab when refreshing ([598850d](https://github.com/lydavid/MusicSearch/commit/598850d1b796439cf74fb5217fc7626efc00fb76)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)

## [1.16.7](https://github.com/lydavid/MusicSearch/compare/v1.16.6...v1.16.7) (2025-03-23)


### Bug Fixes

* show work attributes with the same type ([0188254](https://github.com/lydavid/MusicSearch/commit/01882544a66fc1d816d94ca72c6d2b72816eaafc)), closes [#1394](https://github.com/lydavid/MusicSearch/issues/1394)
* sort work ISWCs ([ee4d82f](https://github.com/lydavid/MusicSearch/commit/ee4d82ff67e003f36e2ca9282e6b6b6bded52e1e))

## [1.16.6](https://github.com/lydavid/MusicSearch/compare/v1.16.5...v1.16.6) (2025-03-22)


### Bug Fixes

* do not delete works that appears in more than one artist's works tab when refreshing ([f427b35](https://github.com/lydavid/MusicSearch/commit/f427b358de527a6ebde82c1245c6a87d21402273)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)

## [1.16.5](https://github.com/lydavid/MusicSearch/compare/v1.16.4...v1.16.5) (2025-03-20)


### Bug Fixes

* do not delete recordings that appears in more than one screen (artist/work's recordings tab) when refreshing ([c7c3626](https://github.com/lydavid/MusicSearch/commit/c7c36269fb7545185f82e4c1b90a6fb4132b977e)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)
* events by collection would not fetch beyond the first 100 ([97d20c6](https://github.com/lydavid/MusicSearch/commit/97d20c6a63b6231be060f9451181b89aea7ede6a))

## [1.16.4](https://github.com/lydavid/MusicSearch/compare/v1.16.3...v1.16.4) (2025-03-14)


### Bug Fixes

* try building on ubuntu-22.04 for fdroid reproducible build ([ea9b055](https://github.com/lydavid/MusicSearch/commit/ea9b05576d6dc2ff09037323efc230963fa98364))

## [1.16.3](https://github.com/lydavid/MusicSearch/compare/v1.16.2...v1.16.3) (2025-03-13)


### Bug Fixes

* do not delete artists that appears in other screens ([3f21168](https://github.com/lydavid/MusicSearch/commit/3f2116825ae0d1285d54aa9759f8b130f230a005)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)
* do not delete labels that appears in other screens ([30fe39b](https://github.com/lydavid/MusicSearch/commit/30fe39ba97b2e8201eb3b53bbd02f4acbf2de409)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)
* do not delete places that appears in multiple areas when refreshing ([f16b9ee](https://github.com/lydavid/MusicSearch/commit/f16b9eec3291faabf4f461215a536e0b3bf98498)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)

## [1.16.2](https://github.com/lydavid/MusicSearch/compare/v1.16.1...v1.16.2) (2025-03-11)


### Bug Fixes

* paging releases by country may not get all releases; refreshing releases by country will not delete releases that are under other countries as well ([bc6614d](https://github.com/lydavid/MusicSearch/commit/bc6614dfb1e349f9fbec2596254417375c737fed)), closes [#1376](https://github.com/lydavid/MusicSearch/issues/1376)

## [1.16.1](https://github.com/lydavid/MusicSearch/compare/v1.16.0...v1.16.1) (2025-03-10)


### Bug Fixes

* sort release events by release date, then name of country ([5fffffe](https://github.com/lydavid/MusicSearch/commit/5fffffe3b8eb40234b2d781c4e5d78449ea4f684))

## [1.16.0](https://github.com/lydavid/MusicSearch/compare/v1.15.4...v1.16.0) (2025-03-09)


### Bug Fixes

* do not crash when refreshing release details due to missing labels ([c7a360a](https://github.com/lydavid/MusicSearch/commit/c7a360a2600eb9dffa4a045443ab6b544ac30ee5))
* do not show duplicate labels in a release and group all catalog numbers by label ([bcc70e2](https://github.com/lydavid/MusicSearch/commit/bcc70e2b69695221e8f0f90ef27efc13a9423af8))


### Features

* show time on event list items ([a22ccad](https://github.com/lydavid/MusicSearch/commit/a22ccad70bb4756218b0a1eb52d9ea13f54e452c))
* show whether event was cancelled in list item ([f017c57](https://github.com/lydavid/MusicSearch/commit/f017c57e266b619434eb6b1f3d883fcf3073da7d))
* support filtering events by date and time ([b1fd3d8](https://github.com/lydavid/MusicSearch/commit/b1fd3d8792869002b11f4193ac7cd3b5dc7fae5c))
* support filtering label type, code, catalog number, and release event date in release details screen ([f52d145](https://github.com/lydavid/MusicSearch/commit/f52d1459d51f2fc2d63f3274c91a41b52a9057af))

## [1.15.4](https://github.com/lydavid/MusicSearch/compare/v1.15.3...v1.15.4) (2025-03-06)


### Bug Fixes

* refreshing events that belong to multiple entities will no longer delete them ([#1379](https://github.com/lydavid/MusicSearch/issues/1379)) ([e063699](https://github.com/lydavid/MusicSearch/commit/e0636996ccb9e9f3c22121e43dd93ed1dc486a9a)), closes [#1241](https://github.com/lydavid/MusicSearch/issues/1241)

## [1.15.3](https://github.com/lydavid/MusicSearch/compare/v1.15.2...v1.15.3) (2025-03-04)


### Bug Fixes

* pagination may reach end without getting all results when filtering ([#1368](https://github.com/lydavid/MusicSearch/issues/1368)) ([09805dc](https://github.com/lydavid/MusicSearch/commit/09805dc307cd3a30d98ebe556c77fb3c2a1474fe))
* releases by label paging may not get all releases ([#1372](https://github.com/lydavid/MusicSearch/issues/1372)) ([0fba9d3](https://github.com/lydavid/MusicSearch/commit/0fba9d3b5292fac2c6914bbdba872e6376a9125e)), closes [#1369](https://github.com/lydavid/MusicSearch/issues/1369)

## [1.15.2](https://github.com/lydavid/MusicSearch/compare/v1.15.1...v1.15.2) (2025-02-23)


### Bug Fixes

* snackbar not showing when adding an area to a collection ([4d12af6](https://github.com/lydavid/MusicSearch/commit/4d12af67218fe9fe3e6ac36da4e7f9f56b1d1e98))

## [1.15.1](https://github.com/lydavid/MusicSearch/compare/v1.15.0...v1.15.1) (2025-02-18)


### Bug Fixes

* use a view for entities with cover arts ([#1345](https://github.com/lydavid/MusicSearch/issues/1345)) ([2e0c46f](https://github.com/lydavid/MusicSearch/commit/2e0c46f92f67905518e8efe2d7bb72b6b6d7f19b))

## [1.15.0](https://github.com/lydavid/MusicSearch/compare/v1.14.2...v1.15.0) (2025-02-17)


### Features

* show event image in event details screen ([3e8e805](https://github.com/lydavid/MusicSearch/commit/3e8e8055c7a755242df18f6945214d24313e0616)), closes [#1008](https://github.com/lydavid/MusicSearch/issues/1008)
* support sorting cover arts by name of artist/release/release group it belongs to, and by the order it was added to local database ([11c10fd](https://github.com/lydavid/MusicSearch/commit/11c10fd9471519fde16656320c391a97985c8d60)), closes [#1325](https://github.com/lydavid/MusicSearch/issues/1325)

## [1.14.2](https://github.com/lydavid/MusicSearch/compare/v1.14.1...v1.14.2) (2025-02-12)


### Bug Fixes

* rebuild for F-Droid ([8600e41](https://github.com/lydavid/MusicSearch/commit/8600e41ef7e440e704f04c172a84966f07023648))

## [1.14.1](https://github.com/lydavid/MusicSearch/compare/v1.14.0...v1.14.1) (2025-02-10)


### Bug Fixes

* revert circuit to 0.25.0 to fix crash on gesture back ([a620f25](https://github.com/lydavid/MusicSearch/commit/a620f250aa40ac833ee01865a2bed0ce68b8bbd2)), closes [#1326](https://github.com/lydavid/MusicSearch/issues/1326)

## [1.14.0](https://github.com/lydavid/MusicSearch/compare/v1.13.0...v1.14.0) (2025-02-03)


### Features

* support genre collections ([#1051](https://github.com/lydavid/MusicSearch/issues/1051)) ([fce1b03](https://github.com/lydavid/MusicSearch/commit/fce1b034f60d697f067b6e3dd382f8e9ea67b247))

## [1.13.0](https://github.com/lydavid/MusicSearch/compare/v1.12.0...v1.13.0) (2024-12-28)


### Features

* add link to artist/release/release-group from image pager ([84abeed](https://github.com/lydavid/MusicSearch/commit/84abeedcb9baadafe6de142fe966505f7ba26a8e)), closes [#1290](https://github.com/lydavid/MusicSearch/issues/1290)

## [1.12.0](https://github.com/lydavid/MusicSearch/compare/v1.11.0...v1.12.0) (2024-12-27)


### Bug Fixes

* increase max zoom to 8x ([10d1e5d](https://github.com/lydavid/MusicSearch/commit/10d1e5d097aa466c38b1a43255afa2e5a0138d79))


### Features

* allow all platforms to access image browser ([6fbee0c](https://github.com/lydavid/MusicSearch/commit/6fbee0c5284d7eadae3e035f0dcceb9dd149382c))
* support browsing local database images and filtering based on name ([f9e75b7](https://github.com/lydavid/MusicSearch/commit/f9e75b7860396204f9efdccbd2c91e69e45eca0d)), closes [#1290](https://github.com/lydavid/MusicSearch/issues/1290)
* support zooming in on images in cover arts pager view ([07644df](https://github.com/lydavid/MusicSearch/commit/07644df92358a82c012f02662a6a61a14f1251f8)), closes [#1214](https://github.com/lydavid/MusicSearch/issues/1214)

## [1.11.0](https://github.com/lydavid/MusicSearch/compare/v1.10.1...v1.11.0) (2024-12-23)


### Features

* filter release cover arts by types and comment ([#1288](https://github.com/lydavid/MusicSearch/issues/1288)) ([ca0e072](https://github.com/lydavid/MusicSearch/commit/ca0e072d20e46bd3e467c51c3d236b5ea8ddb260))

## [1.10.1](https://github.com/lydavid/MusicSearch/compare/v1.10.0...v1.10.1) (2024-12-22)


### Bug Fixes

* full-size images were not loading when viewing images that were not the front release cover art ([19a6894](https://github.com/lydavid/MusicSearch/commit/19a6894c8ffe52748dfdf8a2da6f2a5266bd22a8))

## [1.10.0](https://github.com/lydavid/MusicSearch/compare/v1.9.0...v1.10.0) (2024-12-21)


### Features

* display Wikipedia extract for all types of details screen ([1ede127](https://github.com/lydavid/MusicSearch/commit/1ede1279966a8efb65bff6b181db2e3a113e828f)), closes [#1068](https://github.com/lydavid/MusicSearch/issues/1068)

## [1.9.0](https://github.com/lydavid/MusicSearch/compare/v1.8.0...v1.9.0) (2024-12-19)


### Bug Fixes

* always show track's first medium on initial load ([#1239](https://github.com/lydavid/MusicSearch/issues/1239)) ([b07dfc0](https://github.com/lydavid/MusicSearch/commit/b07dfc0f1dceeaca11fc0d1fe479952947ed41ee))
* delete artist credits linked to tracks when refreshing tracks which will prevent duplicate tracks from appearing after changes are made on MusicBrainz's server ([#1256](https://github.com/lydavid/MusicSearch/issues/1256)) ([fa854e4](https://github.com/lydavid/MusicSearch/commit/fa854e44ca3ac4ef9f698bb0b3775cd78f561381)), closes [#1248](https://github.com/lydavid/MusicSearch/issues/1248)
* speed up relationships queries ([a60f5c8](https://github.com/lydavid/MusicSearch/commit/a60f5c815b39be2d6bdf8b196d22127c8ebdfbac))


### Features

* **android:** include baseline profile to reduce app start time ([#1275](https://github.com/lydavid/MusicSearch/issues/1275)) ([7391407](https://github.com/lydavid/MusicSearch/commit/73914078f052d7ea2c0f217b68985eb102d32243)), closes [#135](https://github.com/lydavid/MusicSearch/issues/135)
* **android:** support exporting database as sqlite database file ([f26b34c](https://github.com/lydavid/MusicSearch/commit/f26b34cf21a69de63517d79c5896e48d75ec6bdc))
* catch wikimedia api errors and show a user-friendly snackbar message ([ed83114](https://github.com/lydavid/MusicSearch/commit/ed8311492a4f9326e72b1b1e31c413de84a2a421)), closes [#1067](https://github.com/lydavid/MusicSearch/issues/1067)
* display series with ordering in order ([0d851be](https://github.com/lydavid/MusicSearch/commit/0d851beccd29cbf45883ac7b2d4b8f7b1b87fed0)), closes [#1000](https://github.com/lydavid/MusicSearch/issues/1000)
* show cover arts grid when clicking on release cover art ([#1226](https://github.com/lydavid/MusicSearch/issues/1226)) ([91eecfd](https://github.com/lydavid/MusicSearch/commit/91eecfd0e55189406fb102af365641e382771f66))
* show related event's date ([231f983](https://github.com/lydavid/MusicSearch/commit/231f983c2b16e1a082aa6d86dd4b44b3392130bb))
* show relationship thumbnail image (if visited) ([#1262](https://github.com/lydavid/MusicSearch/issues/1262)) ([00706c6](https://github.com/lydavid/MusicSearch/commit/00706c6785684f397d55b8dd4c5d1a3001812d48))

## [1.9.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v1.9.0-beta.6...v1.9.0-beta.7) (2024-12-19)


### Bug Fixes

* speed up relationships queries ([a60f5c8](https://github.com/lydavid/MusicSearch/commit/a60f5c815b39be2d6bdf8b196d22127c8ebdfbac))


### Features

* catch wikimedia api errors and show a user-friendly snackbar message ([ed83114](https://github.com/lydavid/MusicSearch/commit/ed8311492a4f9326e72b1b1e31c413de84a2a421)), closes [#1067](https://github.com/lydavid/MusicSearch/issues/1067)

## [1.9.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v1.9.0-beta.5...v1.9.0-beta.6) (2024-12-17)


### Features

* **android:** include baseline profile to reduce app start time ([#1275](https://github.com/lydavid/MusicSearch/issues/1275)) ([7391407](https://github.com/lydavid/MusicSearch/commit/73914078f052d7ea2c0f217b68985eb102d32243)), closes [#135](https://github.com/lydavid/MusicSearch/issues/135)
* show related event's date ([231f983](https://github.com/lydavid/MusicSearch/commit/231f983c2b16e1a082aa6d86dd4b44b3392130bb))

## [1.9.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v1.9.0-beta.4...v1.9.0-beta.5) (2024-12-15)


### Features

* **android:** support exporting database as sqlite database file ([f26b34c](https://github.com/lydavid/MusicSearch/commit/f26b34cf21a69de63517d79c5896e48d75ec6bdc))

## [1.9.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.9.0-beta.3...v1.9.0-beta.4) (2024-12-14)


### Features

* show relationship thumbnail image (if visited) ([#1262](https://github.com/lydavid/MusicSearch/issues/1262)) ([00706c6](https://github.com/lydavid/MusicSearch/commit/00706c6785684f397d55b8dd4c5d1a3001812d48))

## [1.9.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.9.0-beta.2...v1.9.0-beta.3) (2024-12-01)


### Bug Fixes

* delete artist credits linked to tracks when refreshing tracks which will prevent duplicate tracks from appearing after changes are made on MusicBrainz's server ([#1256](https://github.com/lydavid/MusicSearch/issues/1256)) ([fa854e4](https://github.com/lydavid/MusicSearch/commit/fa854e44ca3ac4ef9f698bb0b3775cd78f561381)), closes [#1248](https://github.com/lydavid/MusicSearch/issues/1248)

## [1.9.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.9.0-beta.1...v1.9.0-beta.2) (2024-11-22)


### Features

* display series with ordering in order ([0d851be](https://github.com/lydavid/MusicSearch/commit/0d851beccd29cbf45883ac7b2d4b8f7b1b87fed0)), closes [#1000](https://github.com/lydavid/MusicSearch/issues/1000)

## [1.9.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.8.0...v1.9.0-beta.1) (2024-11-20)


### Bug Fixes

* always show track's first medium on initial load ([#1239](https://github.com/lydavid/MusicSearch/issues/1239)) ([b07dfc0](https://github.com/lydavid/MusicSearch/commit/b07dfc0f1dceeaca11fc0d1fe479952947ed41ee))


### Features

* show cover arts grid when clicking on release cover art ([#1226](https://github.com/lydavid/MusicSearch/issues/1226)) ([91eecfd](https://github.com/lydavid/MusicSearch/commit/91eecfd0e55189406fb102af365641e382771f66))

## [1.8.0](https://github.com/lydavid/MusicSearch/compare/v1.7.0...v1.8.0) (2024-11-18)


### Bug Fixes

* bold artist's area if unvisited ([8e34f84](https://github.com/lydavid/MusicSearch/commit/8e34f84b369641e738d6bf3d80caa8047ed10cfc))
* **desktop:** add missing Main dispatcher ([18854de](https://github.com/lydavid/MusicSearch/commit/18854de75c3ea9f3c327b3fea0034d11c712a035))
* do not refresh details when returning from backstack ([61367b3](https://github.com/lydavid/MusicSearch/commit/61367b310c0e507e5b061b5e608d2e69e69cbaf3))
* do not show splash background through navigation bar; also remove the extra blank space drawn for iOS ([53654c5](https://github.com/lydavid/MusicSearch/commit/53654c5c04fcbaf050bb1e6e54b54b9103b6a6f2))
* do not sort release groups separately if the only difference is a null or empty secondary type ([80a457f](https://github.com/lydavid/MusicSearch/commit/80a457f1533a7d6b11114ecc29a7a305976ffa06))
* entities in collections not bolded when not visited ([9671222](https://github.com/lydavid/MusicSearch/commit/9671222a05180d86f6f4002b0db5ab9854d1409d)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* **ios:** add CADisableMinimumFrameDurationOnPhone ([c62b231](https://github.com/lydavid/MusicSearch/commit/c62b23183f71689366911a0775cb8925c454c11c))
* make snackbar show up again ([f2391a0](https://github.com/lydavid/MusicSearch/commit/f2391a06f6161145df5739b299fb89057572e516))
* make sure navigation bar does not go over content when in landscape mode ([0c807f9](https://github.com/lydavid/MusicSearch/commit/0c807f926e19e0b7f47f3112f31eb082f2f957e1))
* place will show the most specific area if there are multiple areas the place belong ([#1216](https://github.com/lydavid/MusicSearch/issues/1216)) ([3cdaea5](https://github.com/lydavid/MusicSearch/commit/3cdaea505f1ee48a5a6b33e6a09eaba40275d5b3))
* release labels not being shown as visited ([d169a83](https://github.com/lydavid/MusicSearch/commit/d169a83a5a7dc194fff7a4ea3bb91cedd84c4bb0)), closes [#1211](https://github.com/lydavid/MusicSearch/issues/1211)
* retain areas when returning from back stack ([d22efa9](https://github.com/lydavid/MusicSearch/commit/d22efa9e9ee7bf8acbd4eac8079b1475ed0ecc81)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain artists when returning from back stack ([a2d3a60](https://github.com/lydavid/MusicSearch/commit/a2d3a609af06cc09c4f6a464a95cd82e5779a688)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain collections when returning from back stack ([a592bde](https://github.com/lydavid/MusicSearch/commit/a592bde00e90d2152e9860c3b26942a102fad99f)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain events paging position ([c381128](https://github.com/lydavid/MusicSearch/commit/c38112883851295988d5362e9eb9f53481d934f3)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain instruments when returning from back stack ([12be693](https://github.com/lydavid/MusicSearch/commit/12be69337226126e604d6af312376b7703401b5e)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain labels when returning from back stack ([209e753](https://github.com/lydavid/MusicSearch/commit/209e753f28bd8006ac890768b59fe4c23a6a7426)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain lookup history when returning from back stack; needed to retain sort options as well ([adde03f](https://github.com/lydavid/MusicSearch/commit/adde03f40684e7b1fcc3ac64c056aa3dca7b46f7)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain now playing and spotify history position when returning from backstack ([8d76a64](https://github.com/lydavid/MusicSearch/commit/8d76a645f8f43a0e57b0a2a2ffdef3d6a8ba0dd6)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain place collection when returning from back stack ([681eebe](https://github.com/lydavid/MusicSearch/commit/681eebe4a1ba724c6eda75084ad1ab9d98107324)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain places when returning from back stack ([9e2855e](https://github.com/lydavid/MusicSearch/commit/9e2855e8d2d96ac9c576543d5d76dc895d728140)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain recordings when returning from back stack ([a8ab572](https://github.com/lydavid/MusicSearch/commit/a8ab572a662d29b3d5e4c293be1bd2ef6c3a8ea8)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain relationships paging position when returning from back stack ([31c6f59](https://github.com/lydavid/MusicSearch/commit/31c6f5950d7692a10a09b5c1e36666509b88908c)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain release groups when returning from back stack ([2da8e2e](https://github.com/lydavid/MusicSearch/commit/2da8e2e18f35e6450d08a929ff5036b9e129d211)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain releases when returning from back stack ([b2ee006](https://github.com/lydavid/MusicSearch/commit/b2ee0063bd4f6d9ac2dec43b45cce1564c8a2d77)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain search results/history when returning from back stack ([4e110c9](https://github.com/lydavid/MusicSearch/commit/4e110c903eb7d853ce02be09abf3ebb0d3e0405d)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain series when returning from back stack ([a884ca2](https://github.com/lydavid/MusicSearch/commit/a884ca24802ad47897ab2e4da1ce98065728ebd2)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain tracks when returning from back stack ([43ac4d2](https://github.com/lydavid/MusicSearch/commit/43ac4d2c5bc227eec9f6ee55eff509042ab2bef5)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain works when returning from back stack ([bf6cd0b](https://github.com/lydavid/MusicSearch/commit/bf6cd0b5ea3a86b4ed5711ac39de445d028843a3)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* show "Cannot find collection" instead of crashing when clicking a deleted collection ([5343d5e](https://github.com/lydavid/MusicSearch/commit/5343d5eac87192fe014155ceb38bfbab2fe73cab)), closes [#1227](https://github.com/lydavid/MusicSearch/issues/1227)
* use `collectAsRetainedState` to maintain list position when sorting is not set to the default option ([9e4c7f8](https://github.com/lydavid/MusicSearch/commit/9e4c7f82e1238f2e4285415231004c3420c49a46)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* Wikipedia link should not be bolded ([976c983](https://github.com/lydavid/MusicSearch/commit/976c9831e85d7ed4625116e33c62974ef8ff34ac)), closes [#1210](https://github.com/lydavid/MusicSearch/issues/1210)


### Features

* bold areas that have never been clicked on ([761845b](https://github.com/lydavid/MusicSearch/commit/761845bc2fa831aad38d40375f929d99dca09b67)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold artists that the user has never clicked on ([5b98f33](https://github.com/lydavid/MusicSearch/commit/5b98f337cb6019180aa4485e74cb0edf8ef4e761)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold collections that have not been clicked on ([de97907](https://github.com/lydavid/MusicSearch/commit/de9790742600100e4993ae113727adcfcc6bb73b)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold events that have not been clicked on ([15f2523](https://github.com/lydavid/MusicSearch/commit/15f2523b572b29f0278ce586fd75f5108aa54893)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold instruments that have not been clicked on ([a28b2fd](https://github.com/lydavid/MusicSearch/commit/a28b2fd7020ee9e1565e2d8eea8ed2d6195cfdd8)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold labels that have not been clicked on ([9cc85ca](https://github.com/lydavid/MusicSearch/commit/9cc85ca8302b3f31e4a6d5d66de4151d74b4266c)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold places that have not been clicked on ([92b8ffc](https://github.com/lydavid/MusicSearch/commit/92b8ffcf1a706e68bd40237d5de7a51d65e591eb)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold recordings that have not been clicked on ([ed45187](https://github.com/lydavid/MusicSearch/commit/ed45187cae1f319acd89817294b19ade5209b846)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold relations whose entity the user has never visited ([d0a6cb9](https://github.com/lydavid/MusicSearch/commit/d0a6cb95c11e3d380e4a610c6c4ae49e213caf0f)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold release groups that have not been clicked on ([0e1fcbc](https://github.com/lydavid/MusicSearch/commit/0e1fcbc414a548307425abbbd44f76f0b0461543)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold releases that the user has never clicked on ([#1194](https://github.com/lydavid/MusicSearch/issues/1194)) ([58fb37d](https://github.com/lydavid/MusicSearch/commit/58fb37da5a2372753a224e9578d66436cacb98a7)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold series that have not been clicked on ([30ce6f8](https://github.com/lydavid/MusicSearch/commit/30ce6f84642280ed006b6591251c5cd86ac137ee)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold tracks that have not been clicked on ([3c40c85](https://github.com/lydavid/MusicSearch/commit/3c40c85f3c7add0ba5f1143cedaf6ff5301e1e03)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold works that have not been clicked on ([127a2e9](https://github.com/lydavid/MusicSearch/commit/127a2e99f3d463e1791c95d0df6fb8fc73e17b85)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* cache search results until query changes allowing us to show thumbnail images and visited status ([#1196](https://github.com/lydavid/MusicSearch/issues/1196)) ([58d1f44](https://github.com/lydavid/MusicSearch/commit/58d1f448066768644b506183fc177a4050e2e0dd))
* clicking on a medium's name will toggle hiding/showing all of its tracks ([#1186](https://github.com/lydavid/MusicSearch/issues/1186)) ([ef62da1](https://github.com/lydavid/MusicSearch/commit/ef62da11a4e7622ea28d39ff39dd5d4133d4fccc)), closes [#1185](https://github.com/lydavid/MusicSearch/issues/1185)
* copy Wikipedia extract by long clicking ([0957576](https://github.com/lydavid/MusicSearch/commit/0957576707208cff3f8756bee3aa3c3a3dd8b549))
* **desktop:** support paging cover art images with direction keys ([832eefa](https://github.com/lydavid/MusicSearch/commit/832eefa7405a548a8892f3b228d74bf4f2a74fda))
* preselect matching entity type when adding creating a collection from an entity's page ([919e98c](https://github.com/lydavid/MusicSearch/commit/919e98c6c037b97f9416d893e481c9183c145e3d))
* put swipe to delete collection items behind an edit mode ([4027054](https://github.com/lydavid/MusicSearch/commit/4027054d72e6042a2efab68b68abdbf2064ba3c9)), closes [#1009](https://github.com/lydavid/MusicSearch/issues/1009)
* show number of search results found ([d6f6953](https://github.com/lydavid/MusicSearch/commit/d6f69534ab6f64514e46b22b49b0f569923268dc))
* support creating and adding to collections through Android intent ([#1228](https://github.com/lydavid/MusicSearch/issues/1228)) ([cec3e5a](https://github.com/lydavid/MusicSearch/commit/cec3e5a0f669b65baaaed8424b9eb0731bb5e8a0))
* support swiping to delete local collections after toggling edit mode ([5a7a795](https://github.com/lydavid/MusicSearch/commit/5a7a795d2d14dcb4e1c6901515530c178ffe218f)), closes [#1201](https://github.com/lydavid/MusicSearch/issues/1201)
* target Android 15 (API 35) ([#1182](https://github.com/lydavid/MusicSearch/issues/1182)) ([d02c265](https://github.com/lydavid/MusicSearch/commit/d02c265c071859fbc3a357e077c97d700e0f20d3))

## [1.8.0-beta.16](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.15...v1.8.0-beta.16) (2024-11-16)


### Bug Fixes

* show "Cannot find collection" instead of crashing when clicking a deleted collection ([5343d5e](https://github.com/lydavid/MusicSearch/commit/5343d5eac87192fe014155ceb38bfbab2fe73cab)), closes [#1227](https://github.com/lydavid/MusicSearch/issues/1227)

## [1.8.0-beta.15](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.14...v1.8.0-beta.15) (2024-11-14)


### Bug Fixes

* do not refresh details when returning from backstack ([61367b3](https://github.com/lydavid/MusicSearch/commit/61367b310c0e507e5b061b5e608d2e69e69cbaf3))
* do not show splash background through navigation bar; also remove the extra blank space drawn for iOS ([53654c5](https://github.com/lydavid/MusicSearch/commit/53654c5c04fcbaf050bb1e6e54b54b9103b6a6f2))
* do not sort release groups separately if the only difference is a null or empty secondary type ([80a457f](https://github.com/lydavid/MusicSearch/commit/80a457f1533a7d6b11114ecc29a7a305976ffa06))
* make snackbar show up again ([f2391a0](https://github.com/lydavid/MusicSearch/commit/f2391a06f6161145df5739b299fb89057572e516))


### Features

* support creating and adding to collections through Android intent ([#1228](https://github.com/lydavid/MusicSearch/issues/1228)) ([cec3e5a](https://github.com/lydavid/MusicSearch/commit/cec3e5a0f669b65baaaed8424b9eb0731bb5e8a0))

## [1.8.0-beta.14](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.13...v1.8.0-beta.14) (2024-11-12)


### Bug Fixes

* make sure navigation bar does not go over content when in landscape mode ([0c807f9](https://github.com/lydavid/MusicSearch/commit/0c807f926e19e0b7f47f3112f31eb082f2f957e1))

## [1.8.0-beta.13](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.12...v1.8.0-beta.13) (2024-11-10)


### Bug Fixes

* retain areas when returning from back stack ([d22efa9](https://github.com/lydavid/MusicSearch/commit/d22efa9e9ee7bf8acbd4eac8079b1475ed0ecc81)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain artists when returning from back stack ([a2d3a60](https://github.com/lydavid/MusicSearch/commit/a2d3a609af06cc09c4f6a464a95cd82e5779a688)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain collections when returning from back stack ([a592bde](https://github.com/lydavid/MusicSearch/commit/a592bde00e90d2152e9860c3b26942a102fad99f)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain events paging position ([c381128](https://github.com/lydavid/MusicSearch/commit/c38112883851295988d5362e9eb9f53481d934f3)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain instruments when returning from back stack ([12be693](https://github.com/lydavid/MusicSearch/commit/12be69337226126e604d6af312376b7703401b5e)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain labels when returning from back stack ([209e753](https://github.com/lydavid/MusicSearch/commit/209e753f28bd8006ac890768b59fe4c23a6a7426)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain lookup history when returning from back stack; needed to retain sort options as well ([adde03f](https://github.com/lydavid/MusicSearch/commit/adde03f40684e7b1fcc3ac64c056aa3dca7b46f7)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain now playing and spotify history position when returning from backstack ([8d76a64](https://github.com/lydavid/MusicSearch/commit/8d76a645f8f43a0e57b0a2a2ffdef3d6a8ba0dd6)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain place collection when returning from back stack ([681eebe](https://github.com/lydavid/MusicSearch/commit/681eebe4a1ba724c6eda75084ad1ab9d98107324)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain places when returning from back stack ([9e2855e](https://github.com/lydavid/MusicSearch/commit/9e2855e8d2d96ac9c576543d5d76dc895d728140)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain recordings when returning from back stack ([a8ab572](https://github.com/lydavid/MusicSearch/commit/a8ab572a662d29b3d5e4c293be1bd2ef6c3a8ea8)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain release groups when returning from back stack ([2da8e2e](https://github.com/lydavid/MusicSearch/commit/2da8e2e18f35e6450d08a929ff5036b9e129d211)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain releases when returning from back stack ([b2ee006](https://github.com/lydavid/MusicSearch/commit/b2ee0063bd4f6d9ac2dec43b45cce1564c8a2d77)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain search results/history when returning from back stack ([4e110c9](https://github.com/lydavid/MusicSearch/commit/4e110c903eb7d853ce02be09abf3ebb0d3e0405d)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain series when returning from back stack ([a884ca2](https://github.com/lydavid/MusicSearch/commit/a884ca24802ad47897ab2e4da1ce98065728ebd2)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain tracks when returning from back stack ([43ac4d2](https://github.com/lydavid/MusicSearch/commit/43ac4d2c5bc227eec9f6ee55eff509042ab2bef5)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* retain works when returning from back stack ([bf6cd0b](https://github.com/lydavid/MusicSearch/commit/bf6cd0b5ea3a86b4ed5711ac39de445d028843a3)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)
* use `collectAsRetainedState` to maintain list position when sorting is not set to the default option ([9e4c7f8](https://github.com/lydavid/MusicSearch/commit/9e4c7f82e1238f2e4285415231004c3420c49a46)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)


### Features

* show number of search results found ([d6f6953](https://github.com/lydavid/MusicSearch/commit/d6f69534ab6f64514e46b22b49b0f569923268dc))

## [1.8.0-beta.12](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.11...v1.8.0-beta.12) (2024-11-07)


### Bug Fixes

* place will show the most specific area if there are multiple areas the place belong ([#1216](https://github.com/lydavid/MusicSearch/issues/1216)) ([3cdaea5](https://github.com/lydavid/MusicSearch/commit/3cdaea505f1ee48a5a6b33e6a09eaba40275d5b3))
* retain relationships paging position when returning from back stack ([31c6f59](https://github.com/lydavid/MusicSearch/commit/31c6f5950d7692a10a09b5c1e36666509b88908c)), closes [#1197](https://github.com/lydavid/MusicSearch/issues/1197)

## [1.8.0-beta.11](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.10...v1.8.0-beta.11) (2024-11-06)


### Bug Fixes

* **ios:** add CADisableMinimumFrameDurationOnPhone ([c62b231](https://github.com/lydavid/MusicSearch/commit/c62b23183f71689366911a0775cb8925c454c11c))


### Features

* support swiping to delete local collections after toggling edit mode ([5a7a795](https://github.com/lydavid/MusicSearch/commit/5a7a795d2d14dcb4e1c6901515530c178ffe218f)), closes [#1201](https://github.com/lydavid/MusicSearch/issues/1201)

## [1.8.0-beta.10](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.9...v1.8.0-beta.10) (2024-11-04)


### Bug Fixes

* release labels not being shown as visited ([d169a83](https://github.com/lydavid/MusicSearch/commit/d169a83a5a7dc194fff7a4ea3bb91cedd84c4bb0)), closes [#1211](https://github.com/lydavid/MusicSearch/issues/1211)
* Wikipedia link should not be bolded ([976c983](https://github.com/lydavid/MusicSearch/commit/976c9831e85d7ed4625116e33c62974ef8ff34ac)), closes [#1210](https://github.com/lydavid/MusicSearch/issues/1210)


### Features

* copy Wikipedia extract by long clicking ([0957576](https://github.com/lydavid/MusicSearch/commit/0957576707208cff3f8756bee3aa3c3a3dd8b549))

## [1.8.0-beta.9](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.8...v1.8.0-beta.9) (2024-11-03)


### Bug Fixes

* entities in collections not bolded when not visited ([9671222](https://github.com/lydavid/MusicSearch/commit/9671222a05180d86f6f4002b0db5ab9854d1409d)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)


### Features

* bold recordings that have not been clicked on ([ed45187](https://github.com/lydavid/MusicSearch/commit/ed45187cae1f319acd89817294b19ade5209b846)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold relations whose entity the user has never visited ([d0a6cb9](https://github.com/lydavid/MusicSearch/commit/d0a6cb95c11e3d380e4a610c6c4ae49e213caf0f)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold release groups that have not been clicked on ([0e1fcbc](https://github.com/lydavid/MusicSearch/commit/0e1fcbc414a548307425abbbd44f76f0b0461543)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold series that have not been clicked on ([30ce6f8](https://github.com/lydavid/MusicSearch/commit/30ce6f84642280ed006b6591251c5cd86ac137ee)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold tracks that have not been clicked on ([3c40c85](https://github.com/lydavid/MusicSearch/commit/3c40c85f3c7add0ba5f1143cedaf6ff5301e1e03)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold works that have not been clicked on ([127a2e9](https://github.com/lydavid/MusicSearch/commit/127a2e99f3d463e1791c95d0df6fb8fc73e17b85)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* **desktop:** support paging cover art images with direction keys ([832eefa](https://github.com/lydavid/MusicSearch/commit/832eefa7405a548a8892f3b228d74bf4f2a74fda))

## [1.8.0-beta.8](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.7...v1.8.0-beta.8) (2024-11-02)


### Bug Fixes

* bold artist's area if unvisited ([8e34f84](https://github.com/lydavid/MusicSearch/commit/8e34f84b369641e738d6bf3d80caa8047ed10cfc))
* **desktop:** add missing Main dispatcher ([18854de](https://github.com/lydavid/MusicSearch/commit/18854de75c3ea9f3c327b3fea0034d11c712a035))


### Features

* bold events that have not been clicked on ([15f2523](https://github.com/lydavid/MusicSearch/commit/15f2523b572b29f0278ce586fd75f5108aa54893)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold instruments that have not been clicked on ([a28b2fd](https://github.com/lydavid/MusicSearch/commit/a28b2fd7020ee9e1565e2d8eea8ed2d6195cfdd8)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold labels that have not been clicked on ([9cc85ca](https://github.com/lydavid/MusicSearch/commit/9cc85ca8302b3f31e4a6d5d66de4151d74b4266c)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* bold places that have not been clicked on ([92b8ffc](https://github.com/lydavid/MusicSearch/commit/92b8ffcf1a706e68bd40237d5de7a51d65e591eb)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* preselect matching entity type when adding creating a collection from an entity's page ([919e98c](https://github.com/lydavid/MusicSearch/commit/919e98c6c037b97f9416d893e481c9183c145e3d))

## [1.8.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.6...v1.8.0-beta.7) (2024-10-31)


### Features

* bold collections that have not been clicked on ([de97907](https://github.com/lydavid/MusicSearch/commit/de9790742600100e4993ae113727adcfcc6bb73b)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)

## [1.8.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.5...v1.8.0-beta.6) (2024-10-30)


### Features

* bold areas that have never been clicked on ([761845b](https://github.com/lydavid/MusicSearch/commit/761845bc2fa831aad38d40375f929d99dca09b67)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)
* put swipe to delete collection items behind an edit mode ([4027054](https://github.com/lydavid/MusicSearch/commit/4027054d72e6042a2efab68b68abdbf2064ba3c9)), closes [#1009](https://github.com/lydavid/MusicSearch/issues/1009)

## [1.8.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.4...v1.8.0-beta.5) (2024-10-29)


### Features

* cache search results until query changes allowing us to show thumbnail images and visited status ([#1196](https://github.com/lydavid/MusicSearch/issues/1196)) ([58d1f44](https://github.com/lydavid/MusicSearch/commit/58d1f448066768644b506183fc177a4050e2e0dd))

## [1.8.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.3...v1.8.0-beta.4) (2024-10-25)


### Features

* bold releases that the user has never clicked on ([#1194](https://github.com/lydavid/MusicSearch/issues/1194)) ([58fb37d](https://github.com/lydavid/MusicSearch/commit/58fb37da5a2372753a224e9578d66436cacb98a7)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)

## [1.8.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.2...v1.8.0-beta.3) (2024-10-23)


### Features

* clicking on a medium's name will toggle hiding/showing all of its tracks ([#1186](https://github.com/lydavid/MusicSearch/issues/1186)) ([ef62da1](https://github.com/lydavid/MusicSearch/commit/ef62da11a4e7622ea28d39ff39dd5d4133d4fccc)), closes [#1185](https://github.com/lydavid/MusicSearch/issues/1185)

## [1.8.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.8.0-beta.1...v1.8.0-beta.2) (2024-10-22)


### Features

* bold artists that the user has never clicked on ([5b98f33](https://github.com/lydavid/MusicSearch/commit/5b98f337cb6019180aa4485e74cb0edf8ef4e761)), closes [#1158](https://github.com/lydavid/MusicSearch/issues/1158)

## [1.8.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.7.0...v1.8.0-beta.1) (2024-10-21)


### Features

* target Android 15 (API 35) ([#1182](https://github.com/lydavid/MusicSearch/issues/1182)) ([d02c265](https://github.com/lydavid/MusicSearch/commit/d02c265c071859fbc3a357e077c97d700e0f20d3))

## [1.7.0](https://github.com/lydavid/MusicSearch/compare/v1.6.0...v1.7.0) (2024-10-20)


### Bug Fixes

* artist's area is not required ([082527f](https://github.com/lydavid/MusicSearch/commit/082527f1eb6613d08d69ca412e4113ba41930557))
* center canvas when filter text changes ([d6c499e](https://github.com/lydavid/MusicSearch/commit/d6c499ef29464797f571f18a809ef18699570b88))
* clean before assembling ([4e92e83](https://github.com/lydavid/MusicSearch/commit/4e92e838e248167206391fef6a930ba2ec785e7c)), closes [#1178](https://github.com/lydavid/MusicSearch/issues/1178)
* **desktop:** use platform-specific app cache directory to store the preferences data store in a location that is writable by our app ([181e6aa](https://github.com/lydavid/MusicSearch/commit/181e6aa39f82f0e5654b234cb10eae5ff9748760))
* **desktop:** use platform-specific app cache directory to store the sqlite db in a location that is writable by our app ([6bfcbc5](https://github.com/lydavid/MusicSearch/commit/6bfcbc5053a73821b04f0ece2bb777aa693f471a))
* handle back press for api level 33 and below  ([#1154](https://github.com/lydavid/MusicSearch/issues/1154)) ([b0c0baf](https://github.com/lydavid/MusicSearch/commit/b0c0baf0bc4a30753a62e809f03a9682ae759f42)), closes [#1153](https://github.com/lydavid/MusicSearch/issues/1153)
* make all non-sqlite text filter ignore case ([d93885c](https://github.com/lydavid/MusicSearch/commit/d93885c250d3cc732c9030227dcd213dcee7520a))
* store artist's area in artist table  ([#1156](https://github.com/lydavid/MusicSearch/issues/1156)) ([be787a2](https://github.com/lydavid/MusicSearch/commit/be787a2b16ea2a28fd2620b2ffd7be748363c110)), closes [#1155](https://github.com/lydavid/MusicSearch/issues/1155)


### Features

* display artist's area in details screen with navigation ([e81e6ab](https://github.com/lydavid/MusicSearch/commit/e81e6ab5980569476b7474761714ca215fddfdac)), closes [#1064](https://github.com/lydavid/MusicSearch/issues/1064)
* support refreshing area ([fd125f7](https://github.com/lydavid/MusicSearch/commit/fd125f757ac5998823a10e5052fb58a532bb8ef4)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing event ([3698449](https://github.com/lydavid/MusicSearch/commit/36984494bafc1a32e6a5817a152d201f27a9a585)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing instruments ([807ebb2](https://github.com/lydavid/MusicSearch/commit/807ebb2c1b9718f80e58f66400f370611b430959)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing labels ([6329e97](https://github.com/lydavid/MusicSearch/commit/6329e971f85360024797b35bb4fd3d932d5e1fd4)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing places ([8869102](https://github.com/lydavid/MusicSearch/commit/8869102fe86b0b7dfc7b18b3af995152a32f6ba9)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing recording ([3df5f97](https://github.com/lydavid/MusicSearch/commit/3df5f970a5cc75afc0289874a6efebb0a9c3814a)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing release group ([ebb4d8c](https://github.com/lydavid/MusicSearch/commit/ebb4d8c0c1a7ac06ce286a62618dfd99874e5883))
* support refreshing series ([6e003c1](https://github.com/lydavid/MusicSearch/commit/6e003c14d2baed9f1e7df0bc6d335f052bc665f3)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing work ([3388723](https://github.com/lydavid/MusicSearch/commit/33887237693a3c505ffca34431ce4d272ed11323)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)

## [1.7.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v1.7.0-beta.6...v1.7.0-beta.7) (2024-10-18)


### Bug Fixes

* clean before assembling ([4e92e83](https://github.com/lydavid/MusicSearch/commit/4e92e838e248167206391fef6a930ba2ec785e7c)), closes [#1178](https://github.com/lydavid/MusicSearch/issues/1178)

## [1.7.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v1.7.0-beta.5...v1.7.0-beta.6) (2024-10-17)


### Features

* support refreshing places ([8869102](https://github.com/lydavid/MusicSearch/commit/8869102fe86b0b7dfc7b18b3af995152a32f6ba9)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)

## [1.7.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v1.7.0-beta.4...v1.7.0-beta.5) (2024-10-16)


### Bug Fixes

* center canvas when filter text changes ([d6c499e](https://github.com/lydavid/MusicSearch/commit/d6c499ef29464797f571f18a809ef18699570b88))


### Features

* support refreshing area ([fd125f7](https://github.com/lydavid/MusicSearch/commit/fd125f757ac5998823a10e5052fb58a532bb8ef4)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing event ([3698449](https://github.com/lydavid/MusicSearch/commit/36984494bafc1a32e6a5817a152d201f27a9a585)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing instruments ([807ebb2](https://github.com/lydavid/MusicSearch/commit/807ebb2c1b9718f80e58f66400f370611b430959)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing labels ([6329e97](https://github.com/lydavid/MusicSearch/commit/6329e971f85360024797b35bb4fd3d932d5e1fd4)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing recording ([3df5f97](https://github.com/lydavid/MusicSearch/commit/3df5f970a5cc75afc0289874a6efebb0a9c3814a)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing release group ([ebb4d8c](https://github.com/lydavid/MusicSearch/commit/ebb4d8c0c1a7ac06ce286a62618dfd99874e5883))
* support refreshing series ([6e003c1](https://github.com/lydavid/MusicSearch/commit/6e003c14d2baed9f1e7df0bc6d335f052bc665f3)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)
* support refreshing work ([3388723](https://github.com/lydavid/MusicSearch/commit/33887237693a3c505ffca34431ce4d272ed11323)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)

## [1.7.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.7.0-beta.3...v1.7.0-beta.4) (2024-10-08)


### Bug Fixes

* store artist's area in artist table  ([#1156](https://github.com/lydavid/MusicSearch/issues/1156)) ([be787a2](https://github.com/lydavid/MusicSearch/commit/be787a2b16ea2a28fd2620b2ffd7be748363c110)), closes [#1155](https://github.com/lydavid/MusicSearch/issues/1155)

## [1.7.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.7.0-beta.2...v1.7.0-beta.3) (2024-10-07)


### Bug Fixes

* handle back press for api level 33 and below  ([#1154](https://github.com/lydavid/MusicSearch/issues/1154)) ([b0c0baf](https://github.com/lydavid/MusicSearch/commit/b0c0baf0bc4a30753a62e809f03a9682ae759f42)), closes [#1153](https://github.com/lydavid/MusicSearch/issues/1153)

## [1.7.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.7.0-beta.1...v1.7.0-beta.2) (2024-10-06)


### Bug Fixes

* artist's area is not required ([082527f](https://github.com/lydavid/MusicSearch/commit/082527f1eb6613d08d69ca412e4113ba41930557))

## [1.7.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.6.1-beta.2...v1.7.0-beta.1) (2024-10-06)


### Bug Fixes

* make all non-sqlite text filter ignore case ([d93885c](https://github.com/lydavid/MusicSearch/commit/d93885c250d3cc732c9030227dcd213dcee7520a))


### Features

* display artist's area in details screen with navigation ([e81e6ab](https://github.com/lydavid/MusicSearch/commit/e81e6ab5980569476b7474761714ca215fddfdac)), closes [#1064](https://github.com/lydavid/MusicSearch/issues/1064)

## [1.6.1-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.6.1-beta.1...v1.6.1-beta.2) (2024-10-04)


### Bug Fixes

* **desktop:** use platform-specific app cache directory to store the preferences data store in a location that is writable by our app ([181e6aa](https://github.com/lydavid/MusicSearch/commit/181e6aa39f82f0e5654b234cb10eae5ff9748760))

## [1.6.1-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.6.0...v1.6.1-beta.1) (2024-10-04)


### Bug Fixes

* **desktop:** use platform-specific app cache directory to store the sqlite db in a location that is writable by our app ([6bfcbc5](https://github.com/lydavid/MusicSearch/commit/6bfcbc5053a73821b04f0ece2bb777aa693f471a))

## [1.6.0](https://github.com/lydavid/MusicSearch/compare/v1.5.0...v1.6.0) (2024-10-03)


### Bug Fixes

* better error handling; pass error reason and resolution to snackbar; remove redundant snackbar when showing full-screen error ([a183e0a](https://github.com/lydavid/MusicSearch/commit/a183e0aaf92e0f686af5591d442bdb7faaa334a7)), closes [#1127](https://github.com/lydavid/MusicSearch/issues/1127)
* catch handled exceptions ([8af1402](https://github.com/lydavid/MusicSearch/commit/8af1402dc1ecfae9a88e7e5a6d28740b812603f3)), closes [#1127](https://github.com/lydavid/MusicSearch/issues/1127)
* do not crash if we somehow insert two release groups for a given release ([f66f182](https://github.com/lydavid/MusicSearch/commit/f66f182a43c4459000fbb4b0363636e25ed59ce5))
* do not crash when entering spaces into search text field ([e669b34](https://github.com/lydavid/MusicSearch/commit/e669b341ca42c85085962db3144f51d570a91ada))
* do not include Spotify client id/secret in F-Droid APK ([422e987](https://github.com/lydavid/MusicSearch/commit/422e98784eac3c69b1066d747633ef28a2499359))
* exclude commit revision from apk ([103f9af](https://github.com/lydavid/MusicSearch/commit/103f9af105cdcac1dcb4ecb432449e56bcdd317c))
* ignore AboutLibraries generated timestamp ([e53e747](https://github.com/lydavid/MusicSearch/commit/e53e74751ed6d0ded67bc20a6f7c1a1d3491bf2d))
* provide debug MusicBrainz client ID/secret for development and make sure release client ID/secret is available for F-Droid ([6ad0c72](https://github.com/lydavid/MusicSearch/commit/6ad0c72ad37c21c803888d9376a3f3c6ec063629))
* remove DependencyInfoBlock ([f296ccb](https://github.com/lydavid/MusicSearch/commit/f296ccb389ba4a66226c18d7339689fe718377fa))
* remove Firebase and Google services for F-Droid APK ([b38e7ec](https://github.com/lydavid/MusicSearch/commit/b38e7ecad1aa28fe7da1d72dac267b8a71c4afc9))
* remove trailing space in file name ([d8af507](https://github.com/lydavid/MusicSearch/commit/d8af507fe9db10a142026f9d54b764b19931e670))
* send credentials without waiting for 403 when adding to collections ([15d6a5a](https://github.com/lydavid/MusicSearch/commit/15d6a5a23ae2f78857b5d945fbd67da2ba8a123c)), closes [#1119](https://github.com/lydavid/MusicSearch/issues/1119)
* when image url API calls fail, default to showing nothing rather than crashing ([c7922a3](https://github.com/lydavid/MusicSearch/commit/c7922a309559ff328b8ab1559f0fa32f35e7c5c9)), closes [#1140](https://github.com/lydavid/MusicSearch/issues/1140)


### Features

* load release group image async and support swipe to refresh ([0ebd54e](https://github.com/lydavid/MusicSearch/commit/0ebd54e62e9a55a27df498346066e728f09b92bd))
* show artist image in list screens (collection, area, release, and work screens) ([899f957](https://github.com/lydavid/MusicSearch/commit/899f957b4b24c3ce005f213393bd8d58019d33b7))
* show number of images in a release ([08473a9](https://github.com/lydavid/MusicSearch/commit/08473a99177a0c3de4f3da7acfecef4337d6e58b))
* support login from snackbar from all entity details screens ([7a7513b](https://github.com/lydavid/MusicSearch/commit/7a7513b5315909295892f79e7e6b2b671fc39cbf)), closes [#1014](https://github.com/lydavid/MusicSearch/issues/1014)
* support login from snackbar from release screen ([9dbd839](https://github.com/lydavid/MusicSearch/commit/9dbd83914e70b579c46d29fddb9b37e5a62df805))

## [1.6.0-beta.10](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.9...v1.6.0-beta.10) (2024-10-03)


### Features

* show number of images in a release ([08473a9](https://github.com/lydavid/MusicSearch/commit/08473a99177a0c3de4f3da7acfecef4337d6e58b))

## [1.6.0-beta.9](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.8...v1.6.0-beta.9) (2024-09-30)


### Bug Fixes

* do not crash if we somehow insert two release groups for a given release ([f66f182](https://github.com/lydavid/MusicSearch/commit/f66f182a43c4459000fbb4b0363636e25ed59ce5))
* when image url API calls fail, default to showing nothing rather than crashing ([c7922a3](https://github.com/lydavid/MusicSearch/commit/c7922a309559ff328b8ab1559f0fa32f35e7c5c9)), closes [#1140](https://github.com/lydavid/MusicSearch/issues/1140)


### Features

* load release group image async and support swipe to refresh ([0ebd54e](https://github.com/lydavid/MusicSearch/commit/0ebd54e62e9a55a27df498346066e728f09b92bd))

## [1.6.0-beta.8](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.7...v1.6.0-beta.8) (2024-09-29)


### Bug Fixes

* remove Firebase and Google services for F-Droid APK ([b38e7ec](https://github.com/lydavid/MusicSearch/commit/b38e7ecad1aa28fe7da1d72dac267b8a71c4afc9))

## [1.6.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.6...v1.6.0-beta.7) (2024-09-26)


### Bug Fixes

* better error handling; pass error reason and resolution to snackbar; remove redundant snackbar when showing full-screen error ([a183e0a](https://github.com/lydavid/MusicSearch/commit/a183e0aaf92e0f686af5591d442bdb7faaa334a7)), closes [#1127](https://github.com/lydavid/MusicSearch/issues/1127)
* catch handled exceptions ([8af1402](https://github.com/lydavid/MusicSearch/commit/8af1402dc1ecfae9a88e7e5a6d28740b812603f3)), closes [#1127](https://github.com/lydavid/MusicSearch/issues/1127)
* do not crash when entering spaces into search text field ([e669b34](https://github.com/lydavid/MusicSearch/commit/e669b341ca42c85085962db3144f51d570a91ada))
* remove DependencyInfoBlock ([f296ccb](https://github.com/lydavid/MusicSearch/commit/f296ccb389ba4a66226c18d7339689fe718377fa))

## [1.6.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.5...v1.6.0-beta.6) (2024-09-23)


### Features

* show artist image in list screens (collection, area, release, and work screens) ([899f957](https://github.com/lydavid/MusicSearch/commit/899f957b4b24c3ce005f213393bd8d58019d33b7))

## [1.6.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.4...v1.6.0-beta.5) (2024-09-22)


### Bug Fixes

* remove trailing space in file name ([d8af507](https://github.com/lydavid/MusicSearch/commit/d8af507fe9db10a142026f9d54b764b19931e670))

## [1.6.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.3...v1.6.0-beta.4) (2024-09-21)


### Bug Fixes

* exclude commit revision from apk ([103f9af](https://github.com/lydavid/MusicSearch/commit/103f9af105cdcac1dcb4ecb432449e56bcdd317c))

## [1.6.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.2...v1.6.0-beta.3) (2024-09-21)


### Bug Fixes

* do not include Spotify client id/secret in F-Droid APK ([422e987](https://github.com/lydavid/MusicSearch/commit/422e98784eac3c69b1066d747633ef28a2499359))
* ignore AboutLibraries generated timestamp ([e53e747](https://github.com/lydavid/MusicSearch/commit/e53e74751ed6d0ded67bc20a6f7c1a1d3491bf2d))

## [1.6.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.6.0-beta.1...v1.6.0-beta.2) (2024-09-19)


### Bug Fixes

* provide debug MusicBrainz client ID/secret for development and make sure release client ID/secret is available for F-Droid ([6ad0c72](https://github.com/lydavid/MusicSearch/commit/6ad0c72ad37c21c803888d9376a3f3c6ec063629))

## [1.6.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.5.0...v1.6.0-beta.1) (2024-09-18)


### Bug Fixes

* send credentials without waiting for 401 when adding to collections ([15d6a5a](https://github.com/lydavid/MusicSearch/commit/15d6a5a23ae2f78857b5d945fbd67da2ba8a123c)), closes [#1119](https://github.com/lydavid/MusicSearch/issues/1119)


### Features

* support login from snackbar from all entity details screens ([7a7513b](https://github.com/lydavid/MusicSearch/commit/7a7513b5315909295892f79e7e6b2b671fc39cbf)), closes [#1014](https://github.com/lydavid/MusicSearch/issues/1014)
* support login from snackbar from release screen ([9dbd839](https://github.com/lydavid/MusicSearch/commit/9dbd83914e70b579c46d29fddb9b37e5a62df805))




## [1.5.0](https://github.com/lydavid/MusicSearch/compare/v1.4.0...v1.5.0) (2024-09-17)


### Bug Fixes

* add many-body force to help separate nodes; draw text under node rather than from center; handle dark mode; update node colors ([151bbcf](https://github.com/lydavid/MusicSearch/commit/151bbcf10fd2a7e5ee39f26f56f4748537f297d1))
* always use the name/title from entity lookup api ([debf0ab](https://github.com/lydavid/MusicSearch/commit/debf0ab4675f6c3d48ffea65978a034e5fe1d429)), closes [#1010](https://github.com/lydavid/MusicSearch/issues/1010)
* crash when intent is null ([805a329](https://github.com/lydavid/MusicSearch/commit/805a3294d9affa64f49e3e888b9add16e71d9b53))
* disambiguate recording name ([87ad8a2](https://github.com/lydavid/MusicSearch/commit/87ad8a2f417f6bab420e3896d30bb68b51b3e593))
* increase minimum graph node size and draw text centered below node ([2729a43](https://github.com/lydavid/MusicSearch/commit/2729a437318abac314849b7377b76da59e537e56)), closes [#1038](https://github.com/lydavid/MusicSearch/issues/1038)
* make status bar visible in all themes ([8411f0f](https://github.com/lydavid/MusicSearch/commit/8411f0fb32f590e0e4ebaf354c78b1d669d2b710))
* persist top app bar filter mode when popping off backstack; request keyboard focus on end of query rather than beginning ([efcf128](https://github.com/lydavid/MusicSearch/commit/efcf12824434f8548e2cdf53daf00882f453828b)), closes [#1037](https://github.com/lydavid/MusicSearch/issues/1037)
* remove list item placement animation due to occasional visual glitch ([0ab917f](https://github.com/lydavid/MusicSearch/commit/0ab917f24fdca14e275b50ec9d5dd2f7bda8c7ea)), closes [#1038](https://github.com/lydavid/MusicSearch/issues/1038)
* show snackbar informing user of the status when adding artists to collection ([11991b6](https://github.com/lydavid/MusicSearch/commit/11991b63a528e61fc4832e713d4bb38870c08be0)), closes [#1014](https://github.com/lydavid/MusicSearch/issues/1014)
* support refresh from menu item for artist and collection list ([a3b401b](https://github.com/lydavid/MusicSearch/commit/a3b401b47e364dad9cafb60eff7c3e734a5a2b78)), closes [#1031](https://github.com/lydavid/MusicSearch/issues/1031)


### Features

* display wikipedia summary in artist details screen ([#1062](https://github.com/lydavid/MusicSearch/issues/1062)) ([97c5fb8](https://github.com/lydavid/MusicSearch/commit/97c5fb8b0bd647bcd0e6edb768d2ef906e0b8a87))
* hide details image when filtering text so that it's not necessary to always dismiss keyboard ([6361693](https://github.com/lydavid/MusicSearch/commit/6361693b6118e83e4ff7ddf4f5de450b74d4e424))
* include ipi and isni codes for artists ([a29fee4](https://github.com/lydavid/MusicSearch/commit/a29fee45d787ece001d5e3ddce49b7184f9b8960))
* include ipi and isni codes for labels ([cb57251](https://github.com/lydavid/MusicSearch/commit/cb57251d9d72ed39e918c361e1cca009338169dd))
* load image and wikipedia extract asynchronously after loading artist to reduce time spent looking at a fullscreen spinner ([#1072](https://github.com/lydavid/MusicSearch/issues/1072)) ([363fe50](https://github.com/lydavid/MusicSearch/commit/363fe50f33a8a75c6f953db3b1e53fcf65e7de08)), closes [#1063](https://github.com/lydavid/MusicSearch/issues/1063)
* support filtering artists and recordings by name in collaboration graph ([9a9b969](https://github.com/lydavid/MusicSearch/commit/9a9b969a106664fe68ee854e0c44e4ee8c7be04d))
* support login from snackbar from artist screen ([7e3af50](https://github.com/lydavid/MusicSearch/commit/7e3af5039778976b6aa3bc1992a3b84b52f6281d))
* support refreshing release cover arts ([b92d9da](https://github.com/lydavid/MusicSearch/commit/b92d9da41dd14a20b6189b06af076a7423d982e2)), closes [#1082](https://github.com/lydavid/MusicSearch/issues/1082)
* support swipe to refresh from for release details ([6abe4a7](https://github.com/lydavid/MusicSearch/commit/6abe4a7000233167b319b38a11282c6c7944cf5a)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)

## [1.5.0-beta.10](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.9...v1.5.0-beta.10) (2024-09-13)


### Bug Fixes

* crash when intent is null ([805a329](https://github.com/lydavid/MusicSearch/commit/805a3294d9affa64f49e3e888b9add16e71d9b53))
* show snackbar informing user of the status when adding artists to collection ([11991b6](https://github.com/lydavid/MusicSearch/commit/11991b63a528e61fc4832e713d4bb38870c08be0)), closes [#1014](https://github.com/lydavid/MusicSearch/issues/1014)


### Features

* support login from snackbar from artist screen ([7e3af50](https://github.com/lydavid/MusicSearch/commit/7e3af5039778976b6aa3bc1992a3b84b52f6281d))

## [1.5.0-beta.9](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.8...v1.5.0-beta.9) (2024-09-12)


### Bug Fixes

* support refresh from menu item for artist and collection list ([a3b401b](https://github.com/lydavid/MusicSearch/commit/a3b401b47e364dad9cafb60eff7c3e734a5a2b78)), closes [#1031](https://github.com/lydavid/MusicSearch/issues/1031)


### Features

* support refreshing release cover arts ([b92d9da](https://github.com/lydavid/MusicSearch/commit/b92d9da41dd14a20b6189b06af076a7423d982e2)), closes [#1082](https://github.com/lydavid/MusicSearch/issues/1082)
* support swipe to refresh from for release details ([6abe4a7](https://github.com/lydavid/MusicSearch/commit/6abe4a7000233167b319b38a11282c6c7944cf5a)), closes [#148](https://github.com/lydavid/MusicSearch/issues/148)

## [1.5.0-beta.8](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.7...v1.5.0-beta.8) (2024-08-22)


### Bug Fixes

* remove list item placement animation due to occasional visual glitch ([0ab917f](https://github.com/lydavid/MusicSearch/commit/0ab917f24fdca14e275b50ec9d5dd2f7bda8c7ea)), closes [#1038](https://github.com/lydavid/MusicSearch/issues/1038)


### Features

* include ipi and isni codes for artists ([a29fee4](https://github.com/lydavid/MusicSearch/commit/a29fee45d787ece001d5e3ddce49b7184f9b8960))
* include ipi and isni codes for labels ([cb57251](https://github.com/lydavid/MusicSearch/commit/cb57251d9d72ed39e918c361e1cca009338169dd))

## [1.5.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.6...v1.5.0-beta.7) (2024-08-21)


### Features

* load image and wikipedia extract asynchronously after loading artist to reduce time spent looking at a fullscreen spinner ([#1072](https://github.com/lydavid/MusicSearch/issues/1072)) ([363fe50](https://github.com/lydavid/MusicSearch/commit/363fe50f33a8a75c6f953db3b1e53fcf65e7de08)), closes [#1063](https://github.com/lydavid/MusicSearch/issues/1063)

## [1.5.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.5...v1.5.0-beta.6) (2024-08-18)


### Features

* display wikipedia summary in artist details screen ([#1062](https://github.com/lydavid/MusicSearch/issues/1062)) ([97c5fb8](https://github.com/lydavid/MusicSearch/commit/97c5fb8b0bd647bcd0e6edb768d2ef906e0b8a87))

## [1.5.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.4...v1.5.0-beta.5) (2024-08-17)


### Features

* hide details image when filtering text so that it's not necessary to always dismiss keyboard ([6361693](https://github.com/lydavid/MusicSearch/commit/6361693b6118e83e4ff7ddf4f5de450b74d4e424))

## [1.5.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.3...v1.5.0-beta.4) (2024-08-14)


### Bug Fixes

* make status bar visible in all themes ([8411f0f](https://github.com/lydavid/MusicSearch/commit/8411f0fb32f590e0e4ebaf354c78b1d669d2b710))

## [1.5.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.2...v1.5.0-beta.3) (2024-08-07)


### Bug Fixes

* always use the name/title from entity lookup api ([debf0ab](https://github.com/lydavid/MusicSearch/commit/debf0ab4675f6c3d48ffea65978a034e5fe1d429)), closes [#1010](https://github.com/lydavid/MusicSearch/issues/1010)
* disambiguate recording name ([87ad8a2](https://github.com/lydavid/MusicSearch/commit/87ad8a2f417f6bab420e3896d30bb68b51b3e593))

## [1.5.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.5.0-beta.1...v1.5.0-beta.2) (2024-08-04)


### Bug Fixes

* increase minimum graph node size and draw text centered below node ([2729a43](https://github.com/lydavid/MusicSearch/commit/2729a437318abac314849b7377b76da59e537e56)), closes [#1038](https://github.com/lydavid/MusicSearch/issues/1038)
* persist top app bar filter mode when popping off backstack; request keyboard focus on end of query rather than beginning ([efcf128](https://github.com/lydavid/MusicSearch/commit/efcf12824434f8548e2cdf53daf00882f453828b)), closes [#1037](https://github.com/lydavid/MusicSearch/issues/1037)

## [1.5.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.4.0...v1.5.0-beta.1) (2024-08-03)


### Bug Fixes

* add many-body force to help separate nodes; draw text under node rather than from center; handle dark mode; update node colors ([151bbcf](https://github.com/lydavid/MusicSearch/commit/151bbcf10fd2a7e5ee39f26f56f4748537f297d1))


### Features

* support filtering artists and recordings by name in collaboration graph ([9a9b969](https://github.com/lydavid/MusicSearch/commit/9a9b969a106664fe68ee854e0c44e4ee8c7be04d))

## [1.4.0](https://github.com/lydavid/MusicSearch/compare/v1.3.0...v1.4.0) (2024-07-28)


### Bug Fixes

* crash when loading remote collections ([15e4542](https://github.com/lydavid/MusicSearch/commit/15e4542c73cd31c7e6db08cf24828a8690eb21c3))
* display release script (e.g. Latin) ([bb60464](https://github.com/lydavid/MusicSearch/commit/bb6046491931b92577f4aa14efe3e875363c05e9))
* iOS images ([520bd8c](https://github.com/lydavid/MusicSearch/commit/520bd8cc790ace5588d72561d15b98c68e162c03))
* iOS not building ([900773f](https://github.com/lydavid/MusicSearch/commit/900773f2972b7a268a0279fdb1e453650b27bfad))
* persist scroll positions for list screens when returning ([77edcb5](https://github.com/lydavid/MusicSearch/commit/77edcb575ae406b5be3186645e62f9949b566878)), closes [#1011](https://github.com/lydavid/MusicSearch/issues/1011)
* persist scroll positions for rest of list screens when returning ([503b55d](https://github.com/lydavid/MusicSearch/commit/503b55dbfd2af810beb5cbc192abf22a96aeec78)), closes [#1016](https://github.com/lydavid/MusicSearch/issues/1016)
* remove force-directed graph's dependence on Choreographer and JavaFX, enabling rendering in Compose Desktop ([#1027](https://github.com/lydavid/MusicSearch/issues/1027)) ([c35c96c](https://github.com/lydavid/MusicSearch/commit/c35c96cf3a1d4a84e27a6ee7bda693de3ee2793d))
* scroll to top when changing search query ([190869c](https://github.com/lydavid/MusicSearch/commit/190869c7c0f653b51be2006719b40508c8e7e9d9))


### Features

* add artist-recording collaboration force-directed graph view to artist screen ([#1017](https://github.com/lydavid/MusicSearch/issues/1017)) ([9a3dd7f](https://github.com/lydavid/MusicSearch/commit/9a3dd7fed3d28a5e567b73595885fb4f3b9f03d1))
* show release group type in search screen ([20f2783](https://github.com/lydavid/MusicSearch/commit/20f278395b9acbf974586359f9124af787f86f15))
* support clicking on artist/recording nodes in collaboration graph ([62064dd](https://github.com/lydavid/MusicSearch/commit/62064dd55a1fb1d653432d6b3040b4028c5cca70))
* support refreshing artist image ([cdddfb9](https://github.com/lydavid/MusicSearch/commit/cdddfb9dffbdc222812d95ab59a19270f609024e))

## [1.4.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.4.0-beta.3...v1.4.0-beta.4) (2024-07-28)


### Bug Fixes

* remove force-directed graph's dependence on Choreographer and JavaFX, enabling rendering in Compose Desktop ([#1027](https://github.com/lydavid/MusicSearch/issues/1027)) ([c35c96c](https://github.com/lydavid/MusicSearch/commit/c35c96cf3a1d4a84e27a6ee7bda693de3ee2793d))

## [1.4.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.4.0-beta.2...v1.4.0-beta.3) (2024-07-26)


### Features

* support clicking on artist/recording nodes in collaboration graph ([62064dd](https://github.com/lydavid/MusicSearch/commit/62064dd55a1fb1d653432d6b3040b4028c5cca70))

## [1.4.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.4.0-beta.1...v1.4.0-beta.2) (2024-07-25)


### Bug Fixes

* crash when loading remote collections ([15e4542](https://github.com/lydavid/MusicSearch/commit/15e4542c73cd31c7e6db08cf24828a8690eb21c3))
* iOS images ([520bd8c](https://github.com/lydavid/MusicSearch/commit/520bd8cc790ace5588d72561d15b98c68e162c03))


### Features

* add artist-recording collaboration force-directed graph view to artist screen ([#1017](https://github.com/lydavid/MusicSearch/issues/1017)) ([9a3dd7f](https://github.com/lydavid/MusicSearch/commit/9a3dd7fed3d28a5e567b73595885fb4f3b9f03d1))

## [1.4.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.3.1-beta.2...v1.4.0-beta.1) (2024-07-22)


### Bug Fixes

* iOS not building ([900773f](https://github.com/lydavid/MusicSearch/commit/900773f2972b7a268a0279fdb1e453650b27bfad))
* scroll to top when changing search query ([190869c](https://github.com/lydavid/MusicSearch/commit/190869c7c0f653b51be2006719b40508c8e7e9d9))


### Features

* show release group type in search screen ([20f2783](https://github.com/lydavid/MusicSearch/commit/20f278395b9acbf974586359f9124af787f86f15))
* support refreshing artist image ([cdddfb9](https://github.com/lydavid/MusicSearch/commit/cdddfb9dffbdc222812d95ab59a19270f609024e))

## [1.3.1-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.3.1-beta.1...v1.3.1-beta.2) (2024-07-17)


### Bug Fixes

* persist scroll positions for rest of list screens when returning ([503b55d](https://github.com/lydavid/MusicSearch/commit/503b55dbfd2af810beb5cbc192abf22a96aeec78)), closes [#1016](https://github.com/lydavid/MusicSearch/issues/1016)

## [1.3.1-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.3.0...v1.3.1-beta.1) (2024-07-16)


### Bug Fixes

* display release script (e.g. Latin) ([bb60464](https://github.com/lydavid/MusicSearch/commit/bb6046491931b92577f4aa14efe3e875363c05e9))
* persist scroll positions for list screens when returning ([77edcb5](https://github.com/lydavid/MusicSearch/commit/77edcb575ae406b5be3186645e62f9949b566878)), closes [#1011](https://github.com/lydavid/MusicSearch/issues/1011)

## [1.3.0](https://github.com/lydavid/MusicSearch/compare/v1.2.0...v1.3.0) (2024-07-13)


### Bug Fixes

* crash on cover arts screen when app is put in background ([de45ce2](https://github.com/lydavid/MusicSearch/commit/de45ce28828950d2cba494d14707bdde99139945))
* crash when release group has multiple cover arts ([1ef8a6a](https://github.com/lydavid/MusicSearch/commit/1ef8a6a22bfc8adc58a683c9120f31b34232f1cd))
* display qaa language as "[Artificial (Other)]" ([c84a5f3](https://github.com/lydavid/MusicSearch/commit/c84a5f3a5bc8ced7d0893ae26d40be01c78050e6))
* persist area list item states when clicking back ([55e4c26](https://github.com/lydavid/MusicSearch/commit/55e4c26a59c53bbf78f457ad0685c2948879c709))
* reduce recompositions for thumbnail images; enable compose runtime for :core:models ([#990](https://github.com/lydavid/MusicSearch/issues/990)) ([6454482](https://github.com/lydavid/MusicSearch/commit/6454482215b0ce8604ebde9bef63e48596b1f8a6))
* release with multiple cover arts will no longer show their catalog numbers multiple times in releases by label screen ([9558caf](https://github.com/lydavid/MusicSearch/commit/9558caff35f9abedd9bdadf671670d03cd3856e8))
* use 1200px images instead of full-sized images ([e005087](https://github.com/lydavid/MusicSearch/commit/e005087f48a534bf00d85a5abb637baefe3999f8)), closes [#991](https://github.com/lydavid/MusicSearch/issues/991)


### Features

* add collection sort options to sort alphabetically, and by entity count ([#978](https://github.com/lydavid/MusicSearch/issues/978)) ([49413ee](https://github.com/lydavid/MusicSearch/commit/49413ee66d594559ef1e7d6fd771f95170c1123c))
* better support for viewing large image in landscape mode and desktop ([#994](https://github.com/lydavid/MusicSearch/issues/994)) ([9be2001](https://github.com/lydavid/MusicSearch/commit/9be2001df735e5e26a609d7ab5d60ff675f82096))
* browse release cover art images by clicking on it from release details ([#988](https://github.com/lydavid/MusicSearch/issues/988)) ([7b9ccfb](https://github.com/lydavid/MusicSearch/commit/7b9ccfb9fa9896b49e1ceffd38b341952ab984bd))
* filter releases by catalog number ([5bc6a9b](https://github.com/lydavid/MusicSearch/commit/5bc6a9b217f3dd85904ce9425cfbd6212dc6f3b9))
* load images on desktop and iOS with Coil 3 ([#987](https://github.com/lydavid/MusicSearch/issues/987)) ([6d9a735](https://github.com/lydavid/MusicSearch/commit/6d9a735ba9189def4529244c42587a049e2165a5))
* navigation rail for non-Compact window width ([099839b](https://github.com/lydavid/MusicSearch/commit/099839b53a1642ee5b231fc0723099c8e03e3be0))
* show catalog numbers for releases by label ([#982](https://github.com/lydavid/MusicSearch/issues/982)) ([a31e137](https://github.com/lydavid/MusicSearch/commit/a31e1379b6b958511b68445c45b1d1e2a1743466))
* show how many entities each collection list item has ([#976](https://github.com/lydavid/MusicSearch/issues/976)) ([af9d1c7](https://github.com/lydavid/MusicSearch/commit/af9d1c7cbf0f3d693d58ec62c774d323a88bdd9d))

## [1.3.0-beta.8](https://github.com/lydavid/MusicSearch/compare/v1.3.0-beta.7...v1.3.0-beta.8) (2024-07-11)


### Bug Fixes

* release with multiple cover arts will no longer show their catalog numbers multiple times in releases by label screen ([9558caf](https://github.com/lydavid/MusicSearch/commit/9558caff35f9abedd9bdadf671670d03cd3856e8))

## [1.3.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v1.3.0-beta.6...v1.3.0-beta.7) (2024-07-10)


### Bug Fixes

* persist area list item states when clicking back ([55e4c26](https://github.com/lydavid/MusicSearch/commit/55e4c26a59c53bbf78f457ad0685c2948879c709))

## [1.3.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v1.3.0-beta.5...v1.3.0-beta.6) (2024-07-09)


### Bug Fixes

* crash when release group has multiple cover arts ([1ef8a6a](https://github.com/lydavid/MusicSearch/commit/1ef8a6a22bfc8adc58a683c9120f31b34232f1cd))

## [1.3.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v1.3.0-beta.4...v1.3.0-beta.5) (2024-07-08)


### Bug Fixes

* crash on cover arts screen when app is put in background ([de45ce2](https://github.com/lydavid/MusicSearch/commit/de45ce28828950d2cba494d14707bdde99139945))

## [1.3.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.3.0-beta.3...v1.3.0-beta.4) (2024-07-07)


### Bug Fixes

* reduce recompositions for thumbnail images; enable compose runtime for :core:models ([#990](https://github.com/lydavid/MusicSearch/issues/990)) ([6454482](https://github.com/lydavid/MusicSearch/commit/6454482215b0ce8604ebde9bef63e48596b1f8a6))
* use 1200px images instead of full-sized images ([e005087](https://github.com/lydavid/MusicSearch/commit/e005087f48a534bf00d85a5abb637baefe3999f8)), closes [#991](https://github.com/lydavid/MusicSearch/issues/991)


### Features

* better support for viewing large image in landscape mode and desktop ([#994](https://github.com/lydavid/MusicSearch/issues/994)) ([9be2001](https://github.com/lydavid/MusicSearch/commit/9be2001df735e5e26a609d7ab5d60ff675f82096))
* navigation rail for non-Compact window width ([099839b](https://github.com/lydavid/MusicSearch/commit/099839b53a1642ee5b231fc0723099c8e03e3be0))

## [1.3.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.3.0-beta.2...v1.3.0-beta.3) (2024-07-04)


### Features

* browse release cover art images by clicking on it from release details ([#988](https://github.com/lydavid/MusicSearch/issues/988)) ([7b9ccfb](https://github.com/lydavid/MusicSearch/commit/7b9ccfb9fa9896b49e1ceffd38b341952ab984bd))

## [1.3.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.3.0-beta.1...v1.3.0-beta.2) (2024-07-02)


### Features

* load images on desktop and iOS with Coil 3 ([#987](https://github.com/lydavid/MusicSearch/issues/987)) ([6d9a735](https://github.com/lydavid/MusicSearch/commit/6d9a735ba9189def4529244c42587a049e2165a5))

## [1.3.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.2.1-beta.1...v1.3.0-beta.1) (2024-06-30)


### Features

* add collection sort options to sort alphabetically, and by entity count ([#978](https://github.com/lydavid/MusicSearch/issues/978)) ([49413ee](https://github.com/lydavid/MusicSearch/commit/49413ee66d594559ef1e7d6fd771f95170c1123c))
* filter releases by catalog number ([5bc6a9b](https://github.com/lydavid/MusicSearch/commit/5bc6a9b217f3dd85904ce9425cfbd6212dc6f3b9))
* show catalog numbers for releases by label ([#982](https://github.com/lydavid/MusicSearch/issues/982)) ([a31e137](https://github.com/lydavid/MusicSearch/commit/a31e1379b6b958511b68445c45b1d1e2a1743466))
* show how many entities each collection list item has ([#976](https://github.com/lydavid/MusicSearch/issues/976)) ([af9d1c7](https://github.com/lydavid/MusicSearch/commit/af9d1c7cbf0f3d693d58ec62c774d323a88bdd9d))

## [1.2.1-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.2.0...v1.2.1-beta.1) (2024-06-30)


### Bug Fixes

* display qaa language as "[Artificial (Other)]" ([c84a5f3](https://github.com/lydavid/MusicSearch/commit/c84a5f3a5bc8ced7d0893ae26d40be01c78050e6))

## [1.2.0](https://github.com/lydavid/MusicSearch/compare/v1.1.0...v1.2.0) (2024-06-30)


### Bug Fixes

* always show Releases tab for an area; do not change release event areas to country ([a117436](https://github.com/lydavid/MusicSearch/commit/a1174364254ec9691befac0e2bd0b6578c9426d2)), closes [#479](https://github.com/lydavid/MusicSearch/issues/479)
* artists by area shared scroll state with events by area ([5d3e738](https://github.com/lydavid/MusicSearch/commit/5d3e738fc6f04e1d831b0e3557b626d852a3f15a))
* bad migration ([#943](https://github.com/lydavid/MusicSearch/issues/943)) ([7dccaba](https://github.com/lydavid/MusicSearch/commit/7dccaba5a9fb07e40e96e45ebee894c36220a5b8))
* crash due to `artist_credit.name` change after refreshing ([#929](https://github.com/lydavid/MusicSearch/issues/929)) ([a1b88ac](https://github.com/lydavid/MusicSearch/commit/a1b88acd44abb5dd20616d2096e4ac04128c4dd0)), closes [#859](https://github.com/lydavid/MusicSearch/issues/859)
* crash due to grouping tracks incorrectly ([8e3c641](https://github.com/lydavid/MusicSearch/commit/8e3c64148653b4890b5684b42b4fb01fdc42f3e7))
* do not order artists ([4c5e463](https://github.com/lydavid/MusicSearch/commit/4c5e463ee5d59375745650ec6edbb8042e754d2d))
* NowPlayingNotificationListener leak [#895](https://github.com/lydavid/MusicSearch/issues/895) ([3fc54b9](https://github.com/lydavid/MusicSearch/commit/3fc54b90bdab4f4839eca74f0a9c1c47f4161e99))
* save tracks position when moving between tabs ([eb9e36a](https://github.com/lydavid/MusicSearch/commit/eb9e36a5e4a2a11f2620e491b0c3223a73e3a830))
* support refreshing empty state such as "No results found." ([bcae3f1](https://github.com/lydavid/MusicSearch/commit/bcae3f1df3ddebd02ed22b3c2a5e9dd03fd7c38f)), closes [#147](https://github.com/lydavid/MusicSearch/issues/147)


### Features

* able to delete spotify history ([#939](https://github.com/lydavid/MusicSearch/issues/939)) ([d219eab](https://github.com/lydavid/MusicSearch/commit/d219eabd85309db71e66c462f885d9d53cc84fe3))
* **android:** listen to Spotify broadcast on app start in the background ([cad38e0](https://github.com/lydavid/MusicSearch/commit/cad38e03452488e5a43b6a09f617d8e6fe95e092))
* **android:** record spotify listen history ([#888](https://github.com/lydavid/MusicSearch/issues/888)) ([b867b39](https://github.com/lydavid/MusicSearch/commit/b867b39eb00740613ef1b60bae908bc963aa3d3f))
* browse artists by area ([#918](https://github.com/lydavid/MusicSearch/issues/918)) ([6617fcb](https://github.com/lydavid/MusicSearch/commit/6617fcb7ec2b4a32e8af1b50477347299a2ef726))
* browse recordings by artist ([#932](https://github.com/lydavid/MusicSearch/issues/932)) ([04ba6ba](https://github.com/lydavid/MusicSearch/commit/04ba6bab283b214e02b9e3c439be5e20f49f155a))
* **desktop:** use persistent database ([5ace05c](https://github.com/lydavid/MusicSearch/commit/5ace05ca3c6d7ad43a915749cf938b67056c2a89)), closes [#787](https://github.com/lydavid/MusicSearch/issues/787)
* distribute desktop apps in GitHub Releases with Conveyor ([#952](https://github.com/lydavid/MusicSearch/issues/952)) ([e3164e5](https://github.com/lydavid/MusicSearch/commit/e3164e5113310bcd98aae7e2816ffc39c33f9bc9))
* enable edge-to-edge ([#971](https://github.com/lydavid/MusicSearch/issues/971)) ([e84794c](https://github.com/lydavid/MusicSearch/commit/e84794c9c49b806823b3d214a37b5eb16e49043c))
* **ios:** implement datastore preferences ([7816c8d](https://github.com/lydavid/MusicSearch/commit/7816c8d9392aec5bc41c758b462322eceff69aac))
* **ios:** search screen works ([ed4d114](https://github.com/lydavid/MusicSearch/commit/ed4d114955c35ad6b116a7a4af50ac833c4f71c8))
* support browsing artists by release ([358c3cb](https://github.com/lydavid/MusicSearch/commit/358c3cb3e45b25c09fa926a8273d395648aabaf9))
* support browsing artists by work ([65b7962](https://github.com/lydavid/MusicSearch/commit/65b79626f22df0a69c625289f4f436644a0562a0))
* support browsing events by area ([c4007af](https://github.com/lydavid/MusicSearch/commit/c4007af97afad5bc51e3edfcc54908d594c06455))
* support browsing events by artist ([#832](https://github.com/lydavid/MusicSearch/issues/832)) ([9e35904](https://github.com/lydavid/MusicSearch/commit/9e35904dc440ad78d5b4fe33b85d686975a00e5c))
* support browsing labels by area ([#962](https://github.com/lydavid/MusicSearch/issues/962)) ([4ccecf4](https://github.com/lydavid/MusicSearch/commit/4ccecf4cfa8aea4df8857c70109b042fe2212dd9))
* support browsing works by artist ([#958](https://github.com/lydavid/MusicSearch/issues/958)) ([8ba1a48](https://github.com/lydavid/MusicSearch/commit/8ba1a48d342a770d0b7548938f6dbd15bf3e0b06))
* support pull to refresh artist details, refetching artist and url relationships ([#969](https://github.com/lydavid/MusicSearch/issues/969)) ([893dc79](https://github.com/lydavid/MusicSearch/commit/893dc7981eb759bca587bc611ac512c07ff4c751))
* update Kotlin to 2.0.0 ([#924](https://github.com/lydavid/MusicSearch/issues/924)) ([e3d2d0b](https://github.com/lydavid/MusicSearch/commit/e3d2d0bc92cf46cad7f2c8bf7ce367cc298d420d))

## [1.2.0-beta.21](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.20...v1.2.0-beta.21) (2024-06-29)


### Bug Fixes

* support refreshing empty state such as "No results found." ([bcae3f1](https://github.com/lydavid/MusicSearch/commit/bcae3f1df3ddebd02ed22b3c2a5e9dd03fd7c38f)), closes [#147](https://github.com/lydavid/MusicSearch/issues/147)


### Features

* enable edge-to-edge ([#971](https://github.com/lydavid/MusicSearch/issues/971)) ([e84794c](https://github.com/lydavid/MusicSearch/commit/e84794c9c49b806823b3d214a37b5eb16e49043c))
* support pull to refresh artist details, refetching artist and url relationships ([#969](https://github.com/lydavid/MusicSearch/issues/969)) ([893dc79](https://github.com/lydavid/MusicSearch/commit/893dc7981eb759bca587bc611ac512c07ff4c751))

## [1.2.0-beta.20](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.19...v1.2.0-beta.20) (2024-06-29)


### Bug Fixes

* always show Releases tab for an area; do not change release event areas to country ([a117436](https://github.com/lydavid/MusicSearch/commit/a1174364254ec9691befac0e2bd0b6578c9426d2)), closes [#479](https://github.com/lydavid/MusicSearch/issues/479)

## [1.2.0-beta.19](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.18...v1.2.0-beta.19) (2024-06-25)


### Features

* support browsing labels by area ([#962](https://github.com/lydavid/MusicSearch/issues/962)) ([4ccecf4](https://github.com/lydavid/MusicSearch/commit/4ccecf4cfa8aea4df8857c70109b042fe2212dd9))

## [1.2.0-beta.18](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.17...v1.2.0-beta.18) (2024-06-24)


### Features

* support browsing artists by release ([358c3cb](https://github.com/lydavid/MusicSearch/commit/358c3cb3e45b25c09fa926a8273d395648aabaf9))
* support browsing works by artist ([#958](https://github.com/lydavid/MusicSearch/issues/958)) ([8ba1a48](https://github.com/lydavid/MusicSearch/commit/8ba1a48d342a770d0b7548938f6dbd15bf3e0b06))

## [1.2.0-beta.17](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.16...v1.2.0-beta.17) (2024-06-23)


### Bug Fixes

* artists by area shared scroll state with events by area ([5d3e738](https://github.com/lydavid/MusicSearch/commit/5d3e738fc6f04e1d831b0e3557b626d852a3f15a))


### Features

* support browsing artists by work ([65b7962](https://github.com/lydavid/MusicSearch/commit/65b79626f22df0a69c625289f4f436644a0562a0))

## [1.2.0-beta.16](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.15...v1.2.0-beta.16) (2024-06-15)


### Features

* distribute desktop apps in GitHub Releases with Conveyor ([#952](https://github.com/lydavid/MusicSearch/issues/952)) ([e3164e5](https://github.com/lydavid/MusicSearch/commit/e3164e5113310bcd98aae7e2816ffc39c33f9bc9))

## [1.2.0-beta.15](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.14...v1.2.0-beta.15) (2024-06-11)


### Bug Fixes

* bad migration ([#943](https://github.com/lydavid/MusicSearch/issues/943)) ([7dccaba](https://github.com/lydavid/MusicSearch/commit/7dccaba5a9fb07e40e96e45ebee894c36220a5b8))

## [1.2.0-beta.14](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.13...v1.2.0-beta.14) (2024-06-10)


### Features

* able to delete spotify history ([#939](https://github.com/lydavid/MusicSearch/issues/939)) ([d219eab](https://github.com/lydavid/MusicSearch/commit/d219eabd85309db71e66c462f885d9d53cc84fe3))

## [1.2.0-beta.13](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.12...v1.2.0-beta.13) (2024-05-30)


### Features

* browse recordings by artist ([#932](https://github.com/lydavid/MusicSearch/issues/932)) ([04ba6ba](https://github.com/lydavid/MusicSearch/commit/04ba6bab283b214e02b9e3c439be5e20f49f155a))

## [1.2.0-beta.12](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.11...v1.2.0-beta.12) (2024-05-28)


### Bug Fixes

* crash due to grouping tracks incorrectly ([8e3c641](https://github.com/lydavid/MusicSearch/commit/8e3c64148653b4890b5684b42b4fb01fdc42f3e7))

## [1.2.0-beta.11](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.10...v1.2.0-beta.11) (2024-05-27)


### Bug Fixes

* crash due to `artist_credit.name` change after refreshing ([#929](https://github.com/lydavid/MusicSearch/issues/929)) ([a1b88ac](https://github.com/lydavid/MusicSearch/commit/a1b88acd44abb5dd20616d2096e4ac04128c4dd0)), closes [#859](https://github.com/lydavid/MusicSearch/issues/859)

## [1.2.0-beta.10](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.9...v1.2.0-beta.10) (2024-05-23)


### Features

* update Kotlin to 2.0.0 ([#924](https://github.com/lydavid/MusicSearch/issues/924)) ([e3d2d0b](https://github.com/lydavid/MusicSearch/commit/e3d2d0bc92cf46cad7f2c8bf7ce367cc298d420d))

## [1.2.0-beta.9](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.8...v1.2.0-beta.9) (2024-05-21)


### Bug Fixes

* do not order artists ([4c5e463](https://github.com/lydavid/MusicSearch/commit/4c5e463ee5d59375745650ec6edbb8042e754d2d))

## [1.2.0-beta.8](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.7...v1.2.0-beta.8) (2024-05-20)


### Features

* browse artists by area ([#918](https://github.com/lydavid/MusicSearch/issues/918)) ([6617fcb](https://github.com/lydavid/MusicSearch/commit/6617fcb7ec2b4a32e8af1b50477347299a2ef726))

## [1.2.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.6...v1.2.0-beta.7) (2024-05-05)


### Bug Fixes

* NowPlayingNotificationListener leak [#895](https://github.com/lydavid/MusicSearch/issues/895) ([3fc54b9](https://github.com/lydavid/MusicSearch/commit/3fc54b90bdab4f4839eca74f0a9c1c47f4161e99))


### Features

* **android:** listen to Spotify broadcast on app start in the background ([cad38e0](https://github.com/lydavid/MusicSearch/commit/cad38e03452488e5a43b6a09f617d8e6fe95e092))

## [1.2.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.5...v1.2.0-beta.6) (2024-05-02)


### Features

* **android:** record spotify listen history ([#888](https://github.com/lydavid/MusicSearch/issues/888)) ([b867b39](https://github.com/lydavid/MusicSearch/commit/b867b39eb00740613ef1b60bae908bc963aa3d3f))

## [1.2.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.4...v1.2.0-beta.5) (2024-04-13)


### Bug Fixes

* save tracks position when moving between tabs ([eb9e36a](https://github.com/lydavid/MusicSearch/commit/eb9e36a5e4a2a11f2620e491b0c3223a73e3a830))

## [1.2.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.3...v1.2.0-beta.4) (2024-04-11)


### Features

* **ios:** implement datastore preferences ([7816c8d](https://github.com/lydavid/MusicSearch/commit/7816c8d9392aec5bc41c758b462322eceff69aac))
* **ios:** search screen works ([ed4d114](https://github.com/lydavid/MusicSearch/commit/ed4d114955c35ad6b116a7a4af50ac833c4f71c8))

## [1.2.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.2...v1.2.0-beta.3) (2024-04-01)


### Features

* support browsing events by artist ([#832](https://github.com/lydavid/MusicSearch/issues/832)) ([9e35904](https://github.com/lydavid/MusicSearch/commit/9e35904dc440ad78d5b4fe33b85d686975a00e5c))

## [1.2.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.2.0-beta.1...v1.2.0-beta.2) (2024-03-31)


### Features

* **desktop:** use persistent database ([5ace05c](https://github.com/lydavid/MusicSearch/commit/5ace05ca3c6d7ad43a915749cf938b67056c2a89)), closes [#787](https://github.com/lydavid/MusicSearch/issues/787)

## [1.2.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.1.0...v1.2.0-beta.1) (2024-03-21)


### Features

* support browsing events by area ([c4007af](https://github.com/lydavid/MusicSearch/commit/c4007af97afad5bc51e3edfcc54908d594c06455))

## [1.1.0](https://github.com/lydavid/MusicSearch/compare/v1.0.0...v1.1.0) (2024-03-20)


### Bug Fixes

* clicking on search items after coming from Spotify screen, Now Playing screen, or adb deep link works again ([c38679f](https://github.com/lydavid/MusicSearch/commit/c38679f0e4a45da560953225232c4c3c6afa65d7))
* use system's timezone instead of UTC ([74f07d5](https://github.com/lydavid/MusicSearch/commit/74f07d52decceb492b090989d14ce5e036276ab3))


### Features

* convert details to CMP and MVI; use Circuit for navigation ([#777](https://github.com/lydavid/MusicSearch/issues/777)) ([65f0ce5](https://github.com/lydavid/MusicSearch/commit/65f0ce5c0109f5d0ad3bea2ec947a34953d416f3))
* **desktop:** support OAuth to MusicBrainz ([#754](https://github.com/lydavid/MusicSearch/issues/754)) ([3d1f8d9](https://github.com/lydavid/MusicSearch/commit/3d1f8d9427c939e704b56ce04401394bfdf643f6))
* Updated .github/workflows/publish-production ([#687](https://github.com/lydavid/MusicSearch/issues/687)) ([059b459](https://github.com/lydavid/MusicSearch/commit/059b45994ccb65d369dcb035aa35c7b11853c377))

## [1.1.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.1.0-beta.3...v1.1.0-beta.4) (2024-03-16)


### Features

* convert details to CMP and MVI; use Circuit for navigation ([#777](https://github.com/lydavid/MusicSearch/issues/777)) ([65f0ce5](https://github.com/lydavid/MusicSearch/commit/65f0ce5c0109f5d0ad3bea2ec947a34953d416f3))

## [1.1.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.1.0-beta.2...v1.1.0-beta.3) (2024-03-05)


### Bug Fixes

* clicking on search items after coming from Spotify screen, Now Playing screen, or adb deep link works again ([c38679f](https://github.com/lydavid/MusicSearch/commit/c38679f0e4a45da560953225232c4c3c6afa65d7))

## [1.1.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.1.0-beta.1...v1.1.0-beta.2) (2024-02-28)


### Features

* **desktop:** support OAuth to MusicBrainz ([#754](https://github.com/lydavid/MusicSearch/issues/754)) ([3d1f8d9](https://github.com/lydavid/MusicSearch/commit/3d1f8d9427c939e704b56ce04401394bfdf643f6))

## [1.1.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.0.1-beta.1...v1.1.0-beta.1) (2024-01-17)


### Features

* Updated .github/workflows/publish-production ([#687](https://github.com/lydavid/MusicSearch/issues/687)) ([059b459](https://github.com/lydavid/MusicSearch/commit/059b45994ccb65d369dcb035aa35c7b11853c377))

## [1.0.1-beta.1](https://github.com/lydavid/MusicSearch/compare/v1.0.0...v1.0.1-beta.1) (2024-01-11)


### Bug Fixes

* use system's timezone instead of UTC ([74f07d5](https://github.com/lydavid/MusicSearch/commit/74f07d52decceb492b090989d14ce5e036276ab3))

## [1.0.0](https://github.com/lydavid/MusicSearch/compare/v0.15.0...v1.0.0) (2023-11-26)


### Bug Fixes

* allow retry on NoTransformationFoundException (requested json but got xml) ([d83065c](https://github.com/lydavid/MusicSearch/commit/d83065c52a1bb6730dd6c1e5016f9896a5b96765))
* apply core library desugaring to all Android modules ([a15cdff](https://github.com/lydavid/MusicSearch/commit/a15cdff60d573d9587b74c3d79ee52fb3e912c3e))
* crash when clicking into Spotify screen ([9ecf53d](https://github.com/lydavid/MusicSearch/commit/9ecf53db550aad3bd69468997a215772c3da8896))
* crash when space is typed into search screen's search bar ([2daa60b](https://github.com/lydavid/MusicSearch/commit/2daa60b14b28a6dd82bec46e3b7e6da0130d173c)), closes [#629](https://github.com/lydavid/MusicSearch/issues/629)
* display release country count in release list item after visiting the release ([daf4eb4](https://github.com/lydavid/MusicSearch/commit/daf4eb4590fa347f03bba0c98af34a70b4058a7e))
* do not make unnecessary refresh token calls, only when current token has expired ([2876cf3](https://github.com/lydavid/MusicSearch/commit/2876cf3e8cc295119ba024db2c6f2e0bd078f927)), closes [#415](https://github.com/lydavid/MusicSearch/issues/415)
* do not manually add `application/json`, it's already added ([34fcf06](https://github.com/lydavid/MusicSearch/commit/34fcf0652aae3b20dc07ace8b463a5966d19d2f7))
* enable core library desugaring to resolve crash on Android below API 26 ([a7b09a7](https://github.com/lydavid/MusicSearch/commit/a7b09a719386669e1d2a29903093eb2e2812f1d4))
* fix theme ([f3f5456](https://github.com/lydavid/MusicSearch/commit/f3f5456b188583fc3b39b68dd93a853069f56705))
* full screen text padding ([1778b03](https://github.com/lydavid/MusicSearch/commit/1778b038bee6f5fecc6913d116798a60fe1b46a5))
* pass `inc` to api impl so that we query for artist-credits during browse ([045bf9e](https://github.com/lydavid/MusicSearch/commit/045bf9e6f9977ec3f07fb912c2fd0933a64a1610)), closes [#402](https://github.com/lydavid/MusicSearch/issues/402)
* search query change does not scroll to top ([e979c6b](https://github.com/lydavid/MusicSearch/commit/e979c6b092d0a163501d4e3d79a07e21a3b2c7c7)), closes [#302](https://github.com/lydavid/MusicSearch/issues/302)
* send auth bearer token when browsing by collection without waiting for 401 ([8413784](https://github.com/lydavid/MusicSearch/commit/84137847d0c08b916829122de6e2c5d631f13078)), closes [#416](https://github.com/lydavid/MusicSearch/issues/416)
* show loading spinner in profile card between the MB token fetch and user info fetch request ([0a9374d](https://github.com/lydavid/MusicSearch/commit/0a9374d909fc424a69ec319d93dd61a2744026ab))
* try fmt=json to fix xml response issue ([c7195a3](https://github.com/lydavid/MusicSearch/commit/c7195a36916b835aa01d6bec8e470a611a4d3758)), closes [#241](https://github.com/lydavid/MusicSearch/issues/241)
* use the newly acquired access token during token refresh ([28028db](https://github.com/lydavid/MusicSearch/commit/28028dbc0942061c643b4753dfd392cd8bac90a7))


### Features

* cache network responses using OkHttp cache ([8fa0303](https://github.com/lydavid/MusicSearch/commit/8fa030359bce3a225b204ad7a003a04babb8d508)), closes [#400](https://github.com/lydavid/MusicSearch/issues/400)
* log network headers with timber ([fd55c6b](https://github.com/lydavid/MusicSearch/commit/fd55c6b179e47e58355b543b254e0281d54ce363)), closes [#408](https://github.com/lydavid/MusicSearch/issues/408)
* log non-404 ClientRequestException for image url fetches ([9896cdd](https://github.com/lydavid/MusicSearch/commit/9896cdd3bfafc73b7b8bfeea01c26ba32d9edc37)), closes [#410](https://github.com/lydavid/MusicSearch/issues/410)
* migrate dagger/hilt to koin for multiplatform DI ([#434](https://github.com/lydavid/MusicSearch/issues/434)) ([147bc9c](https://github.com/lydavid/MusicSearch/commit/147bc9c68aac4a39047e42832d440aee2952bd6e))
* migrate retrofit to ktor for MusicBrainz module ([#396](https://github.com/lydavid/MusicSearch/issues/396)) ([ef39a0e](https://github.com/lydavid/MusicSearch/commit/ef39a0e76afeb59bd06fdb3bd003cb3d5a1d04fa))
* migrate room to sqldelight for multiplatform database ([#455](https://github.com/lydavid/MusicSearch/issues/455)) ([8485f42](https://github.com/lydavid/MusicSearch/commit/8485f428ca26210554d057e907c85fe33c37a003))
* retry http request up to 3 times on server response error ([7bc7b06](https://github.com/lydavid/MusicSearch/commit/7bc7b06ce2b67091543056f26668a8aad5cca908)), closes [#409](https://github.com/lydavid/MusicSearch/issues/409)
* show full-screen message when search history is empty instead of "Recent searches" without trash icon ([693305e](https://github.com/lydavid/MusicSearch/commit/693305edf5d68a04a621b11508019b347f0300b2))
* support MusicBrainz OAuth refresh so that user will stay logged in until they click log out ([ad79187](https://github.com/lydavid/MusicSearch/commit/ad791873a79ccb4961788dafde698b591312194d)), closes [#415](https://github.com/lydavid/MusicSearch/issues/415)
* use Lyricist for localization ([#504](https://github.com/lydavid/MusicSearch/issues/504)) ([8c93ef6](https://github.com/lydavid/MusicSearch/commit/8c93ef60db0dc1cdff9230e730336a9446308b20))


### BREAKING CHANGES

* renamed database to `musicsearch.db`, changed some order, default values, and constraints

## [1.0.0-beta.9](https://github.com/lydavid/MusicSearch/compare/v1.0.0-beta.8...v1.0.0-beta.9) (2023-11-26)


### Reverts

* Revert "chore: move appAuthRedirectScheme" ([1c0b22e](https://github.com/lydavid/MusicSearch/commit/1c0b22efe6e451a9cd72fbaae36f6248673629b3))

## [1.0.0-beta.8](https://github.com/lydavid/MusicSearch/compare/v1.0.0-beta.7...v1.0.0-beta.8) (2023-11-26)


### Bug Fixes

* crash when space is typed into search screen's search bar ([2daa60b](https://github.com/lydavid/MusicSearch/commit/2daa60b14b28a6dd82bec46e3b7e6da0130d173c)), closes [#629](https://github.com/lydavid/MusicSearch/issues/629)
* search query change does not scroll to top ([e979c6b](https://github.com/lydavid/MusicSearch/commit/e979c6b092d0a163501d4e3d79a07e21a3b2c7c7)), closes [#302](https://github.com/lydavid/MusicSearch/issues/302)

## [1.0.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v1.0.0-beta.6...v1.0.0-beta.7) (2023-10-15)


### Bug Fixes

* crash when clicking into Spotify screen ([9ecf53d](https://github.com/lydavid/MusicSearch/commit/9ecf53db550aad3bd69468997a215772c3da8896))

## [1.0.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v1.0.0-beta.5...v1.0.0-beta.6) (2023-10-09)


### Bug Fixes

* full screen text padding ([1778b03](https://github.com/lydavid/MusicSearch/commit/1778b038bee6f5fecc6913d116798a60fe1b46a5))

## [1.0.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v1.0.0-beta.4...v1.0.0-beta.5) (2023-10-08)


### Bug Fixes

* apply core library desugaring to all Android modules ([a15cdff](https://github.com/lydavid/MusicSearch/commit/a15cdff60d573d9587b74c3d79ee52fb3e912c3e))


### Features

* show full-screen message when search history is empty instead of "Recent searches" without trash icon ([693305e](https://github.com/lydavid/MusicSearch/commit/693305edf5d68a04a621b11508019b347f0300b2))
* use Lyricist for localization ([#504](https://github.com/lydavid/MusicSearch/issues/504)) ([8c93ef6](https://github.com/lydavid/MusicSearch/commit/8c93ef60db0dc1cdff9230e730336a9446308b20))

## [1.0.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v1.0.0-beta.3...v1.0.0-beta.4) (2023-10-06)


### Bug Fixes

* enable core library desugaring to resolve crash on Android below API 26 ([a7b09a7](https://github.com/lydavid/MusicSearch/commit/a7b09a719386669e1d2a29903093eb2e2812f1d4))

## [1.0.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v1.0.0-beta.2...v1.0.0-beta.3) (2023-10-05)


### Bug Fixes

* send auth bearer token when browsing by collection without waiting for 401 ([8413784](https://github.com/lydavid/MusicSearch/commit/84137847d0c08b916829122de6e2c5d631f13078)), closes [#416](https://github.com/lydavid/MusicSearch/issues/416)

## [1.0.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v1.0.0-beta.1...v1.0.0-beta.2) (2023-10-02)


### Bug Fixes

* display release country count in release list item after visiting the release ([daf4eb4](https://github.com/lydavid/MusicSearch/commit/daf4eb4590fa347f03bba0c98af34a70b4058a7e))

## [1.0.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v0.16.0-beta.6...v1.0.0-beta.1) (2023-10-02)


### Bug Fixes

* try fmt=json to fix xml response issue ([c7195a3](https://github.com/lydavid/MusicSearch/commit/c7195a36916b835aa01d6bec8e470a611a4d3758)), closes [#241](https://github.com/lydavid/MusicSearch/issues/241)


### Features

* migrate room to sqldelight for multiplatform database ([#455](https://github.com/lydavid/MusicSearch/issues/455)) ([8485f42](https://github.com/lydavid/MusicSearch/commit/8485f428ca26210554d057e907c85fe33c37a003))


### BREAKING CHANGES

* renamed database to `musicsearch.db`, changed some order, default values, and constraints

## [0.16.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v0.16.0-beta.5...v0.16.0-beta.6) (2023-09-13)


### Features

* migrate dagger/hilt to koin for multiplatform DI ([#434](https://github.com/lydavid/MusicSearch/issues/434)) ([147bc9c](https://github.com/lydavid/MusicSearch/commit/147bc9c68aac4a39047e42832d440aee2952bd6e))

## [0.16.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v0.16.0-beta.4...v0.16.0-beta.5) (2023-09-09)


### Bug Fixes

* fix theme ([f3f5456](https://github.com/lydavid/MusicSearch/commit/f3f5456b188583fc3b39b68dd93a853069f56705))

## [0.16.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v0.16.0-beta.3...v0.16.0-beta.4) (2023-09-06)


### Bug Fixes

* do not manually add `application/json`, it's already added ([34fcf06](https://github.com/lydavid/MusicSearch/commit/34fcf0652aae3b20dc07ace8b463a5966d19d2f7))

## [0.16.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v0.16.0-beta.2...v0.16.0-beta.3) (2023-09-04)


### Bug Fixes

* do not make unnecessary refresh token calls, only when current token has expired ([2876cf3](https://github.com/lydavid/MusicSearch/commit/2876cf3e8cc295119ba024db2c6f2e0bd078f927)), closes [#415](https://github.com/lydavid/MusicSearch/issues/415)


### Features

* support MusicBrainz OAuth refresh so that user will stay logged in until they click log out ([ad79187](https://github.com/lydavid/MusicSearch/commit/ad791873a79ccb4961788dafde698b591312194d)), closes [#415](https://github.com/lydavid/MusicSearch/issues/415)

## [0.16.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v0.16.0-beta.1...v0.16.0-beta.2) (2023-09-03)


### Bug Fixes

* allow retry on NoTransformationFoundException (requested json but got xml) ([d83065c](https://github.com/lydavid/MusicSearch/commit/d83065c52a1bb6730dd6c1e5016f9896a5b96765))


### Features

* cache network responses using OkHttp cache ([8fa0303](https://github.com/lydavid/MusicSearch/commit/8fa030359bce3a225b204ad7a003a04babb8d508)), closes [#400](https://github.com/lydavid/MusicSearch/issues/400)
* log network headers with timber ([fd55c6b](https://github.com/lydavid/MusicSearch/commit/fd55c6b179e47e58355b543b254e0281d54ce363)), closes [#408](https://github.com/lydavid/MusicSearch/issues/408)
* log non-404 ClientRequestException for image url fetches ([9896cdd](https://github.com/lydavid/MusicSearch/commit/9896cdd3bfafc73b7b8bfeea01c26ba32d9edc37)), closes [#410](https://github.com/lydavid/MusicSearch/issues/410)
* retry http request up to 3 times on server response error ([7bc7b06](https://github.com/lydavid/MusicSearch/commit/7bc7b06ce2b67091543056f26668a8aad5cca908)), closes [#409](https://github.com/lydavid/MusicSearch/issues/409)

## [0.16.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v0.15.0...v0.16.0-beta.1) (2023-09-02)


### Bug Fixes

* pass `inc` to api impl so that we query for artist-credits during browse ([045bf9e](https://github.com/lydavid/MusicSearch/commit/045bf9e6f9977ec3f07fb912c2fd0933a64a1610)), closes [#402](https://github.com/lydavid/MusicSearch/issues/402)
* show loading spinner in profile card between the MB token fetch and user info fetch request ([0a9374d](https://github.com/lydavid/MusicSearch/commit/0a9374d909fc424a69ec319d93dd61a2744026ab))
* use the newly acquired access token during token refresh ([28028db](https://github.com/lydavid/MusicSearch/commit/28028dbc0942061c643b4753dfd392cd8bac90a7))


### Features

* migrate retrofit to ktor for MusicBrainz module ([#396](https://github.com/lydavid/MusicSearch/issues/396)) ([ef39a0e](https://github.com/lydavid/MusicSearch/commit/ef39a0e76afeb59bd06fdb3bd003cb3d5a1d04fa))

## [0.15.0](https://github.com/lydavid/MusicSearch/compare/v0.14.0...v0.15.0) (2023-08-26)


### Features

* support several sort options for lookup history ([#385](https://github.com/lydavid/MusicSearch/issues/385)) ([07a375c](https://github.com/lydavid/MusicSearch/commit/07a375c95c018d320a079206c7edc23dbc2dbbcf))

## [0.15.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v0.14.0...v0.15.0-beta.1) (2023-08-25)


### Features

* support several sort options for lookup history ([#385](https://github.com/lydavid/MusicSearch/issues/385)) ([07a375c](https://github.com/lydavid/MusicSearch/commit/07a375c95c018d320a079206c7edc23dbc2dbbcf))

## [0.14.0](https://github.com/lydavid/MusicSearch/compare/v0.13.0...v0.14.0) (2023-08-22)


### Bug Fixes

* double-quote Now Playing title when searching ([8149513](https://github.com/lydavid/MusicSearch/commit/8149513566b93420afcecc01d0e4530e4fb11091)), closes [#336](https://github.com/lydavid/MusicSearch/issues/336)
* fix crash when Spotify artist endpoint returns empty images list ([d83d8f5](https://github.com/lydavid/MusicSearch/commit/d83d8f5192113fb876109fa0131aa94c90537d40))
* fix filtering tracks by a release ([d3b450f](https://github.com/lydavid/MusicSearch/commit/d3b450ffc203564bb8352bd1bf332a0e27bae134)), closes [#353](https://github.com/lydavid/MusicSearch/issues/353)
* make sure clickable list item end icon doesn't get pushed off screen by long text ([cdfae50](https://github.com/lydavid/MusicSearch/commit/cdfae50b206daf8ece6efaaeca87331f61097f41))


### Features

* add icon for url throughout app ([922fc13](https://github.com/lydavid/MusicSearch/commit/922fc13d29c319a0ba4904c107ebd935fb9d9630)), closes [#349](https://github.com/lydavid/MusicSearch/issues/349)
* move url relationships to details tab; support filtering details ([#355](https://github.com/lydavid/MusicSearch/issues/355)) ([9ab6091](https://github.com/lydavid/MusicSearch/commit/9ab6091c8ba7f178da08dad7608547b371291611))
* set compile and target sdk to 34 ([#362](https://github.com/lydavid/MusicSearch/issues/362)) ([fbff092](https://github.com/lydavid/MusicSearch/commit/fbff092b79f320606df4fbaaa30c4ff7d4ad849c))
* support searching playing Spotify artist/album/track using broadcast receiver ([#340](https://github.com/lydavid/MusicSearch/issues/340)) ([1f2ed0d](https://github.com/lydavid/MusicSearch/commit/1f2ed0d68282c87f04ca88c2b64b4e007d62801e)), closes [#199](https://github.com/lydavid/MusicSearch/issues/199)

## [0.14.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v0.14.0-beta.4...v0.14.0-beta.5) (2023-08-18)


### Features

* set compile and target sdk to 34 ([#362](https://github.com/lydavid/MusicSearch/issues/362)) ([fbff092](https://github.com/lydavid/MusicSearch/commit/fbff092b79f320606df4fbaaa30c4ff7d4ad849c))

## [0.14.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v0.14.0-beta.3...v0.14.0-beta.4) (2023-08-16)


### Bug Fixes

* fix filtering tracks by a release ([d3b450f](https://github.com/lydavid/MusicSearch/commit/d3b450ffc203564bb8352bd1bf332a0e27bae134)), closes [#353](https://github.com/lydavid/MusicSearch/issues/353)


### Features

* add icon for url throughout app ([922fc13](https://github.com/lydavid/MusicSearch/commit/922fc13d29c319a0ba4904c107ebd935fb9d9630)), closes [#349](https://github.com/lydavid/MusicSearch/issues/349)

## [0.14.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v0.14.0-beta.2...v0.14.0-beta.3) (2023-08-14)


### Features

* move url relationships to details tab; support filtering details ([#355](https://github.com/lydavid/MusicSearch/issues/355)) ([9ab6091](https://github.com/lydavid/MusicSearch/commit/9ab6091c8ba7f178da08dad7608547b371291611))

## [0.14.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v0.14.0-beta.1...v0.14.0-beta.2) (2023-08-10)


### Bug Fixes

* make sure clickable list item end icon doesn't get pushed off screen by long text ([cdfae50](https://github.com/lydavid/MusicSearch/commit/cdfae50b206daf8ece6efaaeca87331f61097f41))

## [0.14.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v0.13.1-beta.1...v0.14.0-beta.1) (2023-08-02)


### Bug Fixes

* fix crash when Spotify artist endpoint returns empty images list ([d83d8f5](https://github.com/lydavid/MusicSearch/commit/d83d8f5192113fb876109fa0131aa94c90537d40))


### Features

* support searching playing Spotify artist/album/track using broadcast receiver ([#340](https://github.com/lydavid/MusicSearch/issues/340)) ([1f2ed0d](https://github.com/lydavid/MusicSearch/commit/1f2ed0d68282c87f04ca88c2b64b4e007d62801e)), closes [#199](https://github.com/lydavid/MusicSearch/issues/199)

## [0.13.1-beta.1](https://github.com/lydavid/MusicSearch/compare/v0.13.0...v0.13.1-beta.1) (2023-08-01)


### Bug Fixes

* double-quote Now Playing title when searching ([8149513](https://github.com/lydavid/MusicSearch/commit/8149513566b93420afcecc01d0e4530e4fb11091)), closes [#336](https://github.com/lydavid/MusicSearch/issues/336)

## [0.13.0](https://github.com/lydavid/MusicSearch/compare/v0.12.0...v0.13.0) (2023-07-30)


### Bug Fixes

* do not allow TalkBack to focus on switch inside setting switch ([#257](https://github.com/lydavid/MusicSearch/issues/257)) ([f26025c](https://github.com/lydavid/MusicSearch/commit/f26025c46a08b6df1c0c14abaa91e56d3d02d929))
* support deep linking into history, collection, and settings screen ([16fce60](https://github.com/lydavid/MusicSearch/commit/16fce602518a4a724f5c091c620d0af91b26ce7f))
* support more thumbnail image fallbacks ([755172b](https://github.com/lydavid/MusicSearch/commit/755172b874145658d4a8575337b7a975c59b5d66))


### Features

* add dialog to confirm deleting search and lookup history ([#264](https://github.com/lydavid/MusicSearch/issues/264)) ([b8ec5a6](https://github.com/lydavid/MusicSearch/commit/b8ec5a6c2089cfab52063797619cca932a49b4be))
* display images in history screen ([#277](https://github.com/lydavid/MusicSearch/issues/277)) ([8d8dfee](https://github.com/lydavid/MusicSearch/commit/8d8dfeea0f6feca2a75bb8c9e2ecd1edc2cee400)), closes [#237](https://github.com/lydavid/MusicSearch/issues/237)
* filter artist details and external links ([da8e3b6](https://github.com/lydavid/MusicSearch/commit/da8e3b6621d7fd74d2fffe73fd3c066b4401e2ee)), closes [#247](https://github.com/lydavid/MusicSearch/issues/247)
* format datetime in history and now playing history; group by date ([8abca76](https://github.com/lydavid/MusicSearch/commit/8abca76c48bf02d25b1113ea621e4072153a0af6)), closes [#313](https://github.com/lydavid/MusicSearch/issues/313)
* hold click on url will copy it to clipboard ([172a07c](https://github.com/lydavid/MusicSearch/commit/172a07c222f93cd4b198f79aa5482b646e30bc3d)), closes [#279](https://github.com/lydavid/MusicSearch/issues/279)
* now playing history ([#298](https://github.com/lydavid/MusicSearch/issues/298)) ([a376952](https://github.com/lydavid/MusicSearch/commit/a3769522c5b83f6afdbb22409bf0a761c6bc3e59))
* show open source libraries licenses in settings ([#256](https://github.com/lydavid/MusicSearch/issues/256)) ([d006a46](https://github.com/lydavid/MusicSearch/commit/d006a46628262e342a3023b37046b51e210e07f7))
* support deleting Now Playing history item by swiping ([9153528](https://github.com/lydavid/MusicSearch/commit/91535288db1d78cf43250b26062b758fb4efefac))

## [0.13.0-beta.9](https://github.com/lydavid/MusicSearch/compare/v0.13.0-beta.8...v0.13.0-beta.9) (2023-07-30)


### Features

* support deleting Now Playing history item by swiping ([9153528](https://github.com/lydavid/MusicSearch/commit/91535288db1d78cf43250b26062b758fb4efefac))

## [0.13.0-beta.8](https://github.com/lydavid/MusicSearch/compare/v0.13.0-beta.7...v0.13.0-beta.8) (2023-07-18)


### Bug Fixes

* support deep linking into history, collection, and settings screen ([16fce60](https://github.com/lydavid/MusicSearch/commit/16fce602518a4a724f5c091c620d0af91b26ce7f))
* support more thumbnail image fallbacks ([755172b](https://github.com/lydavid/MusicSearch/commit/755172b874145658d4a8575337b7a975c59b5d66))

## [0.13.0-beta.7](https://github.com/lydavid/MusicSearch/compare/v0.13.0-beta.6...v0.13.0-beta.7) (2023-07-17)


### Features

* format datetime in history and now playing history; group by date ([8abca76](https://github.com/lydavid/MusicSearch/commit/8abca76c48bf02d25b1113ea621e4072153a0af6)), closes [#313](https://github.com/lydavid/MusicSearch/issues/313)

## [0.13.0-beta.6](https://github.com/lydavid/MusicSearch/compare/v0.13.0-beta.5...v0.13.0-beta.6) (2023-07-16)


### Features

* now playing history ([#298](https://github.com/lydavid/MusicSearch/issues/298)) ([a376952](https://github.com/lydavid/MusicSearch/commit/a3769522c5b83f6afdbb22409bf0a761c6bc3e59))

## [0.13.0-beta.5](https://github.com/lydavid/MusicSearch/compare/v0.13.0-beta.4...v0.13.0-beta.5) (2023-07-09)


### Features

* filter artist details and external links ([da8e3b6](https://github.com/lydavid/MusicSearch/commit/da8e3b6621d7fd74d2fffe73fd3c066b4401e2ee)), closes [#247](https://github.com/lydavid/MusicSearch/issues/247)

## [0.13.0-beta.4](https://github.com/lydavid/MusicSearch/compare/v0.13.0-beta.3...v0.13.0-beta.4) (2023-07-08)


### Features

* hold click on url will copy it to clipboard ([172a07c](https://github.com/lydavid/MusicSearch/commit/172a07c222f93cd4b198f79aa5482b646e30bc3d)), closes [#279](https://github.com/lydavid/MusicSearch/issues/279)

## [0.13.0-beta.3](https://github.com/lydavid/MusicSearch/compare/v0.13.0-beta.2...v0.13.0-beta.3) (2023-07-04)


### Features

* display images in history screen ([#277](https://github.com/lydavid/MusicSearch/issues/277)) ([8d8dfee](https://github.com/lydavid/MusicSearch/commit/8d8dfeea0f6feca2a75bb8c9e2ecd1edc2cee400)), closes [#237](https://github.com/lydavid/MusicSearch/issues/237)

## [0.13.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v0.13.0-beta.1...v0.13.0-beta.2) (2023-07-02)


### Features

* add dialog to confirm deleting search and lookup history ([#264](https://github.com/lydavid/MusicSearch/issues/264)) ([b8ec5a6](https://github.com/lydavid/MusicSearch/commit/b8ec5a6c2089cfab52063797619cca932a49b4be))

## [0.13.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v0.12.0...v0.13.0-beta.1) (2023-06-26)


### Bug Fixes

* do not allow TalkBack to focus on switch inside setting switch ([#257](https://github.com/lydavid/MusicSearch/issues/257)) ([f26025c](https://github.com/lydavid/MusicSearch/commit/f26025c46a08b6df1c0c14abaa91e56d3d02d929))


### Features

* show open source libraries licenses in settings ([#256](https://github.com/lydavid/MusicSearch/issues/256)) ([d006a46](https://github.com/lydavid/MusicSearch/commit/d006a46628262e342a3023b37046b51e210e07f7))

## [0.12.0](https://github.com/lydavid/MusicSearch/compare/v0.11.2...v0.12.0) (2023-06-24)


### Bug Fixes

* **ci:** use find in page instead of search icon for filtering text ([#232](https://github.com/lydavid/MusicSearch/issues/232)) ([be55fc4](https://github.com/lydavid/MusicSearch/commit/be55fc48da0a7e7411dd91667f00591fb9e5fc23))


### Features

* show artist sort name ([76426ac](https://github.com/lydavid/MusicSearch/commit/76426ac597ac97904da55652156e515ac6edbf98))

## [0.12.0-beta.2](https://github.com/lydavid/MusicSearch/compare/v0.12.0-beta.1...v0.12.0-beta.2) (2023-06-24)


### Bug Fixes

* **ci:** use find in page instead of search icon for filtering text ([#232](https://github.com/lydavid/MusicSearch/issues/232)) ([be55fc4](https://github.com/lydavid/MusicSearch/commit/be55fc48da0a7e7411dd91667f00591fb9e5fc23))

## [0.12.0-beta.1](https://github.com/lydavid/MusicSearch/compare/v0.11.2...v0.12.0-beta.1) (2023-06-23)


### Features

* show artist sort name ([76426ac](https://github.com/lydavid/MusicSearch/commit/76426ac597ac97904da55652156e515ac6edbf98))

## [0.11.2](https://github.com/lydavid/MusicSearch/compare/v0.11.1...v0.11.2) (2023-06-23)


### Bug Fixes

* align all list item text sizes ([0884f29](https://github.com/lydavid/MusicSearch/commit/0884f2984d7ba8b39cc789d7db1545019af8f215))

## [0.11.1](https://github.com/lydavid/MusicSearch/compare/v0.11.0...v0.11.1) (2023-06-20)


### Bug Fixes

* make sure Spotify GHA secrets has ORG_GRADLE_PROJECT_ prefix ([a9d1ae2](https://github.com/lydavid/MusicSearch/commit/a9d1ae2c33301e1bed8c315a97fb2454ecdd47bd))

## [0.11.0](https://github.com/lydavid/MusicSearch/compare/v0.10.0...v0.11.0) (2023-06-18)


### Features

* display artist image in details screen ([#238](https://github.com/lydavid/MusicSearch/issues/238)) ([c6ece71](https://github.com/lydavid/MusicSearch/commit/c6ece71602c7f741bddfdb768486c86e6d07a1de))

## [0.10.0](https://github.com/lydavid/MusicSearch/compare/v0.9.0...v0.10.0) (2023-06-13)


### Features

* implement search history ([#228](https://github.com/lydavid/MusicSearch/issues/228)) ([d9666d1](https://github.com/lydavid/MusicSearch/commit/d9666d1e6849b3be7af6d1d0dabc91aac1723899))

## [0.9.0](https://github.com/lydavid/MusicSearch/compare/v0.8.0...v0.9.0) (2023-06-05)


### Features

* add chips for filtering local/remote collections ([#132](https://github.com/lydavid/MusicSearch/issues/132)) ([1ba4173](https://github.com/lydavid/MusicSearch/commit/1ba4173ee9773f611b9cc435bd545b4d36042e59))

## [0.8.0](https://github.com/lydavid/MusicSearch/compare/v0.7.10...v0.8.0) (2023-06-04)


### Features

* show artist credits in tracks ([ec7fe2e](https://github.com/lydavid/MusicSearch/commit/ec7fe2eb5f6415e0b894ad90dae665c5c6c1deb8))

## [0.7.10](https://github.com/lydavid/MusicSearch/compare/v0.7.9...v0.7.10) (2023-05-31)


### Bug Fixes

* make recording's video nullable ([38fbc50](https://github.com/lydavid/MusicSearch/commit/38fbc5088635d6848152d9e8dc0e4271a09cfb43))

## [0.7.9](https://github.com/lydavid/MusicSearch/compare/v0.7.8...v0.7.9) (2023-05-27)


### Bug Fixes

* do not plant Firebase Crashlytics tree for debug ([c503222](https://github.com/lydavid/MusicSearch/commit/c503222816df0fc775dd365342e741c2c17fc08e))
* include version code in settings ([374e97c](https://github.com/lydavid/MusicSearch/commit/374e97cd4dad405b57a8a86b95e9bfa17f10c129))
* use LazyColumn instead of Column to prevent `SelectionContainer` crash on orientation change ([f5e4bf6](https://github.com/lydavid/MusicSearch/commit/f5e4bf68748e88ccac0721f1e3f40cd0345b0f2e))

## [0.7.8](https://github.com/lydavid/MusicSearch/compare/v0.7.7...v0.7.8) (2023-05-27)


### Bug Fixes

* **deps:** update dependency com.google.firebase:firebase-bom to v32.1.0 ([0c24caa](https://github.com/lydavid/MusicSearch/commit/0c24caa198003bba7982313f3edf0e8c8005f237))

## [0.7.7](https://github.com/lydavid/MusicSearch/compare/v0.7.6...v0.7.7) (2023-05-26)


### Bug Fixes

* **deps:** update dependency com.google.firebase:firebase-analytics-ktx to v21.3.0 ([bcb8433](https://github.com/lydavid/MusicSearch/commit/bcb8433d9b3415021c3a9bc92092e16ee4355f1d))

## [0.7.6](https://github.com/lydavid/MusicSearch/compare/v0.7.5...v0.7.6) (2023-05-25)


### Bug Fixes

* **deps:** update dependency androidx.activity:activity-compose to v1.7.2 ([7c04b44](https://github.com/lydavid/MusicSearch/commit/7c04b44fd3c3a9f8e11ba45e57fb7fe62a6270dc))
* **deps:** update dependency io.nlopez.compose.rules:detekt to v0.1.7 ([8e7736e](https://github.com/lydavid/MusicSearch/commit/8e7736e764d23741e713fadb6d29f8a023001c3c))

## [0.7.5](https://github.com/lydavid/MusicSearch/compare/v0.7.4...v0.7.5) (2023-05-24)


### Bug Fixes

* **deps:** update coil to v2.4.0 ([6ac6f6f](https://github.com/lydavid/MusicSearch/commit/6ac6f6fc582475710f25b1f3449a82e04b345616))
* **deps:** update dependency io.nlopez.compose.rules:detekt to v0.1.6 ([8b4a740](https://github.com/lydavid/MusicSearch/commit/8b4a7409db93f626417a16b09bdd5c8cdb50cf71))
* **deps:** update kotlin and compose compiler to v1.7.1 ([f8aef51](https://github.com/lydavid/MusicSearch/commit/f8aef514539d06ac1e82946cbcd2eae8b30fa56c))

## [0.7.4](https://github.com/lydavid/MusicSearch/compare/v0.7.3...v0.7.4) (2023-05-22)


### Bug Fixes

* show full screen message instead of crashing after navigating to deleted collection ([#202](https://github.com/lydavid/MusicSearch/issues/202)) ([3c68342](https://github.com/lydavid/MusicSearch/commit/3c6834213d9e48ea668015d2e5cf2e11aa31ea1c))

## [0.7.3](https://github.com/lydavid/MusicSearch/compare/v0.7.2...v0.7.3) (2023-05-19)


### Bug Fixes

* **deps:** update dependency org.robolectric:robolectric to v4.10.3 ([db53178](https://github.com/lydavid/MusicSearch/commit/db531784fbc8b3d0b7f0e09ce768ad0b0233643b))

## [0.7.2](https://github.com/lydavid/MusicSearch/compare/v0.7.1...v0.7.2) (2023-05-14)


### Bug Fixes

* **deps:** update dependency androidx.compose.material3:material3 to v1.1.0 ([4e02479](https://github.com/lydavid/MusicSearch/commit/4e024790fceb7aef69db7cc536b5a2421909ad2e))
* **deps:** update dependency com.squareup.moshi:moshi-kotlin to v1.15.0 ([dc26a1b](https://github.com/lydavid/MusicSearch/commit/dc26a1b5289bd2b0ed3c8f8118d5799fc63b66bf))
* **deps:** update hilt to v2.46.1 ([058e13d](https://github.com/lydavid/MusicSearch/commit/058e13d58d4f298bf1bb4bf61292ed65371d0058))

## [0.7.1](https://github.com/lydavid/MusicSearch/compare/v0.7.0...v0.7.1) (2023-05-11)


### Bug Fixes

* **deps:** update dependency androidx.core:core-ktx to v1.10.1 ([7fe3f95](https://github.com/lydavid/MusicSearch/commit/7fe3f95bb880dbb58e207609c31bf71aa42a9b74))

## [0.7.0](https://github.com/lydavid/MusicSearch/compare/v0.6.2...v0.7.0) (2023-05-08)


### Features

* support filtering relationships ([#187](https://github.com/lydavid/MusicSearch/issues/187)) ([5c059c5](https://github.com/lydavid/MusicSearch/commit/5c059c5226ce8679d7beb003805ede4a2dc71814))

## [0.6.2](https://github.com/lydavid/MusicSearch/compare/v0.6.1...v0.6.2) (2023-05-05)


### Bug Fixes

* add proguard rules to resolve R8 full mode causing release app to crash ([3e738fa](https://github.com/lydavid/MusicSearch/commit/3e738fa5e5d19dc1b14083e5136c6125b5a8b067)), closes [/github.com/square/retrofit/issues/3751#issuecomment-1192043644](https://github.com//github.com/square/retrofit/issues/3751/issues/issuecomment-1192043644)

## [0.6.1](https://github.com/lydavid/MusicSearch/compare/v0.6.0...v0.6.1) (2023-05-05)


### Bug Fixes

* **deps:** update dependency org.robolectric:robolectric to v4.10.2 ([9aea73a](https://github.com/lydavid/MusicSearch/commit/9aea73aea46806bddd432a024f3ae850d44ffab3))
* **deps:** update kotlin and compose compiler ([83678cc](https://github.com/lydavid/MusicSearch/commit/83678ccd986f9f4717a66ae432780f9e60675498))

## [0.6.0](https://github.com/lydavid/MusicSearch/compare/v0.5.11...v0.6.0) (2023-05-04)


### Bug Fixes

* **deps:** update compose-ui to v1.4.3 ([84f44da](https://github.com/lydavid/MusicSearch/commit/84f44daff6f17074edd566c5bac2598770ef735d))
* **deps:** update dependency androidx.compose.foundation:foundation to v1.4.3 ([a7eb0e6](https://github.com/lydavid/MusicSearch/commit/a7eb0e66aad8033c85ba64049a2ca850a4944946))
* **deps:** update dependency androidx.compose.material:material-icons-extended to v1.4.3 ([4f5b130](https://github.com/lydavid/MusicSearch/commit/4f5b13071440264436d7bfb5d24f4fa35633dba8))
* **deps:** update dependency com.google.firebase:firebase-bom to v32 ([a6fbc4f](https://github.com/lydavid/MusicSearch/commit/a6fbc4f47768ae46c0b494695a0bf319b82b4c10))


### Features

* **collection:** support deleting items from a collection by swiping ([#163](https://github.com/lydavid/MusicSearch/issues/163)) ([5b0148f](https://github.com/lydavid/MusicSearch/commit/5b0148fff8d9444b8d9d001558a64d6de3f3c7ac))

## [0.5.11](https://github.com/lydavid/MusicSearch/compare/v0.5.10...v0.5.11) (2023-05-03)


### Bug Fixes

* **deps:** update dependency org.robolectric:robolectric to v4.10.1 ([7d3c73b](https://github.com/lydavid/MusicSearch/commit/7d3c73b3f479e36a4f7feac4fa14b876fa497d99))

## [0.5.10](https://github.com/lydavid/MusicSearch/compare/v0.5.9...v0.5.10) (2023-04-25)


### Bug Fixes

* **deps:** update dependency com.squareup.okhttp3:logging-interceptor to v4.11.0 ([58dfd3e](https://github.com/lydavid/MusicSearch/commit/58dfd3e0bd22e416dc7e6f7ed63d6a5ce872efe8))

## [0.5.9](https://github.com/lydavid/MusicSearch/compare/v0.5.8...v0.5.9) (2023-04-24)


### Bug Fixes

* **deps:** update dependency com.squareup.okhttp3:mockwebserver to v4.11.0 ([7d4b590](https://github.com/lydavid/MusicSearch/commit/7d4b590909d83e97884b4e2a037d319033dbf9d5))
* **deps:** update dependency com.squareup.okhttp3:okhttp-tls to v4.11.0 ([d267a4a](https://github.com/lydavid/MusicSearch/commit/d267a4a74ab3dcf665dd55f21d076120e9859966))

## [0.5.8](https://github.com/lydavid/MusicSearch/compare/v0.5.7...v0.5.8) (2023-04-21)


### Bug Fixes

* **deps:** update dependency androidx.compose.material:material-icons-extended to v1.4.2 ([b9af4db](https://github.com/lydavid/MusicSearch/commit/b9af4db91c68a5a7503695f6555c84fb9527c155))
* **deps:** update dependency composeoptions to v1.4.6 ([3e4419d](https://github.com/lydavid/MusicSearch/commit/3e4419d4f45ec45fd0c88263b1acbaf53e3ef40c))

## [0.5.7](https://github.com/lydavid/MusicSearch/compare/v0.5.6...v0.5.7) (2023-04-20)


### Bug Fixes

* **deps:** update compose-ui to v1.4.2 ([7eaf556](https://github.com/lydavid/MusicSearch/commit/7eaf556ccaf521b788467adbe2363da39031c2c5))
* **deps:** update dependency androidx.activity:activity-compose to v1.7.1 ([a95b738](https://github.com/lydavid/MusicSearch/commit/a95b738c0fce5c5815b301ee01a653eee10a6d39))
* **deps:** update dependency androidx.compose.foundation:foundation to v1.4.2 ([550dd0d](https://github.com/lydavid/MusicSearch/commit/550dd0d2eda819f9cb9889410be7b029aa20b5a7))
* **deps:** update dependency androidx.compose.material3:material3 to v1.1.0-rc01 ([a9175c6](https://github.com/lydavid/MusicSearch/commit/a9175c6c2efd68224098c96913cceca09d4d46f9))

## [0.5.6](https://github.com/lydavid/MusicSearch/compare/v0.5.5...v0.5.6) (2023-04-14)


### Bug Fixes

* **deps:** update dependency com.google.firebase:firebase-bom to v31.5.0 ([1dd3542](https://github.com/lydavid/MusicSearch/commit/1dd35426487584d0db747d1bcf736b13efa400f8))

## [0.5.5](https://github.com/lydavid/MusicSearch/compare/v0.5.4...v0.5.5) (2023-04-12)


### Bug Fixes

* **deps:** update dependency io.mockk:mockk to v1.13.5 ([4ca8a01](https://github.com/lydavid/MusicSearch/commit/4ca8a01f01b01d0df1ee173dc8342182a165c9bc))
* **deps:** update dependency org.robolectric:robolectric to v4.10 ([2116d34](https://github.com/lydavid/MusicSearch/commit/2116d342b488284caf53d6c11f428a6b8418dc64))

## [0.5.4](https://github.com/lydavid/MusicSearch/compare/v0.5.3...v0.5.4) (2023-04-09)


### Bug Fixes

* **ci:** add export data to plugins ([1bef1d9](https://github.com/lydavid/MusicSearch/commit/1bef1d975dfc28fbcbb600bcfc7f11fd826e8893))

## [0.5.3](https://github.com/lydavid/MusicSearch/compare/v0.5.2...v0.5.3) (2023-04-09)


### Bug Fixes

* **ci:** commit the version bump before publishing ([41548a0](https://github.com/lydavid/MusicSearch/commit/41548a0ea829a38a3b42faba07cbf0ccd3418467))

## [0.5.2](https://github.com/lydavid/MusicSearch/compare/v0.5.1...v0.5.2) (2023-04-09)


### Bug Fixes

* **ci:** skip ci for version bump ([c7a53fa](https://github.com/lydavid/MusicSearch/commit/c7a53fab173d0ed33af558583d1e57d13e0d33fb))

## [0.5.1](https://github.com/lydavid/MusicSearch/compare/v0.5.0...v0.5.1) (2023-04-09)


### Bug Fixes

* **ci:** pass GitHub token to semantic-release ([5e56646](https://github.com/lydavid/MusicSearch/commit/5e56646d2b4ed04c5a8671c2440224082a4aa5ea))
* **ci:** use `env` ([b952b10](https://github.com/lydavid/MusicSearch/commit/b952b109739213dc67962590dc57ff0b1dff51a9))
* try publishing apk to GitHub, aab to Google Play ([30567b7](https://github.com/lydavid/MusicSearch/commit/30567b7f38800cbe6f274f62a6e9c96c7a5f34ff))

## [0.5.0](https://github.com/lydavid/MusicSearch/compare/v0.4.1...v0.5.0) (2023-04-08)


### Features

* try updating gradle.properties ([9b7a16e](https://github.com/lydavid/MusicSearch/commit/9b7a16ecb21b7d906daeb481d93193e23ef16978))

## [0.4.1](https://github.com/lydavid/MusicSearch/compare/v0.4.0...v0.4.1) (2023-04-08)


### Bug Fixes

* test publishing to GitHub with semantic release ([dcafd49](https://github.com/lydavid/MusicSearch/commit/dcafd49611a5d2b7ebdbc96548ba957a349f93c2))

## Google Play beta release

Features:
- Search MusicBrainz's massive database for any information related to your favorite artist or song
- All data is cached on device after loading each page/tab
- History: See every page you've visited, and quickly get back to them
- Filter: Almost every tab allows you to search its content instantaneously
- Collections: Save anything to a collection
- Login using your MusicBrainz account to add to your existing collections
- Cover arts
- Dark theme
- Material You theme

## What's Changed
* Use CircleCI for instrumented tests (#49) by @lydavid in https://github.com/lydavid/MusicSearch/pull/50
* Update hilt_version to v2.45 by @renovate in https://github.com/lydavid/MusicSearch/pull/54
* Update dependency gradle to v7.6 by @renovate in https://github.com/lydavid/MusicSearch/pull/45
* Add Detekt by @lydavid in https://github.com/lydavid/MusicSearch/pull/57
* Update dependency com.google.firebase:firebase-bom to v31.2.1 by @renovate in https://github.com/lydavid/MusicSearch/pull/60
* Update plugin com.google.firebase.crashlytics to v2.9.4 by @renovate in https://github.com/lydavid/MusicSearch/pull/61
* Update dependency gradle to v8 by @renovate in https://github.com/lydavid/MusicSearch/pull/62
* Update plugin org.jetbrains.kotlin.android to v1.8.10 by @renovate in https://github.com/lydavid/MusicSearch/pull/51
* Create GHA to publish internal build by @lydavid in https://github.com/lydavid/MusicSearch/pull/65
* Update dependency com.google.accompanist:accompanist-swiperefresh to v0.28.0 by @renovate in https://github.com/lydavid/MusicSearch/pull/44
* Update dependency com.google.firebase:firebase-bom to v31.2.2 by @renovate in https://github.com/lydavid/MusicSearch/pull/69
* Update dependency gradle to v8.0.1 by @renovate in https://github.com/lydavid/MusicSearch/pull/70
* Update dependency androidx.arch.core:core-testing to v2.2.0 by @renovate in https://github.com/lydavid/MusicSearch/pull/75
* Update plugin org.jetbrains.kotlin.jvm to v1.8.10 by @renovate in https://github.com/lydavid/MusicSearch/pull/78
* Update dependency io.nlopez.compose.rules:detekt to v0.1.2 by @renovate in https://github.com/lydavid/MusicSearch/pull/79
* Update plugin com.android.library to v7.4.2 by @renovate in https://github.com/lydavid/MusicSearch/pull/81
* Update plugin com.android.application to v7.4.2 by @renovate in https://github.com/lydavid/MusicSearch/pull/80
* Update dependency com.google.firebase:firebase-bom to v31.2.3 by @renovate in https://github.com/lydavid/MusicSearch/pull/83
* Update dependency gradle to v8.0.2 by @renovate in https://github.com/lydavid/MusicSearch/pull/84
* Update dependency composeOptions to v1.4.3 by @renovate in https://github.com/lydavid/MusicSearch/pull/85
* Update dependency androidx.lifecycle:lifecycle-viewmodel-compose to v2.6.0 by @renovate in https://github.com/lydavid/MusicSearch/pull/86
* Add collections feature part 1: local collections and user authentication with MusicBrainz by @lydavid in https://github.com/lydavid/MusicSearch/pull/82
* Update dependency androidx.paging:paging-compose to v1.0.0-alpha18 by @renovate in https://github.com/lydavid/MusicSearch/pull/39


**Full Changelog**: https://github.com/lydavid/MusicSearch/compare/v0.2.0...v0.3.0

Initial release for further testing

