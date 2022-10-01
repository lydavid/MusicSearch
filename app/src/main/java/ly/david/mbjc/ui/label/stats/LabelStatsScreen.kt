package ly.david.mbjc.ui.label.stats

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.R
import ly.david.mbjc.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.theme.TextStyles

// TODO: list state not saved on tab change
@Composable
internal fun LabelStatsScreen(
    labelId: String,
    viewModel: LabelStatsViewModel = hiltViewModel()
) {

    var totalRelations by rememberSaveable { mutableStateOf(0) }
    var relationTypeCounts by rememberSaveable { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(key1 = totalRelations) {

        totalRelations = viewModel.getNumberOfRelationsByResource(labelId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(labelId)
    }

    LazyColumn {
        item {
            Spacer(modifier = Modifier.padding(8.dp))
            ListSeparatorHeader(text = stringResource(id = R.string.relationships))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyles.getCardBodyTextStyle(),
                text = "Total number of relationships: $totalRelations"
            )
        }
        items(relationTypeCounts) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyles.getCardBodyTextStyle(),
                text = "${it.linkedResource.displayText}: ${it.count}"
            )
        }

        item {
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}
