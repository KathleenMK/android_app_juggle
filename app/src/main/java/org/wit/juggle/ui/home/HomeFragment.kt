package org.wit.juggle.ui.home

//import com.google.android.gms.auth.api.signin.JuggleSignIn
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.wit.juggle.R
import org.wit.juggle.adapters.CalendarAdapter
import org.wit.juggle.adapters.CalendarClickListener
import org.wit.juggle.databinding.FragmentHomeBinding
import org.wit.juggle.models.CalendarModel
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

    lateinit var ticktock: AlertDialog

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

        showTickTock(ticktock, "Calendar Info on the way...")
        homeViewModel.observableUser.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                binding.userGoogle.setText(homeViewModel.observableUser.value?.userName.toString())
                binding.userJugglers.setText(homeViewModel.observableUser.value?.jugglers?.keys.toString())
                binding.userJuggled.setText(homeViewModel.observableUser.value?.juggled?.keys.toString())
            }
        })
        homeViewModel.observableCalendars.observe(viewLifecycleOwner, Observer { calendars ->
            calendars?.let {
                render(calendars as ArrayList<CalendarModel>)
                hideTickTock(ticktock)
            }
        })

        binding.editUserBtn.setOnClickListener() {
            Timber.i("in editUser button")
            binding.recyclerViewCalendars.visibility = View.VISIBLE
            binding.editUserBtn.visibility = View.GONE
        }

        binding.saveUserBtn.setOnClickListener() {
            val adapter = binding.recyclerViewCalendars.adapter as CalendarAdapter
            val count = adapter.itemCount

            val jugglers = HashMap<String, String>()
            val juggled = HashMap<String, String>()
            val aliases = arrayListOf<String>()
            var checkComplete = true

            for (a in 0 until count) {
                Timber.i(a.toString())
                val holder =
                    binding.recyclerViewCalendars.findViewHolderForLayoutPosition(a)
                if (holder != null) {
                    val calendarId =
                        holder.itemView.findViewById<View>(R.id.calendarId) as TextView

                    val role =
                        holder.itemView.findViewById<View>(R.id.role_spinner) as Spinner
                    val aliasEntry =
                        holder.itemView.findViewById<View>(R.id.calendarAlias) as TextView
                    val alias = aliasEntry.text.toString()
                    if (role.selectedItemPosition != 0 && (alias.isEmpty()
                                || aliases.contains(alias.uppercase()))
                    ) {
                        checkComplete = false
                        break
                    }
                    aliases.add(alias.uppercase())
                    if (role.selectedItem.toString() == "Juggled") {
                        juggled.put(alias, calendarId.text.toString())
                    }
                    if (role.selectedItem.toString() == "Juggler") {
                        jugglers.put(alias, calendarId.text.toString())
                    }
                }
            }

            if (checkComplete) {
                if (jugglers.isNotEmpty() && juggled.isNotEmpty()) {    //complete screen
                    Timber.i("step1")
                    homeViewModel.saveUser(
                        signedInViewModel.liveFirebaseUser,
                        jugglers,
                        juggled
                    )
                    val action =
                        HomeFragmentDirections.actionNavigationHomeToNavigationEventslist()
                    findNavController().navigate(action)
                } else if (jugglers.isNotEmpty() || juggled.isNotEmpty()) {   //not complete screen but not empty
                    Timber.i("step2")
                    Snackbar.make(
                        it,
                        "At least one Juggler and one Juggled must be chosen to update",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else if (homeViewModel.observableUser.value?.juggled.isNullOrEmpty() || homeViewModel.observableUser.value?.jugglers.isNullOrEmpty()
                    && (juggled.isNullOrEmpty() && jugglers.isNullOrEmpty())
                ) { //empty screen, incomplete DB
                    Timber.i("step3")
                    Snackbar.make(
                        it,
                        "At least one Juggler and one Juggled must be chosen",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {  // empty screen and complete DB
                    Timber.i("step4")
                    val action =
                        HomeFragmentDirections.actionNavigationHomeToNavigationEventslist()
                    findNavController().navigate(action)
                }
            } else {
                Snackbar.make(
                    it,
                    "each alias must be distinct and not blank",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(calendars: ArrayList<CalendarModel>) {
        binding.recyclerViewCalendars.adapter = CalendarAdapter(calendars, this)
        Timber.i("in HomeFragment render")
        homeViewModel.getUser(signedInViewModel.liveFirebaseUser)
        if (homeViewModel.observableUser.value?.equals(null) == false) {
            binding.userGoogle.setText(homeViewModel.observableUser.value?.googleId.toString())
        }


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
            Timber.i("Home fragment render" + calendars.toString())
            binding.textHome.text = ""
        }
    }

    override fun onCalendarClick(calendar: CalendarModel) {
        Timber.i("in onCalendar Click" + calendar.toString())
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationEventslist(calendar)
        findNavController().navigate(action)
    }

}