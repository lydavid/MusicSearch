package ly.david.musicbrainzjetpackcompose.ui.common

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonUtils {
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}

fun Any.toJson(): String = JsonUtils.moshi.adapter(Any::class.java).lenient().toJson(this)

fun <T> String.fromJson(type: Class<T>): T? = JsonUtils.moshi.adapter(type).lenient().fromJson(this)
