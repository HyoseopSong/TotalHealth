package com.medichain.totalhealth.view_adapter

import java.io.Serializable


data class ExamInfo(
    val main_exam : String,
    val sub_exam : ArrayList<String>,
    val is_title : Boolean,
    val is_optional : Boolean,
)

data class HospitalData(
//    val hospitalCd: String,
    val name: String,
    val region: String,
//    val hospitalAddr: String,
//    val latitude: String,
//    val longitude: String,
//    val repPhoneNum: String,
//    val homepage: String,
//    val hospitalScale: String,
//    val bizNo: String,
//    val medicalNo: String,
//    val ctpName: String,
//    val ctpPhoneNo: String,
//    val ctpEmail: String,
//    val memo: String,
//    val visitCnt: Int,

    )

data class Customer(
    val name: String,
    val birth: String,
    val phone: String,
    val ManagerID: String
    ):Serializable

data class Certification(
    val 상호: String,
    val 자격상실일: String,
    val 가입자구분: String,
    val 발급일자: String,
    val 자격취득일: String,
    val 주민번호: String,
    val 발급번호: String,
    ):Serializable
data class ExamSubject(
    val 검진년도: String,
    val 구강검진: String,
    val B형간염검사: String,
    val 일반검진: String,
    val 확진검사: String,
    val 보건소: String,
    val 위암: String,
    val 간암상: String,
    val 간암하: String,
    val 대장암: String,
    val 유방암: String,
    val 자궁경부암: String,
    val 폐암: String,
    ):Serializable

data class CheckUpResult(
    val 검진일자: String,
    val 검진장소: String,
    val 키: String,
    val 몸무게: String,
    val 허리둘레: String,
    val BMI: String,
    val 시력: String,
    val 청력: String,
    val 혈압: String,
    val 요단백: String,
    val 혈색소: String,
    val 식전혈당: String,
    val 총콜레스테롤: String,
    val HDL콜레스테롤: String,
    val 트리글리세라이드: String,
    val LDL콜레스테롤: String,
    val 혈청크레아티닌: String,
    val 신사구체여과율: String,
    val AST: String,
    val ALT: String,
    val GTP: String,
    val 폐결핵: String,
    val 골다공증: String,
    ):Serializable

data class CombinedCustomerData(
    val customer : Customer,
    val certification: Certification?,
    val examSubject : ExamSubject?,
    val checkUpResult: CheckUpResult?
)
data class CreateExamSubjectRequest(
    val customerDTO : Customer,
    val examSubjectDTO : ExamSubject
)

data class CreateCheckUpResultRequest(
    val customerDTO : Customer,
    val checkUpResultDTO : CheckUpResult
)