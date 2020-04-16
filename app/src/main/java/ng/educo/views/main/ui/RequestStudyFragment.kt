package ng.educo.views.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import ng.educo.R
import ng.educo.databinding.FragmentRequestStudyBinding
import ng.educo.views.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class RequestStudyFragment : BaseFragment<FragmentRequestStudyBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(),container,false)

        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = GONE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        fab?.visibility = GONE
        return binding.root
    }

    override fun getLayoutRes(): Int = R.layout.fragment_request_study

}
