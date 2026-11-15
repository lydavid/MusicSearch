---
name: sql
description: Use this when working with SQL, or sqldelight (.sq, .sqm files)
---

# SQL

When making SQL changes, use sqlite3 to test against real `musicsearch.db` database in project directory root.
If file does not exist, or is outdated, run `adb shell "run-as io.github.lydavid.musicsearch.debug cat /data/data/io.github.lydavid.musicsearch.debug/databases/musicsearch.db" > musicsearch.db`.

If a compile error references generated SqlDelight code under `data/database/build/generated/` with no `.sq` changes, the generated sources are stale: run `./gradlew clean` and recompile.
