package com.example.projectwork

import android.app.Application
import android.util.Log
import com.example.projectwork.database.PolyglotData
import com.example.projectwork.database.PolyglotDatabase
import com.example.projectwork.network.PolyglotService
import com.example.projectwork.settings.CurrentLanguageData
import com.example.projectwork.settings.LanguageData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class App : Application() {

    lateinit var userPreferences: UserPreferences
    lateinit var repository: Repository
    lateinit var database : PolyglotDatabase
    val remoteService : PolyglotService by lazy {
        PolyglotService()
    }

    data class Word(var word : String, var transcrypt: String, var translation: String)

    var currentLanguage: Long = 0
    var dictWord = Word("unknown", "unknown", "unknown")
    lateinit var allLanguages: MutableList<LanguageData>
    var studiedWords: MutableList<CurrentLanguageData> = mutableListOf()
    var notStudiedWords: MutableList<CurrentLanguageData> = mutableListOf()

//    suspend fun getLangs() {
//        coroutineScope.launch {
//            var langs = remoteService.getListOfLanguages()
//            try {
//                val listResult = langs
//            }
//        }
//    }

    suspend fun getLangsResponse() {
        var listResult = remoteService.getListOfLanguages()
//        delay(500)
        val languages = listResult!!.languages.split(delimiters = *charArrayOf(';'))
        val wordsCount = listResult!!.wordsCount.split(delimiters = *charArrayOf(';'))
        allLanguages = mutableListOf()
        for (i in 0 until listResult!!.count) {
            allLanguages.add(
                LanguageData(
                    i + 1,
                    languages[(i).toInt()],
                    wordsCount[(i).toInt()].toLong()
                )
            )
        }
    }

    fun refreshCurrentWords(notStudied: List<CurrentLanguageData>) {
        if (notStudiedWords.isEmpty()) {
            notStudiedWords = mutableListOf()
        }
        notStudiedWords.addAll(notStudied)
    }

    fun setLanguage(langInfo: PolyglotData) {
        notStudiedWords = mutableListOf()
        studiedWords = mutableListOf()

        val tempIds1 = langInfo.notStudiedWordIds.split(delimiters = *arrayOf((";"))).toList()
        val tempIds2 = langInfo.studiedWordIds.split(delimiters = *arrayOf((";"))).toList()
        val tempWords1 = langInfo.notStudiedOriginalWords.split(delimiters = *arrayOf((";"))).toList()
        val tempWords2 = langInfo.studiedOriginalWords.split(delimiters = *arrayOf((";"))).toList()
        notStudiedWords = MutableList(langInfo.notStudiedCount.toInt()) { i -> CurrentLanguageData(
            tempIds1[i].toLong(),
            tempWords1[i]
        )}
        studiedWords = MutableList(langInfo.studiedCount.toInt()) { i -> CurrentLanguageData(
            tempIds2[i].toLong(),
            tempWords2[i]
        )}

    }

    suspend fun languageToBase(languageNumber: Long) {
        studiedWords.sortBy { it.wordId }
        notStudiedWords.sortBy { it.wordId }
        val languageBaseFormat = PolyglotData(
            uniqueId = languageNumber + 1,
            studiedWordIds = studiedWords.joinToString(separator = ";") { t -> t.wordId.toString()},
            notStudiedWordIds = notStudiedWords.joinToString(separator = ";") { t -> t.wordId.toString() },
            studiedOriginalWords = studiedWords.joinToString(separator = ";") { t -> t.word },
            notStudiedOriginalWords = notStudiedWords.joinToString(separator = ";") { t -> t.word },
            studiedCount = studiedWords.count().toLong(),
            notStudiedCount = notStudiedWords.count().toLong(),
            allCount = studiedWords.count().toLong() + notStudiedWords.count().toLong()
        )

        Log.d("App", "sendToBase = $languageBaseFormat")
        studiedWords = mutableListOf()
        notStudiedWords = mutableListOf()
        database.polyglotDatabaseDao.update(languageBaseFormat)
    }

//    suspend fun inserts(begin: Long, finish: Long) {
//        val base : PolyglotDatabaseDao = database.polyglotDatabaseDao
//        for (i in begin..finish) {
//            base.insert(PolyglotData(0, currentLanguage, i, "not_studied_yet", false))
//        }
//    }

    override fun onCreate() {
        super.onCreate()
        userPreferences = UserPreferences(applicationContext)
        repository = Repository(userPreferences)
        //добавил database в App
        database = PolyglotDatabase.getInstance(this)
        //TODO Запрос на получение списка языков и сравнение с текущей базой
        //allLanguages = listOf(LanguageData(1, "lang1", 0), LanguageData(2, "lang2", 0))


    }
}