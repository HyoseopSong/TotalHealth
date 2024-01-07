package com.medichain.totalhealth

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.medichain.totalhealth.databinding.ActivityIdentityCertificationBinding
import com.medichain.totalhealth.network.APIInterface
import com.medichain.totalhealth.network.ServerAPI
import com.medichain.totalhealth.view_adapter.CheckUpResult
import com.medichain.totalhealth.view_adapter.CreateCheckUpResultRequest
import com.medichain.totalhealth.view_adapter.CreateExamSubjectRequest
import com.medichain.totalhealth.view_adapter.Customer
import com.medichain.totalhealth.view_adapter.ExamSubject
import io.codef.api.EasyCodef
import io.codef.api.EasyCodefServiceType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class IdentityCertificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentityCertificationBinding
    private val calendar: Calendar = Calendar.getInstance()

    private var resultText = ""
    private var globalCodeF : EasyCodef? = null
//    private var productUrlCert = "/v1/kr/public/pp/nhis-member/identify-confirmation"
    private var productUrlCert = "/v1/kr/public/pp/nhis-join/identify-confirmation"
    private var productUrlExam = "/v1/kr/public/pp/nhis-list/examination"
    private var productUrlCheckUpResult = "/v1/kr/public/pp/nhis-health-checkup/result"
    private val simpleAuth = HashMap<String, Any>()
    private var customer = Customer("", "", "", "")
    private lateinit var loginID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_identity_certification)
        binding.activity = this@IdentityCertificationActivity

        loginID = getSharedPreferences(packageName, MODE_PRIVATE)
            .getString("LoginID", "")!!
        title = "간편인증"
        binding.allDoneButtonText = "확인"

        binding.certificationAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayOf("카카오톡","페이코","삼성패스","KB모바일","통신사","네이버","신한인증서","Toss"))
        binding.carrierAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayOf("SKT(SKT알뜰폰)", "KT(KT알뜰폰)", "LG U+(LG U+알뜰폰)"))

        binding.isInputEnabled = true
        binding.isRequested = false
        binding.isDoneClicked = false
        binding.isLastAnswerReceived = false
        binding.isLoadingLayoutVisible = false

        binding.userName = intent.getStringExtra("name")

        binding.birthYear = intent.getStringExtra("birthYear")
        binding.birthMonth = intent.getStringExtra("birthMonth")
        binding.birthDay = intent.getStringExtra("birthDay")

        binding.phoneNumber = intent.getStringExtra("phone")

        binding.queryExamSubject = true
        binding.queryCheckUpResult = true
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
        binding.checkupResultInfoText = ""

        CoroutineScope(Dispatchers.Default).launch {
            globalCodeF = EasyCodef()
            val codeF = globalCodeF!!
//          codeF.setClientInfo(EasyCodefClientInfo.CLIENT_ID, EasyCodefClientInfo.CLIENT_SECRET);
//            codeF.setClientInfoForDemo("8086901b-bc16-485e-b5f5-4dd2c88dc400", "0c0d700d-4d0a-4446-a027-99967b04a34a");
//            codeF.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA34tQqkNG9gD60SmFuLuHAL9biFno82DYkWY9+H7weE1RNCFeJ1AydVqGDyxm2DOWH+90gwLyUbjMKx6LzZJgpqVgzTR5zd75F+cYhLw4lMavVKenZY1hpEGgfPSVH+Yu0BpJDDw8WCCpED+BbpVSO39D1HKc/dl8TMFQ1q8WQGJscs5S7NU7qSFVjoBOQlM6tAh0YgkI13RKomGmXtUbpbNk1unpyBJv4h4Fqm8BtBUD4bxpgv7w4SCmuG7sr4Arlm/fhL0iqnjGgMYRiDV1LOWCzMz2LPb5kCo9OMHA345AQYCyPtUbFr8SBxwHNug24Uwgk7jALvpBOLEdaE1xeQIDAQAB")
            codeF.setClientInfoForDemo("a66f4375-2a8d-4f23-b1c3-be92f1f90210", "7324a459-ba53-4028-95de-1107a3a359ab")
            codeF.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwFJbR0QTOYHCdbHlphrciHgePaut+lObCEwFdLJIeVJgGmrREl4bhP9YVF9qvN/dRW5MRYCeJC0qEKNRn/5oIWfl7NGNoXXdDAXeGuOJUmwktvgykWfkJvEoMjzzN8BOXzxc+/RRkKOYYMY25mbIK6jB7/GGAXDI0jh9/nK53ndkXeaxAp6phGu2wQh3ZM+uMyUST6qEc4/z+iQrx50hAUDij//w0oczeImmDzo1/B33FQq/zESokSFjIhzubnu2s2tmWGClEdTUXuloRSfHTsxi9+Kb1YGOm7LHHERiuwqTi5KlM37ca0DY1OieborigxPfqwZxZ+VufAmhkV17SQIDAQAB")

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

        setCustomer()

        binding.queryCompanyStatus = ""
        binding.queryExamSubjectStatus = ""
        binding.queryCheckUpResultStatus = ""

        binding.isInputEnabled  = false
        simpleAuth["organization"] = "0002"
        simpleAuth["loginType"] = "5"
        simpleAuth["identity"] = customer.birth
        simpleAuth["loginTypeLevel"] = "1"
        simpleAuth["userName"] = customer.name
        simpleAuth["phoneNo"] = customer.phone
        simpleAuth["useType"] = "0"
        simpleAuth["isIdentityViewYN"] = "0"
        simpleAuth["id"] = getRandomString(10)
        simpleAuth["originDataYN"] = "0"
        simpleAuth["telecom"] = binding.carrierSpinner.selectedItemPosition.toString()

        val codeF = globalCodeF!!
        CoroutineScope(Dispatchers.Default).launch {
            binding.queryCompanyStatus = "..."
            val resultCert = codeF.requestProduct(productUrlCert, EasyCodefServiceType.DEMO, simpleAuth)
            Log.d("MyTag resultCert", resultCert)

            val responseMap = ObjectMapper().readValue(
                resultCert,
                HashMap::class.java
            )
            val resultMap = responseMap?.get("result") as HashMap<*, *>
            if(resultMap["code"].toString() == "CF-03002") {
                binding.queryCompanyStatus = "O"
                ServerAPI().getAPI(this@IdentityCertificationActivity).create(APIInterface::class.java)
                    .CreateCustomer(customer.name, customer.birth, customer.phone, loginID)
                    .enqueue(object :
                        Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if(response.code() == 200) {
                                val responseBody = response.body()!!.string()
                                if(responseBody != "T") {
                                    Toast.makeText(this@IdentityCertificationActivity, "고객 등록을 못 했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            } else {

                                Toast.makeText(this@IdentityCertificationActivity, response.message(), Toast.LENGTH_SHORT).show()

                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            binding.errorText = call.toString()
//                    Toast.makeText(this@IdentityCertificationActivity, "서버에 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
//                    binding.loadingLayout.root.visibility = View.GONE
                        }
                    })
                prepareCertification(responseMap["data"] as HashMap<*, *>)
            } else {
                binding.queryCompanyStatus = "X"
                binding.isInputEnabled  = true
            }
        }
        if(binding.queryExamSubject == true) {
            requestExamList()
        }
        if(binding.queryCheckUpResult == true) {
            requestHealthCheckUpResult()
        }
    }

    fun setCustomer() {
        calendar.set(binding.birthYear!!.toInt(), binding.birthMonth!!.toInt(), binding.birthDay!!.toInt())
        customer = Customer(binding.userName.toString(),
            calendar.get(Calendar.YEAR).toString() + (calendar.get(Calendar.MONTH).toString().padStart(2, '0')) + calendar.get(Calendar.DATE).toString().padStart(2, '0'),
            binding.phoneNumber.toString(), loginID)
    }

    fun requestExamList() {
        val codeF = globalCodeF!!
        CoroutineScope(Dispatchers.Default).launch {
            delay(750)
            binding.queryExamSubjectStatus = "O"
            binding.healthExamInfoText = "검진대상 응답 대기 중"
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
                        binding.healthExamInfoText = "이번 년도 검진 대상이 아닙니다.\n"
                    } else {
                        binding.healthExamInfoText = "관리자에게 문의해 주세요.\n"
                    }
                } else {
                    val dataMap = responseMap["data"] as LinkedHashMap<*,*>
                    binding.healthExamInfoText = "검진대상 완료"
//                    binding.healthExamInfoText = "성명 : " + dataMap["resUserNm"] + "\n" +
//                            "생년월일 : " + dataMap["commBirthDate"] + "\n" +
//                            "검진년도 : " + dataMap["resCheckupYear"] + "\n" +
//                            "구강검진 : " + dataMap["resDentalExam"] + "\n" +
//                            "B형 간염검사 : " + dataMap["reshepatitisBTest"] + "\n" +
//                            "일반검진 : " + dataMap["resGeneralExam"] + "\n" +
//                            "확진검사 : " + dataMap["resConfirmTest"] + "\n" +
//                            "보건소 : " + dataMap["resPublicHealth"] + "\n"
                    val resCancerScreeningList = dataMap["resCancerScreeningList"] as ArrayList<*>
                    val hashMap = java.util.HashMap<String, String>()
                    for(item in resCancerScreeningList) {
                        val itemMap = item as LinkedHashMap<*, *>
                        hashMap[itemMap["resType"].toString()] = itemMap["resCancerScreeningList"].toString() + "," + itemMap["resMedicalExpenses"]
//                        binding.healthExamInfoText += "\n구분 : " + itemMap["resType"] + "\n" +
//                                "암검진 : " + itemMap["resCancerScreeningList"] + "\n" +
//                                "의료비 지원 대상 : " + itemMap["resMedicalExpenses"] + "\n"

                    }
                    val examSubject = ExamSubject(
                        dataMap["resCheckupYear"].toString(),    //검진년도
                        dataMap["resDentalExam"].toString(),    //구강검진
                        dataMap["reshepatitisBTest"].toString(),    //B형간염검사
                        dataMap["resGeneralExam"].toString(),    //일반검진
                        dataMap["resConfirmTest"].toString(),    //확진검사
                        dataMap["resPublicHealth"].toString(),    //보건소
                        hashMap["위암"]!!,    //위암
                        hashMap["간암(상반기)"]!!,    //간암상
                        hashMap["간암(하반기)"]!!,    //간암하
                        hashMap["대장암"]!!,    //대장암
                        hashMap["유방암"]!!,    //유방암
                        hashMap["자궁경부암"]!!,    //자궁경부암
                        hashMap["폐암"]!!    //폐암
                    )
                    val requestParam = CreateExamSubjectRequest(customer, examSubject)

                    ServerAPI().getAPI(this@IdentityCertificationActivity).create(APIInterface::class.java)
                        .CreateExamSubjectData(requestParam)
                        .enqueue(object :
                            Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                val responseBody = response.body()!!.string()
                                if(responseBody != "T") {
                                    Toast.makeText(this@IdentityCertificationActivity, "검사 대상 정보 저장을 못 했습니다.", Toast.LENGTH_SHORT).show()

                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                binding.errorText = call.toString()

//                    Toast.makeText(this@IdentityCertificationActivity, "서버에 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
//                    binding.loadingLayout.root.visibility = View.GONE
                            }
                        })
                }
            } else {
                binding.queryExamSubjectStatus = "X"
                binding.isInputEnabled  = true
            }
        }
    }

    fun requestHealthCheckUpResult() {
        val codeF = globalCodeF!!
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            binding.queryCheckUpResultStatus = "O"
            binding.checkupResultInfoText = "검진결과 응답 대기 중"
            val checkUpResultResponse = codeF.requestProduct(productUrlCheckUpResult, EasyCodefServiceType.DEMO, simpleAuth)

//            val clipboard: ClipboardManager =
//                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("label", checkUpResultResponse)
//            clipboard.setPrimaryClip(clip)

            Log.d("MyTag checkUpResultResponse", checkUpResultResponse)
            val responseMap: java.util.HashMap<*, *>? = ObjectMapper().readValue(
                checkUpResultResponse,
                HashMap::class.java
            )
            val resultMap = responseMap?.get("result") as HashMap<*, *>

            if (resultMap["code"] == "CF-00000") {
                if(responseMap["data"] is ArrayList<*>) {
                    val responseMapArrayList = responseMap["data"] as ArrayList<*>
//                    val userName = getSharedPreferences(packageName, MODE_PRIVATE)
//                            .getString("이름", "")
                    for(dataResponseMap in responseMapArrayList) {
                        val dataMap = dataResponseMap as LinkedHashMap<*,*>
                        if(dataMap["resCheckupTarget"] == binding.userName.toString()) {
                            val resPreviewList = dataMap["resPreviewList"] as ArrayList<*>
                            processPreviewList(resPreviewList)
                            break
                        }
                    }
                } else {
                    val dataMap = responseMap["data"] as LinkedHashMap<*,*>
                    val resPreviewList = dataMap["resPreviewList"] as ArrayList<*>
                    processPreviewList(resPreviewList)
                }
            } else {
                binding.queryCheckUpResultStatus = "X"
                binding.isInputEnabled  = true
            }
        }
    }

    fun processPreviewList(arrayList : ArrayList<*>) {
        var recentPreviewResult = arrayList[0] as HashMap<*, *>
        for(resPrev in arrayList) {
            val resPreview = resPrev as HashMap<*, *>
            if((resPreview["resCheckupYear"].toString()) > recentPreviewResult["resCheckupYear"].toString()) {
                recentPreviewResult = resPreview
            }
        }

        binding.checkupResultInfoText = "검진결과 완료"
        getSharedPreferences(packageName, MODE_PRIVATE).edit()
            .putString("BMI", recentPreviewResult["resBMI"].toString())
            .putString("몸무게", recentPreviewResult["resWeight"].toString())
            .putString("키", recentPreviewResult["resHeight"].toString())
            .apply()

        val checkUpResult = CheckUpResult(
            recentPreviewResult["resCheckupYear"].toString() + recentPreviewResult["resCheckupDate"],//검진일자
            recentPreviewResult["resCheckupPlace"].toString(),//검진장소
            recentPreviewResult["resHeight"].toString(),//키
            recentPreviewResult["resWeight"].toString(),//몸무게
            recentPreviewResult["resWaist"].toString(),//허리둘레
            recentPreviewResult["resBMI"].toString(),//BMI
            recentPreviewResult["resSight"].toString(),//시력
            recentPreviewResult["resHearing"].toString(),//청력
            recentPreviewResult["resBloodPressure"].toString(),//혈압
            recentPreviewResult["resUrinaryProtein"].toString(),//요단백
            recentPreviewResult["resHemoglobin"].toString(),//혈색소
            recentPreviewResult["resFastingBloodSuger"].toString(),//식전혈당
            recentPreviewResult["resTotalCholesterol"].toString(),//총콜레스테롤
            recentPreviewResult["resHDLCholesterol"].toString(),//HDL콜레스테롤
            recentPreviewResult["resTriglyceride"].toString(),//트리글리세라이드
            recentPreviewResult["resLDLCholesterol"].toString(),//LDL콜레스테롤
            recentPreviewResult["resSerumCreatinine"].toString(),//혈청크레아티닌
            recentPreviewResult["resGFR"].toString(),//신사구체여과율
            recentPreviewResult["resAST"].toString(),//AST
            recentPreviewResult["resALT"].toString(),//ALT
            recentPreviewResult["resyGPT"].toString(),//GTP
            recentPreviewResult["resTBChestDisease"].toString(),//폐결핵
            recentPreviewResult["resOsteoporosis"].toString(),//골다공증
        )
        val requestParam = CreateCheckUpResultRequest(customer, checkUpResult)

        ServerAPI().getAPI(this@IdentityCertificationActivity).create(APIInterface::class.java)
            .CreateCheckUpResultData(requestParam)
            .enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val responseBody = response.body()!!.string()
                    if(responseBody != "T") {
                        Toast.makeText(this@IdentityCertificationActivity, "검사 대상 정보 저장을 못 했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    binding.errorText = call.toString()

//                    Toast.makeText(this@IdentityCertificationActivity, "서버에 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
//                    binding.loadingLayout.root.visibility = View.GONE
                }
            })
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
        certificationDone()
    }

    fun onClickCancel(view: View) {
        simpleAuth["simpleAuth"] = "0"
        // {"result":{"code":"CF-12102","extraMessage":"","message":"추가 인증 진행이 취소되었습니다.","transactionId":"6566790428e6482d2c4d7f61"},"data":{}}
        binding.isLoadingLayoutVisible = true
        CoroutineScope(Dispatchers.Default).launch {
            binding.errorText = "간편인증 요청 취소 중"
            val codeF = globalCodeF!!
            val resultCertificationCancel = codeF.requestCertification(productUrlCert, EasyCodefServiceType.DEMO, simpleAuth)
            Log.d("MyTag resultCertificationCancel", resultCertificationCancel)
            binding.isLoadingLayoutVisible = false
            val responseMap: java.util.HashMap<*, *> = ObjectMapper().readValue(
                resultCertificationCancel,
                HashMap::class.java
            )
            val resultMap = responseMap["result"] as LinkedHashMap<*,*>

            if (resultMap["code"] == "CF-12102") {
                binding.isRequested = false
                binding.isDoneClicked = false
                binding.isInputEnabled = true
                binding.errorText = "간편인증 요청 취소 완료"

            } else {
                binding.errorText = "잠시 후 다시 시도해 주세요.\n" +
                        resultMap["code"].toString() + "\n" +
                        resultMap["message"].toString() + "\n" +
                        resultMap["extraMessage"].toString()
            }
        }
    }

    private fun certificationDone() {
        simpleAuth["simpleAuth"] = "1"

        binding.isLoadingLayoutVisible = true
        CoroutineScope(Dispatchers.Default).launch {
            binding.isDoneClicked = true
            binding.companyInfoText1 = "직장정보 응답 대기 중"
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
                binding.companyInfoText1 = "직장정보 완료"
                binding.isLastAnswerReceived = true
                val dataArrayList = responseMap["data"] as ArrayList<*>
                if (dataArrayList.size > 0) {
                    val dataMap0 = dataArrayList[0] as LinkedHashMap<*, *>

                    getSharedPreferences(packageName, MODE_PRIVATE).edit()
                        .putString("회사명", dataMap0["resCompanyNm"].toString())
//                        .putString("이름", dataMap0["resUserNm"].toString())
                        .apply()
//                    binding.companyInfoText1 = "상호 : " + dataMap0["resCompanyNm"]
//                    binding.companyInfoText2 = "\n자격상실일 : " + dataMap0["commEndDate"] + "\n" +
//                            "가입자 구분 : " + dataMap0["resJoinUserType"] + "\n" +
//                            "발급일자 : " + dataMap0["resIssueDate"] + "\n" +
//                            "자격취득일 : " + dataMap0["commStartDate"] + "\n" +
//                            "주민번호 : " + dataMap0["resUserIdentiyNo"] + "\n" +
//                            "성명 : " + dataMap0["resUserNm"] + "\n" +
//                            "발급번호 : " + dataMap0["resIssueNo"] + "\n"


                    ServerAPI().getAPI(this@IdentityCertificationActivity).create(APIInterface::class.java)
                        .CreateCertificationData(
                            customer.name,
                            customer.birth,
                            customer.phone,
                            loginID,
                            dataMap0["resCompanyNm"].toString(),
                            dataMap0["commEndDate"].toString(),
                            dataMap0["resJoinUserType"].toString(),
                            dataMap0["resIssueDate"].toString(),
                            dataMap0["commStartDate"].toString(),
                            dataMap0["resUserIdentiyNo"].toString(),
                            dataMap0["resIssueNo"].toString(),
                        )
                        .enqueue(object :
                            Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                val responseBody = response.body()!!.string()
                                if(responseBody != "T") {
                                    Toast.makeText(this@IdentityCertificationActivity, "검사 대상 정보 저장을 못 했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Toast.makeText(this@IdentityCertificationActivity, "서버에 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
//                    binding.loadingLayout.root.visibility = View.GONE
                                binding.errorText = call.toString()

                            }
                        })
                } else {
                    binding.companyInfoText1 = "자격 득실 내역이 없습니다.\n"
                }
            } else {
                binding.isDoneClicked = false
                if (resultMap["code"] == "CF-03002") {
                    binding.errorText = "인증이 완료되지 않았습니다."
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
        finishWithResultCode()
    }


    private fun finishWithResultCode() {

        val mIntent = Intent(this@IdentityCertificationActivity, CustomerListActivity::class.java)
        setResult(1001, mIntent)

        finish()
    }

    private fun goToNextActivity() {
        resultText = binding.companyInfoText1.toString() + "\n" +
                binding.companyInfoText2.toString() + "\n\n\n" +
                binding.healthExamInfoText.toString() + "\n\n\n" +
                binding.checkupResultInfoText.toString()
        val clipboard: ClipboardManager =
            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", resultText)
        clipboard.setPrimaryClip(clip)

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
            R.id.reCertification -> {
                val mIntent = Intent(this@IdentityCertificationActivity, IdentityCertificationActivity::class.java)

                binding.userName = intent.getStringExtra("name")

                binding.birthYear = intent.getStringExtra("birthYear")
                binding.birthMonth = intent.getStringExtra("birthMonth")
                binding.birthDay = intent.getStringExtra("birthDay")

                binding.phoneNumber = intent.getStringExtra("phone")

                mIntent.putExtra("name", customer.name)
                mIntent.putExtra("phone", customer.phone)
                mIntent.putExtra("birthYear", customer.birth.substring(0,4))
                mIntent.putExtra("birthMonth", customer.birth.substring(4,6))
                mIntent.putExtra("birthDay", customer.birth.substring(6))
                startActivity(mIntent)
                finish()

                true
            }
            R.id.cancelCertification -> {
                finish()
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