package com.jcy.ch24_subwayapp.di

import android.app.Activity
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jcy.ch24_subwayapp.data.api.StationApi
import com.jcy.ch24_subwayapp.data.api.StationArrivalsApi
import com.jcy.ch24_subwayapp.data.api.StationStorageApi
import com.jcy.ch24_subwayapp.data.api.Url
import com.jcy.ch24_subwayapp.data.repository.StationRepository
import com.jcy.ch24_subwayapp.data.repository.StationRepositoryImpl
import com.jcy.ch24_subwayapp.db.AppDatabase
import com.jcy.ch24_subwayapp.preference.PreferenceManager
import com.jcy.ch24_subwayapp.preference.SharedPreferenceManager
import com.jcy.ch24_subwayapp.presentation.stationarrivals.StationArrivalsContract
import com.jcy.ch24_subwayapp.presentation.stationarrivals.StationArrivalsFragment
import com.jcy.ch24_subwayapp.presentation.stationarrivals.StationArrivalsPresenter
import com.jcy.ch24_subwayapp.presentation.stations.StationsContract
import com.jcy.ch24_subwayapp.presentation.stations.StationsFragment
import com.jcy.ch24_subwayapp.presentation.stations.StationsPresenter
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule= module{
    single { Dispatchers.IO }

    //Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    //Preference
    single { androidContext().getSharedPreferences("preference",Activity.MODE_PRIVATE) }
    single<PreferenceManager>{ SharedPreferenceManager(get())}

    //Api
    single{
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG){
                        HttpLoggingInterceptor.Level.BODY
                    }else{
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            ).build()
    }
    single<StationArrivalsApi>{
        Retrofit.Builder().baseUrl(Url.SEOUL_DATA_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }
    single<StationApi>{ StationStorageApi(Firebase.storage)}

    //Repository
    single<StationRepository>{ StationRepositoryImpl(get(),get(),get(),get(),get())}

    //Presentation
    scope<StationsFragment> {
        scoped<StationsContract.Presenter>{
            StationsPresenter(getSource(),get())
        }
    }
    scope<StationArrivalsFragment> {
        scoped<StationArrivalsContract.Presenter> { StationArrivalsPresenter(getSource(), get(), get()) }
    }
}