package com.medichain.totalhealth

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.medichain.totalhealth.databinding.ActivityInsuranceStatusBinding
import io.codef.api.EasyCodef
import io.codef.api.EasyCodefServiceType
import io.codef.api.EasyCodefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

class InsuranceStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsuranceStatusBinding
    private var globalCodeF : EasyCodef? = null
    private var productUrlInsuranceStatus = "/v1/kr/insurance/0001/credit4u/contract-info"
    private val simpleAuth = HashMap<String, Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_status)
        binding.activity = this@InsuranceStatusActivity

        CoroutineScope(Dispatchers.Default).launch {
            val id = "seopjjang"
            val pw = "pojty9-mixQas-wondyn"
            val userName = "송효섭"
            val phoneNo = "01098224728"
            globalCodeF = EasyCodef()
            val codeF = EasyCodef()
            codeF.setClientInfoForDemo("8086901b-bc16-485e-b5f5-4dd2c88dc400", "0c0d700d-4d0a-4446-a027-99967b04a34a");
            codeF.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA34tQqkNG9gD60SmFuLuHAL9biFno82DYkWY9+H7weE1RNCFeJ1AydVqGDyxm2DOWH+90gwLyUbjMKx6LzZJgpqVgzTR5zd75F+cYhLw4lMavVKenZY1hpEGgfPSVH+Yu0BpJDDw8WCCpED+BbpVSO39D1HKc/dl8TMFQ1q8WQGJscs5S7NU7qSFVjoBOQlM6tAh0YgkI13RKomGmXtUbpbNk1unpyBJv4h4Fqm8BtBUD4bxpgv7w4SCmuG7sr4Arlm/fhL0iqnjGgMYRiDV1LOWCzMz2LPb5kCo9OMHA345AQYCyPtUbFr8SBxwHNug24Uwgk7jALvpBOLEdaE1xeQIDAQAB")

            simpleAuth["organization"] = "0001"
            simpleAuth["id"] = id
            simpleAuth["password"] = encryptRSA(pw, codeF.publicKey)
//            simpleAuth["password"] = EasyCodefUtil.encryptRSA(pw, codeF.publicKey)
            simpleAuth["type"] = "0"
            simpleAuth["userName"] = userName
            simpleAuth["identity"] = ""
            simpleAuth["birthDate"] = ""
            simpleAuth["phoneNo"] = phoneNo
            simpleAuth["telecom"] = "0"
            simpleAuth["timeOut"] = "160"
            simpleAuth["authMethod"] = ""
            simpleAuth["applicationType"] = "0"
            simpleAuth["identityEncYn"] = "N"

            CoroutineScope(Dispatchers.Default).launch {
                val resultInsurenceCert = codeF.requestProduct(productUrlInsuranceStatus, EasyCodefServiceType.DEMO, simpleAuth)
                Log.d("MyTag resultInsurenceCert", resultInsurenceCert)
                val responseMap = ObjectMapper().readValue(
                    resultInsurenceCert,
                    HashMap::class.java
                )

                val clipboard: ClipboardManager =
                    getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", resultInsurenceCert)
                clipboard.setPrimaryClip(clip)

                val resultMap = responseMap?.get("result") as HashMap<*, *>
                if(resultMap["code"].toString() == "CF-03002") {
                    val dataMap = responseMap["data"] as HashMap<*, *>
                    if(dataMap.contains("continue2Way")) {

                    } else {

                    }

                    binding.isRequsted = true
//        val dataMap = responseMap["data"] as HashMap<*, *>
                    simpleAuth["is2Way"] = true
                    val twoWayInfo = HashMap<String, Any>()
                    twoWayInfo["jobIndex"] = dataMap["jobIndex"] as Int
                    twoWayInfo["threadIndex"] = dataMap["threadIndex"] as Int
                    twoWayInfo["jti"] = dataMap["jti"] as String
                    twoWayInfo["twoWayTimestamp"] = dataMap["twoWayTimestamp"] as Long
                    simpleAuth["twoWayInfo"] = twoWayInfo
                } else {
                    binding.isInputEnabled  = true
                    binding.errorText = resultMap["code"].toString() + "\n" + resultMap["message"].toString()
                }
            }
        }

    }

    fun encryptRSA(plainText: String, publicKey: String?): String {
        val bytePublicKey: ByteArray = Base64.getDecoder().decode(publicKey)
        val keyFactory = KeyFactory.getInstance("RSA")
        val key = keyFactory.generatePublic(X509EncodedKeySpec(bytePublicKey))
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(1, key)
        val bytePlain = cipher.doFinal(plainText.toByteArray())

        return Base64.getEncoder().encodeToString(bytePlain); //Base64 는 자바의 Base64 사용하셔야합니다.
    }

}