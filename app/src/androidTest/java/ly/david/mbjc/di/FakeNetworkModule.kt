package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.net.InetAddress
import javax.inject.Singleton
import ly.david.data.di.NetworkModule
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
internal object FakeNetworkModule {

    @Singleton
    @Provides
    fun provideHeldCertificate(): HeldCertificate {
        val localhost: String = InetAddress.getByName("localhost").canonicalHostName
        return HeldCertificate.Builder()
            .addSubjectAlternativeName(localhost)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        heldCertificate: HeldCertificate
    ): OkHttpClient {
        val clientCertificates: HandshakeCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(heldCertificate.certificate)
            .build()

        return OkHttpClient().newBuilder()
            .sslSocketFactory(clientCertificates.sslSocketFactory(), clientCertificates.trustManager)
            .build()
    }

    @Singleton
    @Provides
    fun provideMockWebServer(
        heldCertificate: HeldCertificate
    ): MockWebServer {
        val serverCertificates: HandshakeCertificates = HandshakeCertificates.Builder()
            .heldCertificate(heldCertificate)
            .build()
        val server = MockWebServer()
        server.useHttps(serverCertificates.sslSocketFactory(), false)
        return server
    }
}
