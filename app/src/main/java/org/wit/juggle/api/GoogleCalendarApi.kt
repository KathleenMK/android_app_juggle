package org.wit.juggle.api

import org.wit.juggle.models.CalendarList
import org.wit.juggle.models.EventList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import org.wit.juggle.R
import retrofit2.http.Header


interface GoogleCalendarApi {

    //@Headers(value = ["Authorization:Bearer "])

    @GET("users/me/calendarList")
    suspend fun getCalendarList(): Response<CalendarList>
    //suspend fun getCalendarList(@Header("Authorization") token : String): Response<CalendarList>  //wk 9

    //@Headers(value = ["Authorization:Bearer "])

    @GET("calendars/{calendarId}/events")
    suspend fun getCalendarEventList(): Response<EventList>

//    @GET("calendars/rxd842@gmail.com/events")
//    suspend fun getCalendarEventList(): Response<EventList>

// attempt to get at the access token, currently hardcoded above
//    @Headers(value = ["grant_type: authorization_code",
//        "client_id: ***",
//        "client_secret: ",
//        "redirect_uri: ",
//        "code: ***"])
//
//    @POST("token")
//    suspend fun getQuotes() : Response<CalendarList>
}
