package ly.david.data.room

import androidx.room.Room
import org.koin.dsl.module

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
