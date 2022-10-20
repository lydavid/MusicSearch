package ly.david.mbjc.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.di.DatabaseModule
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.MusicBrainzRoomDatabase

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
internal object FakeDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MusicBrainzDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            MusicBrainzRoomDatabase::class.java
        ).build()
    }
}
