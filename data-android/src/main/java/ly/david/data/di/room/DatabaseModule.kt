package ly.david.data.di.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.room.Migrations.MIGRATION_7_8
import ly.david.data.room.MusicSearchDatabase
import ly.david.data.room.MusicSearchRoomDatabase

private const val DATABASE_NAME = "mbjc.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MusicSearchDatabase {
        return Room.databaseBuilder(context, MusicSearchRoomDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_7_8)
            .fallbackToDestructiveMigration()
            .build()
    }
}
