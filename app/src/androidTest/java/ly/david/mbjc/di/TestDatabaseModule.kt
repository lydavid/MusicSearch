package ly.david.mbjc.di

import androidx.room.Room
import ly.david.data.room.MusicSearchDatabase
import ly.david.data.room.MusicSearchRoomDatabase
import org.koin.dsl.module

// TODO: dupe
val testDatabaseModule = module {
    single<MusicSearchDatabase> {
        Room.inMemoryDatabaseBuilder(
            get(),
            MusicSearchRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }
}
