package ly.david.data.coverart

internal const val COVER_ART_ARCHIVE_BASE_URL = "https://coverartarchive.org/"

// TODO: for now, we will use request for large thumbnail instead of full-size since some of them might be MBs
//  might be better user experience to allow them to choose what resolution to download, otherwise just show 500
internal const val SMALL_SUFFIX = "-250"
internal const val LARGE_SUFFIX = "-500"
