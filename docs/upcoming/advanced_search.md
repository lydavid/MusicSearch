# Advanced Search


## Research

All the possible ways we can query for a MusicBrainz entity:
https://wiki.musicbrainz.org/MusicBrainz_API/Search

Also see: https://musicbrainz.org/doc/Indexed_Search_Syntax.
This appears to be the same as the above but non-dev-facing.


## Motivation

I want an analogous of https://musicbrainz.org/search with UI to make the additional field syntax discoverable.


## Implementation

- We don't have to change `SearchApi` at all. Just pass the composed query in as `query`
  - Example that already works: `"voodoo people" AND artist:"the prodigy"` with `Recording`
- We also don't have to change our search deeplinking. Everything besides `type` can go into `query`.
- Example:
```sh
adb shell am start -d '"mbjc://lookup?query=\"voodoo people\" AND artist:\"the prodigy\"&type=recording"' -a android.intent.action.VIEW
```

- [ ] Consider navigating to new screen when the user clicks into search field
  - [ ] in this screen, they have the option to use advanced search
  - or they can click a [past search query](Search%20History.md)
	- this suggests we keep search history and lookup history separate
- [ ] user can build search query manually if they know its syntax
- [ ] or they can use our UI to help them
  - [ ] depending on which MusicBrainz entity is selected, the additional fields will change
    - [ ] Should what the user typed in them clear? How about when switching between entities without shared additional fields?
- [ ] fields that have limited choices can be enum, and can allow user to pick from dropdown


## Open questions


