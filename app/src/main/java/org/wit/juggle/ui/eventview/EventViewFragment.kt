package org.wit.juggle.ui.eventview

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.juggle.databinding.FragmentEventviewBinding
import timber.log.Timber
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import org.wit.juggle.R
import org.wit.juggle.adapters.RelatedEventAdapter
import org.wit.juggle.adapters.RelatedEventClickListener
import org.wit.juggle.models.*
import org.wit.juggle.ui.eventslist.EventsListFragmentDirections






class EventViewFragment : Fragment(), RelatedEventClickListener {

//    companion object {
//        fun newInstance() = EventViewFragment()
//    }

    private lateinit var eventViewViewModel: EventViewViewModel
    private val args by navArgs<EventViewFragmentArgs>()
    private var _binding: FragmentEventviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var ticktock: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_eventview, container, false)
        eventViewViewModel =
            ViewModelProvider(this).get(EventViewViewModel::class.java)

        _binding = FragmentEventviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewRelatedEvents.layoutManager = LinearLayoutManager(activity)



        eventViewViewModel.observableRelatedEvents.observe(viewLifecycleOwner, Observer { relatedEvents ->
            relatedEvents?.let { renderRelatedEvents(relatedEvents as ArrayList<RelatedEventModel>) }
        }) //constantly updating even without changes

        binding.addRelatedEventBtn.setOnClickListener(){
            Timber.i("in my new button")
            if(binding.jugglerSpinner.selectedItemPosition != 0) {
                eventViewViewModel.addRelatedEvent(
                    args.event.id,
                    args.user.jugglers.getValue(binding.jugglerSpinner.selectedItem.toString()),
                    binding.jugglerSpinner.selectedItem.toString(),
                    AddEventModel(
                        summary = args.calendarName.toString()+" -> "+binding.newEventSummary.text.toString(),
                        start = Time(
                            timeZone = "Europe/Dublin",
                            dateTime = binding.newEventStartDate.text.toString() + "T" + binding.newEventStartTime.text.toString() + ":00+01:00"
                        ),
                        end = Time(
                            timeZone = "Europe/Dublin",
                            dateTime = binding.newEventEndDate.text.toString() + "T" + binding.newEventEndTime.text.toString() + ":00+01:00"
                        )
                    )
                )

                render()
            }
            else{
                Snackbar.make(
                    it,
                    "Choose a Juggler to continue...",
                    Snackbar.LENGTH_LONG).show()


            }
        }


        val spinner: Spinner =
            root.findViewById(R.id.jugglerSpinner)
        val values = arrayListOf<String>()
        values.add("Choose a Juggler")
        for (j in 0 until args.user.jugglers.keys.size) {
        values.add(args.user.jugglers.keys.elementAt(j).toString())}
        val adapter = ArrayAdapter(this.activity!!, android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        //https://stackoverflow.com/questions/48055423/spinner-in-a-fragment 16Apr22

        //  ticktock = createTickTock(requireActivity())

//        val textView: TextView = binding.textEventview
//        eventViewViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        //binding.recyclerViewEvents.layoutManager = LinearLayoutManager(activity)

//        showTickTock(ticktock,"Event Info on the way...")
//        eventViewViewModel.observableEvent.observe(viewLifecycleOwner, Observer {
//
//                render()
//                hideTickTock(ticktock)
//
//        })

        //eventViewViewModel.findEvent(args.calendar,args.event)
       render()
        eventViewViewModel.getRelatedEvents(args.event.id)  // doesn't update upon new addtiona
        //renderRelatedEvents()   //renders but does not upon addition of new event





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventViewViewModel = ViewModelProvider(this).get(EventViewViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun render() {
        Timber.i("in Event View Frag render :" + args.calendarName + args.event)
        binding.calendarName = args.calendarName
        binding.event = args.event

        binding.eventStartDate.setText(args.event.start.dateTime.split('T')[0])
        binding.eventStartTime.setText(args.event.start.dateTime.split('T')[1].split(":")[0] + ":" + args.event.start.dateTime.split('T')[1].split(":")[0])
        binding.eventEndDate.setText(args.event.end.dateTime.split('T')[0])
        binding.eventEndTime.setText(args.event.end.dateTime.split('T')[1].split(":")[0] + ":" + args.event.end.dateTime.split('T')[1].split(":")[0])
        //binding.createdTime.setText(args.event.created.split('T')[1])
        //binding.createdDate.setText(args.event.created.split('T')[0])
        binding.newEventStartDate.setText(args.event.start.dateTime.split('T')[0])
        binding.newEventStartTime.setText(args.event.start.dateTime.split('T')[1].split(":")[0] + ":" + args.event.start.dateTime.split('T')[1].split(":")[0])
        binding.newEventEndDate.setText(args.event.end.dateTime.split('T')[0])
        binding.newEventEndTime.setText(args.event.end.dateTime.split('T')[1].split(":")[0] + ":" + args.event.end.dateTime.split('T')[1].split(":")[0])


         }


    private fun renderRelatedEvents(relatedEvents: ArrayList<RelatedEventModel>) {

        //eventViewViewModel.getRelatedEvents(args.event.id)
        binding.recyclerViewRelatedEvents.adapter = RelatedEventAdapter(relatedEvents, this)
        Timber.i("related events"+eventViewViewModel.observableRelatedEvents.value.toString())
    }

    override fun onEventClick(relatedEvent: RelatedEventModel) {
        Timber.i("in onEvent Click"+relatedEvent.toString())
//        val eventCalendarSummary: String
//        if (args.calendar == null){
//            eventCalendarSummary = eventsListViewModel.observableUser.value?.juggled!!.keys.elementAt(0).toString()
//        }
//        else {
//            eventCalendarSummary = args.calendar!!.summary.toString()
//        }

        //val action = EventsListFragmentDirections.actionNavigationEventslistToEventViewFragment(args.calendar!!,event)
        //indNavController().navigate(action)
    }
}