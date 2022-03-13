package org.wit.juggle.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.Request
import org.wit.juggle.R
import timber.log.Timber


object RetrofitHelper {

    val baseUrl = "https://www.googleapis.com/calendar/v3/"

   val temp = "Bearer ya"

//https://stackoverflow.com/questions/41078866/retrofit2-authorization-global-interceptor-for-access-token/41082979#41082979 12Mar22
    var client = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization" , "Bearer ya29")
            .build()
        chain.proceed(newRequest)
    }.build()



    fun getInstance(): Retrofit {
        return Retrofit.Builder()
           .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}