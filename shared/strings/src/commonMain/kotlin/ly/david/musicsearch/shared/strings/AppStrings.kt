package ly.david.musicsearch.shared.strings

import cafe.adriel.lyricist.LanguageTag

data class AppStrings(
    val theme: String,
    val light: String,
    val dark: String,
    val system: String,
    val area: String,
    val artist: String,
    val event: String,
    val genre: String,
    val instrument: String,
    val label: String,
    val place: String,
    val recording: String,
    val release: String,
    val releaseGroup: String,
    val series: String,
    val work: String,
    val works: String,
    val url: String,
    val collection: String,
    val back: String,
    val openInBrowser: String,
    val refresh: String,
    val filter: String,
    val cancel: String,
    val clearFilter: String,
    val searchMusicbrainz: String,
    val search: String,
    val history: String,
    val collections: String,
    val settings: String,
    val resource: String,
    val clearSearch: String,
    val noResultsFound: String,
    val noResultsFoundSearch: String,
    val recentSearches: String,
    val clearSearchHistory: String,
    val deleteSearchHistoryConfirmation: String,
    val yes: String,
    val no: String,
    val recentHistory: String,
    val clearHistory: String,
    val deleteLookupHistoryConfirmation: String,
    val nowPlayingHistory: String,
    val nowPlayingHistorySubtitle: String,
    val enableNotificationListener: String,
    val enableNotificationListenerSubtitle: String,
    val labels: String,
    val details: String,
    val artists: String,
    val events: String,
    val places: String,
    val releaseGroups: String,
    val releases: String,
    val recordings: String,
    val relationships: String,
    val relationshipsReleases: String,
    val relationshipsRecordings: String,
    val tracks: String,
    val format: String,
    val stats: String,
    val barcode: String,
    val labelCode: String,
    val lc: (Int) -> String,
    val length: String,
    val type: String,
    val sortName: String,
    val gender: String,
    val opened: String,
    val closed: String,
    val created: String,
    val born: String,
    val died: String,
    val founded: String,
    val dissolved: String,
    val date: String,
    val firstReleaseDate: String,
    val startDate: String,
    val endDate: String,
    val time: String,
    val address: String,
    val packaging: String,
    val status: String,
    val language: String,
    val script: String,
    val dataQuality: String,
    val asin: String,
    val iswc: String,
    val isrc: String,
    val ipi: String,
    val isni: String,
    val iso31661: String,
    val releaseEvents: String,
    val informationHeader: (String) -> String,
    val attributesHeader: (String) -> String,
    val additionalDetails: String,
    val multipleScripts: String,
    val coordinates: String,
    val cancelled: String,
    val description: String,
    val appVersion: String,
    val ok: String,
    val retry: String,
    val cachedArtists: (Int, Int) -> String,
    val cachedEvents: (Int, Int) -> String,
    val cachedLabels: (Int, Int) -> String,
    val cachedPlaces: (Int, Int) -> String,
    val cachedRecordings: (Int, Int) -> String,
    val cachedReleases: (Int, Int) -> String,
    val cachedReleaseGroups: (Int, Int) -> String,
    val cachedWorks: (Int, Int) -> String,
    val moreActions: String,
    val openGoogleMaps: String,
    val sort: String,
    val unsort: String,
    val showMoreInfo: String,
    val showLessInfo: String,
    val createCollection: String,
    val name: String,
    val collectionNamePlaceholder: String,
    val addToCollection: String,
    val about: String,
    val experimentalSearch: String,
    val openSourceLicenses: String,
    val spotify: String,
    val spotifySubtitle: String,
    val spotifyTutorial: String,
    val spotifyHistory: String,
    val searchX: (String) -> String,
    val searchXByX: (String, String) -> String,
    val alphabetically: String,
    val alphabeticallyReverse: String,
    val recentlyVisited: String,
    val leastRecentlyVisited: String,
    val mostVisited: String,
    val leastVisited: String,
    val loginToMusicBrainz: String,
    val artificialLanguage: String,
    val mostEntities: String,
    val leastEntities: String,
    val seeCollaborators: String,
    val collaborationsWith: (String) -> String,
    val wikipedia: String,
)

object Locales {
    const val EN = "en"
}

val Strings: Map<LanguageTag, AppStrings> = mapOf(
    Locales.EN to EnStrings,
)

expect fun String.fmt(vararg args: Any?): String