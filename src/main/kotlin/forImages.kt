
//import androidx.compose.ui.graphics.Matrix
//import org.jetbrains.skia.Bitmap

//import androidx.compose.ui.graphics.ImageBitmap
//import java.awt.Image
//
//
//fun downscaleImageHighQuality(imageBitmap: ImageBitmap, targetWidth: Int, targetHeight: Int): ImageBitmap {
//    val sourceImage = Image.makeFromBitmap(imageBitmap.asDesktopBitmap())
//
//    // Create a paint object with high-quality filtering
//    val paint = Paint().apply {
//        // Set the filter quality to high
//        filterQuality = FilterQuality.High
//    }
//
//    // Perform the resizing with high-quality filtering
//    val resizedImage = sourceImage.resize(targetWidth, targetHeight, paint)
//
//    return resizedImage.asImageBitmap()
//}

//
//fun getResizedImage(bitmap: Bitmap){
//    val m = Matrix()
//    m.scale(0.5f, 0.5f)
//    while (bitmap.width > 256) {
//        val bitmap_half: Bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true)
//        bitmap.recycle()
//        bitmap = bitmap_half
//    }
//}