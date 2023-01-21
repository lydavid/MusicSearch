package ly.david.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.MusicBrainzRoomDatabase

// Duplicated here and in app but it's okay because trying to inject this upstream just makes a mess of AS modules
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
        )
            .allowMainThreadQueries()
            .build()
    }
}
