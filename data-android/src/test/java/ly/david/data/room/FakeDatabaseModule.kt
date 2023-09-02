package ly.david.data.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.di.room.DatabaseModule

// Duplicated here and in app but it's okay because trying to inject this upstream just makes a mess of AS modules
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
internal object FakeDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MusicSearchDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            MusicSearchRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }
}
