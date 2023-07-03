package ly.david.data.room

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
}
