package com.medichain.totalhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityStartUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_up)
//        setContentView(R.layout.activity_main)
        binding.isSplash = true
        binding.activity = this@StartUpActivity

        CoroutineScope(Dispatchers.Default).launch {
            delay(3000)
            binding.isSplash = false
        }
    }

    fun onClickUserCertification(view: View) {
        val mIntent = Intent(this, IdentityCertificationActivity::class.java)
        mIntent.putExtra("certiType", "confirmation")

        startActivity(mIntent)
        finish()
    }
}