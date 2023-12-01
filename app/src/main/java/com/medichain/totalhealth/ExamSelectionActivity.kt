package com.medichain.totalhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityExamCategoryBinding
import com.medichain.totalhealth.databinding.ActivityExamSelectionBinding
import com.medichain.totalhealth.view_adapter.ExamInfo
import com.medichain.totalhealth.view_adapter.ExamSelectionViewAdapter
import com.medichain.totalhealth.view_adapter.HospitalViewAdapter

class ExamSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExamSelectionBinding
    private lateinit var examListAdapter : ExamSelectionViewAdapter
    private val examList = ArrayList<ExamInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_selection)
        binding.activity = this@ExamSelectionActivity
        title = "검사 선택"

        val onClickSubExamItem: View.OnClickListener = View.OnClickListener { v ->
            val mIntent = Intent(this@ExamSelectionActivity, SubExamDetailActivity::class.java)
            startActivity(mIntent)
        }

        when(intent.getStringExtra("Category")) {
            "종합검진", "국가검진" -> {
                examList.add(ExamInfo("", arrayListOf(), true, false))
                examList.add(ExamInfo("초음파", arrayListOf("갑상선 초음파", "전립선 초음파", "경동맥 초음파"), false, false))
                examList.add(ExamInfo("CT", arrayListOf(), false, false))
                examList.add(ExamInfo("내시경", arrayListOf(), false, false))
                examList.add(ExamInfo("MRI", arrayListOf(), false, false))
                examList.add(ExamInfo("", arrayListOf(), true, true))
                examList.add(ExamInfo("선택검사1", arrayListOf("노력성 폐활량", "1초간 노력성 호기량", "노력성 호기 중간 유량", "최대 호기유량", "노력성호기비", "흉부X-선"), false, true))
                examList.add(ExamInfo("선택검사2", arrayListOf(),false, true))
                examList.add(ExamInfo("선택검사3", arrayListOf(), false, true))
            }
            else -> {
                examList.add(ExamInfo("초음파", arrayListOf(), false, true))
                examList.add(ExamInfo("CT", arrayListOf(), false, true))
                examList.add(ExamInfo("내시경", arrayListOf(), false, true))
                examList.add(ExamInfo("MRI", arrayListOf(), false, true))
            }
        }

        examListAdapter = ExamSelectionViewAdapter(this@ExamSelectionActivity, examList, onClickSubExamItem)
        binding.examSelectionListRecyclerView.adapter = examListAdapter
    }


    fun onClickNextButton(view: View) {
        val mIntent = Intent(this, DateSelectionActivity::class.java)
        startActivity(mIntent)
    }
}