package ly.david.mbjc.ui.release.details

import android.icu.lang.UScript
import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale
import ly.david.data.AreaType.COUNTRY
import ly.david.data.AreaType.WORLDWIDE
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.AreaCardModel
import ly.david.data.domain.LabelCardModel
import ly.david.data.domain.ReleaseScaffoldModel
import ly.david.data.getDisplayTypes
import ly.david.data.network.TextRepresentation
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaCard
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.label.LabelCard
import ly.david.mbjc.ui.theme.PreviewTheme

// TODO: it lags when navigating to this tab
//  consider loading ReleaseWithAllData only when we navigate here
@Composable
internal fun ReleaseDetailsScreen(
    releaseScaffoldModel: ReleaseScaffoldModel,
    onLabelClick: LabelCardModel.() -> Unit = {},
    onAreaClick: AreaCardModel.() -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState(),
    viewModel: ReleaseDetailsViewModel = hiltViewModel()
) {
    // TODO: get this from ui model, then we can save scroll state
    var releaseLength: String? by rememberSaveable { mutableStateOf(null) }

    LaunchedEffect(key1 = releaseScaffoldModel) {
        releaseLength = viewModel.getFormattedReleaseLength(releaseScaffoldModel.id)
    }

    ReleaseDetailsScreen(
        release = releaseScaffoldModel,
        onLabelClick = onLabelClick,
        onAreaClick = onAreaClick,
        lazyListState = lazyListState,
        releaseLength = releaseLength
    )
}

@Composable
private fun ReleaseDetailsScreen(
    release: ReleaseScaffoldModel,
    onLabelClick: LabelCardModel.() -> Unit = {},
    onAreaClick: AreaCardModel.() -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState(),
    releaseLength: String? = null,
) {
    LazyColumn(state = lazyListState) {
        item {
            release.run {
                ListSeparatorHeader(text = stringResource(id = R.string.release_information))
                barcode?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.barcode, text = it)
                }
                formattedFormats?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.format, text = it)
                }
                formattedTracks?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.tracks, text = it)
                }
                releaseLength?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.length, text = it)
                }

                ListSeparatorHeader(text = stringResource(id = R.string.additional_details))
                releaseGroup?.let {
                    TextWithHeading(headingRes = R.string.type, text = it.getDisplayTypes())
                }
                packaging?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.packaging, text = it)
                }
                status?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.status, text = it)
                }
                textRepresentation?.language?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.language, text = Locale(it).displayLanguage)
                }
                textRepresentation?.script?.ifNotNullOrEmpty {
                    val scriptOrCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        // TODO: Works for Latn but not Jpan or Kore
                        //  let's just map the most common codes to their name stored in strings.xml
                        //  then fallback to this for everything else
                        UScript.getName(UScript.getCodeFromName(it))
                    } else {
                        it
                    }
                    TextWithHeading(
                        headingRes = R.string.script,
                        text = scriptOrCode
                    )
                }
                quality?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.data_quality, text = it)
                }
                asin?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.asin, text = it)
                }

                labels.ifNotNullOrEmpty {
                    ListSeparatorHeader(text = stringResource(id = R.string.labels))
                    it.forEach { label ->
                        LabelCard(label = label, onLabelClick = onLabelClick)
                    }
                }

                if (areas.isNotEmpty()) {
                    ListSeparatorHeader(text = stringResource(id = R.string.release_events))
                }
                areas.forEach { item: AreaCardModel ->
                    AreaCard(
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
                        AreaCardModel(
                            id = "a1",
                            name = "Canada",
                            type = COUNTRY,
                            iso_3166_1_codes = listOf("CA"),
                            date = "2022-11-29"
                        ),
                        AreaCardModel(
                            id = "a2",
                            name = WORLDWIDE,
                            iso_3166_1_codes = listOf("XW"),
                            date = "2022-11-30"
                        )
                    ),
                    labels = listOf(
                        LabelCardModel(
                            id = "l1",
                            name = "Label 1",
                            type = "Imprint",
                            catalogNumber = "ASDF-1010"
                        ),
                        LabelCardModel(
                            id = "l1",
                            name = "Label 1",
                            catalogNumber = "ASDF-1011"
                        )
                    )
                ),
                releaseLength = null
            )
        }
    }
}
// endregion
