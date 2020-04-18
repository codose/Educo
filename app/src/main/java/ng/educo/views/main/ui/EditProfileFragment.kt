package ng.educo.views.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import ng.educo.R
import ng.educo.databinding.FragmentEditProfileBinding
import ng.educo.views.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)

        binding.apply {
            backButton.setOnClickListener { activity!!.onBackPressed() }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun getLayoutRes(): Int = R.layout.fragment_edit_profile

}
