package com.medichain.totalhealth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.medichain.totalhealth.R
import com.medichain.totalhealth.databinding.FragmentInsuranceSpecificationBinding
import com.medichain.totalhealth.view_adapter.Certification
import com.medichain.totalhealth.view_adapter.Customer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InsuranceSpecificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InsuranceSpecificationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding : FragmentInsuranceSpecificationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_insurance_specification, container, false)
        arguments?.let {
            val customer = it.getSerializable("Customer") as Customer
            val cert = it.getSerializable("Certification") as Certification

            binding.data1 = customer.name
            binding.data2 = customer.birth
            binding.data3 = customer.phone
            binding.data4 = cert.상호
            binding.data5 = cert.자격상실일
            binding.data6 = cert.가입자구분
            binding.data7 = cert.발급일자
            binding.data8 = cert.자격취득일
            binding.data9 = cert.주민번호
            binding.data10 = cert.발급번호
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InsuranceSpecificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InsuranceSpecificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}