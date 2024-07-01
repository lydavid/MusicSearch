package ly.david.musicsearch.ui.common.label

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LabelListItemPreview(
    @PreviewParameter(LabelCardPreviewParameterProvider::class) label: LabelListItemModel,
) {
    PreviewTheme {
        Surface {
            LabelListItem(label)
        }
    }
}

// Cannot be private.
internal class LabelCardPreviewParameterProvider : CollectionPreviewParameterProvider<LabelListItemModel>(
    listOf(
        LabelListItemModel(
            id = "1",
            name = "Music Label",
        ),
        LabelListItemModel(
            id = "2",
            name = "Sony Records",
            disambiguation = "1991 - 2001 group/division of Sony Music Entertainment (Japan) - " +
                "used to organize imprints; not a release label",
        ),
        LabelListItemModel(
            id = "3",
            name = "Sony Classical",
            type = "Imprint",
        ),
        LabelListItemModel(
            id = "4",
            name = "Sony Music",
            disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
            type = "Original Production",
            labelCode = 10746,
        ),
        LabelListItemModel(
            id = "5",
            name = "Sony Music",
            disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
            type = "Original Production",
            labelCode = 10746,
            catalogNumber = "CAT-123",
        ),
    ),
)
