# MusicBrainzJetpackCompose

## Some deeplinks for testing

<details><summary>instrument</summary><blockquote>

electric guitar
- used in, derivations, derived from, has hybrids, Wikidata
```shell
adb shell am start -d "mbjc://instrument/7ee8ebf5-3aed-4fc8-8004-49f4a8c45a87" -a android.intent.action.VIEW
```

biwa
- from, picture

```shell
adb shell am start -d "mbjc://instrument/1b165fa4-8510-4a3e-a2b5-2d38baf55176" -a android.intent.action.VIEW
```

tubon
- `invented by` a label

```shell
adb shell am start -d "mbjc://instrument/e346ac37-b617-4c12-b54d-d25474b6c7b7" -a android.intent.action.VIEW
```

</blockquote></details>



## Generate unit test coverage

`Run anything` -> `gradle app:createDebugUnitTestCoverageReport`
