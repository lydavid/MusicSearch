package ly.david.musicsearch.test.image

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

fun getFakeImageLoader(context: Context): ImageLoader {
    val engine = FakeImageLoaderEngine.Builder()
        .intercept(
            data = "https://www.example.com/image.jpg",
            createColorSquare(
                context = context,
                size = 1,
            ),
        )
        .intercept(
            data = "https://www.example.com/blue.jpg",
            createColorSquare(
                context = context,
                size = 1,
            ),
        )
        .intercept(
            data = "https://www.example.com/red.jpg",
            createColorSquare(
                context = context,
                size = 1,
                color = Color.RED,
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
