package org.wit.juggle.ui.eventview

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.juggle.R
import org.wit.juggle.databinding.FragmentEventslistBinding
import org.wit.juggle.databinding.FragmentEventviewBinding
import org.wit.juggle.models.EventModel
import org.wit.juggle.ui.eventslist.EventsListFragmentArgs
import org.wit.juggle.ui.eventslist.EventsListViewModel
import org.wit.juggle.utils.createTickTock
import org.wit.juggle.utils.hideTickTock
import org.wit.juggle.utils.showTickTock
import timber.log.Timber
import android.app.Application
import androidx.lifecycle.*
import org.wit.juggle.adapters.EventAdapter
import org.wit.juggle.firebaseintegration.FirebaseAuthorization
import org.wit.juggle.models.CalendarManager
import org.wit.juggle.models.CalendarModel


class EventViewFragment : Fragment() {

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
        Timber.i("in Event View Frag render :" + args.calendar + args.event)
        binding.calendar = args.calendar
        binding.event = args.event

    }
}