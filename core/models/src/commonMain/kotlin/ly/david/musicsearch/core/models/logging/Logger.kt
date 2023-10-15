package ly.david.musicsearch.core.models.logging

interface Logger {
    fun d(text: String)
    fun e(exception: Exception)
}
