# ADB Testing

Either copy/paste the commands into a terminal, or run them using Android Studio's `Run in Terminal` gutter.

## Spotify Broadcast

https://gist.github.com/Pulimet/5013acf2cd5b28e55036c82c91bd56d8

```shell
adb shell 'am broadcast -a com.spotify.music.metadatachanged --es id "spotify:track:7ALurdGTM0BZMHhUcrM2AW" --el timeSent 1714877657893 --es artist "Anonymouz" --es album "11:11" --es track "River" --ei length 198354'
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
