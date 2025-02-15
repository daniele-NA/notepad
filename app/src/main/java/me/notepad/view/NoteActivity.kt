package me.notepad.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.notepad.ui.theme.NotepadTheme
import me.notepad.ui.theme.myBlue
import me.notepad.viewmodel.GenericController
import me.notepad.viewmodel.SqlController


/**
 * activity per apertura di una nota
 */
class NoteActivity : ComponentActivity() {

    private lateinit var genericController: GenericController
    private lateinit var sqlController: SqlController

    private var oldTitleForResearch = ""

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * barra tasti android nera
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.navigationBarColor = Color.Black.toArgb()
        } else {
            window.navigationBarColor = Color.Black.toArgb()
        }


        this.context = this


        /**
         * estrazione valori della nota cliccata nella MainActivity
         */
        val titleInherited = intent.getStringExtra("title").orEmpty()
        val contentInherited = intent.getStringExtra("content").orEmpty()

        this.oldTitleForResearch =
            titleInherited  //fondamentale per l'update che non va con il titolo nuovo altrimenti non va la ricerca

        this.genericController = ViewModelProvider(this)[GenericController::class.java]
        this.sqlController = ViewModelProvider(this)[SqlController::class.java]

        /**
         * si parte con ciò che arriva dalla activity precedente
         */
        this.genericController.setCurrentTitle(titleInherited)
        this.genericController.setCurrentContent(contentInherited)

        setContent {
            NotepadTheme {
                OpenNote()
            }
        }
    }

    @Composable
    fun OpenNote() {
        val title by genericController.currentTitle.observeAsState("")
        val content by genericController.currentContent.observeAsState("")

        val keyboardController = LocalSoftwareKeyboardController.current
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState) // Scrolling se necessario
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = title,
                onValueChange = { genericController.setCurrentTitle(it) },
                label = { Text("Titolo", fontSize = 24.sp, color = myBlue) },
                textStyle = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )

            TextField(
                value = content,
                onValueChange = { genericController.setCurrentContent(it) },
                label = { Text("Contenuto", fontSize = 24.sp, color = myBlue) },
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() } // Nasconde la tastiera alla pressione di "Done"
                )
            )

            /**
             * spazio di fondo pagina per tastiera
             */
            repeat(30) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }



    /**
     * si interfaccia con il database
     */
    private fun updateDatabase(onUpdateComplete: () -> Unit) {
        val newTitle = genericController.currentTitle.value ?: ""
        val newContent = genericController.currentContent.value ?: ""

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                /**
                 * se il titolo è vuoto,la pagina NON SI CHIUDE
                 */
                try {
                    sqlController.updateNote(oldTitleForResearch, newTitle, newContent)
                    withContext(Dispatchers.Main) {
                        onUpdateComplete()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "IMPOSTA UN TITILO", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        /**
         * verrà chiamata la Unit solo se andrà a buon fine l'update
         */
        updateDatabase { super.onBackPressed() }
    }
}
