package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.list.AllEntitiesListUiState
import ly.david.musicsearch.ui.common.list.EntitiesListUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

private val canadianArtists = MutableStateFlow(
    PagingData.from(
        data = listOf(
            ArtistListItemModel(
                id = "celine_dion",
                name = "Céline Dion",
                sortName = "Dion, Céline",
                type = "Person",
                gender = "Female",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(begin = "1968-03-30"),
            ),
            ArtistListItemModel(
                id = "rush",
                name = "Rush",
                sortName = "Rush",
                type = "Group",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(
                    begin = "1968-08",
                    end = "2018-01-19",
                ),
            ),
            ArtistListItemModel(
                id = "neil_young",
                name = "Neil Young",
                sortName = "Young, Neil",
                type = "Person",
                gender = "Male",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(begin = "1945-11-12"),
            ),
            ArtistListItemModel(
                id = "joni_mitchell",
                name = "Joni Mitchell",
                sortName = "Mitchell, Joni",
                type = "Person",
                gender = "Female",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(begin = "1943-11-07"),
            ),
            ArtistListItemModel(
                id = "c8b03190-306c-4120-bb0b-6f2ebfc06ea9",
                name = "The Weeknd",
                sortName = "Weeknd, The",
                disambiguation = "Canadian R&B singer",
                type = "Person",
                gender = "Male",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(begin = "1990-02-16"),
            ),
            ArtistListItemModel(
                id = "shania_twain",
                name = "Shania Twain",
                sortName = "Twain, Shania",
                type = "Person",
                gender = "Female",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(begin = "1965-08-28"),
            ),
            ArtistListItemModel(
                id = "52074ba6-e495-4ef3-9bb4-0703888a9f68",
                name = "Arcade Fire",
                sortName = "Arcade Fire",
                type = "Group",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(begin = "2001"),
            ),
            ArtistListItemModel(
                id = "leonard_cohen",
                name = "Leonard Cohen",
                sortName = "Cohen, Leonard",
                disambiguation = "Canadian singer‐songwriter and poet",
                type = "Person",
                gender = "Male",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(
                    begin = "1934-09-21",
                    end = "2016-11-07",
                ),
            ),
            ArtistListItemModel(
                id = "avril_lavigne",
                name = "Avril Lavigne",
                sortName = "Lavigne, Avril",
                type = "Person",
                gender = "Female",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(begin = "1984-09-27"),
            ),
            ArtistListItemModel(
                id = "the_tragically_hip",
                name = "The Tragically Hip",
                sortName = "Tragically Hip, The",
                type = "Group",
                countryCode = "CA",
                lifeSpan = LifeSpanUiModel(
                    begin = "1983",
                    end = "2016-08-20",
                ),
            ),
            LastUpdatedFooter(
                lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
            ),
        ),
    ),
)

internal val events = MutableStateFlow(
    PagingData.from(
        data = listOf(
            EventListItemModel(
                id = "stratford_1956",
                name = "Stratford Shakespearean Festival 1956",
                type = "Festival",
                lifeSpan = LifeSpanUiModel(
                    begin = "1956",
                ),
            ),
            EventListItemModel(
                id = "expo_67",
                name = "Expo 67",
                type = "Convention/Expo",
                lifeSpan = LifeSpanUiModel(
                    begin = "1967-04-27",
                    end = "1967-10-29",
                ),
            ),
            EventListItemModel(
                id = "toronto_rock_revival_1969",
                name = "Toronto Rock and Roll Revival",
                type = "Festival",
                lifeSpan = LifeSpanUiModel(
                    begin = "1969-09-13",
                ),
            ),
            EventListItemModel(
                id = "transcontinental_pop_festival_1970",
                name = "Transcontinental Pop Festival 1970",
                type = "Festival",
                lifeSpan = LifeSpanUiModel(
                    begin = "1970-06-28",
                    end = "1970-07-05",
                ),
            ),
            EventListItemModel(
                id = "alice_cooper_montreal_1972",
                name = "Alice Cooper at Centre sportif de l'Université de Montréal",
                type = "Concert",
                lifeSpan = LifeSpanUiModel(
                    begin = "1972-01-13",
                ),
            ),
            EventListItemModel(
                id = "world_saxophone_congress_1972",
                name = "3rd World Saxophone Congress",
                type = "Festival",
                lifeSpan = LifeSpanUiModel(
                    begin = "1972-08",
                ),
            ),
            EventListItemModel(
                id = "bette_midler_massey_hall_1973",
                name = "Bette Midler at Massey Hall",
                type = "Concert",
                lifeSpan = LifeSpanUiModel(
                    begin = "1973-02-26",
                ),
            ),
            EventListItemModel(
                id = "b7c3f330-4fa8-4355-95de-af6e7c5d20b9",
                name = "1973-03-06: Vancouver Gardens, Vancouver, BC, Canada",
                type = "Concert",
                lifeSpan = LifeSpanUiModel(
                    begin = "1973-03-06",
                ),
                cancelled = true,
            ),
            LastUpdatedFooter(
                lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
            ),
        ),
    ),
)

internal val labels = MutableStateFlow(
    PagingData.from(
        data = listOf(
            LabelListItemModel(
                id = "4e_regiment",
                name = "4e Régiment",
            ),
            LabelListItemModel(
                id = "awesome_records",
                name = "Awesome Records",
                disambiguation = "Sub-label of Awesome Music, Inc.",
            ),
            LabelListItemModel(
                id = "banyan",
                name = "Banyan",
                type = "Distributor",
            ),
            LabelListItemModel(
                id = "aporia_records",
                name = "Aporia Records",
                type = "Original Production",
            ),
            LabelListItemModel(
                id = "aquarius_2000",
                name = "Aquarius 2000",
                type = "Holding",
            ),
            LabelListItemModel(
                id = "bassdrop_ca",
                name = "bassdrop.ca",
            ),
            LabelListItemModel(
                id = "604_records",
                name = "604 Records",
            ),
            LabelListItemModel(
                id = "artisti",
                name = "Artisti",
                disambiguation = "Canadian rights society",
                type = "Rights Society",
            ),
            LabelListItemModel(
                id = "audio_research_records",
                name = "Audio Research Records",
                type = "Original Production",
            ),
            LabelListItemModel(
                id = "amp_records",
                name = "AMP Records",
                disambiguation = "Canadian punk label",
            ),
            LabelListItemModel(
                id = "arbutus_records",
                name = "Arbutus Records",
                type = "Publisher",
            ),
            LabelListItemModel(
                id = "audira_music",
                name = "Audira Music",
                type = "Publisher",
            ),
            LastUpdatedFooter(
                lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
            ),
        ),
    ),
)

internal val places = MutableStateFlow(
    PagingData.from(
        data = listOf(
            PlaceListItemModel(
                id = "101_5_whistler_fm",
                name = "101.5 Whistler FM",
                type = "Studio",
                address = "102 - 1080 Millar Creek Road. V8E 0S7",
            ),
            PlaceListItemModel(
                id = "14_blue_records",
                name = "14 Blue Records",
                address = "", // Address not provided in the image
            ),
            PlaceListItemModel(
                id = "299_queen_street_west",
                name = "299 Queen Street West",
                type = "Studio",
                address = "299 Queen Street West, Toronto, ON",
                lifeSpan = LifeSpanUiModel(begin = "1913"),
            ),
            PlaceListItemModel(
                id = "4_walls",
                name = "4 Walls",
                type = "Studio",
                address = "", // Address not provided in the image
            ),
            PlaceListItemModel(
                id = "6_nassau",
                name = "6 Nassau",
                type = "Studio",
                address = "6 Nassau St, Kensington Market, Toronto, ON, Canada M5T 1M2",
                lifeSpan = LifeSpanUiModel(
                    begin = "2009-02",
                    end = "2016-06-30",
                ),
            ),
            PlaceListItemModel(
                id = "612_collective",
                name = "612 Collective",
                type = "Venue",
                address = "", // Address not provided in the image
            ),
            PlaceListItemModel(
                id = "669_studios",
                name = "669 Studios",
                type = "Studio",
                address = "", // Address not provided in the image
            ),
            PlaceListItemModel(
                id = "86th_st_music_hall",
                name = "86th St. Music Hall",
                disambiguation = "Post Expo86 venue",
                type = "Venue",
                address = "750 Pacific Blvd.",
                lifeSpan = LifeSpanUiModel(begin = "1986"),
            ),
            LastUpdatedFooter(
                lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
            ),
        ),
    ),
)

private val canadianReleases = MutableStateFlow(
    PagingData.from(
        data = listOf(
            ReleaseListItemModel(
                id = "rush_moving_pictures",
                name = "Moving Pictures",
                date = "1981-02-12",
                countryCode = "CA",
                formattedArtistCredits = "Rush",
            ),
            ReleaseListItemModel(
                id = "celine_dion_falling_into_you",
                name = "Falling into You",
                date = "1996",
                countryCode = "CA",
                formattedArtistCredits = "Céline Dion",
            ),
            ReleaseListItemModel(
                id = "alanis_morissette_jagged_little_pill",
                name = "Jagged Little Pill",
                date = "1995-06-13",
                countryCode = "CA",
                formattedArtistCredits = "Alanis Morissette",
            ),
            ReleaseListItemModel(
                id = "the_tragically_hip_fully_completely",
                name = "Fully Completely",
                date = "1992",
                countryCode = "CA",
                formattedArtistCredits = "The Tragically Hip",
            ),
            ReleaseListItemModel(
                id = "shania_twain_come_on_over",
                name = "Come On Over",
                disambiguation = "original version, club edition",
                date = "1997",
                countryCode = "CA",
                formattedArtistCredits = "Shania Twain",
            ),
            ReleaseListItemModel(
                id = "arcade_fire_funeral",
                name = "Funeral",
                date = "2004-09-14",
                countryCode = "CA",
                formattedArtistCredits = "Arcade Fire",
            ),
            ReleaseListItemModel(
                id = "neil_young_harvest",
                name = "Harvest",
                date = "1984",
                countryCode = "CA",
                formattedArtistCredits = "Neil Young",
            ),
            LastUpdatedFooter(
                lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
            ),
        ),
    ),
)

private val canadianRelations: MutableStateFlow<PagingData<ListItemModel>> = MutableStateFlow(
    PagingData.from(
        data = listOf(
            RelationListItemModel(
                id = "1",
                linkedEntityId = "11e1b699-4e38-49b0-bb24-5092e0f8f4ad",
                linkedEntity = MusicBrainzEntityType.AREA,
                type = "parts",
                name = "Alberta",
            ),
            RelationListItemModel(
                id = "2",
                linkedEntityId = "659cef61-3f19-4435-aae5-360183a42d6c",
                linkedEntity = MusicBrainzEntityType.EVENT,
                type = "held events",
                name = "BadBitz: Surprise",
            ),
            RelationListItemModel(
                id = "3",
                linkedEntityId = "340e3fe3-bb82-467d-9d10-ea2ebbfc8350",
                linkedEntity = MusicBrainzEntityType.GENRE,
                type = "genres",
                name = "cape breton fiddling",
            ),
            RelationListItemModel(
                id = "4",
                linkedEntityId = "7aa50be7-d31a-4e89-9fb2-a32ad290b255",
                linkedEntity = MusicBrainzEntityType.INSTRUMENT,
                type = "instruments",
                name = "qilaut",
            ),
            RelationListItemModel(
                id = "5",
                linkedEntityId = "7aa50be7-d31a-4e89-9fb2-a32ad290b255",
                linkedEntity = MusicBrainzEntityType.RECORDING,
                type = "engineered in",
                name = "Prélude de Sarah / Sarah",
                disambiguation = "live, 1976-04-18: Radio Canada",
            ),
            RelationListItemModel(
                id = "6",
                linkedEntityId = "9cd48072-3b45-4c1f-8af0-fb443582f371",
                linkedEntity = MusicBrainzEntityType.RELEASE,
                type = "manufacturing location for",
                name = "National Melodies",
            ),
            RelationListItemModel(
                id = "7",
                linkedEntityId = "fb4789c6-bfe7-43aa-ae9b-83e14bc7707b",
                linkedEntity = MusicBrainzEntityType.SERIES,
                type = "location for",
                name = "Reckoning Night World Tour",
            ),
            RelationListItemModel(
                id = "8",
                linkedEntityId = "3076d301-eacc-4316-95d2-5b6cda30928c",
                linkedEntity = MusicBrainzEntityType.WORK,
                type = "anthem",
                name = "O Canada",
                disambiguation = "English version by Robert Stanley Weir",
            ),
        ),
    ),
)

private val country = AreaDetailsModel(
    id = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
    name = "Canada",
    type = "Country",
    countryCode = "CA",
    lastUpdated = Instant.parse("2024-06-05T19:42:20Z"),
    urls = persistentListOf(
        RelationListItemModel(
            id = "1",
            linkedEntity = MusicBrainzEntityType.URL,
            linkedEntityId = "82973788-ebfc-46b5-ae59-bc8dcb5a67b9",
            type = "Geonames",
            name = "http://sws.geonames.org/6251999/",
        ),
        RelationListItemModel(
            id = "2",
            linkedEntity = MusicBrainzEntityType.URL,
            linkedEntityId = "2f21890b-48d9-4449-a883-18752b3f4508",
            type = "Wikidata",
            name = "https://m.wikidata.org/wiki/Q16",
        ),
    ),
    wikipediaExtract = WikipediaExtract(
        extract = "Canada is a country in North America. Its ten provinces and three territories extend from " +
            "the Atlantic Ocean to the Pacific Ocean and northward into the Arctic Ocean, " +
            "making it the world's second-largest country by total area, with the world's longest coastline.",
        wikipediaUrl = "https://en.wikipedia.org/wiki/Canada",
    ),
)

private val detailsUiState = DetailsUiState(
    tabs = areaTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = country.copy(
        aliases = persistentListOf(
            BasicAlias(
                name = "Canada",
                locale = "en",
                isPrimary = true,
            ),
            BasicAlias(
                name = "Canada",
                locale = "fr",
                isPrimary = true,
            ),
            BasicAlias(
                name = "カナダ",
                locale = "ja",
                isPrimary = true,
            ),
        ),
    ),
    detailsTabUiState = DetailsTabUiState(
        now = Instant.parse("2025-06-05T20:42:20Z"),
    ),
    allEntitiesListUiState = AllEntitiesListUiState(
        artistsListUiState = EntitiesListUiState(
            pagingDataFlow = canadianArtists,
        ),
        eventsListUiState = EntitiesListUiState(
            pagingDataFlow = events,
        ),
        labelsListUiState = EntitiesListUiState(
            pagingDataFlow = labels,
        ),
        placesListUiState = EntitiesListUiState(
            pagingDataFlow = places,
        ),
        releasesListUiState = EntitiesListUiState(
            pagingDataFlow = canadianReleases,
        ),
        relationsUiState = RelationsUiState(
            pagingDataFlow = canadianRelations,
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewAreaDetails() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = detailsUiState.copy(
                detailsTabUiState = DetailsTabUiState(
                    totalUrls = 2,
                    totalAliases = 3,
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaDetailsCollapsed() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = detailsUiState.copy(
                detailsTabUiState = DetailsTabUiState(
                    isExternalLinksCollapsed = true,
                    totalUrls = 2,
                    totalAliases = 3,
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaDetailsError() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state =
            detailsUiState.copy(
                detailsTabUiState = DetailsTabUiState(
                    handledException = HandledException(
                        userMessage = "Network error. Check your internet connection.",
                        errorResolution = ErrorResolution.Retry,
                    ),
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaRelationships() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = DetailsUiState(
                tabs = areaTabs,
                selectedTab = Tab.RELATIONSHIPS,
                detailsModel = country,
                allEntitiesListUiState = AllEntitiesListUiState(
                    artistsListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianArtists,
                    ),
                    eventsListUiState = EntitiesListUiState(
                        pagingDataFlow = events,
                    ),
                    labelsListUiState = EntitiesListUiState(
                        pagingDataFlow = labels,
                    ),
                    placesListUiState = EntitiesListUiState(
                        pagingDataFlow = places,
                    ),
                    releasesListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianReleases,
                    ),
                    relationsUiState = RelationsUiState(
                        pagingDataFlow = canadianRelations,
                    ),
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaArtists() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = DetailsUiState(
                tabs = areaTabs,
                selectedTab = Tab.ARTISTS,
                detailsModel = country,
                allEntitiesListUiState = AllEntitiesListUiState(
                    artistsListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianArtists,
                    ),
                    eventsListUiState = EntitiesListUiState(
                        pagingDataFlow = events,
                    ),
                    labelsListUiState = EntitiesListUiState(
                        pagingDataFlow = labels,
                    ),
                    placesListUiState = EntitiesListUiState(
                        pagingDataFlow = places,
                    ),
                    releasesListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianReleases,
                    ),
                    relationsUiState = RelationsUiState(
                        pagingDataFlow = canadianRelations,
                    ),
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaEvents() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = DetailsUiState(
                tabs = areaTabs,
                selectedTab = Tab.EVENTS,
                detailsModel = country,
                allEntitiesListUiState = AllEntitiesListUiState(
                    artistsListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianArtists,
                    ),
                    eventsListUiState = EntitiesListUiState(
                        pagingDataFlow = events,
                    ),
                    labelsListUiState = EntitiesListUiState(
                        pagingDataFlow = labels,
                    ),
                    placesListUiState = EntitiesListUiState(
                        pagingDataFlow = places,
                    ),
                    releasesListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianReleases,
                    ),
                    relationsUiState = RelationsUiState(
                        pagingDataFlow = canadianRelations,
                    ),
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaLabels() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = DetailsUiState(
                tabs = areaTabs,
                selectedTab = Tab.LABELS,
                detailsModel = country,
                allEntitiesListUiState = AllEntitiesListUiState(
                    artistsListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianArtists,
                    ),
                    eventsListUiState = EntitiesListUiState(
                        pagingDataFlow = events,
                    ),
                    labelsListUiState = EntitiesListUiState(
                        pagingDataFlow = labels,
                    ),
                    placesListUiState = EntitiesListUiState(
                        pagingDataFlow = places,
                    ),
                    releasesListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianReleases,
                    ),
                    relationsUiState = RelationsUiState(
                        pagingDataFlow = canadianRelations,
                    ),
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaReleases() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = DetailsUiState(
                tabs = areaTabs,
                selectedTab = Tab.RELEASES,
                detailsModel = country,
                allEntitiesListUiState = AllEntitiesListUiState(
                    artistsListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianArtists,
                    ),
                    eventsListUiState = EntitiesListUiState(
                        pagingDataFlow = events,
                    ),
                    labelsListUiState = EntitiesListUiState(
                        pagingDataFlow = labels,
                    ),
                    placesListUiState = EntitiesListUiState(
                        pagingDataFlow = places,
                    ),
                    releasesListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianReleases,
                    ),
                    relationsUiState = RelationsUiState(
                        pagingDataFlow = canadianRelations,
                    ),
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaPlaces() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = DetailsUiState(
                tabs = areaTabs,
                selectedTab = Tab.PLACES,
                detailsModel = country,
                allEntitiesListUiState = AllEntitiesListUiState(
                    artistsListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianArtists,
                    ),
                    eventsListUiState = EntitiesListUiState(
                        pagingDataFlow = events,
                    ),
                    labelsListUiState = EntitiesListUiState(
                        pagingDataFlow = labels,
                    ),
                    placesListUiState = EntitiesListUiState(
                        pagingDataFlow = places,
                    ),
                    releasesListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianReleases,
                    ),
                    relationsUiState = RelationsUiState(
                        pagingDataFlow = canadianRelations,
                    ),
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}

// not previewable yet
@PreviewLightDark
@Composable
internal fun PreviewAreaStats() {
    PreviewWithSharedElementTransition {
        AreaUiInternal(
            state = DetailsUiState(
                tabs = areaTabs,
                selectedTab = Tab.STATS,
                detailsModel = country,
                allEntitiesListUiState = AllEntitiesListUiState(
                    artistsListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianArtists,
                    ),
                    eventsListUiState = EntitiesListUiState(
                        pagingDataFlow = events,
                    ),
                    labelsListUiState = EntitiesListUiState(
                        pagingDataFlow = labels,
                    ),
                    placesListUiState = EntitiesListUiState(
                        pagingDataFlow = places,
                    ),
                    releasesListUiState = EntitiesListUiState(
                        pagingDataFlow = canadianReleases,
                    ),
                    relationsUiState = RelationsUiState(
                        pagingDataFlow = canadianRelations,
                    ),
                ),
            ),
            entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            now = Instant.parse("2025-04-26T16:42:20Z"),
        )
    }
}
