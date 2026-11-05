This is a Kotlin Multiplatform project.

Use `./gradlew data:database:generateSqlDelightInterface` after changing `.sq` files.

Use `./gradlew :data:database:verifyCommonMainDatabaseMigration` after changing `.sqm` files.

Use `./gradlew detektMetadataCommonMain` after changing common source set.

When making SQL changes, use sqlite3 to test against real musicsearch.db database in project directory root.

Run `./gradlew check` and `./gradlew :android:app:assembleFDroidDebug` before reporting done.

If a compile error references generated SqlDelight code under `data/database/build/generated/` with no `.sq` changes, the generated sources are stale: run `./gradlew clean` and recompile.
