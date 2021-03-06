package ng.educo.views.main.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

import ng.educo.R
import ng.educo.databinding.FragmentProfileBinding
import ng.educo.models.Educo
import ng.educo.utils.*
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.EducoClickListener
import ng.educo.views.main.adapters.MainAdapter
import ng.educo.views.main.adapters.ProfileAdapter
import ng.educo.views.main.viewmodels.ProfileViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel : ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(activity!!, factory)[ProfileViewModel::class.java]

        val profileAdapter = ProfileAdapter()

        val mainAdapter = MainAdapter(context!!, EducoClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSingleStudyFragment(it.id))
        })

        binding.recentsRecyclerView.adapter = mainAdapter

        viewModel.myPartners.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading ->{

                }
                is Resource.Success ->{
                    mainAdapter.submitList(it.data)
                    val studyGroups : ArrayList<Educo> = ArrayList()
                    val studyPartners : ArrayList<Educo> = ArrayList()
                    for(i in it.data){
                        if(checkUserGroupEduco(i)){
                            studyGroups.add(i)
                        }else{
                            studyPartners.add(i)
                        }
                    }
                    if(it.data.isEmpty()){
                        binding.apply {
                            emptyImage.visibility = VISIBLE
                            emptyText.visibility = VISIBLE
                        }
                    }else{
                        binding.apply {
                            emptyImage.visibility = GONE
                            emptyText.visibility = GONE
                        }
                    }
                    binding.groupCount.text = "${studyGroups.size}"
                    binding.partnerCount.text = "${studyPartners.size}"
                }

                is Resource.Failure ->{

                }
            }
        })

        val gridLayoutManager = StaggeredGridLayoutManager(2, HORIZONTAL)

        binding.profileRv.apply{
            layoutManager = gridLayoutManager
            adapter = profileAdapter
        }

        binding.apply {
            backButton.setOnClickListener { activity!!.onBackPressed() }
            editButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment) }
        }

        setUpBottomNav()

        viewModel.getUserProfile.observe(viewLifecycleOwner, Observer {
            profileAdapter.submitList(it.interest)
            binding.fullNameTxtView.text = "${it.firstName} ${it.lastName}"
            binding.stateTextView.text = "${it.state} state"
            binding.titleTextView.text = "${it.school } / ${it.dept} / ${yearToString(it.level)}"
            binding.dateJoinedTextView.text = formatDateJoined(it.accountCreated!!)
            if(it.imageUrl.isNotBlank()){
                Glide.with(this)
                    .load(getAppUser()?.imageUrl)
                    .placeholder(R.drawable.ic_undraw_profile_pic)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.circleImageView2)
            }
            hideProgress()
        })
    }

    private fun setUpBottomNav() {
        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = GONE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        fab?.visibility = VISIBLE
    }

    private fun showProgress() {
        binding.mainLayout.visibility = INVISIBLE
        binding.shimmerLinearLayout.visibility = VISIBLE
        binding.shimmerLayout.visibility = VISIBLE
        binding.shimmerLayout.startShimmer()
    }

    private fun hideProgress() {
        binding.mainLayout.visibility = VISIBLE
        binding.shimmerLinearLayout.visibility = GONE
        binding.shimmerLayout.visibility = GONE
        binding.shimmerLayout.stopShimmer()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_profile

}
