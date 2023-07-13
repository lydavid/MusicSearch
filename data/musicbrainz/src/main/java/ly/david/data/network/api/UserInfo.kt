package ly.david.data.network.api

import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "sub")
    val username: String? = null,
)
