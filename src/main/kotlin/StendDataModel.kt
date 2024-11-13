import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.Serializable

data class StendBoxModel(
    var type: String = "stend",
    var shelvesNum: Int = 4,
    var borderNumber: Int = 0, //номер стенда в списке
    var itemsInStend: HashMap<Number, ArrayList<Pair<String, String>>>?
): Serializable
//class StendViewModel : ViewModel() {
//}
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun StendBox(
    model: StendBoxModel,
    stendBordersList: SnapshotStateList<BorderStroke>,
    dialogState: MutableState<Boolean>,
    barForStendVisibility: MutableState<Boolean>?,
    rowValue: MutableState<String>?,
    selectedItem: MutableState<Pair<String, String>>?,
    itemsAddedToStend: SnapshotStateList<String>?,
    inMuseum: Boolean = false,
    onChooseItem: (x: String) -> Unit
){
//    var borderForStend = remember{ mutableStateOf(BorderStroke(2.dp, Color.Black))}
    val windowSize = LocalWindowInfo.current.containerSize
    println("window size = ${windowSize.width}")
    var borderForStend = stendBordersList[model.borderNumber]
    if (model.type=="stend") {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
               // .background(Color(0xffff0000))
                .border(borderForStend)
                .clickable{
                    if (barForStendVisibility != null) {
                        barForStendVisibility.value = true
                    }
                    if (rowValue != null) {
                        rowValue.value = model.shelvesNum.toString()
                    }
                    println("stend left clicked")
                    stendBordersList[model.borderNumber] = BorderStroke(15.dp, Color.Yellow)
                    for (i in stendBordersList.indices){
                        if (i!=model.borderNumber) stendBordersList[i] = BorderStroke(2.dp, Color.Black)
                    }
                }
                .onClick( matcher = PointerMatcher.mouse(PointerButton.Secondary),
                    onClick = {
                        println("right button click")
                        dialogState.value = true
                    }
                )
//                .border(if (model.clicked) BorderStroke(2.dp, Color.Black) else BorderStroke(15.dp, Color.Yellow))
        ) {
            var shelveItemsNum = 0 //кол-во экспонатов на стенде
            model.itemsInStend?.forEach {
                shelveItemsNum += it.value.size
            }
            Text("stend, items = $shelveItemsNum  ")
            var rowWidth =  mutableStateOf( windowSize.width / 2 - 350)
            for (i in 0..< model.shelvesNum) {
                Row(
                    modifier = Modifier
                        .border(BorderStroke(2.dp, Color.Blue))
                         //  .fillMaxSize()
                        .height(200.dp)
                        .width(rowWidth.value.dp)
                        .clickable{
                            println("shelve $i clicked")
                            println("selectedItem = $selectedItem")
                            if (selectedItem != null) {
                                if (selectedItem.value.first!=""){
                                    println("item is selected")
                                    dialogState.value = false
                                    if (model.itemsInStend?.get(i)==null){
                                        println("no items in shelve")
                                        model.itemsInStend?.set(i, ArrayList<Pair<String, String>>())
                                        val arr = model.itemsInStend?.get(i)
                                        arr!!.add(selectedItem.value)
                                        model.itemsInStend?.set(i, arr)
                                        if (itemsAddedToStend != null) {
                                            itemsAddedToStend.add(selectedItem.value.first)
                                        }
                                        if (itemsAddedToStend != null) {
                                            println("itemsAddedToStend = ${itemsAddedToStend.toList()}")
                                        }
                                        selectedItem.value = Pair<String, String>("", "")
                                    } else {
                                        println("there are ${model.itemsInStend?.get(i)?.size}")
                                        val arr = model.itemsInStend?.get(i)
                                        arr!!.add(selectedItem.value)
                                        model.itemsInStend?.set(i, arr)
                                        if (itemsAddedToStend != null) {
                                            itemsAddedToStend.add(selectedItem.value.first)
                                        }
                                        if (itemsAddedToStend != null) {
                                            println("itemsAddedToStend = ${itemsAddedToStend.toList()}")
                                        }
                                        selectedItem.value = Pair<String, String>("", "")
                                    }
                                    dialogState.value = true
                                } else  println("item is not selected")
                            }

//                            stendBordersList[model.borderNumber] = BorderStroke(15.dp, Color.Yellow)
                        }
                ) {
                    var itemWidth = rowWidth.value / 2
                    var arraySize = 2
                    if (model.itemsInStend?.get(i) != null)
                        arraySize = model.itemsInStend!![i]!!.size
                    if (arraySize > 2) itemWidth = rowWidth.value / arraySize
                        model.itemsInStend?.get(i)?.forEach {
                            if (itemsAddedToStend != null) {
                                if (itemsAddedToStend.contains(it.first)) {
                                    Column(
                                        modifier = Modifier
                                            .width(itemWidth.dp)
                                    )
                                    {
                        //                                    makeItem(it, itemsAddedToStend)
                                        Text(text = it.first)
                                        if (!inMuseum){
                                            Button(
                                                onClick = {
                                                    println("remove ${it.first}")
                                                    itemsAddedToStend.remove(it.first)
                                                    println("itemsAddedToStend = ${itemsAddedToStend.toList()}")
                                                    model.itemsInStend?.get(i)!!
                                                        .remove(it)
                                                },
                                                modifier = Modifier
                                                    .width(50.dp)
                                            ) {
                                                Text("x")
                                            }
                                        }

                                        val itemImage = File(it.second)
                                        val itemBitmap: ImageBitmap = remember(itemImage) {
                                            loadImageBitmap(itemImage.inputStream())
                                        }

                                        if (!inMuseum) { //если находимся в режиме Настройки
                                            Image(
                                                    painter = BitmapPainter(image = itemBitmap),
                                                    contentDescription = "", //можно вставить описание изображения
                                                    contentScale = ContentScale.Fit, //параметры масштабирования изображения
                                                    //                        contentScale = ContentScale.Inside, //параметры масштабирования изображения
                                                )
                                        } else { //если в режиме Музея
                                            Image(
                                                painter = BitmapPainter(image = itemBitmap),
                                                contentDescription = "", //можно вставить описание изображения
                                                contentScale = ContentScale.Fit, //параметры масштабирования изображения
                                                //                        contentScale = ContentScale.Inside, //параметры масштабирования изображения
                                                modifier = Modifier
                                                    .clickable {
                                                        println("item clicked on map = ${it.first}")
                                                        onChooseItem(it.first)
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }
    } else if (model.type=="comp") {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxHeight()
//                .wrapContentHeight()
                .fillMaxWidth()
//                .background(Color(0xff00ff00))
//                .border(BorderStroke(2.dp, Color.Yellow))
        ) {
            Text("comp")
            BoxWithConstraints(
                modifier = Modifier

//                    .fillMaxSize()
//                    .size()
                    .height(600.dp)
                    .width(200.dp)
                    //.fillMaxHeight()
//                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    //.background(Color.Red)
                    .border(BorderStroke(2.dp, Color.Green))
            ){
                println("comp height = ${this.maxHeight}")


            }
        }
    }
}
