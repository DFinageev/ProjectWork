package com.example.projectwork.oldwordsmenu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectwork.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OldWordsMenuViewModel(app : Application) : AndroidViewModel(app) {
    // TODO: Implement the ViewModel

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    var myApp = app as App

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }
    fun startShowingSnackbar() {
        _showSnackbarEvent.value = true
    }

    val isOldWords: Boolean
        get() = myApp.studiedWords.count() > 0

//    override fun onCleared() {
//        coroutineScope.launch(Dispatchers.IO) {
//            myApp.languageToBase(myApp.currentLanguage)
//        }
//        super.onCleared()
//    }
}
