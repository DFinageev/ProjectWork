package com.example.projectwork.oldwordsconstruct

import android.app.Application
import androidx.lifecycle.*
import com.example.projectwork.App
import com.example.projectwork.settings.CurrentLanguageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class OldWordsConstructViewModel(app : Application) : AndroidViewModel(app) {
    private var viewModelJob = Job()
    var myApp = app as App
    var word: MutableLiveData<CurrentLanguageData?> = MutableLiveData(CurrentLanguageData(0, "Подождите"))
    var wordText = MutableLiveData(word.value?.word)

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent
    private var _snackbarString = MutableLiveData<String>()
    val snackbarString: LiveData<String>
        get() = _snackbarString
    fun doneShowingSnackbar() {
        _showSnackbarEvent.postValue(false)
    }
    fun startShowingSnackbar() {
        _showSnackbarEvent.postValue(true)
    }
    fun correctAnswerSnackbar() {
        _snackbarString.postValue("Правильно!")
        startShowingSnackbar()
    }
    fun uncorrectAnswerSnackbar() {
        _snackbarString.postValue("Неправильно!")
        startShowingSnackbar()
    }
    fun writeWord(): String {
        return wordText.value as String
    }

    init {
        startingWork()
    }

    fun wordToLetters(parsebleWord : String) : String {
        var letters = mutableListOf<String>()
        for (i in 1..parsebleWord.length)
            letters.add(parsebleWord[i].toString())
        letters.shuffle()
        return letters.joinToString(separator = ",")
    }

    suspend fun getOneWord() {
        if (myApp.studiedWords.count() == 0) {
            word.postValue(CurrentLanguageData(-1, "wait"))
        } else {
            word.postValue(myApp.studiedWords!!.random())
            wordText.postValue(wordToLetters(word.value!!.word))
        }
    }

    private fun startingWork() {
        viewModelScope.launch(Dispatchers.IO) {
            getOneWord()
        }
    }

    fun nextWord(answer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!(("$answer").toLowerCase(Locale.ROOT).toRegex().containsMatchIn(word.value!!.word.toLowerCase()) && (word.value!!.word).toLowerCase(Locale.ROOT).toRegex().containsMatchIn(("$answer").toLowerCase()))){
                word.postValue(myApp.studiedWords!!.find {t -> t.word == word.value!!.word})
                myApp.studiedWords!!.remove(myApp.studiedWords!!.find {t -> t.wordId == word.value!!.wordId})
                myApp.notStudiedWords?.add(word.value!!)
                uncorrectAnswerSnackbar()
                delay(100)
            }
            else {
                correctAnswerSnackbar()
            }
            wordText.postValue("Загрузка слова")
            getOneWord()
            delay(100)
        }
    }
}
