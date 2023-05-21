package ly.david.mbjc.ui.release.details

import android.icu.lang.UScript
import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import java.util.Locale
import ly.david.data.AreaType.COUNTRY
import ly.david.data.AreaType.WORLDWIDE
import ly.david.data.common.UNKNOWN_TIME
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.AreaListItemModel
import ly.david.data.domain.LabelListItemModel
import ly.david.data.domain.ReleaseScaffoldModel
import ly.david.data.getDisplayTypes
import ly.david.data.network.TextRepresentation
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaListItem
import ly.david.mbjc.ui.common.TextWithHeadingRes
import ly.david.mbjc.ui.common.coverart.BigCoverArt
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.label.LabelListItem
import ly.david.ui.common.theme.PreviewTheme

@Composable
internal fun ReleaseDetailsScreen(
    release: ReleaseScaffoldModel,
    modifier: Modifier = Modifier,
    coverArtUrl: String = "",
    onLabelClick: LabelListItemModel.() -> Unit = {},
    onAreaClick: AreaListItemModel.() -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            BigCoverArt(coverArtUrl = coverArtUrl)

            release.run {
                InformationListSeparatorHeader(R.string.release)
                barcode?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.barcode, text = it)
                }
                formattedFormats?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.format, text = it)
                }
                formattedTracks?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.tracks, text = it)
                }

                val releaseLength = releaseLength.toDisplayTime()
                val formattedReleaseLength = if (hasNullLength) {
                    if (releaseLength == UNKNOWN_TIME) UNKNOWN_TIME else "$releaseLength (+ $UNKNOWN_TIME)"
                } else {
                    releaseLength
                }
                TextWithHeadingRes(headingRes = R.string.length, text = formattedReleaseLength)

                date?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.date, text = it)
                }

                ListSeparatorHeader(text = stringResource(id = R.string.additional_details))
                releaseGroup?.let {
                    TextWithHeadingRes(headingRes = R.string.type, text = it.getDisplayTypes())
                }
                packaging?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.packaging, text = it)
                }
                status?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.status, text = it)
                }
                textRepresentation?.language?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.language, text = Locale(it).displayLanguage)
                }
                textRepresentation?.script?.ifNotNullOrEmpty { script ->
                    val scriptOrCode = if (script == "Qaaa") {
                        stringResource(id = R.string.multiple_scripts)
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        // TODO: Works for Latn but not Jpan or Kore
                        //  let's just map the most common codes to their name stored in strings.xml
                        //  then fallback to this for everything else
                        UScript.getName(UScript.getCodeFromName(script))
                    } else {
                        script
                    }
                    TextWithHeadingRes(
                        headingRes = R.string.script,
                        text = scriptOrCode
                    )
                }
                quality?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.data_quality, text = it)
                }
                asin?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.asin, text = it)
                }

                labels.ifNotNullOrEmpty {
                    ListSeparatorHeader(text = stringResource(id = R.string.labels))
                    it.forEach { label ->
                        LabelListItem(label = label, onLabelClick = onLabelClick)
                    }
                }

                if (areas.isNotEmpty()) {
                    ListSeparatorHeader(text = stringResource(id = R.string.release_events))
                }
                areas.forEach { item: AreaListItemModel ->
                    AreaListItem(
                        area = item,
                        showType = false,
                        onAreaClick = onAreaClick
                    )
                }
            }
        }
    }
}

// region Previews
@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            ReleaseDetailsScreen(
                release = ReleaseScaffoldModel(
                    id = "r1",
                    name = "Release",
                    date = "1000-10-10",
                    barcode = "123456789",
                    status = "Official",
                    countryCode = "CA",
                    packaging = "Box",
                    asin = "B12341234",
                    quality = "normal",
                    textRepresentation = TextRepresentation(
                        script = "Latn",
                        language = "eng"
                    ),
                    formattedFormats = "2xCD + Blu-ray",
                    formattedTracks = "15 + 8 + 24",
                    areas = listOf(
                        AreaListItemModel(
                            id = "a1",
                            name = "Canada",
                            type = COUNTRY,
                            countryCodes = listOf("CA"),
                            date = "2022-11-29"
                        ),
                        AreaListItemModel(
                            id = "a2",
                            name = WORLDWIDE,
                            countryCodes = listOf("XW"),
                            date = "2022-11-30"
                        )
                    ),
                    labels = listOf(
                        LabelListItemModel(
                            id = "l1",
                            name = "Label 1",
                            type = "Imprint",
                            catalogNumber = "ASDF-1010"
                        ),
                        LabelListItemModel(
                            id = "l1",
                            name = "Label 1",
                            catalogNumber = "ASDF-1011"
                        )
                    ),
                    releaseLength = 8000,
                    hasNullLength = true
                )
            )
        }
    }
}
// endregion
