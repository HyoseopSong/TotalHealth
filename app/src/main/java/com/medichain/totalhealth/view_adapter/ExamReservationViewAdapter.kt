package com.medichain.totalhealth.view_adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.medichain.totalhealth.ConfirmExamReservationActivity
import com.medichain.totalhealth.R
import com.medichain.totalhealth.databinding.ViewAdapterExamReservationItemBinding
import com.medichain.totalhealth.databinding.ViewAdapterExamSelectionItemBinding
import com.medichain.totalhealth.databinding.ViewAdapterSubExamItemBinding
import java.util.*

class ExamReservationViewAdapter (val context: Context, private val strArr: ArrayList<String>, val onClickReservationItem: View.OnClickListener):RecyclerView.Adapter<ExamReservationViewAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ViewAdapterExamReservationItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(examInfo: String){
            binding.hospitalName = "영남대학교병원"
            binding.programName = "패키지명"
            binding.reservedDateTime = "2023년 12월 30일 16시27분"
            binding.inputDateTime = "2023년 12월 30일 16시27분"
            binding.cancelDateTime = "2023년 12월 30일 16시27분"
            binding.isCanceld = false

            binding.reservationLayout.setOnClickListener {
                val mIntent = Intent(context, ConfirmExamReservationActivity::class.java)
                mIntent.putExtra("from", "ReservationList")
                context.startActivity(mIntent)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewAdapterExamReservationItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(strArr[position])
    }

    override fun getItemCount() = strArr.size
}