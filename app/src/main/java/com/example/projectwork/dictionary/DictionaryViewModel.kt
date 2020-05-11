package com.example.projectwork.dictionary

import android.app.Application
import androidx.lifecycle.*
import com.example.projectwork.App
import com.example.projectwork.database.PolyglotDatabaseDao
import com.example.projectwork.settings.CurrentLanguageData
import kotlinx.coroutines.*

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    private val myApp = application as App
    private val database : PolyglotDatabaseDao = myApp.database.polyglotDatabaseDao
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val stub : LiveData<List<CurrentLanguageData>> = liveData {
        emit(listOf(CurrentLanguageData(1, word = "wait")))
    }
    var okWords: MutableLiveData<List<CurrentLanguageData>> = stub as MutableLiveData<List<CurrentLanguageData>>

    init {
        getValues()
    }

    private fun getValues() {
        okWords.postValue(myApp.studiedWords)
    }

    private fun fillDictionary() {
        coroutineScope.launch(Dispatchers.IO) {
            getValues()
        }
    }

}
