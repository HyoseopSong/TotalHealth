package com.medichain.totalhealth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.medichain.totalhealth.R
import com.medichain.totalhealth.databinding.FragmentExamTargetBinding
import com.medichain.totalhealth.view_adapter.ExamSubject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExamTargetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExamTargetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding : FragmentExamTargetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exam_target, container, false)
        arguments?.let {
            val customer = it.getSerializable("ExamSubject") as ExamSubject

            binding.data1 = customer.검진년도
            binding.data2 = customer.일반검진
            binding.data3 = customer.구강검진
            binding.data4 = customer.B형간염검사
            binding.data5 = customer.확진검사
            binding.data6 = customer.보건소
            binding.data7 = customer.위암
            binding.data8 = customer.간암상 + " / " + customer.간암하
            binding.data9 = customer.대장암
            binding.data10 = customer.유방암
            binding.data11 = customer.자궁경부암
            binding.data12 = customer.폐암

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
         * @return A new instance of fragment ExamTargetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExamTargetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}