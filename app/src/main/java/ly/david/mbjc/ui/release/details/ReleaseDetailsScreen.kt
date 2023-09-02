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
import ly.david.data.core.AreaType.COUNTRY
import ly.david.data.core.AreaType.WORLDWIDE
import ly.david.data.core.common.UNKNOWN_TIME
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.core.common.toDisplayTime
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.listitem.LabelListItemModel
import ly.david.data.domain.release.ReleaseScaffoldModel
import ly.david.data.core.getDisplayTypes
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.core.network.TextRepresentationUiModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.ui.common.R
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.label.LabelListItem
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.image.LargeImage

@Composable
internal fun ReleaseDetailsScreen(
    release: ReleaseScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    coverArtUrl: String = "",
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    lazyListState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            LargeImage(
                url = coverArtUrl,
                mbid = release.id
            )

            release.run {
                InformationListSeparatorHeader(R.string.release)
                barcode?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.barcode,
                        text = it,
                        filterText = filterText,
                    )
                }
                formattedFormats?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.format,
                        text = it,
                        filterText = filterText,
                    )
                }
                formattedTracks?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.tracks,
                        text = it,
                        filterText = filterText,
                    )
                }

                val releaseLength = releaseLength.toDisplayTime()
                val formattedReleaseLength = if (hasNullLength) {
                    if (releaseLength == UNKNOWN_TIME) UNKNOWN_TIME else "$releaseLength (+ $UNKNOWN_TIME)"
                } else {
                    releaseLength
                }
                TextWithHeadingRes(
                    headingRes = R.string.length,
                    text = formattedReleaseLength,
                    filterText = filterText,
                )

                date?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.date,
                        text = it,
                        filterText = filterText,
                    )
                }

                ListSeparatorHeader(text = stringResource(id = R.string.additional_details))
                releaseGroup?.let {
                    TextWithHeadingRes(
                        headingRes = R.string.type,
                        text = it.getDisplayTypes(),
                        filterText = filterText,
                    )
                }
                packaging?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.packaging,
                        text = it,
                        filterText = filterText,
                    )
                }
                status?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.status,
                        text = it,
                        filterText = filterText,
                    )
                }
                textRepresentation?.language?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.language,
                        text = Locale(it).displayLanguage,
                        filterText = filterText,
                    )
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
                        text = scriptOrCode,
                        filterText = filterText,
                    )
                }
                quality?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.data_quality,
                        text = it,
                        filterText = filterText,
                    )
                }
                asin?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.asin,
                        text = it,
                        filterText = filterText,
                    )
                }

                labels.ifNotNullOrEmpty {
                    ListSeparatorHeader(text = stringResource(id = R.string.labels))
                }
                labels
                    .filter { it.getNameWithDisambiguation().contains(filterText) }
                    .forEach { label ->
                        LabelListItem(
                            label = label,
                            onLabelClick = {
                                onItemClick(MusicBrainzEntity.LABEL, id, name)
                            }
                        )
                    }

                areas.ifNotNullOrEmpty {
                    ListSeparatorHeader(text = stringResource(id = R.string.release_events))
                }
                areas
                    .filter { it.getNameWithDisambiguation().contains(filterText) }
                    .forEach { area: AreaListItemModel ->
                        AreaListItem(
                            area = area,
                            showType = false,
                            onAreaClick = {
                                onItemClick(MusicBrainzEntity.AREA, id, name)
                            }
                        )
                    }

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick
                )
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
                    textRepresentation = TextRepresentationUiModel(
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
                ),
                coverArtUrl = "https://i.scdn.co/image/ab6761610000f1786761852cd2852fceb64e8cd9"
            )
        }
    }
}
// endregion
