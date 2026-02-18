package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.alias.AliasType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.areaName
import musicsearch.ui.common.generated.resources.artistName
import musicsearch.ui.common.generated.resources.brandName
import musicsearch.ui.common.generated.resources.eventName
import musicsearch.ui.common.generated.resources.formalName
import musicsearch.ui.common.generated.resources.instrumentName
import musicsearch.ui.common.generated.resources.labelName
import musicsearch.ui.common.generated.resources.legalName
import musicsearch.ui.common.generated.resources.placeName
import musicsearch.ui.common.generated.resources.recordingName
import musicsearch.ui.common.generated.resources.releaseGroupName
import musicsearch.ui.common.generated.resources.releaseName
import musicsearch.ui.common.generated.resources.searchHint
import musicsearch.ui.common.generated.resources.seriesName
import musicsearch.ui.common.generated.resources.workName
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AliasType.getDisplayString(): String {
    return stringResource(
        when (this) {
            AliasType.AREA_NAME -> Res.string.areaName
            AliasType.AREA_FORMAL_NAME -> Res.string.formalName
            AliasType.ARTIST_NAME -> Res.string.artistName
            AliasType.ARTIST_LEGAL_NAME -> Res.string.legalName
            AliasType.EVENT_NAME -> Res.string.eventName
            AliasType.INSTRUMENT_NAME -> Res.string.instrumentName
            AliasType.INSTRUMENT_BRAND_NAME -> Res.string.brandName
            AliasType.LABEL_NAME -> Res.string.labelName
            AliasType.PLACE_NAME -> Res.string.placeName
            AliasType.RECORDING_NAME -> Res.string.recordingName
            AliasType.RELEASE_NAME -> Res.string.releaseName
            AliasType.RELEASE_GROUP_NAME -> Res.string.releaseGroupName
            AliasType.SERIES_NAME -> Res.string.seriesName
            AliasType.WORK_NAME -> Res.string.workName

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
            -> Res.string.searchHint
        },
    )
}
