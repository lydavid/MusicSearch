package ly.david.data.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "mbjc.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MusicBrainzDatabase {
        return Room.databaseBuilder(context, MusicBrainzRoomDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
