This is a Kotlin Multiplatform project.

Use `./gradlew data:database:generateSqlDelightInterface` after changing `.sq` files.

Use `./gradlew :data:database:verifyCommonMainDatabaseMigration` after changing `.sqm` files.

Use `./gradlew detektMetadataCommonMain` after changing common source set.

Run `./gradlew check` and `./gradlew :android:app:assembleFDroidDebug` before reporting done.
