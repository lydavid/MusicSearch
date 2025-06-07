package ly.david.musicsearch.ui.common.listitem

import com.slack.circuit.sharedelements.SharedTransitionKey

data class ListItemSharedTransitionKey(
    val id: String,
    val type: ElementType,
) : SharedTransitionKey {
    enum class ElementType {
        Image,
    }
}
