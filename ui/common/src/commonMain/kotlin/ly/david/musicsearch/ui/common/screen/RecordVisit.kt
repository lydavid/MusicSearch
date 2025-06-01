package ly.david.musicsearch.ui.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface RecordVisit {

    val incrementLookupHistory: IncrementLookupHistory

    @Composable
    fun RecordVisit(
        mbid: String,
        title: String,
        entity: MusicBrainzEntity,
        searchHint: String?,
    ) {
        var recordedHistory by rememberSaveable { mutableStateOf(false) }

        if (!recordedHistory) {
            LaunchedEffect(title, searchHint) {
                if (title.isNotEmpty() && searchHint != null) {
                    incrementLookupHistory(
                        LookupHistory(
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
