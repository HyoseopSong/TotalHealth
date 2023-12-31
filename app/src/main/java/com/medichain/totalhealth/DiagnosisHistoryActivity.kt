package com.medichain.totalhealth

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.medichain.totalhealth.databinding.ActivityDiagnosisHistoryBinding
import io.codef.api.EasyCodef
import io.codef.api.EasyCodefServiceType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class DiagnosisHistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDiagnosisHistoryBinding

    private var globalCodeF : EasyCodef? = null
    private val productUrlDiagnosis = "/v1/kr/public/hw/hira-list/my-medical-information"
    private val productUrlMedicineHistory = "/v1/kr/public/hw/hira-list/my-medicine"
    private val simpleAuth = HashMap<String, Any>()
    private val simpleAuth2 = HashMap<String, Any>()
    private var resultText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diagnosis_history)
        binding.activity = this@DiagnosisHistoryActivity

        title = "진료 정보 조회"
        binding.allDoneButtonText = "확인"

        binding.certificationAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayOf("카카오톡","페이코","삼성패스","KB모바일","통신사","네이버","신한인증서","Toss"))
        binding.carrierAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayOf("SKT(SKT알뜰폰)", "KT(KT알뜰폰)", "LG U+(LG U+알뜰폰)"))

        binding.isInputEnabled = true
        binding.isRequested = false
        binding.isDoneClicked = false
        binding.isLastAnswerReceived = false
        binding.isLoadingLayoutVisible = false
//
//        binding.userName = "송효섭"
//        binding.juminFront = "850228"
//        binding.juminRear = "1684821"
//        binding.phoneNumber = "01098224728"

        setSearchTerm(-60)
//        binding.carrierSpinner.setSelection(2)

        binding.dataText  = ""
        binding.basicTreatText  = ""
        binding.detailTreatText  = ""
        binding.prescribeDrugText  = ""
        binding.pharmacyDrugText  = ""

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

        if(binding.juminFront.isNullOrEmpty() || binding.juminRear.isNullOrEmpty()) {
            binding.errorText = "주민등록 번호를 확인해 주세요."
            return
        }

        if(binding.phoneNumber.isNullOrEmpty()
            || binding.phoneNumber!!.length < 10
            || binding.phoneNumber!!.length > 11
        ){
            binding.errorText = "휴대폰 번호는 7~8자리를 입력해야 합니다."
            return
        }

        binding.isInputEnabled  = false

        simpleAuth["organization"] = "0020"
        simpleAuth["loginType"] = "5"
        simpleAuth["loginTypeLevel"] = "1"
        simpleAuth["phoneNo"] = binding.phoneNumber.toString()
        simpleAuth["userName"] = binding.userName.toString()
        simpleAuth["identity"] = binding.juminFront + binding.juminRear
        simpleAuth["startDate"] = binding.startYear.toString() + (binding.startMonth.toString().padStart(2, '0')) + binding.startDate.toString().padStart(2, '0')
        simpleAuth["endDate"] = binding.endYear.toString() + (binding.endMonth.toString().padStart(2, '0')) + binding.endDate.toString().padStart(2, '0')
        simpleAuth["telecom"] = binding.carrierSpinner.selectedItemPosition.toString()
        simpleAuth["id"] = getRandomString(10)
        simpleAuth["type"] = "0"

        val codeF = globalCodeF!!
        CoroutineScope(Dispatchers.Default).launch {
            Log.d("MyTag simpleAuth", simpleAuth.toString())
            val resultDiagnosis = codeF.requestProduct(productUrlDiagnosis, EasyCodefServiceType.DEMO, simpleAuth)
//                Log.d("MyTag resultDiagnosis", resultDiagnosis)
            delay(500)
            requestMedicineHistory()
            val responseMap = ObjectMapper().readValue(
                resultDiagnosis,
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

    fun requestMedicineHistory() {
        val codeF = globalCodeF!!
        CoroutineScope(Dispatchers.Default).launch {

            simpleAuth2["organization"] = "0020"
            simpleAuth2["loginType"] = "5"
            simpleAuth2["identity"] = binding.juminFront + binding.juminRear
            simpleAuth2["loginTypeLevel"] = "1"
            simpleAuth2["phoneNo"] = binding.phoneNumber.toString()
            simpleAuth2["userName"] = binding.userName.toString()
//            simpleAuth2["startDate"] = binding.startYear.toString() + (binding.startMonth.toString().padStart(2, '0'))
            simpleAuth2["telecom"] = binding.carrierSpinner.selectedItemPosition.toString()
            simpleAuth2["id"] = simpleAuth["id"].toString()
            simpleAuth2["detailYN"] = "0"
            val resultMedicinHistory = codeF.requestProduct(productUrlMedicineHistory, EasyCodefServiceType.DEMO, simpleAuth2)
            Log.d("MyTag MedicineHistoryRequest", resultMedicinHistory)
            val responseMap: java.util.HashMap<*, *>? = ObjectMapper().readValue(
                resultMedicinHistory,
                HashMap::class.java
            )
            val resultMap = responseMap?.get("result") as HashMap<*, *>

            if (resultMap["code"] == "CF-00000") {
                if(responseMap["data"] is ArrayList<*>) {
                    val dataMapArray = responseMap["data"] as ArrayList<*>
                    for(dMap in dataMapArray) {
                        val dataMap = dMap as LinkedHashMap<*, *>
                        val resManufactureDate = dataMap["resManufactureDate"].toString()
                        val commBrandName = dataMap["commBrandName"].toString()
                        val resDrugList = dataMap["resDrugList"] as ArrayList<*>
                        binding.pharmacyDrugText +=
                            "제조사명 : " + commBrandName + "\n" +
                            "제조일자 : " + resManufactureDate + "\n"

                        for(dInfo in resDrugList) {
                            val drugInfo = dInfo as LinkedHashMap<*, *>

//                            "약품명 : " + drugInfo["resDrugName"]
//                            "약품코드 : " + drugInfo["resDrugCode"]
//                            "총 투약일수 : " + drugInfo["resTotalDosingdays"]
//                            "1일 투여횟수 : " + drugInfo["resDailyDosesNumber"]
//                            "1회 투약량 : " + drugInfo["resOneDose"]
//                            "함량 : " + drugInfo["resContent"]
//                            "성분명 : " + drugInfo["resIngredients"]
//                            "호수 (매수) : " + drugInfo["resNumber"]
//                            "처방약품효능 : " + drugInfo["resPrescribeDrugEffect"]

                            binding.pharmacyDrugText +=
                                "약품명 : " + drugInfo["resDrugName"] + "\n" +
                                "약품코드 : " + drugInfo["resDrugCode"] + "\n" +
                                "총 투약일수 : " + drugInfo["resTotalDosingdays"] + "\n" +
                                "1일 투여횟수 : " + drugInfo["resDailyDosesNumber"] + "\n" +
                                "1회 투약량 : " + drugInfo["resOneDose"] + "\n" +
                                "함량 : " + drugInfo["resContent"] + "\n" +
                                "성분명 : " + drugInfo["resIngredients"] + "\n" +
                                "호수 (매수) : " + drugInfo["resNumber"] + "\n" +
                                "처방약품효능 : " + drugInfo["resPrescribeDrugEffect"] + "\n\n"

//                            binding.pharmacyDrugText +=
//                                "약품명 : " + drugInfo["resDrugName"] + "\n" +
//                                "약품코드 : " + drugInfo["resDrugCode"] + "\n" +
//                                "총 투약일수 : " + drugInfo["resTotalDosingdays"] + "\n" +
//                                "1일 투여횟수 : " + drugInfo["resDailyDosesNumber"] + "\n" +
//                                "1회 투약량 : " + drugInfo["resOneDose"] + "\n" +
//                                "함량 : " + drugInfo["resContent"] + "\n" +
//                                "성분명 : " + drugInfo["resIngredients"] + "\n" +
//                                "호수 (매수) : " + drugInfo["resNumber"] + "\n" +
//                                "처방약품효능 : " + drugInfo["resPrescribeDrugEffect"] + "\n" +
//                                "약품이미지 : " + drugInfo["resDrugImageLink"] + "\n" +
//                                "제조회사 : " + drugInfo["resBrand"] + "\n"
//                                "복약지도 : " + drugInfo["resMedicationDirection"] + "\n"
//                                "ATC코드 : " + drugInfo["resATCCode"] + "\n"
//                                "제형 : " + drugInfo["resFormula"] + "\n\n"
                        }
                    }

//                    val responseMapArrayList = responseMap["data"] as ArrayList<*>
//                    if(responseMapArrayList.isEmpty()) {
//                        binding.pharmacyDrugText += "이번 년도 검진 대상이 아닙니다.\n"
//                    } else {
//                        binding.pharmacyDrugText += "관리자에게 문의해 주세요.\n"
//                    }
                } else {
//                    val dataMap = responseMap["data"] as LinkedHashMap<*,*>
//                    binding.pharmacyDrugText += "성명 : " + dataMap["resUserNm"] + "\n" +
//                            "생년월일 : " + dataMap["commBirthDate"] + "\n" +
//                            "검진년도 : " + dataMap["resCheckupYear"] + "\n" +
//                            "구강검진 : " + dataMap["resDentalExam"] + "\n" +
//                            "B형 간염검사 : " + dataMap["reshepatitisBTest"] + "\n" +
//                            "일반검진 : " + dataMap["resGeneralExam"] + "\n" +
//                            "확진검사 : " + dataMap["resConfirmTest"] + "\n" +
//                            "보건소 : " + dataMap["resPublicHealth"] + "\n"
//                    val resCancerScreeningList = dataMap["resCancerScreeningList"] as ArrayList<*>
//                    for(item in resCancerScreeningList) {
//                        val itemMap = item as LinkedHashMap<*, *>
//                        binding.pharmacyDrugText += "\n구분 : " + itemMap["resType"] + "\n" +
//                                "암검진 : " + itemMap["resCancerScreeningList"] + "\n" +
//                                "의료비 지원 대상 : " + itemMap["resMedicalExpenses"] + "\n"
//                    }
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
    fun onClickSearchTerm3(view: View) {
        setSearchTerm(-3)
    }
    fun onClickSearchTerm6(view: View) {
        setSearchTerm(-6)
    }
    fun onClickSearchTerm12(view: View) {
        setSearchTerm(-12)

    }

    fun setSearchTerm(month:Int) {
        val tmpCalendar: Calendar = Calendar.getInstance()
        binding.endYear = tmpCalendar.get(Calendar.YEAR).toString()
        binding.endMonth = (tmpCalendar.get(Calendar.MONTH) + 1).toString()
        binding.endDate = tmpCalendar.get(Calendar.DATE).toString()
        tmpCalendar.add(Calendar.MONTH, month)
        tmpCalendar.add(Calendar.DATE, 1)
        binding.startYear = tmpCalendar.get(Calendar.YEAR).toString()
        binding.startMonth = (tmpCalendar.get(Calendar.MONTH) + 1).toString()
        binding.startDate = tmpCalendar.get(Calendar.DATE).toString()
    }
    fun onClickCancel(view: View) {
        certificationDone(false)
        // {"result":{"code":"CF-12102","extraMessage":"","message":"추가 인증 진행이 취소되었습니다.","transactionId":"6566790428e6482d2c4d7f61"},"data":{}}
    }

    private fun certificationDone(isDone : Boolean) {
        Log.d("MyTag certificationDone", "certificationDone")
        simpleAuth["simpleAuth"] = if(isDone) "1" else "0"

        binding.isLoadingLayoutVisible = true
        binding.loadingLayout.infoTxt = "진료 내역이 많을 경우, 30초 이상 소요될 수도 있습니다."
        CoroutineScope(Dispatchers.Default).launch {
            binding.isDoneClicked = true
            val codeF = globalCodeF!!

            val resultCertification = codeF.requestCertification(productUrlDiagnosis, EasyCodefServiceType.DEMO, simpleAuth)
            Log.d("MyTag resultCertification", resultCertification)
            binding.isLoadingLayoutVisible = false
            val responseMap: java.util.HashMap<*, *> = ObjectMapper().readValue(
                resultCertification,
                HashMap::class.java
            )
            val resultMap = responseMap["result"] as LinkedHashMap<*,*>

            if (resultMap["code"] == "CF-00000") {
                binding.isLastAnswerReceived = true
                val dataMap = responseMap["data"] as LinkedHashMap<*,*>

                binding.dataText = "성명 : " + dataMap["commName"] + "\n" +
                        "조회시작일 : " + dataMap["commStartDate"] + "\n" +
                        "조회종료일 : " + dataMap["commEndDate"] + "\n" +
                        "회사명 : " + getSharedPreferences(packageName, MODE_PRIVATE).getString("회사명", "메디체인") + "\n" +
                        "키 : " + getSharedPreferences(packageName, MODE_PRIVATE).getString("키", "메디체인") + "\n" +
                        "몸무게 : " + getSharedPreferences(packageName, MODE_PRIVATE).getString("몸무게", "메디체인") + "\n" +
                        "BMI : " + getSharedPreferences(packageName, MODE_PRIVATE).getString("BMI", "메디체인") + "\n"

                val resBasicTreatList = dataMap["resBasicTreatList"] as ArrayList<*>
                for(item in resBasicTreatList) {
                    val itemMap = item as LinkedHashMap<*, *>
                    if(itemMap["resDepartment"].toString() == "일반의") {
                        continue
                    }
//                    "진료시작일 : " + itemMap["resTreatStartDate"]
//                    "병/의원&약국 : " + itemMap["resHospitalName"]
//                    "진단과 : " + itemMap["resDepartment"]
//                    "타입 : " + itemMap["resTreatType"]
//                    "주상병코드 : " + itemMap["resDiseaseCode"]
//                    "주상병명 : " + itemMap["resDiseaseName"]
//                    "내원일수 : " + itemMap["resVisitDays"]

                    binding.basicTreatText +=
                        "진료시작일 : " + itemMap["resTreatStartDate"] + "\n" +
                        "병/의원&약국 : " + itemMap["resHospitalName"] + "\n" +
                        "진단과 : " + itemMap["resDepartment"] + "\n" +
                        "타입 : " + itemMap["resTreatType"] + "\n" +
                        "주상병코드 : " + itemMap["resDiseaseCode"] + "\n" +
                        "주상병명 : " + itemMap["resDiseaseName"] + "\n" +
                        "내원일수 : " + itemMap["resVisitDays"] + "\n\n"

//                    binding.basicTreatText += "진료시작일 : " + itemMap["resTreatStartDate"] + "\n" +
//                            "병/의원&약국 : " + itemMap["resHospitalName"] + "\n" +
//                            "진단과 : " + itemMap["resDepartment"] + "\n" +
//                            "타입 : " + itemMap["resTreatType"] + "\n" +
//                            "주상병코드 : " + itemMap["resDiseaseCode"] + "\n" +
//                            "주상병명 : " + itemMap["resDiseaseName"] + "\n" +
//                            "내원일수 : " + itemMap["resVisitDays"] + "\n" +
//                            "총 진료비 : " + itemMap["resTotalAmount"] + "\n" +
//                            "혜택받은 금액 : " + itemMap["resPublicCharge"] + "\n" +
//                            "내가 낸 의료비 : " + itemMap["resDeductibleAmt"] + "\n" +
//                            "병원코드 : " + itemMap["resHospitalCode"] + "\n\n"
                }

                val resDetailTreatList = dataMap["resDetailTreatList"] as ArrayList<*>
                for(item in resDetailTreatList) {
                    val itemMap = item as LinkedHashMap<*, *>
                    if(itemMap["resTreatType"] == "조제료등") {
                        continue
                    }
//                    "진료시작일 : " + itemMap["resTreatStartDate"]
//                    "병/의원&약국 : " + itemMap["resHospitalName"]
//                    "진료형태 : " + itemMap["resTreatType"]
//                    "코드명 : " + itemMap["resCodeName"]
//                    "1회 투약량 : " + itemMap["resOneDose"]
//                    "1회 투여횟수 : " + itemMap["resDailyDosesNumber"]
//                    "총 투약일수 : " + itemMap["resTotalDosingdays"]

                    binding.detailTreatText += "진료시작일 : " + itemMap["resTreatStartDate"] + "\n" +
                            "병/의원&약국 : " + itemMap["resHospitalName"] + "\n" +
                            "진료형태 : " + itemMap["resTreatType"] + "\n" +
                            "코드명 : " + itemMap["resCodeName"] + "\n" +
                            "1회 투약량 : " + itemMap["resOneDose"] + "\n" +
                            "1회 투여횟수 : " + itemMap["resDailyDosesNumber"] + "\n" +
                            "총 투약일수 : " + itemMap["resTotalDosingdays"] + "\n\n"
                }

//                val resPrescribeDrugList = dataMap["resPrescribeDrugList"] as ArrayList<*>
//                for(item in resPrescribeDrugList) {
//                    val itemMap = item as LinkedHashMap<*, *>
//                    binding.prescribeDrugText += "진료시작일 : " + itemMap["resTreatStartDate"] + "\n" +
//                            "병/의원&약국 : " + itemMap["resHospitalName"] + "\n" +
//                            "진료형태 : " + itemMap["resTreatType"] + "\n" +
//                            "약품명 : " + itemMap["resDrugName"] + "\n" +
//                            "성분명 : " + itemMap["resIngredients"] + "\n" +
//                            "1회 투약량 : " + itemMap["resOneDose"] + "\n" +
//                            "1회 투여횟수 : " + itemMap["resDailyDosesNumber"] + "\n" +
//                            "총 투약일수 : " + itemMap["resTotalDosingdays"] + "\n\n"
//                }

            } else {
                if (resultMap["code"] == "CF-03002") {
                    binding.isDoneClicked = false
                    binding.errorText = "인증이 완료되지 않았습니다."
                } else if (resultMap["code"] == "CF-12102") {
                    binding.basicTreatText = resultMap["message"].toString()
                } else {
                    binding.basicTreatText = "잠시 후 다시 시도해 주세요.\n" +
                            resultMap["code"].toString() + "\n" +
                            resultMap["message"].toString() + "\n" +
                            resultMap["extraMessage"].toString()
                }
            }
        }
    }

    fun onClickNext(view: View) {
        resultText = binding.basicTreatText.toString() + "\n\n\n" +
                binding.detailTreatText.toString() + "\n\n\n" +
                binding.pharmacyDrugText.toString()

        val clipboard: ClipboardManager =
            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", resultText)
        clipboard.setPrimaryClip(clip)




        finish()
    }

    fun onClickCopyClipboard(view: View) {
        val tv = view as TextView

        val clipboard: ClipboardManager =
            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", tv.text.toString())
        clipboard.setPrimaryClip(clip)
    }

////    private fun goToNextActivity() {
////        val mIntent = Intent(this@DiagnosisHistoryActivity, ExamCategoryActivity::class.java)
////        startActivity(mIntent)
////        finish()
////    }
////
////    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
////        val inflater: MenuInflater = menuInflater
////        inflater.inflate(R.menu.identification, menu)
////        return true
////    }
////
////    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        return when (item.itemId) {
////            R.id.skipCertification -> {
////                goToNextActivity()
////                true
////            }
////            else -> super.onOptionsItemSelected(item)
////        }
////    }

    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

//    fun onDateChanged(year: Int, month: Int, day: Int){
//
//    }

}