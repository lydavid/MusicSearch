package ly.david.musicsearch.shared.feature.details.alias

import ly.david.musicsearch.shared.domain.alias.AliasType
import ly.david.musicsearch.shared.strings.AppStrings

internal fun AliasType.getDisplayString(strings: AppStrings): String? = when (this) {
    AliasType.AREA_NAME -> strings.areaName
    AliasType.AREA_FORMAL_NAME -> strings.formalName
    AliasType.ARTIST_NAME -> strings.artistName
    AliasType.ARTIST_LEGAL_NAME -> strings.legalName
    AliasType.EVENT_NAME -> strings.eventName
    AliasType.INSTRUMENT_NAME -> strings.instrumentName
    AliasType.INSTRUMENT_BRAND_NAME -> strings.brandName
    AliasType.LABEL_NAME -> strings.labelName
    AliasType.PLACE_NAME -> strings.placeName
    AliasType.RECORDING_NAME -> strings.recordingName
    AliasType.RELEASE_NAME -> strings.releaseName
    AliasType.RELEASE_GROUP_NAME -> strings.releaseGroupName
    AliasType.SERIES_NAME -> strings.seriesName
    AliasType.WORK_NAME -> strings.workName

    AliasType.AREA_SEARCH_HINT,
    AliasType.ARTIST_SEARCH_HINT,
    AliasType.EVENT_SEARCH_HINT,
    AliasType.INSTRUMENT_SEARCH_HINT,
    AliasType.LABEL_SEARCH_HINT,
    AliasType.PLACE_SEARCH_HINT,
    AliasType.RECORDING_SEARCH_HINT,
    AliasType.RELEASE_SEARCH_HINT,
    AliasType.RELEASE_GROUP_SEARCH_HINT,
    AliasType.SERIES_SEARCH_HINT,
    AliasType.WORK_SEARCH_HINT,
    -> strings.searchHint
}
