package com.medichain.totalhealth

import android.content.Intent
import android.graphics.Region
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityExamCategoryBinding
import com.medichain.totalhealth.databinding.ActivityRegionChoiceBinding
import com.medichain.totalhealth.view_adapter.HospitalViewAdapter
import com.medichain.totalhealth.view_adapter.RegionChoiceViewAdapter

class RegionChoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegionChoiceBinding
    lateinit var regionListAdapter : RegionChoiceViewAdapter
    val regionList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_region_choice)
        binding.activity = this@RegionChoiceActivity
        title = "지역 선택"

        regionList.add("서울특별시")
        regionList.add("부산광역시")
        regionList.add("대구광역시")
        regionList.add("인천광역시")
        regionList.add("광주광역시")
        regionList.add("대전광역시")
        regionList.add("울산광역시")
        regionList.add("세종특별자치시")
        regionList.add("경기도")
        regionList.add("강원도")
        regionList.add("충청북도")
        regionList.add("충청남도")
        regionList.add("전라북도")
        regionList.add("전라남도")
        regionList.add("경상북도")
        regionList.add("경상남도")
        regionList.add("제주특별자치도")


        regionListAdapter = RegionChoiceViewAdapter(this@RegionChoiceActivity, regionList, intent.getStringExtra("selected"))
        binding.regionListRecyclerView.adapter = regionListAdapter

        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val mIntent = Intent(this@RegionChoiceActivity, HospitalSelectionActivity::class.java)
            mIntent.putExtra("regionString", regionListAdapter.selectedRegion)

            setResult(1001, mIntent)

            finish()
        }
    }


//    fun onClickNextButton(view: View) {
//        val mIntent = Intent(this, HospitalSelectionActivity::class.java)
//        mIntent.putExtra("regionString", intent.getStringExtra("Category"))
//
//        setResult(1001, mIntent)
//
//        finish()
//    }

}