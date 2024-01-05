package com.medichain.totalhealth.network

import com.medichain.totalhealth.view_adapter.CreateCheckUpResultRequest
import com.medichain.totalhealth.view_adapter.CreateExamSubjectRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIInterface {
    @FormUrlEncoded
    @POST("/api/MediChain/LogIn")
    fun LogIn(
        @Field("data") data: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/api/MediChain/CreateCustomer")
    fun CreateCustomer(
        @Field("NAME") NAME: String,
        @Field("BIRTH") BIRTH: String,
        @Field("PHONE") PHONE: String,
        @Field("ManagerID") ManagerID: String,
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/api/MediChain/CreateCertificationData")
    fun CreateCertificationData(
        @Field("NAME") NAME: String,
        @Field("BIRTH") BIRTH: String,
        @Field("PHONE") PHONE: String,
        @Field("ManagerID") ManagerID: String,
        @Field("상호") 상호:String,
        @Field("자격상실일") 자격상실일:String,
        @Field("가입자구분") 가입자구분:String,
        @Field("발급일자") 발급일자:String,
        @Field("자격취득일") 자격취득일:String,
        @Field("주민번호") 주민번호:String,
        @Field("발급번호") 발급번호:String,
    ): Call<ResponseBody>

    @POST("/api/MediChain/CreateExamSubjectData")
    fun CreateExamSubjectData(
        @Body body: CreateExamSubjectRequest
    ): Call<ResponseBody>

    @POST("/api/MediChain/CreateCheckUpResultData")
    fun CreateCheckUpResultData(
        @Body body: CreateCheckUpResultRequest
    ): Call<ResponseBody>

    @GET("/api/MediChain/GetCustomerList")
    fun GetCustomerList(
        @Query("managerID") managerID: String,
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/api/debugging/saveLog")
    fun SaveLog(
        @Field("programName") programName: String,
        @Field("logMsg") logMsg: String,
        @Field("createdBy") createdBy: String,
    ): Call<ResponseBody>

}