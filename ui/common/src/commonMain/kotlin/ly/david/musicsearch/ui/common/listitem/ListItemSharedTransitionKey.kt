package ly.david.musicsearch.ui.common.listitem

import com.slack.circuit.sharedelements.SharedTransitionKey
import ly.david.musicsearch.shared.domain.image.ImageId

data class ListItemSharedTransitionKey(
    val imageId: ImageId,
    val type: ElementType,
) : SharedTransitionKey {
    enum class ElementType {
        Image,
    }
}
