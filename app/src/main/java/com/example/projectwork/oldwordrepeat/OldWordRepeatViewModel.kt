package com.example.projectwork.oldwordrepeat

import android.app.Application
import androidx.lifecycle.*
import com.example.projectwork.App
import com.example.projectwork.network.SingleWord
import com.example.projectwork.settings.CurrentLanguageData
import kotlinx.coroutines.*

class OldWordRepeatViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val myApp = application as App

    val WordString = myApp.dictWord.word
    val TranscryptionString = myApp.dictWord.transcrypt
    val TranslationString = myApp.dictWord.translation

    val wordText = "Word"

    private val dao = myApp.database.polyglotDatabaseDao


    var intWord: MutableLiveData<SingleWord?> = MutableLiveData(SingleWord(0, "Wait", "Wait", "Wait", "http://mmcspolyglot.mcdir.ru/images/default_picture.jpg"))
    var word: MutableLiveData<CurrentLanguageData?> = MutableLiveData(CurrentLanguageData(1, "wait"))

    init {
        startingWork()
    }

    fun getWord(id : Long){
        word.postValue(myApp.studiedWords!!.find { t -> t.wordId == id })
    }
//    suspend fun getOneWord() {
//        word.postValue(dao.getFirstWord(myApp.currentLanguage))
//    }

    suspend fun getOneWordInternet() {
        intWord.postValue((myApp.currentLanguage + 1).let { word?.value?.wordId?.let { it1 ->
            myApp.remoteService.getWordInfo(it,
                it1
            )
        } })

    }

    private fun startingWork() {
        coroutineScope.launch(Dispatchers.IO) {
//            word.postValue(myApp.studiedWords!!.random())
            delay(100)
            getOneWordInternet()
        }
    }

    fun splittingWord() = buildString {
        for (line in intWord.value?.translation?.split(';')!!)
            append(line + "\n")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
