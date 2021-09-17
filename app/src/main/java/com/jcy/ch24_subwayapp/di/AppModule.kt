package com.jcy.ch24_subwayapp.di

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jcy.ch24_subwayapp.data.api.StationApi
import com.jcy.ch24_subwayapp.data.api.StationStorageApi
import com.jcy.ch24_subwayapp.data.repository.StationRepository
import com.jcy.ch24_subwayapp.data.repository.StationRepositoryImpl
import com.jcy.ch24_subwayapp.db.AppDatabase
import com.jcy.ch24_subwayapp.preference.PreferenceManager
import com.jcy.ch24_subwayapp.preference.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule= module{
    single { Dispatchers.IO }

    //Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    //Preference
    single { androidContext().getSharedPreferences("preference",Activity.MODE_PRIVATE) }
    single<PreferenceManager>{ SharedPreferenceManager(get())}

    //Api
    single<StationApi>{ StationStorageApi(Firebase.storage)}

    //Repository
    single<StationRepository>{ StationRepositoryImpl(get(),get(),get(),get())}
}