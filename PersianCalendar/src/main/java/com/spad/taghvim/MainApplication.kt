package com.spad.taghvim

import android.app.Application
import com.spad.taghvim.utils.initUtils

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ReleaseDebugDifference.mainApplication(this)
        initUtils(applicationContext)
    }
}
