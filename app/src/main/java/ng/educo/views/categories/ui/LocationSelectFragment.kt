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

import ng.educo.R
import ng.educo.databinding.FragmentLocationSelectBinding
import ng.educo.views.base.BaseFragment


class LocationSelectFragment : BaseFragment<FragmentLocationSelectBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        val items = listOf("Nigeria", "Togo", "Benin", "Zimbabwe",  "United Kingdom")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
       (binding.countrySpinner as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.fragmentLocationNextButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_locationSelectFragment_to_interestsFragment)
        }
        return binding.root
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_location_select
    }
}