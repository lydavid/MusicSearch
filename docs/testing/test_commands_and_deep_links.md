# Deep links

Note that these deep links are for the debug version of the app.
The release version also supports deep links, but you will have to remove the `.debug` part.

Either copy/paste the commands into a terminal, or run them using Android Studio's `Run in Terminal` gutter.

ADB commands: https://gist.github.com/Pulimet/5013acf2cd5b28e55036c82c91bd56d8

Intent arguments: https://developer.android.com/tools/adb#IntentSpec


1. [Configurations](#configurations)
2. [Search](#search)
3. [History](#history)
4. [Collection](#collection)
5. [Area](#area)
6. [Artist](#artist)
7. [Event](#event)
8. [Instrument](#instrument)
9. [Label](#label)
10. [Place](#place)
11. [Recording](#recording)
12. [Release](#release)
13. [Release group](#release-group)
14. [Series](#series)
15. [Work](#work)
16. [Genre](#genre)
17. [Spotify](#spotify-broadcast)


## Configurations

```shell
adb shell pm clear io.github.lydavid.musicsearch.debug
```

https://stackoverflow.com/a/69411601

```shell
adb shell "cmd uimode night yes" # change device theme to dark mode
```

```shell
adb shell "cmd uimode night no" # change device theme to light mode
```


## Search

Note the `'`. Need to include this when building uri with `&`.

```shell
adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/search?query=tsukuyomi&type=artist"'
```

```shell
adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/search?query=沈香学&type=release-group"'
```


## History

```shell
adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/history"
```


## Collection

```shell
adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/collection" # All collections
```

```shell
adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/collection/debe8581-e0c8-45ee-8fda-3ddcb5233f91" # My CD collection
```

Create a collection.
```shell
adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/create?name=My collection&type=release-group&id=f2888482-1633-4989-a8d3-313a6c66235e"'
```

Add to collection. Make sure the type is correct.
```shell
adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/f2888482-1633-4989-a8d3-313a6c66235e/add?id=f5b85956-16ef-41c4-a4fe-e2044e2c1a0e"'
```


## Area

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/area/023da4a0-acee-3fb1-b91e-5de74ccf787b" -a android.intent.action.VIEW # Area with parts, instruments, genre, url
```

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/area/74e50e58-5deb-4b99-93a2-decbb365c07f" -a android.intent.action.VIEW # Area with part of
```


## Artist

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/artist/b071f9fa-14b0-4217-8e97-eb41da73f598" -a android.intent.action.VIEW # artist with many release groups; 1000+
```

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/artist/b972f589-fb0e-474e-b64a-803b0364fa75" -a android.intent.action.VIEW # artist with many release groups; 5000+
```

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/artist/89ad4ac3-39f7-470e-963a-56509c546377" -a android.intent.action.VIEW # Various Artists; artist with many release groups; 240000+
```


## Event

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/event/3d6f84e1-bb1b-4caa-9abf-db67a4c2c055" -a android.intent.action.VIEW
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/event/3a6d0f96-d0ec-487d-bfbc-c584c8d31596" -a android.intent.action.VIEW # Cancelled event
```

## Instrument

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/instrument/7ee8ebf5-3aed-4fc8-8004-49f4a8c45a87" -a android.intent.action.VIEW # Instrument with used in, derivations, derived from, has hybrids, Wikidata
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/instrument/1b165fa4-8510-4a3e-a2b5-2d38baf55176" -a android.intent.action.VIEW # Instrument with from, picture
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/instrument/e346ac37-b617-4c12-b54d-d25474b6c7b7" -a android.intent.action.VIEW # instrument invented by a label
```



## Label


```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/label/d4cd174f-784d-48d7-91c6-7427bd5d57fe" -a android.intent.action.VIEW # label with many releases; 1000+
```

## Place

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/place/4d43b9d8-162d-4ac5-8068-dfb009722484" -a android.intent.action.VIEW # Budokan
```

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/place/ed121457-87f6-4df9-a24b-d3f1bab1fdad" -a android.intent.action.VIEW # place with artist, place, recording, release, url relationships; place with coordinates
```


## Recording

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/recording/a53c97d7-5501-443b-baa3-cb282fc64275" -a android.intent.action.VIEW # Recording with recording relationships
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/recording/6c38b565-83ed-4e6f-b8c4-484b963a12ea" -a android.intent.action.VIEW # Recording with artist, label, place, work relationships
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/recording/ac474974-600b-497e-902e-3b85b62cf58f" -a android.intent.action.VIEW # Recording with url
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/recording/dd21677f-d6ae-4dc2-b576-cb4cb5a66b79" -a android.intent.action.VIEW # Recording with many artist credits
```



## Release

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/f7a96d7b-67a7-4bc6-89dc-2a426f51b1f0" -a android.intent.action.VIEW # release with medium with 338 tracks
```

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5" -a android.intent.action.VIEW # release with multiple catalog number; release with multiple cover art
```

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/18572d3b-b8d6-4ac1-8cda-6951a8f625d5" -a android.intent.action.VIEW # release with a massive number of media; 170×CD
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/9427ac9c-f05a-4a45-8c30-da5ac1ae29a0" -a android.intent.action.VIEW # Released in Europe
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/1f22306b-bb99-42b3-b42d-1fa22ff79d17" -a android.intent.action.VIEW # Release with artist credit as "Various Artists", browse artists reveals 104 artists
```



```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/777279a4-efe9-4ab6-99ce-b2263913c93d" -a android.intent.action.VIEW # Released in many regions (with release date)
```


```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/30409e91-44c8-4758-a687-b1784c938cc4" -a android.intent.action.VIEW # Release with medium with name
```


```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/5a69dd22-6431-40ea-8a2e-b52b3973a60f" -a android.intent.action.VIEW # Release with 1 hour+ track
```


```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/3cd31605-0f7e-45ee-aacb-637a53e4c367" -a android.intent.action.VIEW # Release with letters in track's number, tracks by different artist than release's artist
```


```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/e7092039-54ae-4765-84da-732909429c92" -a android.intent.action.VIEW # Release with 500 tracks in a medium
```


```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/0c86c9de-ecb0-42a7-8808-bd06f7541f53" -a android.intent.action.VIEW # Release with 1000 tracks in a medium
```


```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/18572d3b-b8d6-4ac1-8cda-6951a8f625d5" -a android.intent.action.VIEW # Release; Mozart Complete Edition; 170 CDs
```

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release/31ced3da-acb5-4dcd-b3df-1a7319470a63" -a android.intent.action.VIEW # release with multiple labels
```


## Release Group

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release-group/b5d152fb-8274-3275-b2b9-155859fc0056" -a android.intent.action.VIEW # release group with an artist "The Jackson 5" appearing twice in artist credit with different name
```

```sh
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/release-group/807d9a32-e55c-317a-8a97-c4d5eaaa38b0" -a android.intent.action.VIEW # release group with artist-rel `inspired the name of`
```



## Series


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/series/2bb59d7e-88f9-455d-888e-802b5f688dac" -a android.intent.action.VIEW # work award series
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/series/44d6cf07-798b-4667-9d1a-c969d6471e4b" -a android.intent.action.VIEW # tour series
```


## Work

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/82cbbd32-3b19-3fd7-b409-49d1dbededd5" -a android.intent.action.VIEW # work with many artists; 80
```

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/412c8cf4-6905-3b4b-a59a-1a71f98e2677" -a android.intent.action.VIEW # work with work attributes
```


```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/c4ebe5b5-6965-4b8a-9f5e-7e543fc2acf3" -a android.intent.action.VIEW # work with artificial language (qaa); work with arrangements
```

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/7a7d89a4-ddb9-44af-a293-cf3b7ad59cf3" -a android.intent.action.VIEW # work with arrangement of
```

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/90b1e26b-5101-4518-89a7-5f08090d9ec2" -a android.intent.action.VIEW # work with zxx
```

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/work/dcb8c64d-181f-45c3-85d7-cdb6af3d2599" -a android.intent.action.VIEW # work with zxx
```




## Genre

```shell
adb shell am start -d "io.github.lydavid.musicsearch.debug://app/genre/911c7bbb-172d-4df8-9478-dbff4296e791?title=Pop" -a android.intent.action.VIEW # pop
```


## Spotify Broadcast

```shell
adb shell 'am broadcast -a com.spotify.music.metadatachanged --es id "spotify:track:7ALurdGTM0BZMHhUcrM2AW" --el timeSent 1714877657893 --es artist "Anonymouz" --es album "11:11" --es track "River" --ei length 198354'
```
