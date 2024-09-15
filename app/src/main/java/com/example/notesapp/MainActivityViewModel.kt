package com.example.notesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {

    private val repository = Repository()

    private val _notes = MutableStateFlow(emptyList<Note>())
    internal val notes: StateFlow<List<Note>> = _notes

    private val _note = MutableStateFlow("")
    internal val note: StateFlow<String> = _note

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val results = repository.getNotes()
            withContext(Dispatchers.Main) {
                _notes.value = results
            }
        }
    }

    fun addNote(newNote: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val insertedNote = repository.addNote(newNote)
            withContext(Dispatchers.Main) {
                _notes.value += insertedNote
            }
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(noteId)
            withContext(Dispatchers.Main) {
                // Not doing another fetch call to reduce the number of API call
                // This is just to remove the deleted note from the state flow
                _notes.value = _notes.value.filter { it.id != noteId }
            }
        }

    }

}