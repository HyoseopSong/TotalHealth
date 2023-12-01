package com.medichain.totalhealth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityDateSelectionBinding
import com.medichain.totalhealth.databinding.ViewAdapterDateItemBinding
import java.util.Calendar
import java.util.Date
import java.util.Random

class DateSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDateSelectionBinding
    private var calendar = Calendar.getInstance()
    private var dateBindingArray = arrayOfNulls<ViewAdapterDateItemBinding>(42)
    private var selectedDateBinding : ViewAdapterDateItemBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_date_selection)
        title = "검진 예약하기"
        binding.activity = this

        dateBindingArray[0] = binding.date00
        dateBindingArray[1] = binding.date01
        dateBindingArray[2] = binding.date02
        dateBindingArray[3] = binding.date03
        dateBindingArray[4] = binding.date04
        dateBindingArray[5] = binding.date05
        dateBindingArray[6] = binding.date06
        dateBindingArray[7] = binding.date07
        dateBindingArray[8] = binding.date08
        dateBindingArray[9] = binding.date09
        dateBindingArray[10] = binding.date10
        dateBindingArray[11] = binding.date11
        dateBindingArray[12] = binding.date12
        dateBindingArray[13] = binding.date13
        dateBindingArray[14] = binding.date14
        dateBindingArray[15] = binding.date15
        dateBindingArray[16] = binding.date16
        dateBindingArray[17] = binding.date17
        dateBindingArray[18] = binding.date18
        dateBindingArray[19] = binding.date19
        dateBindingArray[20] = binding.date20
        dateBindingArray[21] = binding.date21
        dateBindingArray[22] = binding.date22
        dateBindingArray[23] = binding.date23
        dateBindingArray[24] = binding.date24
        dateBindingArray[25] = binding.date25
        dateBindingArray[26] = binding.date26
        dateBindingArray[27] = binding.date27
        dateBindingArray[28] = binding.date28
        dateBindingArray[29] = binding.date29
        dateBindingArray[30] = binding.date30
        dateBindingArray[31] = binding.date31
        dateBindingArray[32] = binding.date32
        dateBindingArray[33] = binding.date33
        dateBindingArray[34] = binding.date34
        dateBindingArray[35] = binding.date35
        dateBindingArray[36] = binding.date36
        dateBindingArray[37] = binding.date37
        dateBindingArray[38] = binding.date38
        dateBindingArray[39] = binding.date39
        dateBindingArray[40] = binding.date40
        dateBindingArray[41] = binding.date41

        calendar.time = Date()
        drawCalendar()
    }
    fun drawCalendar() {
        binding.thisMonth = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"

        val middleMonth = calendar.get(Calendar.MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        while(calendar[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -1)
        }

        for(i in 0..41) {
            val dateBinding = dateBindingArray[i]!!
            dateBinding.isSelected = false
            when(calendar[Calendar.DAY_OF_WEEK]) {
                Calendar.SUNDAY, Calendar.SATURDAY -> {
                    dateBinding.isHoliday = true
                }
                else -> {
                    dateBinding.isHoliday = false
                }
            }

            dateBinding.dateNo = calendar.get(Calendar.DATE).toString()
            dateBinding.isPast = calendar.time < Calendar.getInstance().time
            if(!dateBinding.isPast!! && !dateBinding.isHoliday!!) {
                dateBinding.canReserve = Random().nextInt(2) == 0
            } else {
                dateBinding.canReserve = false
            }

            if(middleMonth == calendar.get(Calendar.MONTH)) {
                dateBinding.dateTextview.textSize = 20F
            } else {
                dateBinding.dateTextview.textSize = 15F
                val params = dateBinding.dateTextview.layoutParams as FrameLayout.LayoutParams
                params.setMargins(20)
                dateBinding.dateTextview.layoutParams = params
            }
            dateBinding.dateTextview.setOnClickListener {
                if(selectedDateBinding != null) {
                    selectedDateBinding!!.isSelected = false
                }
                binding.isDateSelected = true
                dateBinding.isSelected = true
                selectedDateBinding = dateBinding
            }

            calendar.add(Calendar.DATE, 1)
        }
    }
    fun onClickPreviousMonth(view: View) {
        releaseSelectedDateBinding()
        calendar.add(Calendar.MONTH, -2)
        drawCalendar()
    }
    fun onClickNextMonth(view: View) {
        releaseSelectedDateBinding()
        binding.isDateSelected = false
        drawCalendar()
    }
    private fun releaseSelectedDateBinding() {
        if(selectedDateBinding != null) {
            selectedDateBinding!!.isSelected = false
        }
        selectedDateBinding = null
        binding.isDateSelected = false
    }
    fun onClickReservationTime(view: View) {
        val mIntent = Intent(this, ConfirmExamReservationActivity::class.java)
        startActivity(mIntent)
    }
}