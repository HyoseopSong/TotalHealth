package com.medichain.totalhealth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.medichain.totalhealth.R
import com.medichain.totalhealth.databinding.FragmentCheckUpResultBinding
import com.medichain.totalhealth.view_adapter.CheckUpResult

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckUpResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckUpResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//
        val binding : FragmentCheckUpResultBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_check_up_result, container, false)
        arguments?.let {
            val customer = it.getSerializable("CheckUpResult") as CheckUpResult?

            binding.data1 = customer?.검진일자
            binding.data2 = customer?.검진장소
            binding.data3 = customer?.키
            binding.data4 = customer?.몸무게
            binding.data5 = customer?.허리둘레
            binding.data6 = customer?.BMI
            binding.data7 = customer?.시력
            binding.data8 = customer?.청력
            binding.data9 = customer?.혈압
            binding.data10 = customer?.요단백
            binding.data11 = customer?.혈색소
            binding.data12 = customer?.식전혈당
            binding.data13 = customer?.총콜레스테롤
            binding.data14 = customer?.HDL콜레스테롤
            binding.data15 = customer?.트리글리세라이드
            binding.data16 = customer?.LDL콜레스테롤
            binding.data17 = customer?.혈청크레아티닌
            binding.data18 = customer?.신사구체여과율
            binding.data19 = customer?.AST
            binding.data20 = customer?.ALT
            binding.data21 = customer?.GTP
            binding.data22 = customer?.폐결핵
            binding.data23 = customer?.골다공증

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
         * @return A new instance of fragment CheckUpResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckUpResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}