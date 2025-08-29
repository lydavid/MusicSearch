package ly.david.musicsearch.shared.strings

val EnStrings = AppStrings(
    theme = "Theme",

    light = "Light",

    dark = "Dark",

    system = "System",

    area = "Area",

    artist = "Artist",

    event = "Event",

    genre = "Genre",

    instrument = "Instrument",

    label = "Label",

    place = "Place",

    recording = "Recording",

    release = "Release",

    releaseGroup = "Release Group",

    series = "Series",

    work = "Work",

    works = "Works",

    url = "URL",

    urls = "URLs",

    collection = "Collection",

    back = "Back",

    openInBrowser = "Open in browser",

    refresh = "Refresh",

    refreshXTab = { p0 -> "Refresh %s".fmt(p0) },

    filter = "Filter",

    cancel = "Cancel",

    clearFilter = "Clear filter text",

    searchMusicbrainz = "Search MusicBrainz",

    search = "Search",

    database = "Database",

    history = "History",

    images = "Images",

    collections = "Collections",

    settings = "Settings",

    resource = "Resource",

    clearSearch = "Clear search field",

    noResultsFound = "No results found.",

    noResultsFoundSearch = "No results found.\nTry refining your search query.",

    recentSearches = "Recent searches",

    clearSearchHistory = "Clear search history",

    deleteSearchHistoryConfirmation = "Clear your search history?",

    yes = "Yes",

    no = "No",

    recentHistory = "Recent History",

    clearHistory = "Clear history",

    deleteLookupHistoryConfirmation = "Clear your history?",

    nowPlayingHistory = "Now Playing History",

    nowPlayingHistorySubtitle = "Records tracks picked up by Now Playing",

    enableNotificationListener = "Enable notification listener",

    enableNotificationListenerSubtitle = "Allows the app to listen to Now Playing, " +
        "allowing you to quickly search the track title and artist",

    labels = "Labels",

    details = "Details",

    areas = "Areas",
    artists = "Artists",
    events = "Events",
    genres = "Genres",
    instruments = "Instruments",

    places = "Places",

    releaseGroups = "Release Groups",

    releases = "Releases",

    recordings = "Recordings",

    relationships = "Relationships",

    relationshipsReleases = "Relationships (Releases)",

    relationshipsRecordings = "Relationships (Recordings)",

    tracks = "Tracks",

    format = "Format",

    stats = "Stats",

    barcode = "Barcode",

    labelCode = "Label code",

    lc = { p0 ->
        "LC %d"
            .fmt(p0)
    },

    length = "Length",

    type = "Type",

    sortName = "Sort name",

    gender = "Gender",

    opened = "Opened",

    closed = "Closed",

    created = "Created",

    born = "Born",

    died = "Died",

    founded = "Founded",

    dissolved = "Dissolved",

    date = "Date",

    firstReleaseDate = "First release date",

    startDate = "Start Date",

    endDate = "End Date",

    time = "Time",

    address = "Address",

    packaging = "Packaging",

    status = "Status",

    language = "Language",

    script = "Script",

    dataQuality = "Data Quality",

    asin = "ASIN",

    iswc = "ISWC",

    isrc = "ISRC",

    ipi = "IPI code",

    isni = "ISNI code",

    iso31661 = "ISO 3166–1",

    regionalIndicatorSymbol = "Regional indicator symbol",

    releaseEvents = "Release events",

    informationHeader = { p0 ->
        "%s information"
            .fmt(p0)
    },

    attributesHeader = { p0 ->
        "%s attributes"
            .fmt(p0)
    },

    additionalDetails = "Additional details",

    multipleScripts = "Multiple scripts",

    coordinates = "Coordinates",

    cancelled = "Cancelled",

    description = "Description",

    appVersion = "App version",

    databaseVersion = "Database version",

    ok = "OK",

    retry = "Retry",

    cached = "Cached",

    visited = "Visited",

    collected = "Collected",

    moreActions = "More actions",

    openGoogleMaps = "Open in Google maps",

    sort = "Sort",

    unsort = "Un-sort",

    showMoreInfo = "Show more info",

    showLessInfo = "Show less info",

    createCollection = "Create a collection",

    name = "Name",

    collectionNamePlaceholder = "Enter name",

    addToCollection = "Add to collection",

    about = "About",

    experimentalSearch = "Experimental search methods",

    openSourceLicenses = "Open source licenses",

    spotify = "Spotify",

    spotifySubtitle = "Search the playing song\'s artist/album/track",

    spotifyTutorial = "Enable Device Broadcast Status in the Spotify app\'s settings " +
        "in order for MusicSearch to detect the playing song",

    spotifyHistory = "Spotify History",

    searchX = { p0 ->
        "Search: %s"
            .fmt(p0)
    },

    searchXByX = { p0, p1 ->
        "Search: %s by %s"
            .fmt(p0, p1)
    },

    alphabetically = "Alphabetically",

    alphabeticallyReverse = "Reverse alphabetically",

    recentlyVisited = "Recently visited",

    leastRecentlyVisited = "Least recently visited",

    mostVisited = "Most visited",

    leastVisited = "Least visited",

    recentlyAdded = "Recently added",

    leastRecentlyAdded = "Least recently added",

    loginToMusicBrainz = "Login to MusicBrainz",
    artificialLanguage = "[Artificial (Other)]",
    mostEntities = "Most entities",
    leastEntities = "Least entities",
    seeCollaborators = "See collaborators",
    collaborationsWith = { p0 ->
        "Collaborations with %s"
            .fmt(p0)
    },
    wikipedia = "Wikipedia",
    readMore = "Read more",

    numberOfImages = "Number of Images",

    lastUpdatedFromMusicBrainz = { p0, p1 ->
        "Last updated from MusicBrainz %1s (%2s)"
            .fmt(p0, p1)
    },
    justNow = "just now",
    secondsAgo = "seconds ago",
    minuteAgo = "minute ago",
    minutesAgo = "minutes ago",
    hourAgo = "hour ago",
    hoursAgo = "hours ago",
    yesterday = "yesterday",
    daysAgo = "days ago",
    weekAgo = "week ago",
    weeksAgo = "weeks ago",
    monthAgo = "month ago",
    monthsAgo = "months ago",
    yearAgo = "year ago",
    yearsAgo = "years ago",
    appearance = "Appearance",
    areaName = "Area name",
    artistName = "Artist name",
    eventName = "Event name",
    instrumentName = "Instrument name",
    labelName = "Label name",
    placeName = "Place name",
    recordingName = "Recording name",
    releaseName = "Release name",
    releaseGroupName = "Release group name",
    seriesName = "Series name",
    workName = "Work name",
    searchHint = "Search hint",
    legalName = "Legal name",
    formalName = "Formal name",
    brandName = "Brand name",
    primary = "Primary",
    listens = "Listens",
    xListens = { p0 -> "%s's Listens".fmt(p0) },
    username = "Username",
    enterUsername = "Enter username",
    changeUsername = "Change username",
    set = "Set",
    youAreLoggedIn = "You are logged in",
    youAreLoggedOut = "You are logged out",
    invalidToken = "Invalid token",
)
