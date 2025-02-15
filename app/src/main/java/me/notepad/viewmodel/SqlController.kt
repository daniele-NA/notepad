package me.notepad.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.notepad.model.Note
import me.notepad.model.NoteDatabase


/**
 * controller per il database
 */
class SqlController(application: Application) : AndroidViewModel(application) {


    private val noteDao = NoteDatabase.getDatabase(application).noteDao()

    // Recupera tutte le note in tempo reale
    val allNotes: LiveData<List<Note>> = noteDao.selectAllLive()


    /**
     * viene creata una nuova nota con titolo casuale ed univoco e contenuto TODO
     */
    fun addNote() {
        viewModelScope.launch {
            val todoNote = Note(
                title = getGenericTitle(), content = "TODO"
            )
            noteDao.insert(todoNote)

        }
    }

    /**
     * aggiorna una nota,il contenuto può essere anche vuoto
     * il titolo NON DEVE ESSERE VUOTO
     */
    fun updateNote(oldTitle: String, title: String, content: String) {
        check(title)

        viewModelScope.launch {
            try {
                noteDao.updateNoteByTitle(
                    oldTitle,
                    title.trim(),
                    content.trim(),
                    System.currentTimeMillis()
                )
            } catch (e: Exception) {
                noteDao.updateNoteByTitle(
                    oldTitle,
                    newTitle = getGenericTitle(),
                    newContent = content.trim(),
                    System.currentTimeMillis()
                )
            }
        }
    }

    /**
     * eliminazione nota
     */
    fun deleteNote(title: String) {
        check(title)
        viewModelScope.launch {
            noteDao.deleteByTitle(title = title.trim())
        }
    }


    /**
     * data la key (title) , in caso di errori,per evitare valori ripetuti,si utilizza sempre
     * una sorta di titolo random grazie al timestamp
     */
    private fun getGenericTitle(): String {
        return "TODO -${
            System.currentTimeMillis()
        }"
    }

    /**
     * controllo sulle stringhe
     */
    private fun check(value: String) {
        if (value.trim().isEmpty())
            throw Exception("Invalid string")
    }
}
