package ly.david.mbjc.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.mbjc.data.persistence.Migrations.ADD_FK_TO_RELEASES_RELEASE_GROUPS
import ly.david.mbjc.data.persistence.Migrations.MIGRATION_10_11
import ly.david.mbjc.data.persistence.Migrations.MIGRATION_29_30
import ly.david.mbjc.data.persistence.Migrations.MIGRATION_32_33
import ly.david.mbjc.data.persistence.Migrations.MIGRATION_34_35
import ly.david.mbjc.data.persistence.Migrations.MIGRATION_36_37
import ly.david.mbjc.data.persistence.Migrations.MOVE_COVER_ART_URL_TO_RELEASES
import ly.david.mbjc.data.persistence.MusicBrainzDatabase
import ly.david.mbjc.data.persistence.MusicBrainzRoomDatabase

private const val DATABASE_NAME = "mbjc.db"

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MusicBrainzDatabase {
        return Room.databaseBuilder(context, MusicBrainzRoomDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_10_11)
            .addMigrations(MIGRATION_29_30)
            .addMigrations(MIGRATION_32_33)
            .addMigrations(MIGRATION_34_35)
            .addMigrations(MIGRATION_36_37)
            .addMigrations(MOVE_COVER_ART_URL_TO_RELEASES)
            .addMigrations(ADD_FK_TO_RELEASES_RELEASE_GROUPS)
            .fallbackToDestructiveMigration()
            .build()
    }
}
