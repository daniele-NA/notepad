package me.notepad.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import me.notepad.model.Note
import me.notepad.ui.theme.NotepadTheme
import me.notepad.ui.theme.myBlue
import me.notepad.viewmodel.SqlController


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * bottom bar android colore nero
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Per Android 11 (API 30) e superiori
            val window = window

            window.navigationBarColor =
                Color.Black.toArgb()  // Imposta il colore della barra di navigazione a nero
        } else {
            // Per Android 10 (API 29) e inferiori
            window.navigationBarColor =
                Color.Black.toArgb()  // Imposta il colore della barra di navigazione a nero
        }

        val sqlController: SqlController =
            ViewModelProvider(this)[SqlController::class.java]  // evita di ricreare l'istanza
        setContent {
            NotepadTheme {
                NoteList(sqlController = sqlController, onNoteClick = { title, content ->
                    /**
                     * diventa una sorta di callback che rimane in attesa del click su una nota
                     */
                    val intent: Intent = Intent(this, NoteActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("content", content)
                    startActivity(intent)
                }, onDeleteClick = { title ->
                    /**
                     * click sul cestino
                     */
                    deleteConfirmationDialog(
                        onConfirm = {
                            /**
                             * se si conferma si elimina la nota,la lista si aggiorna automaticamente
                             */
                            try {
                                sqlController.deleteNote(title = title)
                            } catch (_: Exception) {



                            }

                        }
                    )

                })
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NoteList(
        sqlController: SqlController,
        onNoteClick: (String, String) -> Unit,
        onDeleteClick: (String) -> Unit
    ) {

        /**
         * osserva in LiveData i cambiamenti
         */
        val notes by sqlController.allNotes.observeAsState(listOf())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            /**
             * barra di accoglienza
             */
            TopAppBar(
                title = { Text("Le Tue Note") },
                modifier = Modifier
                    .statusBarsPadding() // Aggiunge padding per la barra di stato
                    .fillMaxWidth(),
                actions = {
                    IconButton(onClick = {
                        sqlController.addNote()
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Aggiungi Nota")
                    }
                }
            )

            // Lista delle note
            LazyColumn {
                items(notes) { note ->
                    NoteCard(note, onNoteClick, onDeleteClick)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }


    /**
     * card delle note
     */
    @Composable
    fun NoteCard(
        note: Note,
        onNoteClick: (String, String) -> Unit,
        onDeleteClick: (String) -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    onNoteClick(note.title, note.content)
                },
            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = if (note.title.length < 30) note.title else note.title.substring(
                            0,
                            30
                        ) + "...",
                        color = myBlue,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontSize = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (note.content.length < 70) note.content else (note.content.substring(
                            0,
                            70
                        ) + "......"),
                        style = TextStyle(fontSize = 15.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                IconButton(
                    onClick = { onDeleteClick(note.title) },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Elimina Nota",
                        tint = Color.Red
                    )
                }
            }
        }

    }


    /**
     * apre un dialog di conferma se l'utente è convinto di eliminare la nota
     * non ci interessiamo in caso del pulsante 'annulla' (dismiss)
     */
    private fun deleteConfirmationDialog(
        onConfirm: () -> Unit
    ) {

        val builder = AlertDialog.Builder(this) // 'this' è il contesto dell'Activity
        builder.apply {
            setTitle("Conferma eliminazione")
            builder.setMessage("Sei sicuro di voler eliminare questa nota?")
        }
        builder.setPositiveButton("Conferma") { _, _ ->
            onConfirm()
        }
        builder.setNegativeButton("Annulla") { _, _ ->

        }
        builder.show()

    }

}

