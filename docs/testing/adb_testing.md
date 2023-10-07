# ADB Testing

Either copy/paste the commands into a terminal, or run them using Android Studio's `Run in Terminal` gutter.

## Spotify Broadcast

https://gist.github.com/Pulimet/5013acf2cd5b28e55036c82c91bd56d8

```shell
adb shell 'am broadcast -a com.spotify.music.metadatachanged --es artist "The Artist" --es album "The Album" --es track "That Song"'
```

## App Deep Links

[Deep Links](./deep_links.md)

## Changing Device Theme

https://stackoverflow.com/a/69411601

```shell
adb shell "cmd uimode night yes"
```

```shell
adb shell "cmd uimode night no"
```
