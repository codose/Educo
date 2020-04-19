package ng.educo.views.categories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_location_select.*

import ng.educo.R
import ng.educo.databinding.FragmentLocationSelectBinding
import ng.educo.utils.Constants.states
import ng.educo.utils.Constants.year
import ng.educo.views.base.BaseFragment


class LocationSelectFragment : BaseFragment<FragmentLocationSelectBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        val user = getAppUser()!!
        val countries = listOf("Nigeria")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, countries)
        val mAdapter = ArrayAdapter(requireContext(), R.layout.list_item, states)
        val sAdapter = ArrayAdapter(requireContext(), R.layout.list_item, year)
       (binding.countrySpinner as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.stateSpinner as? AutoCompleteTextView)?.setAdapter(mAdapter)
        (binding.yearSpinner as? AutoCompleteTextView)?.setAdapter(sAdapter)
        binding.fragmentLocationNextButton.setOnClickListener {
            if (validateFields()){
                user.state = binding.stateSpinner.text.toString()
                user.level = binding.yearSpinner.text.toString()
                user.school = binding.schoolTextInputEditText.text.toString()
                user.dept = binding.deptTextInputEditText.text.toString()
                this.findNavController().navigate(R.id.action_locationSelectFragment_to_interestsFragment)
            }

        }
        binding.fragmentLocationNameTextView.text = getAppUser()?.firstName
        return binding.root
    }

    private fun validateFields() : Boolean{
        if(!isValidUserName(binding.deptTextInputEditText.text.toString())){
            binding.deptTextInputLayout.error = "This field is required"
            return false
        }else{
            binding.deptTextInputLayout.error = null
        }
        if(!isValidUserName(binding.schoolTextInputEditText.text.toString())){
            binding.schoolTextInputEditText.error = "This field is required"
            return false
        }else{
            binding.deptTextInputLayout.error = null
        }
        return true
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_location_select
    }
}
