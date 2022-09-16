package com.example.holidayapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.holidayapp.R
import com.example.holidayapp.ui.HolidaysAdapter
import com.example.holidayapp.ui.HolidaysViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_holidays_list.tvNoData
import kotlinx.android.synthetic.main.fragment_saved_holiday.*

class SavedHolidayFragment : Fragment(R.layout.fragment_saved_holiday) {

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
                R.id.action_savedHolidayFragment_to_holidayItemFragment,
                bundle
            )
        }

        viewModel.getSavedHolidays().observe(viewLifecycleOwner, Observer { holidays ->
            if (holidays.isEmpty()) {
                showDoDataText(true)
                return@Observer
            }
            showDoDataText(false)
            holidaysAdapter.differ.submitList(holidays)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val holiday = holidaysAdapter.differ.currentList[position]
                viewModel.deleteHoliday(holiday)
                Snackbar.make(view, "Successfully deleted holiday", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveHoliday(holiday)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvFavoriteHolidays)
        }
    }

    private fun setupRecyclerView() {
        holidaysAdapter = HolidaysAdapter()
        rvFavoriteHolidays.apply {
            adapter = holidaysAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showDoDataText(toShow: Boolean) {
        tvNoData.visibility = if (toShow) View.VISIBLE else View.INVISIBLE
    }
}