# Test Data

Use this instead of top-level README to collect test deeplinks.

Also use this folder to store example JSON.

Use this as a sort of checklist for what features we support.

## Search

Note the `'`. Need to include this when building uri with `&`.

```shell
adb shell am start -d '"mbjc://lookup?query=tsukuyomi&type=artist"' -a android.intent.action.VIEW
```

## Artist

```sh
adb shell am start -d "mbjc://artist/d044577e-aa22-43b7-ab29-cabee5f6643c" -a android.intent.action.VIEW
```


## Release Group

Death Magnetic
- has relationships

```sh
adb shell am start -d "mbjc://release-group/5c8a25bf-4764-3cce-8f37-30af79d3b101" -a android.intent.action.VIEW
```

Heaven Shall Burn… When We Are Gathered
- artist-rel `inspired the name of`

```sh
adb shell am start -d "mbjc://release-group/807d9a32-e55c-317a-8a97-c4d5eaaa38b0" -a android.intent.action.VIEW
```




## Event

```shell
adb shell am start -d "mbjc://event/3d6f84e1-bb1b-4caa-9abf-db67a4c2c055" -a android.intent.action.VIEW
```




<details><summary>work</summary><blockquote>

## イニシエノウタ

- arrangements

```shell
adb shell am start -d "mbjc://work/c4ebe5b5-6965-4b8a-9f5e-7e543fc2acf3" -a android.intent.action.VIEW
```

- arrangement of

```shell
adb shell am start -d "mbjc://work/7a7d89a4-ddb9-44af-a293-cf3b7ad59cf3" -a android.intent.action.VIEW
```

</blockquote></details>


<details><summary>area</summary><blockquote>

## Kenya
- parts, instruments, genre, url

```shell
adb shell am start -d "mbjc://area/023da4a0-acee-3fb1-b91e-5de74ccf787b" -a android.intent.action.VIEW
```

## New York
- part of

```shell
adb shell am start -d "mbjc://area/74e50e58-5deb-4b99-93a2-decbb365c07f" -a android.intent.action.VIEW
```

</blockquote></details>

<details><summary>instrument</summary><blockquote>

## electric guitar
- used in, derivations, derived from, has hybrids, Wikidata

```shell
adb shell am start -d "mbjc://instrument/7ee8ebf5-3aed-4fc8-8004-49f4a8c45a87" -a android.intent.action.VIEW
```

## biwa
- from, picture

```shell
adb shell am start -d "mbjc://instrument/1b165fa4-8510-4a3e-a2b5-2d38baf55176" -a android.intent.action.VIEW
```

## tubon
- `invented by` a label

```shell
adb shell am start -d "mbjc://instrument/e346ac37-b617-4c12-b54d-d25474b6c7b7" -a android.intent.action.VIEW
```

</blockquote></details>


<details><summary>artist</summary><blockquote>

## The Rolling Stones
- Many release groups (~950)
- 10 browse requests

```shell
adb shell am start -d "mbjc://artist/b071f9fa-14b0-4217-8e97-eb41da73f598" -a android.intent.action.VIEW
```

## Wolfgang Amadeus Mozart
- Huge number of release groups (~4600)

```shell
adb shell am start -d "mbjc://artist/b972f589-fb0e-474e-b64a-803b0364fa75" -a android.intent.action.VIEW
```

## Various Artist
- Massive number of release groups (~210880)

```shell
adb shell am start -d "mbjc://artist/89ad4ac3-39f7-470e-963a-56509c546377" -a android.intent.action.VIEW
```

</blockquote></details>


## Assorted



### Place with artist


```shell
adb shell am start -d "mbjc://place/ed121457-87f6-4df9-a24b-d3f1bab1fdad" -a android.intent.action.VIEW
```






### Recording with recording


```shell
adb shell am start -d "mbjc://recording/a53c97d7-5501-443b-baa3-cb282fc64275" -a android.intent.action.VIEW
```


### release with misc: miscellaneous support: task


```sh
adb shell am start -d "mbjc://release/f6719001-9d2f-4511-88d7-80393524aa23" -a android.intent.action.VIEW
```




### massive number of media in a release

170×CD

```sh
adb shell am start -d "mbjc://release-group/3ea5428d-1d2b-35ee-bbb4-5fb7171a7269" -a android.intent.action.VIEW
```

```sh
adb shell am start -d "mbjc://release/18572d3b-b8d6-4ac1-8cda-6951a8f625d5" -a android.intent.action.VIEW
```

### massive number of tracks in a medium

500
```sh
adb shell am start -d "mbjc://release/e7092039-54ae-4765-84da-732909429c92" -a android.intent.action.VIEW
```
1000
```sh
adb shell am start -d "mbjc://release/0c86c9de-ecb0-42a7-8808-bd06f7541f53" -a android.intent.action.VIEW
```


### An artist "The Jackson 5" appears twice in artist credit with different name

```sh
adb shell am start -d "mbjc://release-group/b5d152fb-8274-3275-b2b9-155859fc0056" -a android.intent.action.VIEW
```

### Medium with name

```sh
adb shell am start -d "mbjc://release/30409e91-44c8-4758-a687-b1784c938cc4" -a android.intent.action.VIEW
```

### 1 hour+ track

```sh
adb shell am start -d "mbjc://release/5a69dd22-6431-40ea-8a2e-b52b3973a60f" -a android.intent.action.VIEW
```

### Release with letters in track's number, tracks by different artist than release's artist

```sh
adb shell am start -d "mbjc://release/3cd31605-0f7e-45ee-aacb-637a53e4c367" -a android.intent.action.VIEW
```


### Recording with artist, label, place, work rels

https://musicbrainz.org/recording/6c38b565-83ed-4e6f-b8c4-484b963a12ea
```shell
adb shell am start -d "mbjc://recording/6c38b565-83ed-4e6f-b8c4-484b963a12ea" -a android.intent.action.VIEW
```


### Recording with url


```shell
adb shell am start -d "mbjc://recording/ac474974-600b-497e-902e-3b85b62cf58f" -a android.intent.action.VIEW
```


### Place with url


```shell
adb shell am start -d "mbjc://place/4d43b9d8-162d-4ac5-8068-dfb009722484" -a android.intent.action.VIEW
```

