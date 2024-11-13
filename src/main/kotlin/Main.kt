import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import java.io.*
import java.util.*

@Composable
fun App(state: MutableState<WindowState>, curPath: String) {
    val backgroundImage = "items/background.jpg"
    println("user dir = $curPath")
    var filesSet = remember { mutableStateListOf("$curPath/welcome.txt", "$curPath/$backgroundImage") }
    val appProps = Properties()
    val folderNamePath = "$curPath/folderName.properties"
    appProps.load(FileInputStream(folderNamePath))
    println("props = ${appProps.entries}")
    val itemsFolderName = appProps.getProperty("itemsFolder")
    val itemsDir = "$curPath/items/$itemsFolderName"
    val dirsList = listDirsUsingDirectoryStream(itemsDir)
    println("dirsList = $dirsList")
    var itemsMap2 = mutableMapOf<String, Set<String>>() //мап для хранения названия экспоната и набора из его описания и картинки
    dirsList.forEach {
        val filesList = listFilesUsingDirectoryStream("$itemsDir/$it")
        val txtFile = filesList.find { it.contains(".txt") }
        val imgFile = filesList.find { it != txtFile }
        val txtFilePath = "$itemsDir/$it/$txtFile"
        val imgFilePath = "$itemsDir/$it/$imgFile"
        val file = File(txtFilePath)
        val ioStream = BufferedReader(FileReader(file))
        val firstStringInFile = ioStream.readLine()
        val s = if (firstStringInFile=="") ioStream.readLine() else firstStringInFile //если первая строка пустая
        itemsMap2[s] = setOf(txtFilePath, imgFilePath)
    }
    var stendBordersList = remember { mutableStateListOf<BorderStroke>() }
    var itemsAddedToStend = remember { mutableStateListOf<String>() }
    var stendList = loadStendModel(stendBordersList, itemsAddedToStend)
    val fitimLogo = "fitim.png"
    val facultyLogoWhite = "faculty_white.png"
    val nvsuLogoWhite = "NVSU_white.png"
    var fontSize = remember { mutableStateOf(26.sp) }
        Column(Modifier.fillMaxSize()) {
            UpperBar(nvsuLogoWhite, facultyLogoWhite)
            DropdownDemo(itemsMap2.keys.toList(), fontSize) {
                println("selected = $it")
                val tempList = itemsMap2[it]?.toList()
                filesSet.clear()
                filesSet.add(tempList!!.first())
                filesSet.add(tempList.last())
            }
            MyContent(filesSet, fontSize, state)
        }
}

fun loadStendModel(bordersList: SnapshotStateList<BorderStroke>, itemsAddedToStend: SnapshotStateList<String>,): MutableList<StendBoxModel> {
    val myFile = File("itemsInStend.dat")
    val fin = FileInputStream(myFile)
    val oin = ObjectInputStream(fin)
    val stendList2 = oin.readObject() as MutableList<StendBoxModel>
    stendList2.forEach {//цикл по экспонатам, чтобы добавить границу в массив
        if (it.type=="stend") {
            bordersList.add(BorderStroke(2.dp, Color.Black))
            it.itemsInStend!!.forEach { (t, u) ->  u.forEach { itemsAddedToStend.add(it.first) }     }
        }
    }
    println("stendList from file = ${stendList2.toList()}")
    oin.close()
    fin.close()
    return stendList2
}

@Composable
private fun UpperBar(nvsuLogoWhite: String, facultyLogoWhite: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.DarkGray)
    ) {
        val nvsuFile = File(nvsuLogoWhite)
        val nvsuBitmap: ImageBitmap = remember(nvsuFile) { loadImageBitmap(nvsuFile.inputStream())    }
        val facultyFile = File(facultyLogoWhite)
        val facultyBitmap: ImageBitmap = remember(facultyFile) { loadImageBitmap(facultyFile.inputStream())   }
        Image(
            painter = BitmapPainter(image = nvsuBitmap),
            contentDescription = "", //можно вставить описание изображения
            contentScale = ContentScale.Fit, //параметры масштабирования изображения
            modifier = Modifier.padding(8.dp)
        )
        var text = File("captionText.txt").readText()
        Text(text, color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
        Image(
            painter = BitmapPainter(image = facultyBitmap), //указываем источник изображения
            contentDescription = "", //можно вставить описание изображения
            contentScale = ContentScale.Fit, //параметры масштабирования изображения
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun MyContent(filesSet: SnapshotStateList<String>, fontSize: MutableState<TextUnit>, state: MutableState<WindowState>) {
    val curPath = System.getProperty("user.dir")
    println("user dir = $curPath")
    println("filesSet = ${filesSet.toList()}")
    val textFile = filesSet.find { it.contains(".txt") }
    val imageFile = filesSet.find { !it.contains(".txt")    }
    var text = File("$textFile").readText().replace("\t", "   ")
    val file = File("$imageFile")
    val imageBitmap: ImageBitmap = remember(file) {  loadImageBitmap(file.inputStream())   }
    var isOpen by remember { mutableStateOf(true) }
    var secondWindowShow by remember { mutableStateOf(false) }
    println()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .border(BorderStroke(2.dp, Color(0xff1e63b2)))
            .padding(30.dp)
    ) {
        val stateVertical = rememberScrollState(0)
            val textSize:TextUnit = fontSize.value
            Text(
                text, fontSize = textSize,
                modifier = Modifier
                    .verticalScroll(stateVertical)
                    .weight(5f),
                textAlign = TextAlign.Justify,
            )
        VerticalScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
        Image(
            modifier = Modifier
                .weight(2f)
                .clickable(
                    onClick = {
                        println("image clicked")
                        secondWindowShow = true
                    }
                ),
            painter = BitmapPainter(image = imageBitmap),
            contentDescription = null
        )
        if (secondWindowShow) {
            Window( //второе окно для увеличения изображения
                onCloseRequest = {
                    state.value = WindowState(WindowPlacement.Fullscreen)
                    secondWindowShow = false
                },
                undecorated = true, //эти 3 строки нужны для фуллскрина без оконных кнопок
                alwaysOnTop = true,
                resizable = false,
                state = WindowState(WindowPlacement.Fullscreen),
                title = " Window Example"
            ){
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = BitmapPainter(image = imageBitmap),
                    contentDescription = null
                )
                Button(
                    onClick = {
                        state.value = WindowState(WindowPlacement.Fullscreen)
                        secondWindowShow = false
                    }
                ){   Text("Закрыть")  }
            }
        }
    }
}

fun main() = application {
    val curPath = System.getProperty("user.dir")
    var windowMode = File("mode.txt").readText() //для переключения режима с заголовком / без  для деплоя и отладки
    println("windowMode  = $windowMode")
    var stateMuseumWindow: MutableState<WindowState> = remember { mutableStateOf(WindowState(WindowPlacement.Fullscreen))}
    var choice = remember { mutableStateOf(0) }
    var stateFirstWindow: MutableState<WindowState> = remember { mutableStateOf(WindowState(WindowPlacement.Fullscreen))}
    var loadingWindowIsVisible = remember{ mutableStateOf(true)}

    Window( //стартовое окно
        onCloseRequest = ::exitApplication,
        undecorated = false, //сделать true, чтобы без рамок было
        alwaysOnTop = false,
        visible = loadingWindowIsVisible.value,
        state = WindowState(WindowPlacement.Floating)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = {
                    choice.value = 1
                    println("choice = $choice")
                }
            ){ Text("Настройки") }
            Button(  onClick = { choice.value = 2 } ){
                Text("Музей")
            }
        }
    }
    var settingsWindowVisible = mutableStateOf(false)

    if (choice.value == 1){  //для окна с настройками
        println("Settings")
        settingsWindowVisible.value = true
        loadingWindowIsVisible.value = false
        SettingsWindow(settingsWindowVisible, choice, loadingWindowIsVisible, curPath)
    }

    if (choice.value == 2) {//для основного окна с музеем
        loadingWindowIsVisible.value = false
        Window(
            onCloseRequest = ::exitApplication,
            undecorated = windowMode.contains("0"), //если в файле mode.txt первое число 0, то без оконных кнопок, иначе они будут, для отладки
            resizable = false,
            state = stateMuseumWindow.value
        ) {   App(stateMuseumWindow, curPath)  }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DropdownDemo(
    itemsInitial: List<String>,
    fontSize: MutableState<TextUnit>,
    onUpdate: (x: String) -> Unit
) { //комбобокс для выбора экспоната в музее
    var expanded by remember { mutableStateOf(false) }
    var items = remember { mutableStateListOf<String>() }
    itemsInitial.forEach {  if (!items.contains(it)) items.add(it)   }
    var selectedIndex by remember { mutableStateOf(-1) }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)
        .height(IntrinsicSize.Min)
        .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ){
            Text( //заголовок комбобокса
                if (selectedIndex < 0) "Выберите экспонат: ▼" //если еще ничего не выбрано
                else items[selectedIndex] + " ▼", //если выбрано
                fontSize = 20.sp,
                modifier = Modifier.clickable(onClick = { expanded = true  })
            )
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = "Шрифт: ",
                    modifier = Modifier.onClick {  fontSize.value = 26.sp }
                )
                FontSizeButton("+"){
                    var size = fontSize.value.value
                    size++
                    if (size<=55){
                        println("font size = $size")
                        fontSize.value = size.sp
                    }
                }
                FontSizeButton("-"){
                    var size = fontSize.value.value
                    size--
                    if (size>=10){
                        println("font size = $size")
                        fontSize.value = size.sp
                    }
                }
            }
        }
        DropdownMenu( //сам выпадающий список для комбобокса
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            items.forEachIndexed { index, s -> //заполняем элементы выпадающего списка
                DropdownMenuItem(
                    onClick = { //обработка нажатия на элемент
                        selectedIndex = index
                        expanded = false
                        onUpdate(s)
                    }
                ) {  Text(text = s)  }
            }
        }
    }
}

@Composable //ф-ия для создания кнопок изменения размера шрифта
private fun FontSizeButton(s: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .border(BorderStroke(2.dp, Color.White))
            .heightIn(max = 30.dp),
        contentPadding = PaddingValues(all = 0.dp),
        onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color.DarkGray)
    )
    {  Text( s, color = Color.White, fontSize = 25.sp)   }
}