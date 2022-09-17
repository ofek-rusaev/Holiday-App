package com.example.holidayapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holidayapp.R
import com.example.holidayapp.ui.HolidaysAdapter
import com.example.holidayapp.ui.HolidaysViewModel
import com.example.holidayapp.utils.Resource
import com.example.holidayapp.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_holidays_list.*


class HolidaysListFragment : Fragment(R.layout.fragment_holidays_list) {

    private val viewModel: HolidaysViewModel by activityViewModels()
    lateinit var holidaysAdapter: HolidaysAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        holidaysAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("holidayItem", it)
            }
            findNavController().navigate(
                R.id.action_holidaysListFragment_to_holidayItemFragment,
                bundle
            )
        }

        holidaysAdapter.setOnFavoriteBtnClickListener {
            viewModel.saveHoliday(it)
            Snackbar.make(view, "Holiday saved successfully", Snackbar.LENGTH_SHORT).show()
        }
        
        viewModel.holidays.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // todo: add loader
                }
                is Resource.Success -> {
                    if (response.data?.isNullOrEmpty() == true) {
                        showDoDataText(true)
                    }
                    holidaysAdapter.differ.submitList(response.data.toList())
                }
            }.exhaustive
        })
    }


    private fun showDoDataText(toShow: Boolean) {
        tvNoData.visibility = if (toShow) View.VISIBLE else View.INVISIBLE
    }

    private fun setupRecyclerView() {
        holidaysAdapter = HolidaysAdapter(false)
        rvAllHolidays.apply {
            adapter = holidaysAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}