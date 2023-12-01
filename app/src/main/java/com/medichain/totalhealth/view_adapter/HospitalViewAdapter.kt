package com.medichain.totalhealth.view_adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.medichain.totalhealth.databinding.ViewAdapterTextviewItemBinding
import java.util.*


class HospitalViewAdapter (val context: Context, private val hospitalArr: ArrayList<HospitalData>, val onClickItem: View.OnClickListener):RecyclerView.Adapter<HospitalViewAdapter.ViewHolder>(), Filterable{
    var filteredHospitals = ArrayList<HospitalData>()
    private var itemFilter = ItemFilter()
    inner class ViewHolder(private val binding: ViewAdapterTextviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        private val item_text_view = binding.itemTextView

        fun bind(text: HospitalData){
            if(text.name == "영남대학교병원") {
                binding.isHighlighted = true
                binding.canMakeCall = true
                binding.phoneButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:07012341234"))
                    context.startActivity(intent)
                }
            } else {
                binding.isHighlighted = false
                binding.canMakeCall = false

            }
            item_text_view.text = text.name
            item_text_view.isPressed = binding.isHighlighted!!
            item_text_view.setOnClickListener(onClickItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewAdapterTextviewItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredHospitals[position])
    }

    override fun getItemCount() = filteredHospitals.size

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()

            val filteredList: ArrayList<HospitalData> = ArrayList<HospitalData>()
            for (hospital in hospitalArr) {
                if (hospital.region == filterString) {
                    if(hospital.name == "영남대학교병원") {
                        filteredList.add(0, hospital)
                    } else {
                        filteredList.add(hospital)
                    }
                }
            }

            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            filteredHospitals.clear()
            filteredHospitals.addAll(filterResults.values as ArrayList<HospitalData>)
            notifyDataSetChanged()
        }
    }
}