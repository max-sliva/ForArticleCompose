import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.collections.HashSet

@Composable
fun makeItem(it: Map.Entry<String, Set<String>>) {
    Text(text = it.key)
    val itemImage = File(it.value.last())
    val itemBitmap: ImageBitmap = remember(itemImage) {
        loadImageBitmap(itemImage.inputStream())
    }

    Image(
        painter = BitmapPainter(image = itemBitmap),
        contentDescription = "", //можно вставить описание изображения
        contentScale = ContentScale.Fit, //параметры масштабирования изображения
//                        contentScale = ContentScale.Inside, //параметры масштабирования изображения
    )
}

@Composable
fun makeItem(it: Pair<String, String>, itemsAddedToStend: SnapshotStateList<String>,) {
        Text(text = it.first)
        Button(
            onClick = {
                println("remove ${it.first}")
                itemsAddedToStend.remove(it.first)
                println("itemsAddedToStend = ${itemsAddedToStend.toList()}")
            },
            modifier = Modifier
                .width(50.dp)
        ) {
            Text("x")
        }


        val itemImage = File(it.second)
        val itemBitmap: ImageBitmap = remember(itemImage) {
            loadImageBitmap(itemImage.inputStream())
        }

        Image(
            painter = BitmapPainter(image = itemBitmap),
            contentDescription = "", //можно вставить описание изображения
            contentScale = ContentScale.Fit, //параметры масштабирования изображения
//                        contentScale = ContentScale.Inside, //параметры масштабирования изображения
        )
}

fun getItems() {

}

fun listFilesUsingJavaIO(dir: String?): Set<String> {
    return Stream.of(*File(dir).listFiles())
        .filter { file: File -> !file.isDirectory }
        .map { obj: File -> obj.name }
        .collect(Collectors.toSet())
}

fun listDirsUsingJavaIO(dir: String?): Set<String> {
    return Stream.of(*File(dir).listFiles())
        .filter { file: File -> file.isDirectory }
        .map { obj: File -> obj.name }
        .collect(Collectors.toSet())
}

@Throws(IOException::class)
fun listFilesUsingDirectoryStream(dir: String?): Set<String> {
    val fileSet: MutableSet<String> = HashSet()
    Files.newDirectoryStream(Paths.get(dir)).use { stream ->
        for (path in stream) {
            if (!Files.isDirectory(path)) {
                fileSet.add(
                    path.fileName
                        .toString()
                )
            }
        }
    }
    return fileSet
}

@Throws(IOException::class)
fun listDirsUsingDirectoryStream(dir: String?): Set<String> {
    val fileSet: MutableSet<String> = HashSet()
    Files.newDirectoryStream(Paths.get(dir)).use { stream ->
        for (path in stream) {
            if (Files.isDirectory(path)) {
                fileSet.add(
                    path.fileName
                        .toString()
                )
            }
        }
    }
    return fileSet
}

fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}

fun getItemsMap(folderNamePath: String): MutableMap<String, Set<String>> {
    val curPath = System.getProperty("user.dir")
    val backgroundImage = "items/background.jpg"
    println("user dir = $curPath")
//    var filesSet =  listOf("$curPath/welcome.txt", "$curPath/$backgroundImage")
    val appProps = Properties()
    appProps.load(FileInputStream(folderNamePath))
    println("props = ${appProps.entries}")
    val itemsFolderName = appProps.getProperty("itemsFolder")
    val itemsDir = "$curPath/items/$itemsFolderName"
    val dirsList = listDirsUsingDirectoryStream(itemsDir)
    println("dirsList = $dirsList")
    //var setOfItems =
//    var itemsMap = mutableMapOf<String, String>()
    val itemsMap2 =
        mutableMapOf<String, Set<String>>() //мап для хранения названия экспоната и набора из его описания и картинки
    dirsList.forEach {
        val filesList = listFilesUsingDirectoryStream("$itemsDir/$it")
        val txtFile = filesList.find { it.contains(".txt") }
        val imgFile = filesList.find { it != txtFile }
        val txtFilePath = "$itemsDir/$it/$txtFile"
        val imgFilePath = "$itemsDir/$it/$imgFile"
//        val filesSet = setOf(txtFilePath, imgFilePath)
        val file = File(txtFilePath)
        val ioStream = BufferedReader(FileReader(file))
        val firstStringInFile = ioStream.readLine()
        val s = if (firstStringInFile == "") ioStream.readLine() else firstStringInFile //если первая строка пустая
//        println("for $it: $filesList caption = $s")
//        itemsMap[s] = "$itemsDir/$it"
        itemsMap2[s] = setOf(txtFilePath, imgFilePath)
    }
    itemsMap2.forEach {
        println("${it.key} : ${it.value}")
    }
    return itemsMap2
}