package org.wit.juggle.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.juggle.R
import org.wit.juggle.api.GoogleCalendarApi
import org.wit.juggle.api.RetrofitHelper
import org.wit.juggle.databinding.FragmentDashboardBinding
import timber.log.Timber

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val googleCalendarApi = RetrofitHelper.getInstance().create(GoogleCalendarApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val result = googleCalendarApi.getCalendarEventList()
            Timber.i(result.toString())
            if (result != null)
            // Checking the results
                Timber.i("api reponse: "+result.body().toString())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}