package ly.david.musicsearch.shared.domain.series

// search results does include type_id, but let's map with name anyway for simplicity
enum class SeriesType(val displayName: String) {
    ArtistAward("Artist award"),
    ArtistSeries("Artist series"),
    AwardCeremony("Award ceremony"),
    Catalogue("Catalogue"),
    EventSeries("Event series"),
    Festival("Festival"),
    Podcast("Podcast"),
    RecordingAward("Recording award"),
    RecordingSeries("Recording series"),
    ReleaseGroupAward("Release group award"),
    ReleaseGroupSeries("Release group series"),
    ReleaseSeries("Release series"),
    Residency("Residency"),
    Run("Run"),
    SeriesAward("Series award"),
    SeriesSeries("Series series"),
    Tour("Tour"),
    WorkAward("Work award"),
    WorkSeries("Work series"),
    ;

    companion object {
        private val NAME_MAP = entries.associateBy { it.displayName }

        fun fromName(name: String?): SeriesType? = NAME_MAP[name]
    }
}
