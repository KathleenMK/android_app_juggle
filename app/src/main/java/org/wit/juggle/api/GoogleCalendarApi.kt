package org.wit.juggle.api

import org.wit.juggle.models.CalendarList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface GoogleCalendarApi {

    @Headers(value = ["Authorization:Bearer ya29.A0ARrdaM-yA7QCSsmRWUj-NOGJqIkMc_9yhYTfg_kgH5ssUmGMHL_hE9h0yu4EeTxMIQ7_eUmKFmZ8ve5ch8YwlRzYI5BspZDauPOmHxp46ii1-nJan6XLIIhrva0Ay2osBLfSDaYdREceUQf1I6NE8CGdMbwS"])

    @GET("calendar/v3/users/me/calendarList")
suspend fun getCalendarList() : Response<CalendarList>

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
