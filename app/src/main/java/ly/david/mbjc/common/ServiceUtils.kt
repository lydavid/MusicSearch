package ly.david.mbjc.common

import okhttp3.Interceptor

private const val USER_AGENT = "User-Agent"
private const val USER_AGENT_VALUE = "MusicBrainzJetpackCompose/0.1.0"
private const val ACCEPT = "Accept"
private const val ACCEPT_VALUE = "application/json"

object ServiceUtils {
    val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(USER_AGENT, USER_AGENT_VALUE)
            .addHeader(ACCEPT, ACCEPT_VALUE)
            // TODO: response in App Inspection is gibberish, is there a header we need to add?
            .build()
        chain.proceed(request)
    }
}
