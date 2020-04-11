package com.example.projectwork.oldwords

import android.app.Application
import androidx.lifecycle.*
import com.example.projectwork.App
import com.example.projectwork.database.PolyglotData
import com.example.projectwork.database.PolyglotDatabaseDao
import com.example.projectwork.network.SingleWord
import kotlinx.coroutines.*
import java.util.*
import kotlin.random.Random
import kotlin.random.nextLong

class OldWordsViewModel(app : Application) : AndroidViewModel(app) {
    private val database : PolyglotDatabaseDao = (app as App).database.polyglotDatabaseDao
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var myApp = app as App
    private lateinit var okWords: MutableLiveData<List<PolyglotData?>>

    var intWord: MutableLiveData<SingleWord?> = MutableLiveData(SingleWord(0, "Wait", "Wait", "Wait", "http://mmcspolyglot.mcdir.ru/images/default_picture.jpg"))
    var word: MutableLiveData<PolyglotData?> = MutableLiveData(PolyglotData(0, 1, 1, "wait", false))

    init {
        startingWork()
    }

    suspend fun getOneWord() {
        if (okWords.value?.isEmpty()!!){
            word.postValue(null)
        }else{
            word.postValue(okWords.value!!.random())
        }
    }

    suspend fun getOneWordInternet() {
        intWord.postValue(word?.value?.langId?.let { word?.value?.wordId?.let { it1 ->
            myApp.remoteService.getWordInfo(it,
                it1
            )
        } })

    }

    private fun startingWork() {
        coroutineScope.launch(Dispatchers.IO) {
            okWords.postValue(database.getWords(myApp.currentLanguage).value)
            delay(10000)
            getOneWord()
            delay(1000)
            getOneWordInternet()
        }
    }

    fun splittingWord() = buildString {
        for (line in intWord.value?.translation?.split(';')!!)
            append(line + "\n")
    }

    fun nextWord(answer: String) {
        coroutineScope.launch(Dispatchers.IO) {
            if (!answer.toRegex().containsMatchIn(intWord.value!!.translation.toLowerCase(Locale.ROOT))) {
                word?.value?.isStudied = false
                word?.let { database.update(it.value!!) }
                delay(500)
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
