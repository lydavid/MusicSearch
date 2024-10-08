#!/bin/bash
set -e

# Pre-conditions:
# - You are in the scripts directory
# - The app is installed
# - You are logged into MusicBrainz (required for collections screenshot)
#   - Although this collection is public, we currently don't support loading a single collection from network, so this won't work for anyone else atm

SCREENSHOT_FOLDER="../fastlane/metadata/android/en-US/images/phoneScreenshots"

file_names=(
  "1_search_artist"
  "2_artist_details"
  "3_artist_release_groups"
  "4_release_details"
  "5_release_tracks"
  "6_all_collections"
  "7_collection"
  "8_lookup_history"
)

commands=(
  "adb shell am start -a android.intent.action.VIEW -d '\"io.github.lydavid.musicsearch.debug://app/search?query=aimer&type=artist\"'"
  "adb shell am start -a android.intent.action.VIEW -d 'io.github.lydavid.musicsearch.debug://app/artist/dfc6a151-3792-4695-8fda-f64723eaa788'"
  "adb shell input swipe 900 500 100 500"
  "adb shell am start -d 'io.github.lydavid.musicsearch.debug://app/release/3cd31605-0f7e-45ee-aacb-637a53e4c367' -a android.intent.action.VIEW"
  "adb shell input swipe 900 500 100 500"
  "adb shell am start -d 'io.github.lydavid.musicsearch.debug://app/collection'"
  "adb shell am start -d 'io.github.lydavid.musicsearch.debug://app/collection/debe8581-e0c8-45ee-8fda-3ddcb5233f91'"
  "adb shell am start -d 'io.github.lydavid.musicsearch.debug://app/history'"
)

for ((i = 0; i < ${#file_names[@]}; i++)); do
  file_name="${file_names[i]}"
  command="${commands[i]}"
  eval "$command"
  sleep 5
  adb exec-out screencap -p >"$SCREENSHOT_FOLDER/$file_name.png"
done
