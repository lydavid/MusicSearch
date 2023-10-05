package ly.david.data.core.logging

interface Logger {
    fun d(text: String)
    fun e(exception: Exception)
}
