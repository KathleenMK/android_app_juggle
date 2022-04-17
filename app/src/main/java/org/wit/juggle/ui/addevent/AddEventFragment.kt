package org.wit.juggle.ui.addevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.wit.juggle.R
import org.wit.juggle.databinding.FragmentAddeventBinding
import org.wit.juggle.models.AddEventModel
import org.wit.juggle.models.Time
import org.wit.juggle.ui.signin.SignedInViewModel
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddEventFragment : Fragment() {

    private lateinit var addEventViewModel: AddEventViewModel
    private var _binding: FragmentAddeventBinding? = null
    private val signedInViewModel: SignedInViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addEventViewModel =
            ViewModelProvider(this).get(AddEventViewModel::class.java)

        _binding = FragmentAddeventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        addEventViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        addEventViewModel.observableUser.observe(viewLifecycleOwner, Observer { user ->
            user?.let { Timber.i(addEventViewModel.observableUser.value?.jugglers.toString())}
        })

        val spinner: Spinner =
            root.findViewById(R.id.addEventTypeSpinner)  //https://developer.android.com/guide/topics/ui/controls/spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        this.activity?.let {
            ArrayAdapter.createFromResource(
                it,   //https://stackoverflow.com/questions/48055423/spinner-in-a-fragment 08Dec21
                R.array.addEventType,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }

        binding.addNewEventBtn.setOnClickListener(){
            Timber.i("in my new button")
            addEventViewModel.getUser(signedInViewModel.liveFirebaseUser)
            Timber.i(addEventViewModel.observableUser.value?.jugglers.toString())
            val diners = arrayListOf<String>()
            for (j in 0 until addEventViewModel.observableUser.value?.jugglers!!.size) {
                diners.add(addEventViewModel.observableUser.value?.jugglers!!.values.elementAt(j).toString())
            }
            for (j in 0 until addEventViewModel.observableUser.value?.juggled!!.size) {
                diners.add(addEventViewModel.observableUser.value?.juggled!!.values.elementAt(j).toString())
            }
            for (j in 0 until diners.size) {
                //diners.add(addEventViewModel.observableUser.value?.juggled!!.values.elementAt(j).toString())
                    var eventPrefix = ""
                    if(binding.addEventTypeSpinner.selectedItem.toString() != ""){
                        eventPrefix = binding.addEventTypeSpinner.selectedItem.toString() + ": "
                    }
                   addEventViewModel.addNewEvent(diners[j],
                    AddEventModel(summary = eventPrefix+binding.eventSummary.text.toString(),
                        start = Time(timeZone="Europe/Dublin",
                            dateTime=binding.eventStartDate.text.toString()+"T"+binding.eventStartTime.text.toString()+":00+01:00"),
                        end = Time(timeZone="Europe/Dublin",
                            dateTime=binding.eventEndDate.text.toString()+"T"+binding.eventEndTime.text.toString()+":00+01:00")
                    )
                    )

            }

            findNavController().popBackStack()
        }

        addEventViewModel.getUser(signedInViewModel.liveFirebaseUser)

        render()

        return root
    }

    private fun render() {

        val startTime = getString(R.string.defaultStartTime)
        val endTime = getString(R.string.defaultEndTime)
            //LocalDateTime.now().plusHours(1)

        //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        var now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = now.format(formatter)

        binding.eventStartDate.setText(formatted)
        binding.eventEndDate.setText(formatted)

        binding.eventStartTime.setText(startTime)
        binding.eventEndTime.setText(endTime)

        // https://www.datetimeformatter.com/how-to-format-date-time-in-kotlin/ 16Apr22
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}