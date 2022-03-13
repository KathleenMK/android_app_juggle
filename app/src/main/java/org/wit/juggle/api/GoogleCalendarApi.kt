package org.wit.juggle.api

import org.wit.juggle.models.CalendarListModel
import retrofit2.http.GET
import retrofit2.http.Headers
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.models.EventModel
import retrofit2.Call
import retrofit2.http.Path


interface GoogleCalendarApi {

    @Headers(value = ["Authorization:Bearer ya29.A0ARrdaM8ePGeFhTCQJQcFgjL4tKvuZZKbc42yaNDIMxBGl4i_p3_Ng_M_lzV5Fs7Kqng65-q7GMH_HQSH45vkOsl_uoR5bfMR_MOqTCRSVe-4IUkimOVbsn-FpnROv6lERivkfJGjA_6MbNo4bjBMBZUiuPkv_g"])
    @GET("users/me/calendarList")
    fun getCalendars(): Call<CalendarListModel>
    //suspend fun getCalendarList(@Header("Authorization") token : String): Response<CalendarListModel>  //wk 9

    //@Headers(value = ["Authorization:Bearer "])

    @GET("calendars/{calendarId}/events")
    fun getCalendarEvents(@Path("calendarId") calendarId:String): Call<List<EventModel>>

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
//    suspend fun getQuotes() : Response<CalendarListModel>
}
