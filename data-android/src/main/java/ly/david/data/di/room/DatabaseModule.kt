package ly.david.data.di.room

import androidx.room.Room
import ly.david.data.room.Migrations.MIGRATION_7_8
import ly.david.data.room.MusicSearchDatabase
import ly.david.data.room.MusicSearchRoomDatabase
import org.koin.dsl.module

private const val DATABASE_NAME = "mbjc.db"

//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//    @Singleton
//    @Provides
//    fun provideDatabase(@ApplicationContext context: Context): MusicSearchDatabase {
//        return Room.databaseBuilder(context, MusicSearchRoomDatabase::class.java, DATABASE_NAME)
//            .addMigrations(MIGRATION_7_8)
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//}

val databaseModule = module {
    single<MusicSearchDatabase> {
        Room.databaseBuilder(get(), MusicSearchRoomDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_7_8)
            .fallbackToDestructiveMigration()
            .build()
    }
}
