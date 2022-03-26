package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData
import org.wit.juggle.api.EventWrapper
import org.wit.juggle.api.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object CalendarManager : CalendarStore {


    override fun findCalendars(token:String, calendars: MutableLiveData<List<CalendarModel>>) {

        val call = RetrofitHelper.getApi().getCalendars(token)

        call.enqueue(object : Callback<CalendarListModel> {

            override fun onResponse(
                call: Call<CalendarListModel>,
                response: Response<CalendarListModel>
            ) {
                val calendarListModelValue = response.body() as CalendarListModel
                calendars.value = calendarListModelValue.items as ArrayList<CalendarModel>
               Timber.i("Retrofit JSON = ${response.body()}")
            }

            override fun onFailure(call: Call<CalendarListModel>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })

    }

    override fun findCalendarEvents(token:String, calendar: CalendarModel, events: MutableLiveData<List<EventModel>>) {

        val call = RetrofitHelper.getApi().getCalendarEvents(token, calendar.id)

        call.enqueue(object : Callback<EventListModel> {

            override fun onResponse(
                call: Call<EventListModel>,
                response: Response<EventListModel>
            ) {
                val eventListModelValue = response.body() as EventListModel
                val eventListItems = eventListModelValue.items.reversed() as ArrayList<EventModel>
                events.value = eventListItems

                Timber.i("Retrofit JSON = ${response.body()}")
            }

            override fun onFailure(call: Call<EventListModel>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })

    }

    override fun addRelatedEvent(token:String, calendarId: String, event: AddEventModel) {

        val call = RetrofitHelper.getApi().addRelatedEvent(token, calendarId, event)
        Timber.i(event.toString())
        call.enqueue(object : Callback<EventWrapper> {
            override fun onResponse(call: Call<EventWrapper>,
                                    response: Response<EventWrapper>
            ) {
                Timber.i(response.body().toString())
                val eventWrapper = response.body()
                if (eventWrapper != null) {
                    Timber.i("Retrofit ${eventWrapper.message}")
                    Timber.i("Retrofit ${eventWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<EventWrapper>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
                Timber.i("Retrofit create Error : $t.message")
            }
        })
    }
}