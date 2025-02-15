package me.notepad.model

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Tutto il database viene autogestito e creato da solo
 * all'interno delle annotazioni,ci saranno le query per il sqlite
 * TUTTE CHIAMATE ASINCRONE
 */
@Dao
interface NotesDao {

    @Query("SELECT * FROM note_database ORDER BY timestamp DESC")
    fun selectAllLive(): LiveData<List<Note>> // Cambiato per aggiornarsi automaticamente

    @Insert
    suspend fun insert(note:Note)

    /**
     * si rinnova il timestamp per l'ordinamento
     */
    @Query("UPDATE note_database SET title = :newTitle, content = :newContent,timestamp = :newTimestamp WHERE title = :oldTitle")
    suspend fun updateNoteByTitle(oldTitle: String, newTitle: String, newContent: String,newTimestamp:Long)


    @Query("DELETE FROM note_database WHERE title = :title")
    suspend fun deleteByTitle(title: String)


}