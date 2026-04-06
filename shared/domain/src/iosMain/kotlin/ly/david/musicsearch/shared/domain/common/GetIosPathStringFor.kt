package ly.david.musicsearch.shared.domain.common

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun getIosPathStringFor(directory: NSSearchPathDirectory): String {
    val directory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = directory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null,
    )

    return requireNotNull(directory?.path)
}
