package ly.david.mbjc.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

internal object JsonUtils {
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}

internal fun Any.toJson(): String = JsonUtils.moshi.adapter(Any::class.java).lenient().toJson(this)

internal fun <T> String.fromJson(type: Class<T>): T? = JsonUtils.moshi.adapter(type).lenient().fromJson(this)
