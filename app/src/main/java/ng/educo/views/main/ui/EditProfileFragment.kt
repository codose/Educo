package ng.educo.views.main.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.android.material.floatingactionbutton.FloatingActionButton

import ng.educo.R
import ng.educo.databinding.FragmentEditProfileBinding
import ng.educo.models.User
import ng.educo.utils.Constants
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.viewmodels.ProfileViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    @Inject
    lateinit var factory : ViewModelProvider.Factory

    lateinit var viewModel : ProfileViewModel



    lateinit var user : User

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        fab?.visibility = GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doubleBounce = FadingCircle()
        doubleBounce.color = resources.getColor(R.color.colorPrimary)
        val progressBar = binding.progressBar2
        progressBar.indeterminateDrawable = doubleBounce


        user = getAppUser()!!

        val oldUser = user

        viewModel = ViewModelProvider(this,factory)[ProfileViewModel::class.java]

        setTexts()

        binding.apply {
            backButton.setOnClickListener { activity!!.onBackPressed() }
            doneButton.setOnClickListener {
                updateUserData()
            }
        }

        viewModel.userDataUpdated.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> disableButton()

                is Resource.Success -> {
                    setAppUser(user)
                    showToast(it.data)
                    activity!!.onBackPressed()
                }

                is Resource.Failure -> {
                    enableButton()
                    setAppUser(oldUser)
                    showToast(it.message)
                }
            }
        })
    }

    private fun disableButton() {
        binding.doneButton.isEnabled = false
        binding.doneButton.visibility = INVISIBLE
        binding.progressBar2.visibility = VISIBLE
    }

    private fun enableButton(){
        binding.doneButton.isEnabled = true
        binding.doneButton.visibility = VISIBLE
        binding.progressBar2.visibility = GONE
    }

    private fun updateUserData() {
        if(isValidated()){
            binding.apply {
                user.firstName = firstNameTextInputEditText.text.toString()
                user.lastName = lastNameTextInputEditText.text.toString()
                user.phone = phoneTextInputEditText.text.toString()
                user.dept = deptTextInputEditText.text.toString()
                user.school = schoolTextInputEditText.text.toString()
                user.state = stateSpinner.text.toString()
                user.level = levelSpinner.text.toString()
            }
            viewModel.updateUser(user)
        }
    }

    private fun isValidated(): Boolean {
        if(!isValidUserName(binding.deptTextInputEditText.text.toString().trim())){
            binding.deptTextInputLayout.error = "This field is required"
            return false
        }else{
            binding.deptTextInputLayout.error = null
        }
        if(!isValidUserName(binding.schoolTextInputEditText.text.toString().trim())){
            binding.schoolTextInputEditText.error = "This field is required"
            return false
        }else{
            binding.deptTextInputLayout.error = null
        }
        if (!isValidUserName(binding.firstNameTextInputEditText.text.toString().trim())) {
            binding.firstNameTextInputLayout.error = "First name required"
            return false
        } else {
            binding.firstNameTextInputLayout.error = null
        }
        if (!isValidUserName(binding.lastNameTextInputEditText.text.toString().trim())) {
            binding.lastNameTextInputLayout.error = "Last name required"
            return false
        }else {
            binding.lastNameTextInputLayout.error = null
        }
        if (!isValidPhoneNo(binding.phoneTextInputEditText.text.toString().trim())) {
            binding.phoneTextInputLayout.error = "Valid phone number Required"
            return false
        }else {
            binding.phoneTextInputLayout.error = null
        }
        return true
    }

    private fun setTexts() {
        binding.apply {
            firstNameTextInputEditText.setText(user.firstName)
            lastNameTextInputEditText.setText(user.lastName)
            emailTextInputEditText.setText(user.email)
            phoneTextInputEditText.setText(user.phone)
            deptTextInputEditText.setText(user.dept)
            schoolTextInputEditText.setText(user.school)
            stateSpinner.setText(user.state)
            levelSpinner.setText("${user.level}")
        }
        val mAdapter = ArrayAdapter(requireContext(), R.layout.list_item, Constants.states)
        val sAdapter = ArrayAdapter(requireContext(), R.layout.list_item, Constants.year)
        (binding.stateSpinner as? AutoCompleteTextView)?.setAdapter(mAdapter)
        (binding.levelSpinner as? AutoCompleteTextView)?.setAdapter(sAdapter)
    }

    override fun getLayoutRes(): Int = R.layout.fragment_edit_profile

}
