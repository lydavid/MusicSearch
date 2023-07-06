package ly.david.mbjc.ui.label.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.listitem.LabelListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.R
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun LabelDetailsScreen(
    modifier: Modifier = Modifier,
    label: LabelListItemModel,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            label.run {
                InformationListSeparatorHeader(R.string.label)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.type, text = it)
                }
                labelCode?.ifNotNull {
                    TextWithHeadingRes(headingRes = R.string.label_code, text = stringResource(id = R.string.lc, it))
                }

                // TODO: lifespan, founded, defunct for end

                // TODO: area
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
            LabelDetailsScreen(
                label = LabelListItemModel(
                    id = "f9ada3ae-3081-44df-8581-ca27a3462b68",
                    name = "Sony BMG Music Entertainment",
                    disambiguation = "Aug 5, 2004 - Oct 1, 2008",
                    type = "Original Production",
                    labelCode = 13989
                )
            )
        }
    }
}
// endregion
