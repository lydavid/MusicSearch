package ly.david.musicsearch.shared.domain.app

data class AppInfo(
    private val versionName: String,
    private val versionCode: Int,
) {
    val versionNameAndCode: String = "$versionName ($versionCode)"
}
