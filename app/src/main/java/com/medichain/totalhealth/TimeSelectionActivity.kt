package com.medichain.totalhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TimeSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_selection)

        title = "검진 예약하기"
    }
}