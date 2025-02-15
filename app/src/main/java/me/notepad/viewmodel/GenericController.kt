package me.notepad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * controller generico per tutte le operazioni di salvataggi scomodi da fare nel view e login
 */
class GenericController : ViewModel() {

    /**
     * utilizzati per scambio dati tra le funzioni della NoteActivity
     */
    private val _currentTitle = MutableLiveData("")
    val currentTitle: LiveData<String> get() = _currentTitle

    private val _currentContent = MutableLiveData("")
    val currentContent: LiveData<String> get() = _currentContent


    private val innerPassword: String = "4848"

    private val _loggedIn = MutableLiveData(false)

    /**
     * valore pubblico che ascolta quello privato
     */
    val loggedIn: LiveData<Boolean> get() = _loggedIn

    fun login(password: String) {
        if (password.trim().lowercase() == this.innerPassword) {
            _loggedIn.value = true
        }
    }


    /**
     * ENTRAMBE LE FUNZIONI SENZA TRIM,altrimenti l'utente non può mettere spazi
     */

    fun setCurrentTitle(currentTitle:String){
        _currentTitle.value=currentTitle
    }

    fun setCurrentContent(currentContent:String){
        _currentContent.value=currentContent
    }

}