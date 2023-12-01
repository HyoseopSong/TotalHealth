package com.medichain.totalhealth

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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

        permissionCheck()

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
    fun onClickCopyFcmKey(view: View) {

        // 현재 토큰을 가져오려면
        // FirebaseMessaging.getInstace().getToken()을 호출한다.
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MyTAG_FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // FCM 등록 토큰 가져오기
            val token = task.result


            val clipboard: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", token)
            clipboard.setPrimaryClip(clip)

            val msg = "푸시키가 클립보드에 복사되었습니다."
            Log.d("MyTAG_FCM", token)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission is denied", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission is granted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private val PERMISSION_REQUEST_CODE = 5000
    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }
}