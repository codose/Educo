package ng.educo.views.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import ng.educo.R
import ng.educo.databinding.ActivityMainBinding
import ng.educo.di.components.MainComponent
import ng.educo.utils.App
import ng.educo.views.base.BaseActivity

class MainActivity : BaseActivity() {

    lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (application as App).appComponent.profileComponent().create()
        mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        binding.searchNewBtn.setOnClickListener{
            navController.navigate(R.id.requestStudyFragment)
        }

    }
}
