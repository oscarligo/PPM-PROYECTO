package com.example.ppm_proyecto.core.di

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AttendanceAPP : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

    }
}