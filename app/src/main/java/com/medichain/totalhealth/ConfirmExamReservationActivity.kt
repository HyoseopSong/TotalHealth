package com.medichain.totalhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityConfirmExamReservationBinding

class ConfirmExamReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmExamReservationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_exam_reservation)
        title = "예약 확정"
        binding.activity = this
        binding.hospitalPackageName = "한국건강관리협회-경북지부-기업남성"
        binding.reservationDate = "2020.10.10"
        binding.reservationTime = "19:00"
        binding.basePrice = "200,000"
        binding.addPrice = "250,000"
        binding.totalPrice = "450,000"
        binding.examType = "협약검사"
        binding.isAdditionalExamExist = true
        binding.isOptionalExamExist = true
        val from = intent.getStringExtra("from")
        binding.fromReservation = from == "ReservationList"
        if(binding.fromReservation!!) {
            binding.buttonText = "확인"

            val onClickExam: View.OnClickListener = View.OnClickListener { v ->
                val mIntent = Intent(this@ConfirmExamReservationActivity, SubExamDetailActivity::class.java)
                startActivity(mIntent)
            }

            binding.tmpLayout0.setOnClickListener(onClickExam)
            binding.tmpLayout1.setOnClickListener(onClickExam)
            binding.tmpLayout2.setOnClickListener(onClickExam)
        } else {
            binding.buttonText = "예약 하기"
        }

    }


    fun onClickReserve(view: View) {
        val from = intent.getStringExtra("from")
        if(from == "ReservationList") {
            finish()
        } else {
            val mIntent = Intent(this, ReservationListActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(mIntent)
        }
    }
}