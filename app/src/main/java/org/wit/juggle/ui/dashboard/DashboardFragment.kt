package org.wit.juggle.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.juggle.adapters.EventAdapter
import org.wit.juggle.adapters.EventClickListener
import org.wit.juggle.databinding.FragmentDashboardBinding
import org.wit.juggle.models.EventModel
import org.wit.juggle.utils.createTickTock
import org.wit.juggle.utils.hideTickTock
import org.wit.juggle.utils.showTickTock
import timber.log.Timber

class DashboardFragment : Fragment(), EventClickListener {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val args by navArgs<DashboardFragmentArgs>()
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var ticktock : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ticktock = createTickTock(requireActivity())

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(activity)
        dashboardViewModel.observableEvents.observe(viewLifecycleOwner, Observer { events ->
            events?.let { render(events as ArrayList<EventModel>) }
        })

        Timber.i("dashboard Frag: "+args.calendar)


        showTickTock(ticktock,"Event Info on the way...")
        dashboardViewModel.observableEvents.observe(viewLifecycleOwner, Observer {
                events ->
            events?.let {
                render(events as ArrayList<EventModel>)
                hideTickTock(ticktock)
            }
        })

//        val googleCalendarApi = RetrofitHelper.getInstance().create(GoogleCalendarApi::class.java)
//        // launching a new coroutine
//        GlobalScope.launch {
//            val result = googleCalendarApi.getCalendarEventList()
//            Timber.i(result.toString())
//            if (result != null)
//            // Checking the results
//                Timber.i("api reponse: "+result.body().toString())
//        }

        dashboardViewModel.findCalendarEvents(args.calendar)
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(events: ArrayList<EventModel>) {
        Timber.i("in DB Frag render :"+args.calendar)
        binding.recyclerViewEvents.adapter = EventAdapter(events,this)

//        var mCredential: GoogleAccountCredential? = null
//
//        mCredential = GoogleAccountCredential.usingOAuth2(
//            context,
//            arrayListOf(CalendarScopes.CALENDAR))
//            .setBackOff(ExponentialBackOff())
//
//        mCredential.selectedAccountName = homeViewModel.googleSignInClient.value.toString()
//
//        Timber.i("mCred name"+mCredential.selectedAccountName)  // error: java.lang.IllegalArgumentException: the name must not be empty: null
//        //at android.accounts.Account
//
//        Timber.i("mCredential..."+mCredential.toString())
//
//        val transport = AndroidHttp.newCompatibleTransport()
//        val jsonFactory = JacksonFactory.getDefaultInstance()
//        val service = com.google.api.services.calendar.Calendar.Builder(
//            transport, jsonFactory, mCredential)
//            .setApplicationName("Juggle")
//            .build()
//
//        val calendar = service.calendars().get("primary").execute()
//
//        println("println"+calendar.summary)

        if (events.isEmpty()) {
            Toast.makeText(context, "There are no events found for this calendar", Toast.LENGTH_LONG).show()
        } else {
            Timber.i("Dashboard fragment render"+events.toString())
            Toast.makeText(context, "Here you go...", Toast.LENGTH_LONG).show()
        }
    }


    override fun onEventClick(event: EventModel) {
        TODO("Not yet implemented")
    }
}