# Tips and Tricks

When working on anything that requires a database migration, always make a PR.
That way if it reaches a state beyond repair, we can just start again.

If I need to build the release build locally, go into `secrets.properties` and edit these lines:
```
# Debug
MUSICBRAINZ_CLIENT_ID=
MUSICBRAINZ_CLIENT_SECRET=

# Release
#MUSICBRAINZ_CLIENT_ID=
#MUSICBRAINZ_CLIENT_SECRET=
```

Need to debug on a physical phone but don't want to keep it connected via usb?
Use [scrcpy](https://github.com/Genymobile/scrcpy/blob/master/doc/device.md).

