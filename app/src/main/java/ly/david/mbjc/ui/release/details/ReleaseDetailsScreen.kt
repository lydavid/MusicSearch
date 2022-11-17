package ly.david.mbjc.ui.release.details

import android.icu.lang.UScript
import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.AreaUiModel
import ly.david.data.domain.LabelUiModel
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.toAreaUiModel
import ly.david.data.getDisplayTypes
import ly.david.data.persistence.area.AreaWithReleaseDate
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaCard
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.label.LabelCard

@Composable
internal fun ReleaseDetailsScreen(
    releaseUiModel: ReleaseUiModel?,
    onLabelClick: LabelUiModel.() -> Unit = {},
    onAreaClick: AreaUiModel.() -> Unit = {},
    lazyListState: LazyListState,
    viewModel: ReleaseDetailsViewModel = hiltViewModel()
) {

    var releaseLength: String? by rememberSaveable { mutableStateOf(null) }
    var areasWithReleaseDate: List<AreaWithReleaseDate> by rememberSaveable { mutableStateOf(listOf()) }

    LaunchedEffect(key1 = releaseUiModel) {
        if (releaseUiModel == null) return@LaunchedEffect
        releaseLength = viewModel.getFormattedReleaseLength(releaseUiModel.id)
        areasWithReleaseDate = viewModel.getAreasWithReleaseDate(releaseUiModel.id)
    }

    // TODO: scroll position not saved on tab change
    //  it is saved on config change at least
    LazyColumn(state = lazyListState) {
        item {
            releaseUiModel?.run {
                ListSeparatorHeader(text = stringResource(id = R.string.release_information))
                barcode?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.barcode, text = it)
                }
                formats?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.format, text = it)
                }
                tracks?.ifNotNullOrEmpty {
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
                if (areasWithReleaseDate.isEmpty()) {
                    date?.ifNotNullOrEmpty {
                        TextWithHeading(headingRes = R.string.release_events, text = it)
                    }
                }

                // TODO: query for these
                labels.ifNotNullOrEmpty {
                    ListSeparatorHeader(text = stringResource(id = R.string.labels))
                    it.forEach { label ->
                        LabelCard(label = label, onLabelClick = onLabelClick)
                    }
                }
            }

            if (areasWithReleaseDate.isNotEmpty()) {
                ListSeparatorHeader(text = stringResource(id = R.string.release_events))
            }
        }

        items(areasWithReleaseDate) { item: AreaWithReleaseDate ->
            AreaCard(
                area = item.toAreaUiModel(),
                showType = false,
                onAreaClick = onAreaClick
            )
        }
    }
}
