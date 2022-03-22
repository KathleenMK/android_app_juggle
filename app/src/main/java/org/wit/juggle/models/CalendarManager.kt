package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData
import org.wit.juggle.api.EventWrapper
import org.wit.juggle.api.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object CalendarManager : CalendarStore {


    override fun findCalendars(calendars: MutableLiveData<List<CalendarModel>>) {

        val call = RetrofitHelper.getApi().getCalendars()

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

    override fun findCalendarEvents(calendar: CalendarModel, events: MutableLiveData<List<EventModel>>) {

        val call = RetrofitHelper.getApi().getCalendarEvents(calendar.id)

        call.enqueue(object : Callback<EventListModel> {

            override fun onResponse(
                call: Call<EventListModel>,
                response: Response<EventListModel>
            ) {
                val eventListModelValue = response.body() as EventListModel
                events.value = eventListModelValue.items as ArrayList<EventModel>
                Timber.i("Retrofit JSON = ${response.body()}")
            }

            override fun onFailure(call: Call<EventListModel>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })

    }

    override fun addRelatedEvent(event: AddEventModel) {

        val call = RetrofitHelper.getApi().addRelatedEvent(event)
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