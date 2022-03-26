package org.wit.juggle.api

import org.wit.juggle.models.*
import retrofit2.Call
import retrofit2.http.*


interface GoogleCalendarApi {

    @GET("users/me/calendarList")
    fun getCalendars(@Header("Authorization") token : String): Call<CalendarListModel>

    @GET("calendars/{calendarId}/events")
    fun getCalendarEvents(@Header("Authorization") token : String, @Path("calendarId") calendarId:String): Call<EventListModel>

    @POST("calendars/{calendarId}/events")
    fun addRelatedEvent(@Header("Authorization") token : String, @Path("calendarId") calendarId:String, @Body event: AddEventModel): Call<EventWrapper>



// attempt to get at the access token, currently hardcoded in secret strings
//    @Headers(value = ["grant_type: authorization_code",
//        "client_id: ***",
//        "client_secret: ",
//        "redirect_uri: ",
//        "code: ***"])
//
//    @POST("token")
//    suspend fun getQuotes() : Response<CalendarListModel>
}
