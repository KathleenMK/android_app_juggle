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

val tempAc = R.string.temp_bearer_access_token

object RetrofitHelper {

    val baseUrl = "https://www.googleapis.com/calendar/v3/"



    fun getApi() : GoogleCalendarApi {

        val gson = GsonBuilder().create()

//https://stackoverflow.com/questions/41078866/retrofit2-authorization-global-interceptor-for-access-token/41082979#41082979 12Mar22
        var client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


        Timber.i("tempAC"+tempAc.toString())

        val apiInterface = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
//            .client(OkHttpClient.Builder().addInterceptor { chain ->
//            val request = chain.request().newBuilder().addHeader("Authorization", "Bearer $tempAc").build()
//            chain.proceed(request)
//        }.build())
            .build()
        return apiInterface.create(GoogleCalendarApi::class.java)
        }
    }
