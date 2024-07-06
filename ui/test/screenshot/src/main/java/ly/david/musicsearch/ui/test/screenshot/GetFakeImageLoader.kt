package ly.david.musicsearch.ui.test.screenshot

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import coil3.ImageLoader
import coil3.test.FakeImageLoaderEngine
import coil3.test.default
import coil3.test.intercept

internal fun getFakeImageLoader(context: Context): ImageLoader {
    val engine = FakeImageLoaderEngine.Builder()
        .intercept(
            "https://www.example.com/image.jpg",
            createColorSquare(
                context,
                1,
            ),
        )
        .default(ColorDrawable(Color.RED))
        .build()
    val imageLoader = ImageLoader.Builder(context)
        .components { add(engine) }
        .build()
    return imageLoader
}

private fun createColorSquare(
    context: Context,
    size: Int,
    color: Int = Color.BLUE,
): BitmapDrawable {
    val colorDrawable = ColorDrawable(color)
    val bitmap = Bitmap.createBitmap(
        size,
        size,
        Bitmap.Config.ARGB_8888,
    )
    val canvas = Canvas(bitmap)
    colorDrawable.setBounds(
        0,
        0,
        canvas.width,
        canvas.height,
    )
    colorDrawable.draw(canvas)
    return BitmapDrawable(
        context.resources,
        bitmap,
    )
}
