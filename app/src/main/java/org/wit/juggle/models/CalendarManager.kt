package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData
import org.wit.juggle.api.EventWrapper
import org.wit.juggle.api.RetrofitHelper
import org.wit.juggle.firebaseintegration.FirebaseDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

object CalendarManager : CalendarStore {


    override fun findCalendars(token:String, calendars: MutableLiveData<List<CalendarModel>>) {

        val call = RetrofitHelper.getApi().getCalendars(token)

        call.enqueue(object : Callback<CalendarListModel> {

            override fun onResponse(
                call: Call<CalendarListModel>,
                response: Response<CalendarListModel>
            ) {
                try {
                    val calendarListModelValue = response.body() as CalendarListModel
                    val allCalendars = calendarListModelValue.items as ArrayList<CalendarModel>
                    val ownerCalendars = ArrayList<CalendarModel>()
                    for (cal in allCalendars) {
                        if (cal.accessRole == "owner") {
                            ownerCalendars.add(cal)
                        }

                    }
                    calendars.value = ownerCalendars
                    Timber.i("Retrofit JSON = ${response.body()}")
                }
                catch (e: Exception) {
                    calendars.value = arrayListOf()
                }
            }

            override fun onFailure(call: Call<CalendarListModel>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })

    }

    override fun findCalendarEvents(token:String, calendarId: String, events: MutableLiveData<List<EventModel>>) {

        val call = RetrofitHelper.getApi().getCalendarEvents(token, calendarId)

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

    override fun addRelatedEvent(token:String, eventId: String, calendarId: String, event: AddEventModel) {

        val call = RetrofitHelper.getApi().addRelatedEvent(token, calendarId, event)
        Timber.i(event.toString())
        call.enqueue(object : Callback<EventWrapper> {
            override fun onResponse(call: Call<EventWrapper>,
                                    response: Response<EventWrapper>
            ) {
                Timber.i(response.body().toString())
                val eventWrapper = response.body()
                if (eventWrapper != null) {
                    //Timber.i("Retrofit ${eventWrapper.message}")
                    Timber.i("Retrofit ${eventWrapper.id.toString()}")
                    Timber.i("Retrofit ${eventWrapper.organizer?.email.toString()}")    // calendar to which the event has been added
                    Timber.i("Retrofit ${eventWrapper.start?.dateTime.toString()}")
                    Timber.i("Retrofit ${eventWrapper.summary}")
                    Timber.i("Retrofit ${eventWrapper.creator?.email.toString()}")
                    Timber.i("Retrofit ${eventWrapper.status}")
                    var relatedEvent = RelatedEventModel(eventWrapper.id.toString(), eventWrapper.organizer?.email.toString(),
                        eventWrapper.summary, eventWrapper.start?.timeZone.toString(), eventWrapper.start?.dateTime.toString(),
                    eventWrapper.end?.timeZone.toString(), eventWrapper.end?.dateTime.toString())
                    FirebaseDB.saveRelatedEvent(eventId, eventWrapper.id.toString(), relatedEvent)
                }
            }

            override fun onFailure(call: Call<EventWrapper>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
                Timber.i("Retrofit create Error : $t.message")
            }
        })
    }
}