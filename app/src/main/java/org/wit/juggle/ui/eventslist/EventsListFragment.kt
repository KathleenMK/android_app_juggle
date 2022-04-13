package org.wit.juggle.ui.eventslist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.juggle.adapters.EventAdapter
import org.wit.juggle.adapters.EventClickListener
import org.wit.juggle.databinding.FragmentEventslistBinding
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.models.EventModel
import org.wit.juggle.ui.home.HomeFragmentDirections
import org.wit.juggle.ui.signin.SignedInViewModel
import org.wit.juggle.utils.createTickTock
import org.wit.juggle.utils.hideTickTock
import org.wit.juggle.utils.showTickTock
import timber.log.Timber

class EventsListFragment : Fragment(), EventClickListener {

    private lateinit var eventsListViewModel: EventsListViewModel
    private val args by navArgs<EventsListFragmentArgs>()
    private var _binding: FragmentEventslistBinding? = null
    private val signedInViewModel: SignedInViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var ticktock : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventsListViewModel =
            ViewModelProvider(this).get(EventsListViewModel::class.java)

        _binding = FragmentEventslistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ticktock = createTickTock(requireActivity())

        val textView: TextView = binding.textEventslist
        eventsListViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(activity)
//        eventsListViewModel.observableEvents.observe(viewLifecycleOwner, Observer { events ->
//            events?.let { render(events as ArrayList<EventModel>) }
//        })

      //  Timber.i("eventslist Frag: "+args.calendar)

        //binding.userJuggled.setText(eventsListViewModel.observableUser.value?.juggled!!.values.toString()) //.juggled!!.values.elementAt(0).toString())
        showTickTock(ticktock,"Event Info on the way...")
        eventsListViewModel.observableUser.observe(viewLifecycleOwner, Observer { user ->
            user?.let { binding.userJuggled.setText(eventsListViewModel.observableUser.value?.juggled!!.keys.elementAt(0).toString())
                eventsListViewModel.findCalendarEvents(eventsListViewModel.observableUser.value?.juggled!!.values.elementAt(0))}
        })

        eventsListViewModel.observableEvents.observe(viewLifecycleOwner, Observer {
                events ->
            events?.let {
                render(events as ArrayList<EventModel>)
                hideTickTock(ticktock)
            }
        })

        eventsListViewModel.getUser(signedInViewModel.liveFirebaseUser)

          if(args.calendar != null)
        {
            eventsListViewModel.findCalendarEvents(args.calendar!!.id)

        }
        else{
              Timber.i("args.calendar is null")
              Timber.i(eventsListViewModel.observableUser.value.toString())
              binding.userJuggled.setText("All the Juggled")
              //eventsListViewModel.findCalendarEvents("primary")
            }
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(events: ArrayList<EventModel>) {
        Timber.i("in DB Frag render :"+args.calendar)
        binding.recyclerViewEvents.adapter = EventAdapter(events,this)
        //eventsListViewModel.getUser(signedInViewModel.liveFirebaseUser)
        //binding.userJuggled.setText(eventsListViewModel.observableUser.value?.juggled!!.values.elementAt(0).toString())
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
        Timber.i("in onEvent Click"+event.toString())
        val calendarName: String
        if (args.calendar == null){
            calendarName = eventsListViewModel.observableUser.value?.juggled!!.keys.elementAt(0).toString()
        }
        else {
            calendarName = args.calendar!!.summary.toString()
        }


            val action = EventsListFragmentDirections.actionNavigationEventslistToEventViewFragment(
                calendarName,
                event
            )
            findNavController().navigate(action)
        }


}