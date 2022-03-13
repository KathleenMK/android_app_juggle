package org.wit.juggle.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.juggle.adapters.CalendarAdapter
import org.wit.juggle.adapters.CalendarClickListener
import org.wit.juggle.api.GoogleCalendarApi
import org.wit.juggle.api.RetrofitHelper
//import com.google.android.gms.auth.api.signin.JuggleSignIn
import org.wit.juggle.databinding.FragmentHomeBinding
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.utils.createTickTock
import org.wit.juggle.utils.hideTickTock
import org.wit.juggle.utils.showTickTock
import timber.log.Timber


class HomeFragment : Fragment(), CalendarClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var ticktock : AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ticktock = createTickTock(requireActivity())

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        binding.recyclerViewCalendars.layoutManager = LinearLayoutManager(activity)
        homeViewModel.observableCalendars.observe(viewLifecycleOwner, Observer { calendars ->
            calendars?.let { render(calendars as ArrayList<CalendarModel>) }
        })

        showTickTock(ticktock,"Calendar Info on the way...")
        homeViewModel.observableCalendars.observe(viewLifecycleOwner, Observer {
                calendars ->
            calendars?.let {
                render(calendars as ArrayList<CalendarModel>)
                hideTickTock(ticktock)
            }
        })

        //Toast.makeText(context, "home frag line 40", Toast.LENGTH_LONG).show()
        //Toast.makeText(context, homeViewModel.googleSignInClient.toString(), Toast.LENGTH_LONG).show()
        Log.w(TAG, "line 44 : ${homeViewModel.googleSignInClient}")
        //Log.w(TAG, "line 44 : ${signed.googleSignInClient}")
        Log.w(TAG, "line 44 : ${homeViewModel.googleSignInClient.value?.signInIntent}")
        //revokeAccess()

//        val googleCalendarApi = RetrofitHelper.getInstance().create(GoogleCalendarApi::class.java)
//        // launching a new coroutine
//        GlobalScope.launch {
//            val result = googleCalendarApi.getCalendarList()
//            Timber.i(result.toString())
//            if (result != null)
//            // Checking the results
//                Timber.i("api reponse: "+result.body().toString())
//        }
        
        //val intent = Intent(this, GrabberLogin::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //startActivity(intent)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(calendars: ArrayList<CalendarModel>) {
      binding.recyclerViewCalendars.adapter = CalendarAdapter(calendars,this)

        if (calendars.isEmpty()) {
            Toast.makeText(context, "home frag in render is empty", Toast.LENGTH_LONG).show()
        } else {
            Timber.i("Home fragment render"+calendars.toString())
            Toast.makeText(context, "home frag in render else", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val TAG = "HomeFragment"

    }

    override fun onCalendarClick(calendar: CalendarModel) {
        TODO("Not yet implemented")
    }

}