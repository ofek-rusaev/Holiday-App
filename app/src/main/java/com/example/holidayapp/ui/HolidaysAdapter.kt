package com.example.holidayapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.holidayapp.R
import com.example.holidayapp.models.HolidayItem
import kotlinx.android.synthetic.main.holiday_item.view.*

class HolidaysAdapter : RecyclerView.Adapter<HolidaysAdapter.HolidayViewHolder>() {

    inner class HolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<HolidayItem>() {
        override fun areItemsTheSame(oldItem: HolidayItem, newItem: HolidayItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: HolidayItem, newItem: HolidayItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolidaysAdapter.HolidayViewHolder {
        return HolidayViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holiday_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HolidaysAdapter.HolidayViewHolder, position: Int) {
        val holiday = differ.currentList[position]
        holder.itemView.apply {
            tvName.text = holiday.name
            tvDate.text = holiday.date

            setOnClickListener {
                onItemClickListener?.let { it(holiday) }
            }
        }
    }

    private var onItemClickListener: ((HolidayItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (HolidayItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}