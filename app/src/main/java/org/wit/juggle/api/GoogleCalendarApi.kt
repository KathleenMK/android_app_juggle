package org.wit.juggle.api

import org.wit.juggle.models.CalendarListModel
import retrofit2.http.GET
import retrofit2.http.Headers
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.models.EventListModel
import org.wit.juggle.models.EventModel
import retrofit2.Call
import retrofit2.http.Path


interface GoogleCalendarApi {

    @Headers(value = ["Authorization:Bearer "])
    @GET("users/me/calendarList")
    fun getCalendars(): Call<CalendarListModel>
    //suspend fun getCalendarList(@Header("Authorization") token : String): Response<CalendarListModel>  //wk 9


    @Headers(value = ["Authorization:Bearer "])
    @GET("calendars/{calendarId}/events")
    fun getCalendarEvents(@Path("calendarId") calendarId:String): Call<EventListModel>

//    @Headers(value = ["Authorization:Bearer "])
//    @GET("calendars/{calendarId}/events/{eventId}")
//    fun getEvent(@Path("calendarId") calendarId:String, @Path("eventId") eventId:String): Call<EventModel>

// attempt to get at the access token, currently hardcoded above
//    @Headers(value = ["grant_type: authorization_code",
//        "client_id: ***",
//        "client_secret: ",
//        "redirect_uri: ",
//        "code: ***"])
//
//    @POST("token")
//    suspend fun getQuotes() : Response<CalendarListModel>
}
