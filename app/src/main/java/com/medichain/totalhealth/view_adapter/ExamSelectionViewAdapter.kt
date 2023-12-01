package com.medichain.totalhealth.view_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.medichain.totalhealth.R
import com.medichain.totalhealth.databinding.ViewAdapterExamSelectionItemBinding
import com.medichain.totalhealth.databinding.ViewAdapterSubExamItemBinding
import java.util.*

class ExamSelectionViewAdapter (val context: Context, private val strArr: ArrayList<ExamInfo>, val onClickSubExamItem: View.OnClickListener):RecyclerView.Adapter<ExamSelectionViewAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ViewAdapterExamSelectionItemBinding, private val parent: ViewGroup) : RecyclerView.ViewHolder(binding.root){
        fun bind(examInfo: ExamInfo){
            binding.isTitle = examInfo.is_title
            if(examInfo.is_title) {
                binding.titleTextHeader.text = if(examInfo.is_optional) "선택" else "필수"
                return
            }

            binding.isOptional = examInfo.is_optional
            binding.examName = examInfo.main_exam
            binding.canSpread = examInfo.sub_exam.size > 0
            binding.isSpread = false
            if(binding.canSpread!!) {
                binding.mainExamLayout.setOnClickListener {
                    binding.isSpread = !(binding.isSpread!!)
                }
            }

            for(examName in examInfo.sub_exam) {
                val subExamItemBinding = ViewAdapterSubExamItemBinding.inflate(LayoutInflater.from(context), parent, false)
                subExamItemBinding.examName = examName
                subExamItemBinding.subExamItem.setOnClickListener(onClickSubExamItem)

                binding.subExamList.addView(subExamItemBinding.root)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewAdapterExamSelectionItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(strArr[position])
    }

    override fun getItemCount() = strArr.size
}