package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData
import org.wit.juggle.api.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object CalendarManager : CalendarStore {


    override fun findCalendars(calendarsList: MutableLiveData<List<CalendarModel>>) {

        val call = RetrofitHelper.getApi().getCalendars()

        call.enqueue(object : Callback<CalendarListModel> {
//            override fun onResponse(call: Call<List<CalendarModel>>,
//                                    response: Response<List<CalendarModel>>
//            ) {
//                calendarsList.value = response.body() as ArrayList<CalendarModel>
//                Timber.i("Retrofit JSON = ${response.body()}")
//            }
//
//            override fun onFailure(call: Call<List<CalendarModel>>, t: Throwable) {
//                Timber.i("Retrofit Error : $t.message")
//            }

            override fun onResponse(
                call: Call<CalendarListModel>,
                response: Response<CalendarListModel>
            ) {
                val calendarListModelValue = response.body() as CalendarListModel
                calendarsList.value = calendarListModelValue.items as ArrayList<CalendarModel>
               Timber.i("Retrofit JSON = ${response.body()}")
            }

            override fun onFailure(call: Call<CalendarListModel>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })

    }
}