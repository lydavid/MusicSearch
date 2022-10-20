package ly.david.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.artist.ArtistDao

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseDaoModule {
    @Provides
    fun provideArtistDao(db: MusicBrainzDatabase): ArtistDao = db.getArtistDao()

    @Provides
    fun provideReleaseGroupArtistDao(db: MusicBrainzDatabase) = db.getReleaseGroupArtistDao()

    @Provides
    fun provideReleaseGroupDao(db: MusicBrainzDatabase) = db.getReleaseGroupDao()

    @Provides
    fun provideReleasesReleaseGroupsDao(db: MusicBrainzDatabase) = db.getReleasesReleaseGroupsDao()

    @Provides
    fun provideReleaseDao(db: MusicBrainzDatabase) = db.getReleaseDao()

    @Provides
    fun provideMediumDao(db: MusicBrainzDatabase) = db.getMediumDao()

    @Provides
    fun provideTrackDao(db: MusicBrainzDatabase) = db.getTrackDao()

    @Provides
    fun provideRecordingDao(db: MusicBrainzDatabase) = db.getRecordingDao()

    @Provides
    fun provideWorkDao(db: MusicBrainzDatabase) = db.getWorkDao()

    @Provides
    fun provideAreaDao(db: MusicBrainzDatabase) = db.getAreaDao()

    @Provides
    fun provideReleasesCountriesDao(db: MusicBrainzDatabase) = db.getReleasesCountriesDao()

    @Provides
    fun providePlaceDao(db: MusicBrainzDatabase) = db.getPlaceDao()

    @Provides
    fun provideInstrumentDao(db: MusicBrainzDatabase) = db.getInstrumentDao()

    @Provides
    fun provideLabelDao(db: MusicBrainzDatabase) = db.getLabelDao()

    @Provides
    fun provideReleasesLabelsDao(db: MusicBrainzDatabase) = db.getReleasesLabelsDao()

    @Provides
    fun provideEventDao(db: MusicBrainzDatabase) = db.getEventDao()

    @Provides
    fun provideRelationDao(db: MusicBrainzDatabase) = db.getRelationDao()

    @Provides
    fun provideLookupHistoryDao(db: MusicBrainzDatabase) = db.getLookupHistoryDao()
}
