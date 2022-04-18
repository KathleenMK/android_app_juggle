package org.wit.juggle.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import org.wit.juggle.R
import timber.log.Timber
import java.util.concurrent.TimeUnit

val tempAc = R.string.temp_bearer_access_token

object RetrofitHelper {

    val baseUrl = "https://www.googleapis.com/calendar/v3/"

    // initial retrofit attempt based on: https://www.geeksforgeeks.org/retrofit-with-kotlin-coroutine-in-android/ visited 05Mar22
    fun getApi(): GoogleCalendarApi {

        val gson = GsonBuilder().create()

        var client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        Timber.i("tempAC" + tempAc.toString())

        val apiInterface = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return apiInterface.create(GoogleCalendarApi::class.java)
    }
}
