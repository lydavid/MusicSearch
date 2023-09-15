package ly.david.data.di.room

import androidx.room.Room
import ly.david.data.room.Migrations.MIGRATION_7_8
import ly.david.data.room.MusicSearchDatabase
import ly.david.data.room.MusicSearchRoomDatabase
import org.koin.dsl.module

private const val DATABASE_NAME = "mbjc.db"

val roomDatabaseModule = module {
    single<MusicSearchDatabase> {
        Room.databaseBuilder(get(), MusicSearchRoomDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_7_8)
            .fallbackToDestructiveMigration()
            .build()
    }
}
