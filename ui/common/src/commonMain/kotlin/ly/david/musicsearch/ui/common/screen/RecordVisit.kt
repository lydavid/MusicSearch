package ly.david.musicsearch.ui.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface RecordVisit {

    val incrementLookupHistory: IncrementLookupHistory

    @Suppress("NotConstructor")
    @Composable
    fun RecordVisit(
        oldId: String,
        mbid: String?,
        title: String,
        entity: MusicBrainzEntityType,
        searchHint: String?,
    ) {
        var recordedHistory by rememberSaveable { mutableStateOf(false) }

        if (!recordedHistory) {
            LaunchedEffect(title, searchHint) {
                if (mbid != null && title.isNotEmpty() && searchHint != null) {
                    incrementLookupHistory(
                        oldId = oldId,
                        lookupHistory = LookupHistory(
                            mbid = mbid,
                            title = title,
                            entity = entity,
                            searchHint = searchHint,
                        ),
                    )
                    recordedHistory = true
                }
            }
        }
    }
}
