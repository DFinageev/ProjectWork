package com.example.projectwork

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectwork.database.PolyglotData
import com.example.projectwork.database.PolyglotDatabaseDao
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onDestroy() {
        super.onDestroy()
        var activityJob = Job()
        var myApp = MainActivity().application as App
        val coroutineScope = CoroutineScope(activityJob + Dispatchers.Main )
        coroutineScope.launch(Dispatchers.IO) {
            myApp.languageToBase(myApp.currentLanguage)
            delay(500)
        }
    }

//    override fun onPause() {
//        super.onPause()
//        var activityJob = Job()
//        var myApp = MainActivity().application as App?
//        val coroutineScope = CoroutineScope(activityJob + Dispatchers.Main )
//        coroutineScope.launch(Dispatchers.IO) {
//            myApp?.languageToBase(myApp?.currentLanguage)
//        }
//    }

//    override fun onStop() {
//        super.onStop()
//        var activityJob = Job()
//        var myApp = MainActivity().application as App?
//        val coroutineScope = CoroutineScope(activityJob + Dispatchers.Main )
//        coroutineScope.launch(Dispatchers.IO) {
//            myApp?.languageToBase(myApp?.currentLanguage)
//        }
//    }
}
