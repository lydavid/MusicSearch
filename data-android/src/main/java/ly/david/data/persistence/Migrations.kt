package ly.david.data.persistence

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

internal object Migrations {

    @RenameColumn(tableName = "artists", fromColumnName = "country", toColumnName = "country_code")
    class RenameCountry : AutoMigrationSpec

    @RenameColumn(tableName = "releases", fromColumnName = "country", toColumnName = "country_code")
    class RenameReleasesCountryToCountryCode : AutoMigrationSpec

    @RenameTable(fromTableName = "recordings_relations", toTableName = "relations")
    @RenameColumn(tableName = "recordings_relations", fromColumnName = "resource", toColumnName = "linked_resource")
    class GeneralizeRecordingRelation : AutoMigrationSpec

    @RenameColumn(tableName = "relations", fromColumnName = "recording_id", toColumnName = "resource_id")
    class RenameResourceId : AutoMigrationSpec

    @DeleteColumn(tableName = "relations", columnName = "resource")
    class DeleteResource : AutoMigrationSpec

    @RenameColumn(tableName = "lookup_history", fromColumnName = "summary", toColumnName = "title")
    class RenameHistorySummaryToTitle : AutoMigrationSpec

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                ALTER TABLE lookup_history
                RENAME COLUMN destination TO resource
            """
            )

            // removes leading "lookup/"
            database.execSQL(
                """
                UPDATE lookup_history
                SET resource = SUBSTR(resource, 8)
            """
            )

            database.execSQL(
                """
                ALTER TABLE recordings_relations
                RENAME COLUMN destination TO resource
            """
            )
            database.execSQL(
                """
                UPDATE recordings_relations
                SET resource = SUBSTR(resource, 8)
            """
            )
        }
    }

    val MIGRATION_29_30 = object : Migration(29, 30) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                INSERT INTO has_relations ( resource_id, has_relations )
                SELECT id, has_default_relations
                FROM labels
            """
            )
        }
    }

    @DeleteColumn(tableName = "labels", columnName = "has_default_relations")
    class DeleteHasRelationsFromLabel : AutoMigrationSpec

    val MIGRATION_32_33 = object : Migration(32, 33) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                INSERT INTO has_relations ( resource_id, has_relations )
                SELECT id, has_default_relations
                FROM release_groups
            """
            )
        }
    }

    // ALTER TABLE DROP COLUMN isn't supported but @DeleteColumn automigration is
    @DeleteColumn(tableName = "release_groups", columnName = "has_default_relations")
    class DeleteHasRelationsFromReleaseGroup : AutoMigrationSpec

    val MIGRATION_34_35 = object : Migration(34, 35) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                INSERT INTO has_relations ( resource_id, has_relations )
                SELECT id, has_default_relations
                FROM areas
            """
            )
        }
    }

    @DeleteColumn(tableName = "areas", columnName = "has_default_relations")
    class DeleteHasRelationsFromArea : AutoMigrationSpec

    val MIGRATION_36_37 = object : Migration(36, 37) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                INSERT INTO has_relations ( resource_id, has_relations )
                SELECT id, has_default_relations
                FROM artists
            """
            )
        }
    }

    @DeleteColumn(tableName = "artists", columnName = "has_default_relations")
    class DeleteHasRelationsFromArtist : AutoMigrationSpec

    val MOVE_COVER_ART_URL_TO_RELEASES = object : Migration(40, 41) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
            ALTER TABLE releases
            ADD COLUMN cover_art_url TEXT DEFAULT null
        """
            )
            database.execSQL(
                """
                UPDATE releases
                SET cover_art_url = (
                    SELECT ca.small_url
                    FROM cover_arts ca
                    WHERE ca.resource_id = releases.id
                )
            """
            )
            database.execSQL(
                """
                DROP TABLE cover_arts
            """
            )
        }
    }

    val ADD_FK_TO_RELEASES_RELEASE_GROUPS = object : Migration(43, 44) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS new_rrg (`release_id` TEXT NOT NULL, `release_group_id` TEXT NOT NULL,
            PRIMARY KEY(`release_id`, `release_group_id`), FOREIGN KEY(`release_id`) REFERENCES `releases`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )
        """
            )
            database.execSQL(
                """
                INSERT INTO new_rrg (release_id, release_group_id)
                SELECT release_id, release_group_id
                FROM releases_release_groups
            """
            )
            database.execSQL(
                """
                DROP TABLE releases_release_groups
            """
            )
            database.execSQL(
                """
                ALTER TABLE new_rrg RENAME TO releases_release_groups
            """
            )
        }
    }

    @DeleteColumn(tableName = "areas", columnName = "release_count")
    @DeleteColumn(tableName = "labels", columnName = "release_count")
    @DeleteColumn(tableName = "release_groups", columnName = "release_count")
    class DeleteReleaseCount : AutoMigrationSpec

    @DeleteTable(tableName = "releases_release_groups")
    class DeleteReleasesReleaseGroups : AutoMigrationSpec

    @RenameColumn(tableName = "artists", fromColumnName = "sort-name", toColumnName = "sort_name")
    class RenameSortName : AutoMigrationSpec

    @DeleteTable(tableName = "release_groups_artists")
    class DropReleaseGroupsArtists : AutoMigrationSpec

    @DeleteTable(tableName = "releases_artists")
    class DropReleasesArtists : AutoMigrationSpec

    @DeleteTable(tableName = "recordings_artists")
    class DropRecordingsArtists : AutoMigrationSpec

    @DeleteColumn(tableName = "artists", columnName = "release_group_count")
    class DeleteReleaseGroupCount : AutoMigrationSpec

    @RenameTable(fromTableName = "areas", toTableName = "area")
    @RenameTable(fromTableName = "releases_countries", toTableName = "release_country")
    @RenameTable(fromTableName = "artist_credits", toTableName = "artist_credit")
    @RenameTable(fromTableName = "artist_credit_names", toTableName = "artist_credit_name")
    @RenameTable(fromTableName = "artist_credits_resources", toTableName = "artist_credit_resource")
    @RenameTable(fromTableName = "artists_release_groups", toTableName = "artist_release_group")
    @RenameTable(fromTableName = "artists", toTableName = "artist")
    @RenameTable(fromTableName = "events", toTableName = "event")
    @RenameTable(fromTableName = "instruments", toTableName = "instrument")
    @RenameTable(fromTableName = "labels", toTableName = "label")
    @RenameTable(fromTableName = "releases_labels", toTableName = "release_label")
    @RenameTable(fromTableName = "places", toTableName = "place")
    @RenameTable(fromTableName = "recordings", toTableName = "recording")
    @RenameTable(fromTableName = "releases_recordings", toTableName = "recording_release")
    @RenameTable(fromTableName = "browse_resource_counts", toTableName = "browse_resource_count")
    @RenameTable(fromTableName = "relations", toTableName = "relation")
    @RenameTable(fromTableName = "media", toTableName = "medium")
    @RenameTable(fromTableName = "releases", toTableName = "release")
    @RenameTable(fromTableName = "tracks", toTableName = "track")
    @RenameTable(fromTableName = "release_groups", toTableName = "release_group")
    @RenameTable(fromTableName = "recordings_works", toTableName = "recording_work")
    @RenameTable(fromTableName = "works", toTableName = "work")
    class RenameTablesToSingular : AutoMigrationSpec

    val MOVE_RELEASE_GROUP_ID_OUT_OF_RELEASE = object : Migration(79, 80) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                INSERT INTO release_release_group (release_id, release_group_id)
                SELECT id, release_group_id
                FROM release
                WHERE release_group_id IS NOT NULL
            """
            )
            // Literally cannot drop column here, need to do another migration with @DeleteColumn
        }
    }

    @DeleteColumn(tableName = "release", columnName = "release_group_id")
    class DeleteReleaseGroupIdFromRelease : AutoMigrationSpec

    @DeleteColumn(tableName = "release", columnName = "formats")
    @DeleteColumn(tableName = "release", columnName = "tracks")
    class DeleteFormatsAndTracksFromRelease : AutoMigrationSpec

    @RenameColumn(tableName = "release", fromColumnName = "cover_art_url", toColumnName = "cover_art_path")
    class RenameToCoverArtPath : AutoMigrationSpec

    // Remove leading http://coverartarchive.org/release/e83e9090-3590-4b95-b27d-6f442014bd4d/
    val REMOVE_LEADING_CAA_PATH = object : Migration(86, 87) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                UPDATE `release`
                SET cover_art_path = SUBSTR(cover_art_path, 73)
                WHERE cover_art_path IS NOT NULL
            """
            )
        }
    }

    @RenameColumn(tableName = "release_group", fromColumnName = "cover_art_url", toColumnName = "cover_art_path")
    class RenameToCoverArtPathForReleaseGroup : AutoMigrationSpec

    // Remove leading http://coverartarchive.org/release/
    val REMOVE_LEADING_CAA_PATH_FOR_RELEASE_GROUP = object : Migration(88, 89) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                UPDATE release_group
                SET cover_art_path = SUBSTR(cover_art_path, 36)
                WHERE cover_art_path IS NOT NULL
            """
            )
        }
    }

    val CHANGE_LOOKUP_HISTORY_PK = object : Migration(90, 91) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                INSERT INTO lookup_history_copy (mbid, title, resource, number_of_visits, last_accessed, search_hint)
                SELECT mbid, title, resource, number_of_visits, last_accessed, search_hint FROM lookup_history
            """
            )
            database.execSQL(
                """
                DROP TABLE lookup_history
            """
            )
            database.execSQL(
                """
                ALTER TABLE lookup_history_copy RENAME TO lookup_history
            """
            )
        }
    }

    // Remove trailing -250.jpg
    val TRIM_250_JPG_FOR_RELEASE_GROUP = object : Migration(91, 92) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                UPDATE release_group
                SET cover_art_path = SUBSTR(cover_art_path, 1, LENGTH(cover_art_path)-8)
                WHERE cover_art_path IS NOT NULL
            """
            )
        }
    }

    // Remove trailing -250.jpg
    val TRIM_250_JPG_FOR_RELEASE = object : Migration(92, 93) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                UPDATE release
                SET cover_art_path = SUBSTR(cover_art_path, 1, LENGTH(cover_art_path)-8)
                WHERE cover_art_path IS NOT NULL
            """
            )
        }
    }

    val ADD_UUID_TO_RELEASE_PATH_FOR_CONSISTENCY = object : Migration(93, 94) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                UPDATE release
                SET cover_art_path = id || '/' || cover_art_path
                WHERE cover_art_path IS NOT NULL AND cover_art_path != ''
            """
            )
        }
    }

    @DeleteColumn(tableName = "release_group", columnName = "has_cover_art")
    class DeleteHasCoverArtFromReleaseGroup : AutoMigrationSpec

    val UPDATE_IS_REMOTE = object : Migration(100, 101) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.query(
                """
                UPDATE collection SET is_remote = (CASE WHEN mbid IS NULL THEN 0 ELSE 1 END)
            """
            )
        }
    }

    val SET_RANDOM_UUID = object : Migration(101, 102) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.query("SELECT id FROM collection").use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(0)
                    val uuid = UUID.randomUUID().toString()
                    database.execSQL("UPDATE collection SET mbid = '$uuid' WHERE id = $id")
                }
            }

            database.execSQL("ALTER TABLE collection_entity ADD COLUMN mbid TEXT")

            database.execSQL(
                """
                UPDATE collection_entity 
                SET mbid = (SELECT mbid FROM collection WHERE collection.id = collection_entity.id)
                """
            )
        }
    }

    val CHANGE_COLLECTION_PRIMARY_KEY_TO_UUID = object : Migration(102, 103) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                    CREATE TABLE IF NOT EXISTS new_collection
                    (`id` TEXT NOT NULL,
                    `is_remote` INTEGER NOT NULL,
                    `name` TEXT NOT NULL,
                    `entity` TEXT NOT NULL,
                    `type` TEXT,
                    `type_id` TEXT,
                    `entity_count` INTEGER NOT NULL,
                    PRIMARY KEY(`id`))
                    """
            )
            database.execSQL(
                """
                    INSERT INTO new_collection (`id`, `is_remote`, `name`, `entity`, `type`, `type_id`, `entity_count`)
                    SELECT `mbid`, `is_remote`, `name`, `entity`, `type`, `type-id`, `entity-count`
                    FROM collection
                """
            )

            database.execSQL(
                """
                    CREATE TABLE IF NOT EXISTS new_collection_entity
                     (`id` TEXT NOT NULL,
                     `entity_id` TEXT NOT NULL,
                     PRIMARY KEY(`id`, `entity_id`),
                     FOREIGN KEY(`id`) REFERENCES collection(`id`) ON UPDATE CASCADE ON DELETE CASCADE)
                """
            )
            database.execSQL(
                """
                    INSERT INTO new_collection_entity (id, entity_id)
                    SELECT mbid, entity_id
                    FROM collection_entity
                """
            )

            database.execSQL(
                """
                DROP TABLE collection
            """
            )
            database.execSQL(
                """
                    ALTER TABLE new_collection RENAME TO collection
                """
            )
            database.execSQL(
                """
                DROP TABLE collection_entity
            """
            )
            database.execSQL(
                """
                    ALTER TABLE new_collection_entity RENAME TO collection_entity
                """
            )
        }
    }

    val CHANGE_LOOKUP_COLLECTION_TO_RECORDING = object : Migration(103, 104) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                UPDATE lookup_history SET resource = "recording" WHERE resource = "collection"
            """
            )

            // If our schema doesn't change, no migration takes place,
            // so even though we just want to update a column due to a bug,
            // we will have to make a schema change as well
            database.execSQL(
                """
                ALTER TABLE work_attribute RENAME COLUMN `type-id` TO `type_id`
            """
            )
        }
    }
}
