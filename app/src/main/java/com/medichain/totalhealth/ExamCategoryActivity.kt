package com.medichain.totalhealth

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityExamCategoryBinding
import com.medichain.totalhealth.view_adapter.ExamCategoryViewAdapter

class ExamCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExamCategoryBinding
    private lateinit var examCategoryListAdapter : ExamCategoryViewAdapter
    private val examCategoryList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_category)
        title = "검진 예약"

        val onClickItem: View.OnClickListener = View.OnClickListener { v ->
            val selectedCategory = (v as TextView).text
            val mIntent = Intent(this@ExamCategoryActivity, HospitalSelectionActivity::class.java)
            mIntent.putExtra("Category", selectedCategory)

            startActivity(mIntent)
        }
        examCategoryList.add("종합검진")
        examCategoryList.add("국가검진")
        examCategoryList.add("내시경")
        examCategoryList.add("초음파")
        examCategoryList.add("CT")
        examCategoryList.add("MRI")
        examCategoryListAdapter = ExamCategoryViewAdapter(this@ExamCategoryActivity, examCategoryList, onClickItem)
        binding.examCategoryListRecyclerView.adapter = examCategoryListAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.exam_category, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.queryDiagnosisList -> {
                val mIntent = Intent(this, DiagnosisHistoryActivity::class.java)
                startActivity(mIntent)
                true
            }
            R.id.queryInsuranceList -> {
                val mIntent = Intent(this, InsuranceStatusActivity::class.java)
                startActivity(mIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}