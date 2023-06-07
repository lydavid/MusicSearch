package ly.david.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ly.david.data.room.MusicBrainzDatabase

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseDaoModule {
    @Provides
    fun provideArtistDao(db: MusicBrainzDatabase) = db.getArtistDao()

    @Provides
    fun provideArtistReleaseDao(db: MusicBrainzDatabase) = db.getArtistReleaseDao()

    @Provides
    fun provideArtistReleaseGroupDao(db: MusicBrainzDatabase) = db.getArtistReleaseGroupDao()

    @Provides
    fun provideReleaseGroupDao(db: MusicBrainzDatabase) = db.getReleaseGroupDao()

    @Provides
    fun provideReleaseReleaseGroupDao(db: MusicBrainzDatabase) = db.getReleaseReleaseGroupDao()

    @Provides
    fun provideReleaseDao(db: MusicBrainzDatabase) = db.getReleaseDao()

    @Provides
    fun provideMediumDao(db: MusicBrainzDatabase) = db.getMediumDao()

    @Provides
    fun provideTrackDao(db: MusicBrainzDatabase) = db.getTrackDao()

    @Provides
    fun provideRecordingDao(db: MusicBrainzDatabase) = db.getRecordingDao()

    @Provides
    fun provideRecordingReleaseDao(db: MusicBrainzDatabase) = db.getRecordingReleaseDao()

    @Provides
    fun provideWorkDao(db: MusicBrainzDatabase) = db.getWorkDao()

    @Provides
    fun provideRecordingWorkDao(db: MusicBrainzDatabase) = db.getRecordingWorkDao()

    @Provides
    fun provideAreaDao(db: MusicBrainzDatabase) = db.getAreaDao()

    @Provides
    fun provideAreaPlaceDao(db: MusicBrainzDatabase) = db.getAreaPlaceDao()

    @Provides
    fun provideEventPlaceDao(db: MusicBrainzDatabase) = db.getEventPlaceDao()

    @Provides
    fun provideReleaseCountryDao(db: MusicBrainzDatabase) = db.getReleaseCountryDao()

    @Provides
    fun providePlaceDao(db: MusicBrainzDatabase) = db.getPlaceDao()

    @Provides
    fun provideInstrumentDao(db: MusicBrainzDatabase) = db.getInstrumentDao()

    @Provides
    fun provideLabelDao(db: MusicBrainzDatabase) = db.getLabelDao()

    @Provides
    fun provideReleaseLabelDao(db: MusicBrainzDatabase) = db.getReleaseLabelDao()

    @Provides
    fun provideEventDao(db: MusicBrainzDatabase) = db.getEventDao()

    @Provides
    fun provideSeriesDao(db: MusicBrainzDatabase) = db.getSeriesDao()

    @Provides
    fun provideRelationDao(db: MusicBrainzDatabase) = db.getRelationDao()

    @Provides
    fun provideLookupHistoryDao(db: MusicBrainzDatabase) = db.getLookupHistoryDao()

    @Provides
    fun provideCollectionDao(db: MusicBrainzDatabase) = db.getCollectionDao()

    @Provides
    fun provideCollectionEntityDao(db: MusicBrainzDatabase) = db.getCollectionEntityDao()
}
