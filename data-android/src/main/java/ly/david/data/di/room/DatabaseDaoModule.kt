package ly.david.data.di.room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ly.david.data.core.image.ImageUrlSaver
import ly.david.data.room.MusicSearchDatabase

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseDaoModule {
    @Provides
    fun provideArtistDao(db: MusicSearchDatabase) = db.getArtistDao()

    @Provides
    fun provideArtistReleaseDao(db: MusicSearchDatabase) = db.getArtistReleaseDao()

    @Provides
    fun provideArtistReleaseGroupDao(db: MusicSearchDatabase) = db.getArtistReleaseGroupDao()

    @Provides
    fun provideReleaseGroupDao(db: MusicSearchDatabase) = db.getReleaseGroupDao()

    @Provides
    fun provideReleaseReleaseGroupDao(db: MusicSearchDatabase) = db.getReleaseReleaseGroupDao()

    @Provides
    fun provideReleaseDao(db: MusicSearchDatabase) = db.getReleaseDao()

    @Provides
    fun provideMediumDao(db: MusicSearchDatabase) = db.getMediumDao()

    @Provides
    fun provideTrackDao(db: MusicSearchDatabase) = db.getTrackDao()

    @Provides
    fun provideRecordingDao(db: MusicSearchDatabase) = db.getRecordingDao()

    @Provides
    fun provideRecordingReleaseDao(db: MusicSearchDatabase) = db.getRecordingReleaseDao()

    @Provides
    fun provideWorkDao(db: MusicSearchDatabase) = db.getWorkDao()

    @Provides
    fun provideRecordingWorkDao(db: MusicSearchDatabase) = db.getRecordingWorkDao()

    @Provides
    fun provideAreaDao(db: MusicSearchDatabase) = db.getAreaDao()

    @Provides
    fun provideAreaPlaceDao(db: MusicSearchDatabase) = db.getAreaPlaceDao()

    @Provides
    fun provideEventPlaceDao(db: MusicSearchDatabase) = db.getEventPlaceDao()

    @Provides
    fun provideReleaseCountryDao(db: MusicSearchDatabase) = db.getReleaseCountryDao()

    @Provides
    fun providePlaceDao(db: MusicSearchDatabase) = db.getPlaceDao()

    @Provides
    fun provideInstrumentDao(db: MusicSearchDatabase) = db.getInstrumentDao()

    @Provides
    fun provideLabelDao(db: MusicSearchDatabase) = db.getLabelDao()

    @Provides
    fun provideReleaseLabelDao(db: MusicSearchDatabase) = db.getReleaseLabelDao()

    @Provides
    fun provideEventDao(db: MusicSearchDatabase) = db.getEventDao()

    @Provides
    fun provideSeriesDao(db: MusicSearchDatabase) = db.getSeriesDao()

    @Provides
    fun provideRelationDao(db: MusicSearchDatabase) = db.getRelationDao()

    @Provides
    fun provideLookupHistoryDao(db: MusicSearchDatabase) = db.getLookupHistoryDao()

    @Provides
    fun provideSearchHistoryDao(db: MusicSearchDatabase) = db.getSearchHistoryDao()

    @Provides
    fun provideNowPlayingHistoryDao(db: MusicSearchDatabase) = db.getNowPlayingHistoryDao()

    @Provides
    fun provideCollectionDao(db: MusicSearchDatabase) = db.getCollectionDao()

    @Provides
    fun provideCollectionEntityDao(db: MusicSearchDatabase) = db.getCollectionEntityDao()

    @Provides
    fun provideImageUrlSaver(db: MusicSearchDatabase): ImageUrlSaver = db.getMbidImageDao()
}
