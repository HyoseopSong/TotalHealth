package com.medichain.totalhealth.view_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medichain.totalhealth.R
import com.medichain.totalhealth.databinding.ViewAdapterTextviewItemBinding
import java.util.*

class ExamCategoryViewAdapter (val context: Context, private val strArr: ArrayList<String>, val onClickItem: View.OnClickListener):RecyclerView.Adapter<ExamCategoryViewAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ViewAdapterTextviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        private val item_text_view = binding.itemTextView

        fun bind(text: String){
            item_text_view.setOnClickListener(onClickItem)
            item_text_view.text = text
            if(text == "종합검진") {
                binding.isHighlighted = true
                item_text_view.isPressed = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewAdapterTextviewItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(strArr[position])
    }

    override fun getItemCount() = strArr.size
}