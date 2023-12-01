package com.medichain.totalhealth.view_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medichain.totalhealth.R
import com.medichain.totalhealth.databinding.ViewAdapterRadiobuttonItemBinding
import com.medichain.totalhealth.databinding.ViewAdapterTextviewItemBinding
import java.util.*

class RegionChoiceViewAdapter (val context: Context, private val strArr: ArrayList<String>, private var selected:String?):RecyclerView.Adapter<RegionChoiceViewAdapter.ViewHolder>(){
    var selectedRegion = ""
    var previousSelectedButton: RadioButton? = null
    inner class ViewHolder(private val binding: ViewAdapterRadiobuttonItemBinding) : RecyclerView.ViewHolder(binding.root){
//    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!){
        private val item_radio_button = binding.itemRadioButton

        fun bind(text: String){
            item_radio_button.text = text

            if(selected.isNullOrEmpty()) {
                selected = "대구광역시"
            }
            selectedRegion = selected as String
            if(text == selected) {
                item_radio_button.isChecked = true
                previousSelectedButton = item_radio_button
            }

            item_radio_button.setOnClickListener {
                selectedRegion = (it as RadioButton).text.toString()
                previousSelectedButton?.isChecked = false
                previousSelectedButton = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewAdapterRadiobuttonItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(strArr[position])
    }

    override fun getItemCount() = strArr.size
}