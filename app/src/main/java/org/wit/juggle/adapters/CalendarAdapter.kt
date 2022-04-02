package org.wit.juggle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import org.wit.juggle.R
import org.wit.juggle.databinding.CardCalendarBinding
import org.wit.juggle.models.CalendarModel

interface CalendarClickListener {
    fun onCalendarClick(calendar: CalendarModel)
}

class CalendarAdapter constructor(private var calendars: ArrayList<CalendarModel>,
                                  private val listener: CalendarClickListener)
    : RecyclerView.Adapter<CalendarAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCalendarBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        val roleSpinner: Spinner = binding.roleSpinner
        ArrayAdapter.createFromResource(
            parent.context,
            R.array.role_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            roleSpinner.adapter = adapter
        }

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val calendar = calendars[holder.adapterPosition]
        holder.bind(calendar,listener)
    }

    override fun getItemCount(): Int = calendars.size

    inner class MainHolder(val binding : CardCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calendar: CalendarModel, listener: CalendarClickListener) {
            binding.calendar = calendar
            binding.root.setOnClickListener { listener.onCalendarClick(calendar) }
            binding.executePendingBindings()


        }
    }
}