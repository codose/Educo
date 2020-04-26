package ng.educo.views.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.R
import ng.educo.databinding.ActivityMainBinding
import ng.educo.di.components.MainComponent
import ng.educo.utils.App
import ng.educo.views.base.BaseActivity
import ng.educo.views.main.viewmodels.MainViewModel
import ng.educo.views.main.viewmodels.ProfileViewModel
import javax.inject.Inject
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel : MainViewModel

    lateinit var profileViewModel : ProfileViewModel


    lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (application as App).appComponent.mainComponent().create()
        mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        val navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        binding.searchNewBtn.setOnClickListener{
            navController.navigate(R.id.requestStudyFragment)
        }
    }
}
