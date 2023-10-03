package ly.david.ui.common.label

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.core.common.ifNotNull
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.core.getNameWithDisambiguation
import ly.david.musicsearch.domain.listitem.LabelListItemModel
import ly.david.ui.common.R
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles
import ly.david.ui.core.theme.getSubTextColor

@Composable
fun LabelListItem(
    label: LabelListItemModel,
    modifier: Modifier = Modifier,
    onLabelClick: LabelListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = label.getNameWithDisambiguation(),
                    style = TextStyles.getCardBodyTextStyle()
                )

                label.type?.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        color = getSubTextColor(),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                label.labelCode?.ifNotNull {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(id = R.string.lc, it),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                // TODO: area

                // TODO: lifespan

                label.catalogNumber.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        modifier = modifier.clickable { onLabelClick(label) },
    )
}

// Cannot be private.
internal class LabelCardPreviewParameterProvider : PreviewParameterProvider<LabelListItemModel> {
    override val values = sequenceOf(
        LabelListItemModel(
            id = "1",
            name = "Music Label",
        ),
        LabelListItemModel(
            id = "2",
            name = "Sony Records",
            disambiguation = "1991 - 2001 group/division of Sony Music Entertainment (Japan) - " +
                "used to organize imprints; not a release label"
        ),
        LabelListItemModel(
            id = "3",
            name = "Sony Classical",
            type = "Imprint"
        ),
        LabelListItemModel(
            id = "4",
            name = "Sony Music",
            disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
            type = "Original Production",
            labelCode = 10746
        ),
        LabelListItemModel(
            id = "5",
            name = "Sony Music",
            disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
            type = "Original Production",
            labelCode = 10746,
            catalogNumber = "CAT-123"
        ),
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview(
    @PreviewParameter(LabelCardPreviewParameterProvider::class) label: LabelListItemModel,
) {
    PreviewTheme {
        Surface {
            LabelListItem(label)
        }
    }
}
