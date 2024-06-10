import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString


@Composable
fun copyToClipboard(textToCopy: String) {
    val clipboardManager = LocalClipboardManager.current
    clipboardManager.setText(AnnotatedString(textToCopy))

    
}


