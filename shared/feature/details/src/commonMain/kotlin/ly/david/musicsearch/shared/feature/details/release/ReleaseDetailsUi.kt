package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.common.UNKNOWN_TIME
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.common.toDisplayTime
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.release.ReleaseDetailsModel
import ly.david.musicsearch.core.models.releasegroup.getDisplayTypes
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.image.LargeImage
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.label.LabelListItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.UrlsSection
import ly.david.musicsearch.ui.common.work.getDisplayLanguage

@Composable
internal fun ReleaseDetailsUi(
    release: ReleaseDetailsModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    imageUrl: String = "",
    onImageClick: () -> Unit = {},
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            LargeImage(
                url = imageUrl,
                id = release.id,
                modifier = Modifier.clickable { onImageClick() },
            )

            release.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.release))
                barcode?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.barcode,
                        text = it,
                        filterText = filterText,
                    )
                }
                formattedFormats?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.format,
                        text = it,
                        filterText = filterText,
                    )
                }
                formattedTracks?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.tracks,
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
                TextWithHeading(
                    heading = strings.length,
                    text = formattedReleaseLength,
                    filterText = filterText,
                )

                date?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.date,
                        text = it,
                        filterText = filterText,
                    )
                }

                ListSeparatorHeader(strings.additionalDetails)
                releaseGroup?.let {
                    TextWithHeading(
                        heading = strings.type,
                        text = it.getDisplayTypes(),
                        filterText = filterText,
                    )
                }
                packaging?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.packaging,
                        text = it,
                        filterText = filterText,
                    )
                }
                status?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.status,
                        text = it,
                        filterText = filterText,
                    )
                }
                textRepresentation?.language?.getDisplayLanguage(strings).ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.language,
                        text = it,
                        filterText = filterText,
                    )
                }
                // TODO: handle script
//                textRepresentation?.script?.ifNotNullOrEmpty { script ->
//                    val scriptOrCode = if (script == "Qaaa") {
//                        strings.multipleScripts
//                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        // TODO: Works for Latn but not Jpan or Kore
//                        //  let's just map the most common codes to their name stored in strings.xml
//                        //  then fallback to this for everything else
//                        UScript.getName(UScript.getCodeFromName(script))
//                    } else {
//                        script
//                    }
//                    TextWithHeading(
//                        heading = strings.script,
//                        text = scriptOrCode,
//                        filterText = filterText,
//                    )
//                }
                quality?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.dataQuality,
                        text = it,
                        filterText = filterText,
                    )
                }
                asin?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.asin,
                        text = it,
                        filterText = filterText,
                    )
                }

                labels.ifNotNullOrEmpty {
                    ListSeparatorHeader(strings.labels)
                }
                labels
                    .filter { it.getNameWithDisambiguation().contains(filterText) }
                    .forEach { label ->
                        LabelListItem(
                            label = label,
                            onLabelClick = {
                                onItemClick(
                                    MusicBrainzEntity.LABEL,
                                    id,
                                    name,
                                )
                            },
                        )
                    }

                areas.ifNotNullOrEmpty {
                    ListSeparatorHeader(strings.releaseEvents)
                }
                areas
                    .filter { it.getNameWithDisambiguation().contains(filterText) }
                    .forEach { area: AreaListItemModel ->
                        AreaListItem(
                            area = area,
                            showType = false,
                            onAreaClick = {
                                onItemClick(
                                    MusicBrainzEntity.AREA,
                                    id,
                                    name,
                                )
                            },
                        )
                    }

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}
