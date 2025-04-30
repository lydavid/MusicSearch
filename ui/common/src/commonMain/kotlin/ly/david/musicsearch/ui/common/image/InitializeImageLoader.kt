package ly.david.musicsearch.ui.common.image

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import okio.FileSystem

@OptIn(ExperimentalCoilApi::class)
@Composable
fun InitializeImageLoader() {
    setSingletonImageLoaderFactory { context ->
        newImageLoader(
            context = context,
            debug = false,
        )
    }
}

private fun newImageLoader(
    context: PlatformContext,
    debug: Boolean,
): ImageLoader {
    return ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder()
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(
                    context,
                    percent = 0.25,
                )
                .build()
        }
        .diskCache {
            newDiskCache()
        }
        // Show a short crossfade when loading images asynchronously.
        .crossfade(true)
        // Enable logging if this is a debug build.
        .apply {
            if (debug) {
                logger(DebugLogger())
            }
        }
        .build()
}

private const val DISK_SIZE_BYTES = 512L * 1024 * 1024 // 512MB

private fun newDiskCache(): DiskCache {
    return DiskCache.Builder()
        .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(DISK_SIZE_BYTES)
        .build()
}
