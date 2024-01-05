package com.medichain.totalhealth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.medichain.totalhealth.databinding.ActivityCustomerInfoBinding
import com.medichain.totalhealth.fragment.CheckUpResultFragment
import com.medichain.totalhealth.fragment.ExamTargetFragment
import com.medichain.totalhealth.fragment.InsuranceSpecificationFragment
import com.medichain.totalhealth.view_adapter.Customer

class CustomerInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerInfoBinding
    private lateinit var customer:Customer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_info)
        title = "고객 상세 정보"

        customer = intent.getSerializableExtra("Customer") as Customer
        val certification = intent.getSerializableExtra("Certification")
        val examSubject = intent.getSerializableExtra("ExamSubject")
        val checkUpResult = intent.getSerializableExtra("CheckUpResult")


        val bundle = Bundle()
        bundle.putSerializable("Customer", customer)
        bundle.putSerializable("Certification", certification)
        bundle.putSerializable("ExamSubject", examSubject)
        bundle.putSerializable("CheckUpResult", checkUpResult)
        val insuranceFragment: Fragment = InsuranceSpecificationFragment()
        insuranceFragment.arguments = bundle
        val examTargetFragment: Fragment = ExamTargetFragment()
        examTargetFragment.arguments = bundle
        val checkUpFragment: Fragment = CheckUpResultFragment()
        checkUpFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_view, insuranceFragment).commit()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position) {
                    0 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, insuranceFragment).commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, examTargetFragment).commit()
                    }
                    2 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, checkUpFragment).commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.customer_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reCertification -> {

                val mIntent = Intent(this@CustomerInfoActivity, IdentityCertificationActivity::class.java)
                mIntent.putExtra("reCertification", true)
                mIntent.putExtra("name", customer.name)
                mIntent.putExtra("phone", customer.phone)
                mIntent.putExtra("birthYear", customer.birth.substring(0,4))
                mIntent.putExtra("birthMonth", customer.birth.substring(4,6))
                mIntent.putExtra("birthDay", customer.birth.substring(6))
                startActivity(mIntent)

                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}