package com.medichain.totalhealth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.medichain.totalhealth.databinding.ActivityIdentityCertificationBinding
import io.codef.api.EasyCodef
import io.codef.api.EasyCodefServiceType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


class IdentityCertificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentityCertificationBinding
    private val calendar: Calendar = Calendar.getInstance()

    private var finalBirth = ""
    var carrier = ""
    private var globalCodeF : EasyCodef? = null
    private var productUrlCert = "/v1/kr/public/pp/nhis-join/identify-confirmation"
    private var productUrlExam = "/v1/kr/public/pp/nhis-list/examination"
    private val simpleAuth = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_identity_certification)
        binding.activity = this@IdentityCertificationActivity

        title = "간편인증"
        binding.allDoneButtonText = "확인"

        binding.certificationAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayOf("카카오톡","페이코","삼성패스","KB모바일","통신사","네이버","신한인증서","Toss"))
        binding.carrierAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayOf("SKT(SKT알뜰폰)", "KT(KT알뜰폰)", "LG U+(LG U+알뜰폰)"))

        binding.isInputEnabled = true
        binding.isRequested = false
        binding.isDoneClicked = false
        binding.isLastAnswerReceived = false
        binding.isLoadingLayoutVisible = false

//        binding.userName = "송효섭"
////        binding.juminBirthPart = "851223"
////        binding.juminSexPart = "1234567"
//        binding.phoneNumber = "01098224728"
//
////        binding.birthYear = calendar.get(Calendar.YEAR).toString()
////        binding.birthMonth = (calendar.get(Calendar.MONTH) + 1).toString()
////        binding.birthDay = calendar.get(Calendar.DATE).toString()
//        binding.birthYear = "1985"
//        binding.birthMonth = "2"
//        binding.birthDay = "28"
////        binding.carrierSpinner.setSelection(2)

        binding.companyInfoText1  = ""
        binding.companyInfoText2  = ""
        binding.healthExamInfoText  = ""
        binding.isAgreed = true

        CoroutineScope(Dispatchers.Default).launch {
            globalCodeF = EasyCodef()
            val codeF = globalCodeF!!
//          codeF.setClientInfo(EasyCodefClientInfo.CLIENT_ID, EasyCodefClientInfo.CLIENT_SECRET);
            codeF.setClientInfoForDemo("8086901b-bc16-485e-b5f5-4dd2c88dc400", "0c0d700d-4d0a-4446-a027-99967b04a34a");
            codeF.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA34tQqkNG9gD60SmFuLuHAL9biFno82DYkWY9+H7weE1RNCFeJ1AydVqGDyxm2DOWH+90gwLyUbjMKx6LzZJgpqVgzTR5zd75F+cYhLw4lMavVKenZY1hpEGgfPSVH+Yu0BpJDDw8WCCpED+BbpVSO39D1HKc/dl8TMFQ1q8WQGJscs5S7NU7qSFVjoBOQlM6tAh0YgkI13RKomGmXtUbpbNk1unpyBJv4h4Fqm8BtBUD4bxpgv7w4SCmuG7sr4Arlm/fhL0iqnjGgMYRiDV1LOWCzMz2LPb5kCo9OMHA345AQYCyPtUbFr8SBxwHNug24Uwgk7jALvpBOLEdaE1xeQIDAQAB")

        }
    }

    fun onClickAgree(view: View) {
        binding.errorText = ""

        if(binding.userName.isNullOrEmpty()){
            binding.errorText = "이름을 입력해 주세요."
            return
        }

        if(binding.birthYear.isNullOrEmpty()
            || binding.birthMonth.isNullOrEmpty()
            || binding.birthDay.isNullOrEmpty()
            ) {
            binding.errorText = "생년월일을 입력해 주세요."
            return
        }

//        try{
//            calendar.set(binding.birthYear, binding.birthMonth, binding.birthDay)
//        } catch () {
//            binding.errorText = "주민등록 번호를 확인해 주세요."
//            return
//        }

//        try{
//            val cent = when(binding.juminSexPart){
//                "1" -> "19"
//                "2" -> "19"
//                "3" -> "20"
//                else -> "20"
//            }
//            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
//            var dt = LocalDate.parse(cent + binding.juminBirthPart,formatter)
//        }catch (e:Exception){
//            Log.e("Mytag", e.message.toString())
//            Log.e("Mytag", e.stackTraceToString())
//            binding.errorText = "생년월일을 다시 확인해 주세요."
//            return
//        }

        if(binding.phoneNumber.isNullOrEmpty()
            || binding.phoneNumber!!.length < 10
            || binding.phoneNumber!!.length > 11
        ){
            binding.errorText = "휴대폰 번호는 7~8자리를 입력해야 합니다."
            return
        }

//        when(binding.juminSexPart){
//            "1" -> {
//                finalBirth = "19${binding.juminBirthPart}"
//            }
//            "2" -> {
//                finalBirth = "19${binding.juminBirthPart}"
//            }
//            "3" -> {
//                finalBirth = "20${binding.juminBirthPart}"
//            }
//            "4" -> {
//                finalBirth = "20${binding.juminBirthPart}"
//            }
//        }

        binding.isInputEnabled  = false
        calendar.set(binding.birthYear!!.toInt(), binding.birthMonth!!.toInt(), binding.birthDay!!.toInt())
        simpleAuth["organization"] = "0002"
        simpleAuth["loginType"] = "5"
        simpleAuth["identity"] = calendar.get(Calendar.YEAR).toString() + (calendar.get(Calendar.MONTH).toString().padStart(2, '0')) + calendar.get(Calendar.DATE).toString().padStart(2, '0')
        simpleAuth["loginTypeLevel"] = "1"
        simpleAuth["userName"] = binding.userName.toString()
        simpleAuth["phoneNo"] = binding.phoneNumber.toString()
        simpleAuth["useType"] = "0"
        simpleAuth["isIdentityViewYN"] = "0"
        simpleAuth["id"] = getRandomString(10)
        simpleAuth["originDataYN"] = "0"
        simpleAuth["telecom"] = binding.carrierSpinner.selectedItemPosition.toString()

        val codeF = globalCodeF!!
        CoroutineScope(Dispatchers.Default).launch {
            val resultCert = codeF.requestProduct(productUrlCert, EasyCodefServiceType.DEMO, simpleAuth)
//                Log.d("MyTag CertRequest", resultCert)
            delay(500)
            requestExamList()
            val responseMap = ObjectMapper().readValue(
                resultCert,
                HashMap::class.java
            )
            val resultMap = responseMap?.get("result") as HashMap<*, *>
            if(resultMap["code"].toString() == "CF-03002") {
                prepareCertification(responseMap["data"] as HashMap<*, *>)
            } else {
                binding.isInputEnabled  = true
                binding.errorText = resultMap["code"].toString() + "\n" + resultMap["message"].toString()
            }
        }

    }

    fun requestExamList() {
        val codeF = globalCodeF!!
        CoroutineScope(Dispatchers.Default).launch {
            val resultExam = codeF.requestProduct(productUrlExam, EasyCodefServiceType.DEMO, simpleAuth)
            Log.d("MyTag ExamListRequest", resultExam)
            val responseMap: java.util.HashMap<*, *>? = ObjectMapper().readValue(
                resultExam,
                HashMap::class.java
            )
            val resultMap = responseMap?.get("result") as HashMap<*, *>

            if (resultMap["code"] == "CF-00000") {
                if(responseMap["data"] is ArrayList<*>) {
                    val responseMapArrayList = responseMap["data"] as ArrayList<*>
                    if(responseMapArrayList.isEmpty()) {
                        binding.healthExamInfoText += "이번 년도 검진 대상이 아닙니다.\n"
                    } else {
                        binding.healthExamInfoText += "관리자에게 문의해 주세요.\n"
                    }
                } else {
                    val dataMap = responseMap["data"] as LinkedHashMap<*,*>
                    binding.healthExamInfoText += "성명 : " + dataMap["resUserNm"] + "\n" +
                            "생년월일 : " + dataMap["commBirthDate"] + "\n" +
                            "검진년도 : " + dataMap["resCheckupYear"] + "\n" +
                            "구강검진 : " + dataMap["resDentalExam"] + "\n" +
                            "B형 간염검사 : " + dataMap["reshepatitisBTest"] + "\n" +
                            "일반검진 : " + dataMap["resGeneralExam"] + "\n" +
                            "확진검사 : " + dataMap["resConfirmTest"] + "\n" +
                            "보건소 : " + dataMap["resPublicHealth"] + "\n"
                    val resCancerScreeningList = dataMap["resCancerScreeningList"] as ArrayList<*>
                    for(item in resCancerScreeningList) {
                        val itemMap = item as LinkedHashMap<*, *>
                        binding.healthExamInfoText += "\n구분 : " + itemMap["resType"] + "\n" +
                                "암검진 : " + itemMap["resCancerScreeningList"] + "\n" +
                                "의료비 지원 대상 : " + itemMap["resMedicalExpenses"] + "\n"
                    }
                }
            } else {
                binding.isInputEnabled  = true
                binding.errorText += resultMap["code"].toString() + "\n" + resultMap["message"].toString() + "\n"
            }
        }
    }

    fun prepareCertification(dataMap : HashMap<*, *>) {
        binding.isRequested = true
//        val dataMap = responseMap["data"] as HashMap<*, *>
        simpleAuth["is2Way"] = true
        val twoWayInfo = HashMap<String, Any>()
        twoWayInfo["jobIndex"] = dataMap["jobIndex"] as Int
        twoWayInfo["threadIndex"] = dataMap["threadIndex"] as Int
        twoWayInfo["jti"] = dataMap["jti"] as String
        twoWayInfo["twoWayTimestamp"] = dataMap["twoWayTimestamp"] as Long
        simpleAuth["twoWayInfo"] = twoWayInfo
    }

    fun onClickDone(view: View) {
        certificationDone(true)
    }

    fun onClickCancel(view: View) {
        certificationDone(false)
        // {"result":{"code":"CF-12102","extraMessage":"","message":"추가 인증 진행이 취소되었습니다.","transactionId":"6566790428e6482d2c4d7f61"},"data":{}}
    }

    private fun certificationDone(isDone : Boolean) {
        simpleAuth["simpleAuth"] = if(isDone) "1" else "0"

        binding.isLoadingLayoutVisible = true
        CoroutineScope(Dispatchers.Default).launch {
            binding.isDoneClicked = true
            val codeF = globalCodeF!!
            val resultCertification = codeF.requestCertification(productUrlCert, EasyCodefServiceType.DEMO, simpleAuth)
            Log.d("MyTag resultCertification", resultCertification)
            binding.isLoadingLayoutVisible = false
            val responseMap: java.util.HashMap<*, *> = ObjectMapper().readValue(
                resultCertification,
                HashMap::class.java
            )
            val resultMap = responseMap["result"] as LinkedHashMap<*,*>

            if (resultMap["code"] == "CF-00000") {
                binding.isLastAnswerReceived = true
                val dataArrayList = responseMap["data"] as ArrayList<*>
                if (dataArrayList.size > 0) {
                    val dataMap0 = dataArrayList[0] as LinkedHashMap<*, *>

                    binding.companyInfoText1 = "상호 : " + dataMap0["resCompanyNm"]
                    binding.isAgreed = false
                    binding.companyInfoText2 = "\n자격상실일 : " + dataMap0["commEndDate"] + "\n" +
                            "가입자 구분 : " + dataMap0["resJoinUserType"] + "\n" +
                            "발급일자 : " + dataMap0["resIssueDate"] + "\n" +
                            "자격취득일 : " + dataMap0["commStartDate"] + "\n" +
                            "주민번호 : " + dataMap0["resUserIdentiyNo"] + "\n" +
                            "성명 : " + dataMap0["resUserNm"] + "\n" +
                            "발급번호 : " + dataMap0["resIssueNo"] + "\n"
                } else {
                    binding.companyInfoText1 = "자격 득실 내역이 없습니다.\n"
                }
            } else {
                if (resultMap["code"] == "CF-03002") {
                    binding.isDoneClicked = false
                    binding.errorText = "인증이 완료되지 않았습니다."
                } else if (resultMap["code"] == "CF-12102") {
                    binding.companyInfoText1 = resultMap["message"].toString()
                } else {
                    binding.companyInfoText1 = "잠시 후 다시 시도해 주세요.\n" +
                            resultMap["code"].toString() + "\n" +
                            resultMap["message"].toString() + "\n" +
                            resultMap["extraMessage"].toString()
                }
            }
        }
    }

    fun onClickNext(view: View) {
        goToNextActivity()
    }

    private fun goToNextActivity() {
        val mIntent = Intent(this@IdentityCertificationActivity, ExamCategoryActivity::class.java)
        startActivity(mIntent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.identification, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.skipCertification -> {
                goToNextActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}