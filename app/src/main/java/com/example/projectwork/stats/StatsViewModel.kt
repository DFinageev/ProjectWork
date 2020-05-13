package com.example.projectwork.stats

import android.app.Application
import androidx.lifecycle.*
import com.example.projectwork.App
import com.example.projectwork.database.PolyglotDatabaseDao
import kotlinx.coroutines.*

class StatsViewModel(app : Application) : AndroidViewModel(app){

    private val database : PolyglotDatabaseDao = (app as App).database.polyglotDatabaseDao
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    var myApp = app as App
//    private var okWords = database.getStudiedWords(mApp.currentLanguage)
//    private var allWords = database.getWords(mApp.currentLanguage)
//    private val allLangs = mApp.allLanguages

    var studiedWordsAmount = MutableLiveData(0)
    var wordsAmount = MutableLiveData(0)
    var wordsLeft = MutableLiveData(wordsAmount.value?.minus(studiedWordsAmount.value!!))
    var studiedWordsAmountString = MutableLiveData("Изучено слов: идёт подсчёт")
    var wordsAmountString = MutableLiveData("Всего слов: идёт подсчёт")
    var wordsLeftString = MutableLiveData("Осталось слов: идёт подсчёт")

    var getCurrentLanguage = MutableLiveData("Текущий язык: ")

    init {
        startLangs()
    }

    suspend fun getValues() {
        var okWords = myApp.studiedWords!!.count()
        var allWords = myApp.notStudiedWords!!.count() + okWords
//        delay(500)
        studiedWordsAmount.postValue(okWords)
        wordsAmount.postValue(allWords)
        wordsLeft.postValue(allWords - okWords)
        delay(100)
    }

    private fun startLangs() {
        coroutineScope.launch(Dispatchers.IO) {
//            var okWords = database.countStudiedWords(mApp.currentLanguage)
//            var allWords = database.countWords(mApp.currentLanguage)
//            delay(20000)
//            studiedWordsAmount.postValue(okWords)
//            wordsAmount.postValue(allWords)
//            wordsLeft.postValue(wordsAmount.value?.minus(studiedWordsAmount.value!!))
//            getCurrentLanguage = "Текущий язык " + mApp.allLanguages[(mApp.currentLanguage - 1).toInt()].toString()
            getValues()
            getCurrentLanguage.postValue("Текущий язык: " + myApp.allLanguages[(myApp.currentLanguage).toInt()].language)
            studiedWordsAmountString.postValue("Изучено слов: " + studiedWordsAmount.value.toString())
            wordsAmountString.postValue("Всего слов: " + wordsAmount.value.toString())
            wordsLeftString.postValue("Осталось слов: " + wordsLeft.value.toString())
        }
    }

    override fun onCleared() {
        coroutineScope.launch(Dispatchers.IO) {
            myApp.languageToBase(myApp.currentLanguage)
        }
        super.onCleared()
        viewModelJob.cancel()
    }
}
