package com.example.holidayapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.holidayapp.R
import com.example.holidayapp.databinding.FragmentHolidayItemBinding
import com.example.holidayapp.ui.HolidaysViewModel
import com.example.holidayapp.ui.MainActivity

class HolidayItemFragment : Fragment(R.layout.fragment_holiday_item) {

    lateinit var binding: FragmentHolidayItemBinding
    private lateinit var viewModel: HolidaysViewModel
    private val args: HolidayItemFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val holidayItem = args.holidayItem

        binding = FragmentHolidayItemBinding.bind(view)
        binding.apply {
            myHoliday = holidayItem

            itemFixed.text = if (holidayItem.fixed == false) "No" else "Yes"
            itemGlobal.text = if (holidayItem.global == false) "No" else "Yes"
            itemCounties.text = if (holidayItem.counties.isNullOrEmpty()) "-" else holidayItem.counties.toString()
            itemLaunchYear.text = holidayItem.launchYear.toString().ifEmpty { "-" }
            itemTypes.text = if (holidayItem.types.isNullOrEmpty()) "-" else holidayItem.types.toString()
        }
    }
}