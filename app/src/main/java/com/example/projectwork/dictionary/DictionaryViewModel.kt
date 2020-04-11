package com.example.projectwork.dictionary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.projectwork.App
import com.example.projectwork.database.PolyglotData
import com.example.projectwork.database.PolyglotDatabaseDao
import kotlinx.coroutines.*

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    private val myApp = application as App
    private val database : PolyglotDatabaseDao = myApp.database.polyglotDatabaseDao
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val stub : LiveData<List<PolyglotData>> = liveData {
        emit(listOf(PolyglotData(1, originalWord = "wait")))
    }
    var okWords = stub

    init {
        fillDictionary()
    }

    suspend fun getValues() {
        okWords = database.getWords(myApp.currentLanguage)
    }

    private fun fillDictionary() {
        coroutineScope.launch(Dispatchers.IO) {
            getValues()
            delay(20000)
        }
    }

}
