package ly.david.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.persistence.Migrations.ADD_FK_TO_RELEASES_RELEASE_GROUPS
import ly.david.data.persistence.Migrations.ADD_UUID_TO_RELEASE_PATH_FOR_CONSISTENCY
import ly.david.data.persistence.Migrations.CHANGE_COLLECTION_PRIMARY_KEY_TO_UUID
import ly.david.data.persistence.Migrations.CHANGE_LOOKUP_COLLECTION_TO_RECORDING
import ly.david.data.persistence.Migrations.CHANGE_LOOKUP_HISTORY_PK
import ly.david.data.persistence.Migrations.MIGRATION_10_11
import ly.david.data.persistence.Migrations.MIGRATION_29_30
import ly.david.data.persistence.Migrations.MIGRATION_32_33
import ly.david.data.persistence.Migrations.MIGRATION_34_35
import ly.david.data.persistence.Migrations.MIGRATION_36_37
import ly.david.data.persistence.Migrations.MOVE_COVER_ART_URL_TO_RELEASES
import ly.david.data.persistence.Migrations.MOVE_RELEASE_GROUP_ID_OUT_OF_RELEASE
import ly.david.data.persistence.Migrations.REMOVE_LEADING_CAA_PATH
import ly.david.data.persistence.Migrations.REMOVE_LEADING_CAA_PATH_FOR_RELEASE_GROUP
import ly.david.data.persistence.Migrations.SET_RANDOM_UUID
import ly.david.data.persistence.Migrations.TRIM_250_JPG_FOR_RELEASE
import ly.david.data.persistence.Migrations.TRIM_250_JPG_FOR_RELEASE_GROUP
import ly.david.data.persistence.Migrations.UPDATE_IS_REMOTE
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.MusicBrainzRoomDatabase

private const val DATABASE_NAME = "mbjc.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
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
            .addMigrations(MOVE_RELEASE_GROUP_ID_OUT_OF_RELEASE)
            .addMigrations(REMOVE_LEADING_CAA_PATH)
            .addMigrations(REMOVE_LEADING_CAA_PATH_FOR_RELEASE_GROUP)
            .addMigrations(CHANGE_LOOKUP_HISTORY_PK)
            .addMigrations(TRIM_250_JPG_FOR_RELEASE_GROUP)
            .addMigrations(TRIM_250_JPG_FOR_RELEASE)
            .addMigrations(ADD_UUID_TO_RELEASE_PATH_FOR_CONSISTENCY)
            .addMigrations(UPDATE_IS_REMOTE)
            .addMigrations(SET_RANDOM_UUID)
            .addMigrations(CHANGE_COLLECTION_PRIMARY_KEY_TO_UUID)
            .addMigrations(CHANGE_LOOKUP_COLLECTION_TO_RECORDING)
            .fallbackToDestructiveMigration()
            .build()
    }
}
