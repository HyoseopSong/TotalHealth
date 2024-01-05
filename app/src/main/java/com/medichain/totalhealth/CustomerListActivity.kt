package com.medichain.totalhealth

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityCustomerListBinding
import com.medichain.totalhealth.network.APIInterface
import com.medichain.totalhealth.network.ServerAPI
import com.medichain.totalhealth.view_adapter.Certification
import com.medichain.totalhealth.view_adapter.CheckUpResult
import com.medichain.totalhealth.view_adapter.CombinedCustomerData
import com.medichain.totalhealth.view_adapter.Customer
import com.medichain.totalhealth.view_adapter.CustomerViewAdapter
import com.medichain.totalhealth.view_adapter.ExamSubject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerListBinding
    lateinit var CustomerListAdapter : CustomerViewAdapter
    val CombinedCustomerDataList = ArrayList<CombinedCustomerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_list)

        CustomerListAdapter = CustomerViewAdapter(this@CustomerListActivity, CombinedCustomerDataList)
        binding.customerListRecyclerView.adapter = CustomerListAdapter

        refreshCustomerList()
        title = "고객 목록"

    }

    private fun refreshCustomerList() {
        CombinedCustomerDataList.clear()
        ServerAPI().getAPI(this@CustomerListActivity).create(APIInterface::class.java)
            .GetCustomerList(getSharedPreferences(packageName, MODE_PRIVATE).getString("LoginID", "")!!)
            .enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val resBodyString = response.body()!!.string()
                    if(resBodyString.isEmpty()) {
                        CustomerListAdapter.notifyDataSetChanged()
                        return
                    }
                    val responseBodyArr = resBodyString.split("@")
                    for(cRawData in responseBodyArr) {
                        // 송효섭^19850228^01098224728^CD0002^(주)대일전산^^직장가입자^20240105^20231106^8502281******^G20240105022512|2024^대상(본인부담 없음)^^대상(본인부담 없음)^^^대상아님,^대상아님,^대상아님,^대상아님,^대상아님,^대상아님,^대상아님,|20230321^^178.5^88.6^98.0^27.8^0.7/1.0^정상/정상^116/63^음성^14.3^98^^^^^1.0^89^18^14^9^정상^

                        val cRawDataArr = cRawData.split("|")
                        val customerArr = cRawDataArr[0].split("^")
                        val customer = Customer(
                            customerArr[0],
                            customerArr[1],
                            customerArr[2],
                            customerArr[3]
                        )
                        val certificationArr = cRawDataArr[1].split("^")
                        val certification = Certification(
                            certificationArr[0],
                            certificationArr[1],
                            certificationArr[2],
                            certificationArr[3],
                            certificationArr[4],
                            certificationArr[5],
                            certificationArr[6]
                        )
                        val examSubjectArr = cRawDataArr[2].split("^")
                        val examSubject = ExamSubject(
                            examSubjectArr[0],
                            examSubjectArr[1].split(",")[0],
                            examSubjectArr[2].split(",")[0],
                            examSubjectArr[3].split(",")[0],
                            examSubjectArr[4].split(",")[0],
                            examSubjectArr[5].split(",")[0],
                            examSubjectArr[6].split(",")[0],
                            examSubjectArr[7].split(",")[0],
                            examSubjectArr[8].split(",")[0],
                            examSubjectArr[9].split(",")[0],
                            examSubjectArr[10].split(",")[0],
                            examSubjectArr[11].split(",")[0],
                            examSubjectArr[12].split(",")[0],
                        )
                        val checkUpArr = cRawDataArr[3].split("^")
                        val checkUp = if(checkUpArr.size == 1) null else CheckUpResult(
                            checkUpArr[0],
                            checkUpArr[1],
                            checkUpArr[2],
                            checkUpArr[3],
                            checkUpArr[4],
                            checkUpArr[5],
                            checkUpArr[6],
                            checkUpArr[7],
                            checkUpArr[8],
                            checkUpArr[9],
                            checkUpArr[10],
                            checkUpArr[11],
                            checkUpArr[12],
                            checkUpArr[13],
                            checkUpArr[14],
                            checkUpArr[15],
                            checkUpArr[16],
                            checkUpArr[17],
                            checkUpArr[18],
                            checkUpArr[19],
                            checkUpArr[20],
                            checkUpArr[21],
                            checkUpArr[22]
                        )

                        CombinedCustomerDataList.add(CombinedCustomerData(customer, certification, examSubject, checkUp))
                    }

                    CustomerListAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }
            })
    }

    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        if(it.resultCode == 1001) {
//            val resultStrArr = it.data!!.getStringArrayListExtra("resultStrArr")
//            Log.d("MyTag", resultStrArr.toString())
            refreshCustomerList()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.customer_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addNewCustomer -> {
                val mIntent = Intent(this@CustomerListActivity, IdentityCertificationActivity::class.java)
                mIntent.putExtra("selected", title)
                activityResultLauncher.launch(mIntent)
                true
            }
            R.id.refreshCustomerList -> {
                refreshCustomerList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}