package com.example.wrlpages

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
//        context = applicationContext
        context = this
    }

    companion object {
//        var context: Context? = null
        lateinit var context: Application
    }

}