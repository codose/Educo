package ng.educo.views.main.ui

import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import ng.educo.R
import ng.educo.databinding.FragmentRequestStudyBinding
import ng.educo.models.Educo
import ng.educo.utils.*
import ng.educo.utils.Constants.interests
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.viewmodels.RequestStudyViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RequestStudyFragment : BaseFragment<FragmentRequestStudyBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: RequestStudyViewModel

    private lateinit var title : String
    private lateinit var category : String
    private lateinit var type : String
    private var users : Int = 1
    private lateinit var description : String



    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(),container,false)

        viewModel = ViewModelProvider(this, factory)[RequestStudyViewModel::class.java]
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, convertInterestList(App.appUser!!))
        (binding.categoryEditText as? AutoCompleteTextView)?.setAdapter(adapter)

        val adapter1 = ArrayAdapter(requireContext(), R.layout.list_item, listOf("Study Partner", "Study Group"))
        (binding.typeText as? AutoCompleteTextView)?.setAdapter(adapter1)

        val adapter2 = ArrayAdapter(requireContext(), R.layout.list_item, listOf("2","3","4"))
        (binding.usersText as? AutoCompleteTextView)?.setAdapter(adapter2)

        binding.doneButton.setOnClickListener {
            createNewRequest()
        }

        viewModel.onCreateRequest.observe(viewLifecycleOwner, Observer{
            when(it){
                is Resource.Loading -> showProgress()

                is Resource.Success -> {
                    showToast("Request created successfully")
                    hideProgress()
                    activity?.onBackPressed()
                }

                is Resource.Failure -> {
                    showToast(it.message)
                    hideProgress()
                }
            }
        })

        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = GONE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        fab?.visibility = GONE
        return binding.root
    }

    private fun showProgress() {
        binding.requestBar.visibility = VISIBLE
        binding.doneButton.isEnabled = false

    }

    private fun hideProgress() {
        binding.requestBar.visibility = GONE
        binding.doneButton.isEnabled = true

    }

    private fun createNewRequest() {
        title = binding.titleEditText.text.toString()
        category = binding.categoryEditText.text.toString()
        type = binding.typeText.text.toString()
        users = Integer.parseInt(binding.usersText.text.toString())
        description = binding.descriptionEditText.text.toString()
        val location = App.appUser?.state
        if(validateInputs()){
            val educo = Educo(title, longStringToInt(category), typeStringToInt(type), users, description, location!!, auth.currentUser!!.uid)
            viewModel.createNewRequest(educo)
        }
    }

    private fun validateInputs(): Boolean {
        if (!isValidUserName(title)) {
            binding.titleTextInputLayout.error = "Input a valid title"
            return false
        } else {
            binding.titleTextInputLayout.error = null
        }
        if (!isValidUserName(description)) {
            binding.descriptionTextInputLayout.error = "Description Required"
            return false
        }else {
            binding.descriptionTextInputLayout.error = null
        }
        return true
    }


    override fun getLayoutRes(): Int = R.layout.fragment_request_study

}
