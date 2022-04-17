package ly.david.mbjc.data.network

import okhttp3.Interceptor

private const val USER_AGENT = "User-Agent"
private const val USER_AGENT_VALUE = "MusicBrainzJetpackCompose/0.1.0"
private const val ACCEPT = "Accept"
private const val ACCEPT_VALUE = "application/json"

internal object NetworkUtils {
    val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(USER_AGENT, USER_AGENT_VALUE)
            .addHeader(ACCEPT, ACCEPT_VALUE)
            .build()
        chain.proceed(request)
    }
}
