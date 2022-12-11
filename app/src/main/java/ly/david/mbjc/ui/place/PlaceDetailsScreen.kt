package ly.david.mbjc.ui.place

import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ly.david.data.Coordinates
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.AreaListItemModel
import ly.david.data.domain.PlaceListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun PlaceDetailsScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    place: PlaceListItemModel,
    lazyListState: LazyListState = rememberLazyListState(),
) {

    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            place.run {
                ListSeparatorHeader(text = stringResource(id = R.string.place_information))

                type?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.type, text = it)
                }
                lifeSpan?.run {
                    begin?.ifNotNullOrEmpty {
                        TextWithHeading(headingRes = R.string.opened, text = it)
                    }
                    end?.ifNotNullOrEmpty {
                        TextWithHeading(headingRes = R.string.closed, text = it)
                    }
                }
                address.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.address, text = it)
                }
                // TODO: area

                coordinates?.let {
                    val label = place.name +
                        if (place.lifeSpan?.ended == true) " (${stringResource(id = R.string.closed)})" else ""
                    CoordinateListItem(
                        context = context,
                        coordinates = it,
                        label = label
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
            PlaceDetailsScreen(
                place = PlaceListItemModel(
                    id = "p1",
                    name = "Some Place",
                    type = "Venue",
                    address = "123 Fake St",
                    coordinates = Coordinates(-123.4567, 123.4567),
                    lifeSpan = LifeSpan(
                        begin = "2022-01-01",
                        end = "2022-12-10",
                        ended = true
                    ),
                    area = AreaListItemModel(
                        id = "a1",
                        "Area",
                        "that one"
                    )
                )
            )
        }
    }
}
// endregion
