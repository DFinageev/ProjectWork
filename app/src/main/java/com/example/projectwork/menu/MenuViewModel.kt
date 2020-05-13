package com.example.projectwork.menu

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.projectwork.App
import com.example.projectwork.database.PolyglotData
import com.example.projectwork.database.PolyglotDatabaseDao
import com.example.projectwork.settings.CurrentLanguageData
import com.example.projectwork.settings.LanguageData
import kotlinx.coroutines.*
//убрал передачу dao через конструктор, чтобы не создавать Factory, потому что это геморой
class MenuViewModel(app : Application) : AndroidViewModel(app) {

//    val repository = (app as App).repository
//    val userName : MutableLiveData<String> = MutableLiveData()
//    val wordsCount : MutableLiveData<Int> = MutableLiveData()
//    val averageMistakes : MutableLiveData<Float> = MutableLiveData()
//    val userPhoto : MutableLiveData<String> = MutableLiveData()

    private val myApp = app as App
    //предпочтительно вот так получать dao
    private val database : PolyglotDatabaseDao = myApp.database.polyglotDatabaseDao
    private val remoteService = myApp.remoteService

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    private lateinit var hereLanguage: LanguageData
    private var okWords: MutableLiveData<List<CurrentLanguageData>> = MutableLiveData(listOf())
    private var notOkWords: MutableLiveData<List<CurrentLanguageData>> = MutableLiveData(listOf())
    var oldWordsButtonVisible = MutableLiveData<Boolean?>(true)
    var newWordsButtonVisible = MutableLiveData<Boolean?>(true)
//    var insertsCount = MutableLiveData(PolyglotData(0, 1, 1, "not_studied_yet", false))

    init {
        startLangs()
    }

//    suspend fun inserts(begin: Long, finish: Long) {
////        insertsCount.postValue(begin.toInt())
//        for (i in begin..finish) {
//            database.insert(PolyglotData(0, myApp.currentLanguage, i, "not_studied_yet", false))
////            insertsCount.postValue(insertsCount.value?.plus(1))
//        }
//    }

    suspend fun insertsLangs(begin: Int, finish: Int) {
        for (i in begin..finish) {
            database.insert(PolyglotData(0, "", "", "", "", 0, 0, 0))
        }
    }


    private fun startLangs() {
        viewModelScope.launch(Dispatchers.IO) {
            myApp.getLangsResponse() //Получение языков из интернета
//            val wordsCheck = database.wordsCount() //Кол-во слов в языках на устройстве
            val langsCheck = database.countLangs() //Кол-во языков на устройстве
            delay(500)
            if (langsCheck < myApp.allLanguages.count()) { //Дополнение языков
                insertsLangs(langsCheck.toInt() + 1, myApp.allLanguages.count())
                delay(100)
            }

            if (myApp.studiedWords.count() == 0 && myApp.notStudiedWords.count() == 0) { //Оба 0 только когда программа только запустилась или поменял язык (проверить)
                val curLanguage =
                    database.getLang(myApp.currentLanguage + 1) //Получаю строку текущего языка
                delay(200)
                Log.d("Menu", "language_string = $curLanguage")
                myApp.setLanguage(curLanguage) //Обрабатываю язык в массивы
                delay(100)
            }

            if (myApp.studiedWords.count() + myApp.notStudiedWords.count() < myApp.allLanguages[(myApp.currentLanguage).toInt()].countWords) { //Дополняю слова в язык
                val newWords = List((myApp.allLanguages[(myApp.currentLanguage).toInt()].countWords - (myApp.studiedWords.count() + myApp.notStudiedWords.count())).toInt()) { i -> CurrentLanguageData(
                    wordId = (myApp.studiedWords.count() + myApp.notStudiedWords.count()).toLong() + i + 1, word = "not_studied_yet")}
                myApp.refreshCurrentWords(newWords)
            }
            delay(300)

            var t = myApp.notStudiedWords.count()
            Log.d("Menu", "notStudiedWords.count() = $t")
            okWords.postValue(myApp.studiedWords)
            notOkWords.postValue(myApp.notStudiedWords)
//            okWords = LiveData(myApp.studiedWords)
//            notOkWords = myApp.notStudiedWords

            val old = Transformations.map(okWords) {
                it?.count()!! > 0
            }
            oldWordsButtonVisible.postValue(old.value)

            val new = Transformations.map(notOkWords) {
                it?.count()!! > 0
            }
            newWordsButtonVisible.postValue(new.value)
        }
    }


    val languages = liveData{ emit(remoteService.getListOfLanguages()) }

    val word = liveData{ emit(remoteService.getWordInfo(1, 1)) }

//    override fun onCleared() {
//        coroutineScope.launch(Dispatchers.IO) {
//            myApp.languageToBase(myApp.currentLanguage)
//        }
//        super.onCleared()
//    }
}