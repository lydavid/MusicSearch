package ly.david.mbjc

import java.io.IOException
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import javax.inject.Inject
import ly.david.mbjc.di.TEST_PORT
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before

internal open class MainActivityTestWithMockServer : MainActivityTest() {

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Throws(IOException::class)
    fun readFileWithNewLineFromResources(fileName: String): String {
        return composeTestRule.activity.assets.open(fileName).bufferedReader()
            .use { bufferReader -> bufferReader.readText() }
    }

    @Before
    open fun setupApp() {
        hiltRule.inject()

        mockWebServer.start(TEST_PORT)

        // TODO: learn more: https://github.com/square/okhttp/blob/master/mockwebserver/README.md#dispatcher
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(HTTP_NOT_FOUND)
            }
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
