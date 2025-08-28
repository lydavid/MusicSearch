package ly.david.musicsearch.shared.domain.listen

import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

interface UpdateListenBrainzToken {
    suspend operator fun invoke(token: String): ListenBrainzLoginState?
}

@Parcelize
sealed class ListenBrainzLoginState : CommonParcelable {
    data object InvalidToken : ListenBrainzLoginState()
    data object LoggedIn : ListenBrainzLoginState()
    data object LoggedOut : ListenBrainzLoginState()
    data class OtherError(val message: String) : ListenBrainzLoginState()
}
