package ly.david.musicsearch.shared.domain.auth

import androidx.activity.result.contract.ActivityResultContract

abstract class MusicBrainzLoginActivityResultContract :
    ActivityResultContract<Unit, MusicBrainzLoginActivityResultContract.Result>() {
    sealed class Result {
        data class Success(val requestJsonString: String) : Result()
        data class Error(val exceptionString: String) : Result()
    }
}
