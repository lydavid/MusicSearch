package ly.david.musicsearch.data.musicbrainz

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

class MusicBrainzLoginActivityResultContract(
    private val authService: AuthorizationService,
    private val authRequest: AuthorizationRequest,
) : ActivityResultContract<Unit, MusicBrainzLoginActivityResultContract.Result>() {

    data class Result(
        val response: AuthorizationResponse?,
        val exception: AuthorizationException?,
    )

    override fun createIntent(
        context: Context,
        input: Unit,
    ): Intent {
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): Result {
        val response = intent?.run { AuthorizationResponse.fromIntent(intent) }
        val exception = AuthorizationException.fromIntent(intent)
        return Result(
            response = response,
            exception = exception,
        )
    }
}
