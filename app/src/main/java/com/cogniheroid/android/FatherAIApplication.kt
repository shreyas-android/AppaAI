package com.cogniheroid.android

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.androidai.android.appa.ai.BuildConfig
import com.cogniheroid.framework.feature.appaai.FatherAICore
import com.cogniheroid.framework.shared.core.fatherai.data.enum.GeminiAIModel

class FatherAIApplication : Application(), Application.ActivityLifecycleCallbacks,
                            LifecycleObserver {

    lateinit var  pref: SharedPreferences

    lateinit var prefEditor: SharedPreferences.Editor


    companion object {
        lateinit var INSTANCE: FatherAIApplication

        private val prefName = "app_ai_app_pref"


    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        pref = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        prefEditor = pref.edit()

        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        FatherAICore.init(this, BuildConfig.DEBUG, BuildConfig.APPA_AI_GEMINI_API_KEY,
            GeminiAIModel.GEMINI_PRO.modelName)
    }


    /** Inner class that loads and shows app open ads. */



    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

}