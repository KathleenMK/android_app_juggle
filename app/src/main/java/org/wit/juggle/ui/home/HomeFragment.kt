package org.wit.juggle.ui.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.CalendarScopes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.juggle.R
import org.wit.juggle.adapters.CalendarAdapter
import org.wit.juggle.adapters.CalendarClickListener
import org.wit.juggle.api.GoogleCalendarApi
import org.wit.juggle.api.RetrofitHelper
import org.wit.juggle.databinding.CardCalendarBinding
//import com.google.android.gms.auth.api.signin.JuggleSignIn
import org.wit.juggle.databinding.FragmentHomeBinding
import org.wit.juggle.models.AddEventModel
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.models.Time
import org.wit.juggle.models.UserModel
import org.wit.juggle.ui.signin.SignedInViewModel
import org.wit.juggle.utils.createTickTock
import org.wit.juggle.utils.hideTickTock
import org.wit.juggle.utils.showTickTock
import timber.log.Timber



class HomeFragment : Fragment(), CalendarClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
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

        binding.saveUserBtn.setOnClickListener(){
            val adapter = binding.recyclerViewCalendars.adapter as CalendarAdapter
            val count = adapter.itemCount

            val jugglers = arrayListOf<String>()
            val juggled = arrayListOf<String>()

            // iterating through recycler view: https://gist.github.com/dominicthomas/1a0d6d7c81eb69e5ad56a62cb7bfd11d
            for (a in 0 until count){
                    Timber.i(a.toString())
                val holder = binding.recyclerViewCalendars.findViewHolderForLayoutPosition(a)
                if (holder != null) {
                    val calendarId = holder.itemView.findViewById<View>(R.id.calendarId) as TextView
                    val alias = holder.itemView.findViewById<View>(R.id.calendarAlias) as TextView
                    //Timber.i(alias.text.toString())
                    val role = holder.itemView.findViewById<View>(R.id.role_spinner) as Spinner
                    //Timber.i(role.selectedItem.toString())
                    if(role.selectedItem.toString()=="Juggled"){
                        juggled.add(calendarId.text.toString())
                    }
                    if(role.selectedItem.toString()=="Juggler"){
                        jugglers.add(calendarId.text.toString())
                    }
                }
            }
            Timber.i("in my new button")
            homeViewModel.saveUser(signedInViewModel.liveFirebaseUser,jugglers, juggled)
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationEventslist()
            findNavController().navigate(action)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(calendars: ArrayList<CalendarModel>) {
      binding.recyclerViewCalendars.adapter = CalendarAdapter(calendars,this)

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

        if (calendars.isEmpty()) {
            Toast.makeText(context, "No Calendars have been found...", Toast.LENGTH_LONG).show()
        } else {
            Timber.i("Home fragment render"+calendars.toString())
            Toast.makeText(context, "Here are your calendars...", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val TAG = "HomeFragment"

    }

    override fun onCalendarClick(calendar: CalendarModel) {
        Timber.i("in onCalendar Click"+calendar.toString())
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationEventslist(calendar)
        findNavController().navigate(action)
    }

}