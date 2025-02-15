package me.notepad.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import me.notepad.ui.theme.NotepadTheme
import me.notepad.ui.theme.myBlue
import me.notepad.viewmodel.GenericController


/**
 * pagina di login
 */
class LockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * bottom bar android nero
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Per Android 11 (API 30) e superiori
            val window = window

            window.navigationBarColor = Color.Black.toArgb()  // Imposta il colore della barra di navigazione a nero
        } else {
            // Per Android 10 (API 29) e inferiori
            window.navigationBarColor = Color.Black.toArgb()  // Imposta il colore della barra di navigazione a nero
        }

        val genericController: GenericController = ViewModelProvider(this)[GenericController::class.java]
        setContent {
            NotepadTheme {
                Greeting(genericController)
            }
        }
    }


    @Composable
    fun Greeting(genericController: GenericController) {
        val context = LocalContext.current
        val isLoggedIn by genericController.loggedIn.observeAsState(initial = false)

        LaunchedEffect(isLoggedIn) {
            /**
             * rimane in ascolto del parametro booleano che indica se si è pronti per la MainAcivity
             */
            if (isLoggedIn) {
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
        }


        /**
         * si aggiorna ad ogni tasto premuto dall'utente
         */
        var password by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = myBlue)
        ) {
            // Title Text
            Text(
                text = "Welcome!",
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 50.dp)
            )

            // Password input TextField con i colori del tema
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    genericController.login(password)  // Aggiorna LiveData nel ViewModel
                },
                label = { Text("Password:", color = Color.White, fontSize = 20.sp) },
                textStyle = TextStyle(color = Color.White, fontSize = 20.sp),  // Colore del testo
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))  // Bordo bianco
                    .background(myBlue, RoundedCornerShape(16.dp))  // Usa il colore del tema
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
    }
}
