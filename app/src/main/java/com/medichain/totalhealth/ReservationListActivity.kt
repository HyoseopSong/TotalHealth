package com.medichain.totalhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityReservationListBinding
import com.medichain.totalhealth.view_adapter.ExamCategoryViewAdapter
import com.medichain.totalhealth.view_adapter.ExamReservationViewAdapter

class ReservationListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationListBinding
    private lateinit var examReservationListAdapter: ExamReservationViewAdapter
    private val tmpList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation_list)
        binding.activity = this
        title = "예약 현황"

        val onClickItem: View.OnClickListener = View.OnClickListener { v ->
            val mIntent = Intent(this@ReservationListActivity, ConfirmExamReservationActivity::class.java)
            mIntent.putExtra("from", "ReservationList")

            startActivity(mIntent)
        }

        tmpList.add("당일검사")
        examReservationListAdapter = ExamReservationViewAdapter(this@ReservationListActivity, tmpList, onClickItem)
        binding.examReservationListRecyclerView.adapter = examReservationListAdapter

    }

    fun onClickReserve(view: View) {
            val mIntent = Intent(this, ExamCategoryActivity::class.java)
            startActivity(mIntent)
    }

}