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
import com.medichain.totalhealth.network.APIInterface
import com.medichain.totalhealth.network.ServerAPI
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StartUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_up)
//        setContentView(R.layout.activity_main)
        binding.isSplash = false
        binding.activity = this@StartUpActivity
        binding.loginEnabled = true
        binding.loginStatus = "로그인"

        permissionCheck()

        binding.loginID = "CD0002"
        binding.loginPW = "bbbb"

//        CoroutineScope(Dispatchers.Default).launch {
//            delay(3000)
//            binding.isSplash = false
//        }
    }

    fun onClickUserCertification(view: View) {
        val mIntent = Intent(this, IdentityCertificationActivity::class.java)
        mIntent.putExtra("certiType", "confirmation")

        startActivity(mIntent)
        finish()
    }
    var loginCount = 0
    fun onClickLogIn(view: View) {
        binding.loginEnabled = false
        binding.loginStatus = if(loginCount == 0)"서버 응답 대기 중..." else "로그인 재시도 $loginCount"
        ServerAPI().getAPI(this@StartUpActivity).create(APIInterface::class.java)
            .LogIn(binding.loginID + "," + binding.loginPW)
            .enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful) {
                        val responseBody = response.body()!!.string()
                        if(responseBody == "T") {
                            val mIntent = Intent(this@StartUpActivity, CustomerListActivity::class.java)

                            getSharedPreferences(packageName, MODE_PRIVATE).edit()
                                .putString("LoginID", binding.loginID)
                                .apply()

                            startActivity(mIntent)
                            finish()
                        } else {
                            Toast.makeText(this@StartUpActivity, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        val errorDetail = jObjError["detail"].toString()
                        if(errorDetail.startsWith("ORA-12570")) {
                            Toast.makeText(this@StartUpActivity, errorDetail, Toast.LENGTH_SHORT).show()
                            loginCount++
                            onClickLogIn(view)
                        } else {
                            binding.loginStatus = errorDetail
                            binding.loginEnabled = true
                        }

//                        {"type":"OracleException","title":"An unexpected error occurred","status":500,"detail":"ORA-12570: Network Session: Unexpected packet read error","instance":"POST /Error"}
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Toast.makeText(this@IdentityCertificationActivity, "서버에 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
//                    binding.loadingLayout.root.visibility = View.GONE
                }
            })
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