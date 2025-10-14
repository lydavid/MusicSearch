package ly.david.musicsearch.ui.common.release

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.StarFilled
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TINY_ICON_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun ReleaseListItem(
    release: ReleaseListItemModel,
    modifier: Modifier = Modifier,
    showMoreInfo: Boolean = true,
    requestForMissingCoverArtUrl: suspend () -> Unit = {},
    onClick: ReleaseListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    val latestRequestForMissingCoverArtUrl by rememberUpdatedState(requestForMissingCoverArtUrl)
    LaunchedEffect(key1 = release.id) {
        if (release.imageUrl == null) {
            latestRequestForMissingCoverArtUrl()
        }
    }

    ListItem(
        headlineContent = {
            Text(
                text = release.getAnnotatedName(),
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = release.fontWeight,
            )
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    onClick(release)
                },
                onLongClick = {
                    onSelect(release.id)
                },
            ),
        colors = listItemColors(isSelected = isSelected),
        supportingContent = {
            Column {
                if (showMoreInfo) {
                    val countryAndDate = release.countryCode.takeIf { it.isNotEmpty() }?.let { countryCode ->
                        buildString {
                            append("${countryCode.toFlagEmoji()} $countryCode")
                            if (release.releaseCountryCount > 1) append(" + ${release.releaseCountryCount - 1}")
                            if (release.date.isNotEmpty()) append("・${release.date}")
                        }
                    } ?: release.date
                    if (countryAndDate.isNotEmpty()) {
                        Text(
                            text = countryAndDate,
                            style = TextStyles.getCardBodySubTextStyle(),
                            textAlign = TextAlign.End,
                            fontWeight = release.fontWeight,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }

                    // TODO: formats/tracks count are not currently shown
                    //  consider showing at least in release group's releases to help disambiguate
                    Row {
                        release.formattedFormats.ifNotNullOrEmpty {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .weight(1f),
                                text = it,
                                style = TextStyles.getCardBodySubTextStyle(),
                                fontWeight = release.fontWeight,
                            )
                        }

                        release.formattedTracks.ifNotNullOrEmpty {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .weight(1f),
                                text = it,
                                style = TextStyles.getCardBodySubTextStyle(),
                                textAlign = TextAlign.End,
                                fontWeight = release.fontWeight,
                            )
                        }
                    }

                    release.formattedArtistCredits.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = release.fontWeight,
                        )
                    }

                    release.catalogNumbers.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = release.fontWeight,
                        )
                    }

                    when (val state = release.listenState) {
                        ReleaseListItemModel.ListenState.Hide -> {
                            // Do nothing
                        }

                        is ReleaseListItemModel.ListenState.Known,
                        ReleaseListItemModel.ListenState.Unknown,
                        -> {
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val hasListens = state is ReleaseListItemModel.ListenState.Known
                                TextWithIcon(
                                    imageVector = CustomIcons.Headphones,
                                    iconSize = TINY_ICON_SIZE,
                                    text = if (hasListens) state.listenCount.toString() else "?",
                                    textStyle = TextStyles.getCardBodySubTextStyle(),
                                )
                                if (hasListens && state.completeListenCount > 0) {
                                    val completeListenCount = state.completeListenCount.toString()
                                    TextWithIcon(
                                        modifier = Modifier.padding(start = 4.dp),
                                        imageVector = CustomIcons.StarFilled,
                                        iconSize = TINY_ICON_SIZE,
                                        iconTint = MaterialTheme.colorScheme.primary,
                                        text = completeListenCount,
                                        textStyle = TextStyles.getCardBodySubTextStyle(),
                                        contentDescription = "$completeListenCount complete listens",
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = release.imageUrl.orEmpty(),
                imageId = release.imageId,
                placeholderIcon = MusicBrainzEntityType.RELEASE.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(release.id)
                    },
                isSelected = isSelected,
            )
        },
        trailingContent = {
            AddToCollectionIconButton(
                entityListItemModel = release,
                onClick = onEditCollectionClick,
            )
        },
    )
}
