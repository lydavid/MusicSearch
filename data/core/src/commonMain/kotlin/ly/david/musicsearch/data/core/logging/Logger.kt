package ly.david.musicsearch.data.core.logging

interface Logger {
    fun d(text: String)
    fun e(exception: Exception)
}
