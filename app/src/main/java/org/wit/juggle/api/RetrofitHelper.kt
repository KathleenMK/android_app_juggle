package org.wit.juggle.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.Request
import org.wit.juggle.R
import timber.log.Timber
import java.util.concurrent.TimeUnit


object RetrofitHelper {

    val baseUrl = "https://www.googleapis.com/calendar/v3/"

   val temp = "Bearer ya"

    fun getApi() : GoogleCalendarApi {

        val gson = GsonBuilder().create()

//https://stackoverflow.com/questions/41078866/retrofit2-authorization-global-interceptor-for-access-token/41082979#41082979 12Mar22
        var client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


//        fun getInstance(): Retrofit {
//            return Retrofit.Builder()
//                .client(client)
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                // we need to add converter factory to
//                // convert JSON object to Java object
//                .build()

        val apiInterface = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return apiInterface.create(GoogleCalendarApi::class.java)
        }
    }
