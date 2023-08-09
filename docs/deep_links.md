# Deep links

Note that these deep links are for the debug version of the app.
The release version also supports deep links, but you will have to remove the `.debug` part.

## Search

Note the `'`. Need to include this when building uri with `&`.

```shell
adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/lookup?query=tsukuyomi&type=artist"'
```

```shell
adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/lookup?query=沈香学&type=release-group"'
```


## History

```shell
adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/history"
```


## Collection

```shell
adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/collections"
```

```shell
adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/collections/debe8581-e0c8-45ee-8fda-3ddcb5233f91"
```


## Artist

```sh
adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/artist/dfc6a151-3792-4695-8fda-f64723eaa788"
```

## Event

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/event/3d6f84e1-bb1b-4caa-9abf-db67a4c2c055" -a android.intent.action.VIEW
```

Cancelled
```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/event/3a6d0f96-d0ec-487d-bfbc-c584c8d31596" -a android.intent.action.VIEW
```


## Recording

Recording with recording

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/recording/a53c97d7-5501-443b-baa3-cb282fc64275" -a android.intent.action.VIEW
```
Recording with artist, label, place, work rels

https://app/musicbrainz.org/recording/6c38b565-83ed-4e6f-b8c4-484b963a12ea
```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/recording/6c38b565-83ed-4e6f-b8c4-484b963a12ea" -a android.intent.action.VIEW
```

Recording with url

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/recording/ac474974-600b-497e-902e-3b85b62cf58f" -a android.intent.action.VIEW
```

Recording with many artist credits

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/recording/dd21677f-d6ae-4dc2-b576-cb4cb5a66b79" -a android.intent.action.VIEW
```



## Release


Has label multiple labels, and has multiple catalog numbers from the same label
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/e56065c8-709e-4df8-952b-57031c352a03" -a android.intent.action.VIEW
```

Released in many regions (with release date)
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/777279a4-efe9-4ab6-99ce-b2263913c93d" -a android.intent.action.VIEW
```

Medium with name
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/30409e91-44c8-4758-a687-b1784c938cc4" -a android.intent.action.VIEW
```

1 hour+ track
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/5a69dd22-6431-40ea-8a2e-b52b3973a60f" -a android.intent.action.VIEW
```

Release with letters in track's number, tracks by different artist than release's artist
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/3cd31605-0f7e-45ee-aacb-637a53e4c367" -a android.intent.action.VIEW
```

Massive number of tracks in a medium
500
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/e7092039-54ae-4765-84da-732909429c92" -a android.intent.action.VIEW
```
1000
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/0c86c9de-ecb0-42a7-8808-bd06f7541f53" -a android.intent.action.VIEW
```

Mozart Complete Edition
- 170 CDs
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/18572d3b-b8d6-4ac1-8cda-6951a8f625d5" -a android.intent.action.VIEW
```


release with misc: miscellaneous support: task
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/f6719001-9d2f-4511-88d7-80393524aa23" -a android.intent.action.VIEW
```



## Release Group

Release group with relations
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release-group/5c8a25bf-4764-3cce-8f37-30af79d3b101" -a android.intent.action.VIEW
```

artist-rel `inspired the name of`
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release-group/807d9a32-e55c-317a-8a97-c4d5eaaa38b0" -a android.intent.action.VIEW
```

Release event with null area
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release-group/f2b2abf8-09af-4d45-aee9-b0c99d4fcabc" -a android.intent.action.VIEW
```

Has urls-rels
```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release-group/367b4517-4cb1-3834-8132-e980ddb65162" -a android.intent.action.VIEW
```

## Label


Warner Records
- some 1000 releases

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/label/d4cd174f-784d-48d7-91c6-7427bd5d57fe" -a android.intent.action.VIEW
```


## Genre

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/genre/911c7bbb-172d-4df8-9478-dbff4296e791?title=Pop" -a android.intent.action.VIEW
```

## Series

- Work award
```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/series/2bb59d7e-88f9-455d-888e-802b5f688dac" -a android.intent.action.VIEW
```



## Work

- work attributes

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/412c8cf4-6905-3b4b-a59a-1a71f98e2677" -a android.intent.action.VIEW
```

- arrangements

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/c4ebe5b5-6965-4b8a-9f5e-7e543fc2acf3" -a android.intent.action.VIEW
```

- arrangement of

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/7a7d89a4-ddb9-44af-a293-cf3b7ad59cf3" -a android.intent.action.VIEW
```


<details><summary>area</summary><blockquote>

## Kenya
- parts, instruments, genre, url

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/area/023da4a0-acee-3fb1-b91e-5de74ccf787b" -a android.intent.action.VIEW
```

## New York
- part of

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/area/74e50e58-5deb-4b99-93a2-decbb365c07f" -a android.intent.action.VIEW
```

</blockquote></details>

<details><summary>instrument</summary><blockquote>

## electric guitar
- used in, derivations, derived from, has hybrids, Wikidata

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/instrument/7ee8ebf5-3aed-4fc8-8004-49f4a8c45a87" -a android.intent.action.VIEW
```

## biwa
- from, picture

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/instrument/1b165fa4-8510-4a3e-a2b5-2d38baf55176" -a android.intent.action.VIEW
```

## tubon
- `invented by` a label

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/instrument/e346ac37-b617-4c12-b54d-d25474b6c7b7" -a android.intent.action.VIEW
```

</blockquote></details>


<details><summary>artist</summary><blockquote>

## The Rolling Stones
- Many release groups (~950)
- 10 browse requests

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/artist/b071f9fa-14b0-4217-8e97-eb41da73f598" -a android.intent.action.VIEW
```

## Wolfgang Amadeus Mozart
- Huge number of release groups (~4600)

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/artist/b972f589-fb0e-474e-b64a-803b0364fa75" -a android.intent.action.VIEW
```

## Various Artist
- Massive number of release groups (~210880)

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/artist/89ad4ac3-39f7-470e-963a-56509c546377" -a android.intent.action.VIEW
```

</blockquote></details>


## Assorted



### Place with artist


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/place/ed121457-87f6-4df9-a24b-d3f1bab1fdad" -a android.intent.action.VIEW
```











### massive number of media in a release

170×CD

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release-group/3ea5428d-1d2b-35ee-bbb4-5fb7171a7269" -a android.intent.action.VIEW
```

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/18572d3b-b8d6-4ac1-8cda-6951a8f625d5" -a android.intent.action.VIEW
```



### An artist "The Jackson 5" appears twice in artist credit with different name

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release-group/b5d152fb-8274-3275-b2b9-155859fc0056" -a android.intent.action.VIEW
```





### Place with url


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/place/4d43b9d8-162d-4ac5-8068-dfb009722484" -a android.intent.action.VIEW
```
