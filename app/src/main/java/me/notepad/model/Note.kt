package me.notepad.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * struttura di tutta la nostra tabella sqlite,
 * TITOLO UNIVOCO
 */
@Entity(tableName = "note_database", indices = [Index(value = ["title"], unique = true)])
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()

)
