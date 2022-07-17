# MusicBrainzJetpackCompose

## Some deeplinks for testing

<details><summary>area</summary><blockquote>

### Kenya
- parts, instruments, genre, url

```shell
adb shell am start -d "mbjc://area/023da4a0-acee-3fb1-b91e-5de74ccf787b" -a android.intent.action.VIEW
```

### New York
- part of

```shell
adb shell am start -d "mbjc://area/74e50e58-5deb-4b99-93a2-decbb365c07f" -a android.intent.action.VIEW
```

</blockquote></details>

<details><summary>instrument</summary><blockquote>

### electric guitar
- used in, derivations, derived from, has hybrids, Wikidata

```shell
adb shell am start -d "mbjc://instrument/7ee8ebf5-3aed-4fc8-8004-49f4a8c45a87" -a android.intent.action.VIEW
```

### biwa
- from, picture

```shell
adb shell am start -d "mbjc://instrument/1b165fa4-8510-4a3e-a2b5-2d38baf55176" -a android.intent.action.VIEW
```

### tubon
- `invented by` a label

```shell
adb shell am start -d "mbjc://instrument/e346ac37-b617-4c12-b54d-d25474b6c7b7" -a android.intent.action.VIEW
```

</blockquote></details>


<details><summary>artist</summary><blockquote>

### The Rolling Stones
- Many release groups (~950)
- 10 browse requests

```shell
adb shell am start -d "mbjc://artist/b071f9fa-14b0-4217-8e97-eb41da73f598" -a android.intent.action.VIEW
```

### Wolfgang Amadeus Mozart
- Huge number of release groups (~4600)

```shell
adb shell am start -d "mbjc://artist/b972f589-fb0e-474e-b64a-803b0364fa75" -a android.intent.action.VIEW
```

### Various Artist
- Massive number of release groups (~210880)

```shell
adb shell am start -d "mbjc://artist/89ad4ac3-39f7-470e-963a-56509c546377" -a android.intent.action.VIEW
```

</blockquote></details>


## Generate unit test coverage

`Run anything` -> `gradle app:createDebugUnitTestCoverageReport`
