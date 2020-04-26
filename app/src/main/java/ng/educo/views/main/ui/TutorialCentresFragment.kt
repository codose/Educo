package ng.educo.views.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

import ng.educo.R
import ng.educo.databinding.FragmentTutorialCentresBinding
import ng.educo.views.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TutorialCentresFragment : BaseFragment<FragmentTutorialCentresBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)

        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        fab?.visibility = View.VISIBLE

        binding.apply {
            nothingImage.visibility = View.VISIBLE
            nothingText.visibility = View.VISIBLE
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun getLayoutRes(): Int = R.layout.fragment_tutorial_centres

}
