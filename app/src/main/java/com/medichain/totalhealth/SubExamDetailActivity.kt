package com.medichain.totalhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivitySubExamDetailBinding

class SubExamDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubExamDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_exam_detail)
        title = "상세 설명"
    }
}