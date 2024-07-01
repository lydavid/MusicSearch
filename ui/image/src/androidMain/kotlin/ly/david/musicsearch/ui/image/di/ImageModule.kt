package ly.david.musicsearch.ui.image.di

//import coil3.ImageLoader
//import coil3.SingletonImageLoader
//import coil3.network.okhttp.OkHttpNetworkFetcherFactory
//import okhttp3.Interceptor
//import okhttp3.OkHttpClient
//import okhttp3.Response
//import org.koin.dsl.module
//
//private class RequestHeaderInterceptor(
//    private val name: String,
//    private val value: String,
//) : Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request().newBuilder()
//            .header(name, value)
//            .build()
//        return chain.proceed(request)
//    }
//}

//actual val imageModule = module {
//    single<SingletonImageLoader.Factory> {
//        SingletonImageLoader.Factory {
//            ImageLoader.Builder(get())
//                .components {
//                    add(OkHttpNetworkFetcherFactory())
//                }
////                .okHttpClient {
////                    OkHttpClient.Builder()
////                        // Make sure we don't use okhttp cache.
////                        .addInterceptor(RequestHeaderInterceptor("Cache-Control", "no-cache"))
////                        .build()
////                }
//                .build()
//        }
//    }
//}
