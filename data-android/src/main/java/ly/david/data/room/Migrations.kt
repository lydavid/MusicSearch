package ly.david.data.room

import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object Migrations {

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                    INSERT INTO mbid_image ( mbid, image_path )
                    SELECT id, REPLACE('https://coverartarchive.org/release/' || cover_art_path, '.png', '')
                    FROM `release`
                    WHERE cover_art_path IS NOT NULL AND cover_art_path != ''
                """
            )

            database.execSQL(
                """
                    INSERT INTO mbid_image ( mbid, image_path )
                    SELECT id, REPLACE('https://coverartarchive.org/release/' || cover_art_path, '.png', '')
                    FROM `release_group`
                    WHERE cover_art_path IS NOT NULL AND cover_art_path != ''
                """
            )
        }
    }

    @DeleteColumn(tableName = "release", columnName = "cover_art_path")
    @DeleteColumn(tableName = "release_group", columnName = "cover_art_path")
    class DeleteCoverArtPath : AutoMigrationSpec

    @RenameColumn(tableName = "mbid_image", fromColumnName = "image_path", toColumnName = "thumbnail_url")
    class RenameThumbnailUrl : AutoMigrationSpec

    @RenameTable(fromTableName = "ArtistCreditNamesWithResource", toTableName = "artist_credit_names_entity")
    @RenameTable(fromTableName = "artist_credit_resource", toTableName = "artist_credit_entity")
    @RenameTable(fromTableName = "browse_resource_count", toTableName = "browse_entity_count")
    class RenameTablesToEntity : AutoMigrationSpec

    @RenameColumn(tableName = "relation", fromColumnName = "resource_id", toColumnName = "entity_id")
    @RenameColumn(tableName = "relation", fromColumnName = "linked_resource_id", toColumnName = "linked_entity_id")
    @RenameColumn(tableName = "relation", fromColumnName = "linked_resource", toColumnName = "linked_entity")
    @RenameColumn(tableName = "has_relations", fromColumnName = "resource_id", toColumnName = "entity_id")
    class RenameColumnsToEntity : AutoMigrationSpec
}
