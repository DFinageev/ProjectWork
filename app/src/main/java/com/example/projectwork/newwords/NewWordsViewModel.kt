package com.example.projectwork.newwords

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projectwork.App
import com.example.projectwork.database.PolyglotDatabaseDao
import com.example.projectwork.network.SingleWord
import com.example.projectwork.settings.CurrentLanguageData
import kotlinx.coroutines.*

class NewWordsViewModel(app : Application) : AndroidViewModel(app) {
    private val myApp = app as App
    //предпочтительно вот так получать dao
    private val database : PolyglotDatabaseDao = myApp.database.polyglotDatabaseDao
    private val remoteService = myApp.remoteService

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )


    var intWord: MutableLiveData<SingleWord?> = MutableLiveData(SingleWord(0, "Wait", "Wait", "Wait", "http://mmcspolyglot.mcdir.ru/images/default_picture.jpg"))
    var word: MutableLiveData<CurrentLanguageData?> = MutableLiveData(CurrentLanguageData(1, "wait"))

    init {
        startingWork()
    }

    fun getOneWord() {
        if (myApp.notStudiedWords.count() == 0) {
            word.postValue(null)
        } else {
            word.postValue(myApp.notStudiedWords!![0])
        }
    }

    suspend fun getOneWordInternet() {
        intWord.postValue((myApp.currentLanguage + 1).let { word?.value?.wordId?.let { it1 ->
            myApp.remoteService.getWordInfo(it,
                it1
            )
        } })

    }

    private fun startingWork() {
        viewModelScope.launch(Dispatchers.IO) {
            getOneWord()
            delay(200)
            getOneWordInternet()

        }
    }

    fun splittingWord() = buildString {
        for (line in intWord.value?.translation?.split(';')!!)
            append(line + "\n")
    }

    fun nextWord() {
        viewModelScope.launch(Dispatchers.IO) {
            word?.value?.word = intWord?.value?.originWord ?: "not_studied_yet"
            myApp.notStudiedWords!!.remove(myApp.notStudiedWords!!.find {t -> t.wordId == word.value!!.wordId})
            myApp.studiedWords?.add(word.value!!)
            val temp1 = myApp.studiedWords
            val temp2 = myApp.notStudiedWords
            Log.d("AppData", "studiedWords = $temp1")
            Log.d("AppData", "notStudiedWords = $temp2")
            delay(100)
            getOneWord()
            delay(100)
            getOneWordInternet()
//            intWord.postValue(word?.value?.langId?.let { word?.value?.wordId?.let { it1 ->
//                myApp.remoteService.getWordInfo(it,
//                    it1
//                )
//            } })

        }
    }

//    override fun onCleared() {
//        coroutineScope.launch(Dispatchers.IO) {
//            myApp.languageToBase(myApp.currentLanguage)
//        }
//        super.onCleared()
//    }
}
