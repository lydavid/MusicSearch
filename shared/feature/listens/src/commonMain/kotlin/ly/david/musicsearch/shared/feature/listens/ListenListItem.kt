package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.MoreVert
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun ListenListItem(
    listen: ListenListItemModel,
    modifier: Modifier = Modifier,
    onClick: (id: String) -> Unit = {},
    onClickMoreActions: ListenListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            fontWeight = listen.fontWeight,
                        ),
                    ) {
                        append(listen.getAnnotatedName())
                    }
                    withStyle(style = SpanStyle(fontSize = 13.sp)) {
                        append(" ${listen.durationMs.toDisplayTime()}")
                    }
                },
                style = TextStyles.getCardBodyTextStyle(),
                lineHeight = 24.sp,
            )
        },
        modifier = modifier.clickable {
            val recordingId = listen.recordingId
            if (recordingId.isNotEmpty()) { onClick(recordingId) }
        },
        supportingContent = {
            Column {
                Text(
                    text = listen.formattedArtistCredits,
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyles.getCardBodySubTextStyle(),
                )
                Text(
                    text = listen.listenedAt.getTimeFormatted(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = listen.imageUrl.orEmpty(),
                imageId = listen.imageId,
                placeholderIcon = MusicBrainzEntityType.RECORDING.getIcon(),
            )
        },
        trailingContent = {
            IconButton(
                onClick = {
                    onClickMoreActions(listen)
                },
            ) {
                Icon(
                    imageVector = CustomIcons.MoreVert,
                    contentDescription = "More actions for ${listen.name} by ${listen.formattedArtistCredits}",
                )
            }
        },
    )
}
