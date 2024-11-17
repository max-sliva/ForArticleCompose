import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

fun main() = application {
    val myText = remember { mutableStateOf("") }
    Window( onCloseRequest = ::exitApplication, title = "ComposeTest", visible = true,
            state = WindowState(width = 300.dp, height = 100.dp),
    ) {
        Row( modifier = Modifier.fillMaxSize() ) {
            Button(onClick = { myText.value = "Hi" }) { Text("Hello") }
            Text(text = myText.value)
        }
    }
}