package com.example.projectwork.oldwords

import android.app.Application
import androidx.lifecycle.*
import com.example.projectwork.App
import com.example.projectwork.database.PolyglotData
import com.example.projectwork.database.PolyglotDatabaseDao
import com.example.projectwork.network.SingleWord
import com.example.projectwork.settings.CurrentLanguageData
import kotlinx.coroutines.*
import java.util.*

class OldWordsViewModel(app : Application) : AndroidViewModel(app) {
    private val database : PolyglotDatabaseDao = (app as App).database.polyglotDatabaseDao
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var myApp = app as App
    private lateinit var okWords: MutableLiveData<List<PolyglotData?>>

    var intWord: MutableLiveData<SingleWord?> = MutableLiveData(SingleWord(0, "Wait", "Wait", "Wait", "http://mmcspolyglot.mcdir.ru/images/default_picture.jpg"))
    var word: MutableLiveData<CurrentLanguageData?> = MutableLiveData(CurrentLanguageData(1, "wait"))
    var resultText = ""

    init {
        startingWork()
    }

    fun getOneWord() {
        if (myApp.studiedWords.count() == 0){
            word.postValue(null)
        }else{
            word.postValue(myApp.studiedWords!!.random())
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
        coroutineScope.launch(Dispatchers.IO) {
            getOneWord()
            delay(100)
            getOneWordInternet()
        }
    }

//    fun splittingWord() = buildString {
//        for (line in intWord.value?.translation?.split(';')!!)
//            append(line + "\n")
//    }

    fun nextWord(answer: String) {
        coroutineScope.launch(Dispatchers.IO) {
            if (!answer.toLowerCase(Locale.ROOT).toRegex().containsMatchIn(intWord.value!!.translation.toLowerCase(Locale.ROOT))) {
                word.postValue(myApp.notStudiedWords!!.find {t -> t.word == word.value!!.word})
                myApp.studiedWords!!.remove(myApp.studiedWords!!.find {t -> t.wordId == word.value!!.wordId})
                myApp.notStudiedWords?.add(word.value!!)
                resultText = "Неправильно!"
                delay(100)
            }
            else {
                resultText = "Правильно!"
            }
            getOneWord()
            delay(500)
            getOneWordInternet()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
