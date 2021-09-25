package com.jcy.ch24_subwayapp.presentation.stationarrivals

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jcy.ch24_subwayapp.databinding.ItemArrivalBinding
import com.jcy.ch24_subwayapp.domain.ArrivalInformation

class StationArrivalsAdapter : RecyclerView.Adapter<StationArrivalsAdapter.ViewHolder>() {

    var data: List<ArrivalInformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemArrivalBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val viewBinding: ItemArrivalBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(arrival: ArrivalInformation) {
            viewBinding.labelTextView.badgeColor = arrival.subway.color
            viewBinding.labelTextView.text = "${arrival.subway.label} - ${arrival.direction}"
            viewBinding.destinationTextView.text = "🚩 ${arrival.destination}"
            viewBinding.arrivalMessageTextView.text = arrival.message
            viewBinding.arrivalMessageTextView.setTextColor(if (arrival.message.contains("당역")) Color.RED else Color.DKGRAY)
            viewBinding.updatedTimeTextView.text = "측정 시간: ${arrival.updatedAt}"
        }
    }
}