package ng.educo.views.registration.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import ng.educo.R
import ng.educo.databinding.FragmentStarterBinding

/**
 * A simple [Fragment] subclass.
 */
class StarterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentStarterBinding>(layoutInflater,R.layout.fragment_starter, container, false)
        // Inflate the layout for this fragment
        binding.getStartedButton.setOnClickListener {
            view?.findNavController()?.navigate(StarterFragmentDirections.actionStarterFragmentToLoginFragment2())
        }
        return binding.root
    }

}
