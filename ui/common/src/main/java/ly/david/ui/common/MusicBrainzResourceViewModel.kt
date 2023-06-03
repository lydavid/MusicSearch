package ly.david.ui.common

import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.data.network.MusicBrainzResource

/**
 * Contract with generic properties that each MusicBrainz scaffold ViewModel should implement.
 */
interface MusicBrainzResourceViewModel {
    val resource: MusicBrainzResource

    val title: MutableStateFlow<String>
    val isError: MutableStateFlow<Boolean>

    fun setTitle(title: String?) {
        this.title.value = title ?: return
    }
}
