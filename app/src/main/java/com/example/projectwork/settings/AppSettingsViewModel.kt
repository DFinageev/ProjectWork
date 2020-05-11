package com.example.projectwork.settings

import android.app.Application
import androidx.lifecycle.*
import com.example.projectwork.App
import com.example.projectwork.network.ListOfLanguages
import kotlinx.coroutines.*

class AppSettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val myApp = application as App
    private var viewModelJob = Job()
    private val remoteService = myApp.remoteService

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val stub : LiveData<List<LanguageData>> = liveData {
        emit(myApp.allLanguages.toList())
    }
    var listResult : MutableLiveData<ListOfLanguages?> = MutableLiveData(ListOfLanguages(1, "English", "1000"))
//    var languages = stub//Запрос в интернет
    val languages = stub

    init {
        startLangs()
    }

    private fun startLangs() {
        coroutineScope.launch(Dispatchers.IO) {
            listResult.postValue(remoteService.getListOfLanguages())
//            languages = liveData {
//                listOf(1, listResult!!.value!!.languages, listResult!!.value!!.count)
//            }

        }
    }

    fun languageToBase(languageNumber: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            myApp.languageToBase(languageNumber)
        }
    }

    fun changeLangs() {
//        languages = liveData {
//            listOf(1, listResult!!.value!!.languages, listResult!!.value!!.count)
//        }
    }

}
