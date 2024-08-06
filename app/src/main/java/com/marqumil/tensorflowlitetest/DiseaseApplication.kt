package com.marqumil.tensorflowlitetest

import android.app.Application
import com.marqumil.tensorflowlitetest.data.AppContainer
import com.marqumil.tensorflowlitetest.data.DefaultAppContainer

class DiseaseApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}