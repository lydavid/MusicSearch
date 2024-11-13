#!/bin/bash
set -e

# Pre-conditions:
# - You are in the scripts directory
# - The app is installed
# - You are logged into MusicBrainz (required for collections screenshot)
#   - Although this collection is public, we currently don't support loading a single collection from network, so this won't work for anyone else atm

SCREENSHOT_FOLDER="../fastlane/metadata/android/en-US/images/phoneScreenshots"

# Setup

collection_id="f2888482-1633-4989-a8d3-313a6c66235e"

setup() {
  adb shell "cmd uimode night yes" # change device theme to dark mode
  sleep 5

  adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/create?name=My collection&type=release-group&id=$collection_id"'
  sleep 5
  adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/create?name=Releases&type=release&id=6690b309-fbbe-41ac-a955-5aaa287a1aab"'
  sleep 5
  adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/create?name=Artists&type=artist&id=6690b309-fbbe-41ac-a955-5aaa287a1aac"'
  sleep 5
  adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/create?name=Labels&type=label&id=6690b309-fbbe-41ac-a955-5aaa287a1aad"'
  sleep 5
  adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/create?name=Events&type=event&id=6690b309-fbbe-41ac-a955-5aaa287a1aae"'
  sleep 5
  adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/create?name=Works&type=work&id=6690b309-fbbe-41ac-a955-5aaa287a1aaf"'
  sleep 5
  adb shell am start -a android.intent.action.VIEW -d '"io.github.lydavid.musicsearch.debug://app/collection/create?name=Recordings&type=recording&id=6690b309-fbbe-41ac-a955-5aaa287a1ab1"'
  sleep 5

  visit_then_add_to_collection() {
    collectable_id=$1
    adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/release-group/$collectable_id"
    sleep 5
    adb shell am start -a android.intent.action.VIEW -d "io.github.lydavid.musicsearch.debug://app/collection/$collection_id/add?id=$collectable_id"
    sleep 5
  }

  visit_then_add_to_collection "f5b85956-16ef-41c4-a4fe-e2044e2c1a0e"
  visit_then_add_to_collection "3cd6db35-1ef6-423f-b22d-19eaf97f5bec"
  visit_then_add_to_collection "ee0aed4c-0ab5-4ce4-b809-bd479577d4fe"
  visit_then_add_to_collection "e838586e-2e43-451a-81df-b2ea4aa28e11"
  visit_then_add_to_collection "1b7b8e19-9f17-40af-b63e-18911918fd83"
  visit_then_add_to_collection "d7e5e3ae-5aa9-449e-9199-e79173ab1f88"
  visit_then_add_to_collection "92f914f2-b537-4573-bc14-8e8f2450ec63"
  visit_then_add_to_collection "ae5d56e6-f0d9-4379-bb50-59c44284a12f"
  visit_then_add_to_collection "0dd51230-c886-4422-8385-072d0688ce36"
}

setup

# Go to specific screens to record

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
  "adb shell am start -a android.intent.action.VIEW -d '\"io.github.lydavid.musicsearch.debug://app/search?query=bump&type=artist\"'"
  "adb shell am start -a android.intent.action.VIEW -d 'io.github.lydavid.musicsearch.debug://app/artist/0f718079-e5ea-4cfb-b512-b2d04da66901'"
  "adb shell input swipe 900 500 100 500"
  "adb shell am start -d 'io.github.lydavid.musicsearch.debug://app/release/9322eb2e-b53e-4510-810b-5bb61bf4a952' -a android.intent.action.VIEW"
  "adb shell input swipe 900 500 100 500"
  "adb shell am start -d 'io.github.lydavid.musicsearch.debug://app/collection'"
  "adb shell am start -d 'io.github.lydavid.musicsearch.debug://app/collection/$collection_id'"
  "adb shell am start -d 'io.github.lydavid.musicsearch.debug://app/history'"
)

for ((i = 0; i < ${#file_names[@]}; i++)); do
  file_name="${file_names[i]}"
  command="${commands[i]}"
  eval "$command"
  sleep 5
  adb exec-out screencap -p >"$SCREENSHOT_FOLDER/$file_name.png"
done
