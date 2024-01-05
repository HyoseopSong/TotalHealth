package com.medichain.totalhealth.view_adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medichain.totalhealth.CustomerInfoActivity
import com.medichain.totalhealth.databinding.ViewAdapterCustomerItemBinding
import com.medichain.totalhealth.databinding.ViewAdapterTextviewItemBinding
import java.util.ArrayList

class CustomerViewAdapter (val context: Context, private val customerArr: ArrayList<CombinedCustomerData>):
    RecyclerView.Adapter<CustomerViewAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ViewAdapterCustomerItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(cData: CombinedCustomerData, position:Int){
            binding.serial = (position + 1).toString()
            binding.name = cData.customer.name
            binding.phone = cData.customer.phone
            binding.birth = cData.customer.birth

            binding.root.setOnClickListener {
                val mIntent = Intent(context, CustomerInfoActivity::class.java)
                mIntent.putExtra("Customer", cData.customer)
                mIntent.putExtra("Certification", cData.certification)
                mIntent.putExtra("ExamSubject", cData.examSubject)
                mIntent.putExtra("CheckUpResult", cData.checkUpResult)

                context.startActivity(mIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewAdapterCustomerItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(customerArr[position], position)
    }

    override fun getItemCount() = customerArr.size

}