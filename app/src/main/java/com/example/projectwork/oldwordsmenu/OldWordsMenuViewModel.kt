package com.example.projectwork.oldwordsmenu

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.projectwork.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OldWordsMenuViewModel(app : Application) : ViewModel() {
    // TODO: Implement the ViewModel

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    var myApp = app as App

//    override fun onCleared() {
//        coroutineScope.launch(Dispatchers.IO) {
//            myApp.languageToBase(myApp.currentLanguage)
//        }
//        super.onCleared()
//    }
}
