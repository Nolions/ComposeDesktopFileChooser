import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

private var chooser: JFileChooser? = null

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

@Composable
@Preview
fun App() {
    val text = "Chooser"
    val file = File(ClassLoader.getSystemResource("demo.png").file)

    var bitmap by remember { mutableStateOf(loadImageBitmap(file.inputStream())) }

    if (chooser == null) {
        initFileChooser()
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Button(
                modifier = Modifier.padding(0.dp),
                onClick = {
                    selectedFile()?.let {
                        bitmap = it
                    }
                }) {
                Text(text)
            }

            Image(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .size(100.dp, 90.dp).clickable {
                        selectedFile()?.let {
                            bitmap = it
                        }
                    },
                bitmap = bitmap,
                contentDescription = null,
            )
        }

    }
}

/**
 * 檔案選擇
 */
private fun selectedFile(): ImageBitmap? {
    chooser?.let {
        val result = it.showOpenDialog(null) // 返回的是0，代表已經選擇了某個檔案。如果返回1代表選擇了取消按鈕或者直接關閉了視窗
        if (result == 0) {
            val path = it.selectedFile.absolutePath
            println("Chooser file:$path")
            return loadImageBitmap(File(path).inputStream())
        }
    }

    return null
}

/**
 * Init JFileChooser
 */
fun initFileChooser() {
    chooser = JFileChooser()

    chooser?.let {
        //  預設開啟目錄路徑
        it.currentDirectory = File(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures");

        // 只允許圖片
        val imageFilter = FileNameExtensionFilter(
            "Image files", *ImageIO.getReaderFileSuffixes()
        )
        it.fileFilter = imageFilter
    }

}

