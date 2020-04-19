package ng.educo.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.github.ybq.android.spinkit.style.ChasingDots
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.style.FadingCircle
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.R
import ng.educo.databinding.ActivitySplashBinding
import ng.educo.utils.App
import ng.educo.utils.Resource
import ng.educo.views.base.BaseActivity
import ng.educo.views.categories.CategoryActivity
import ng.educo.views.main.MainActivity
import ng.educo.views.registration.RegistrationActivity

class SplashActivity : BaseActivity() {

    private val APP_OPEN_FIRST_TIME = "ng.educo.APP_OPEN_FIRST_TIME"
    private var firstTime : Boolean? = null
    private var ignoreHandler : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this,R.layout.activity_splash)

        val heartBeatAnim =
            AnimationUtils.loadAnimation(this, R.anim.bounce)

        val doubleBounce = FadingCircle()
        doubleBounce.color = resources.getColor(R.color.colorPrimary)

        val progressBar = binding.progressBar

        progressBar.indeterminateDrawable = doubleBounce

        binding.logoImage.animation = heartBeatAnim

        firstTime = Prefs.getBoolean(APP_OPEN_FIRST_TIME, true)
        navigateTo()
    }

    private fun navigateTo() {
        Handler().postDelayed({
            if (ignoreHandler) return@postDelayed
            when {
                firstTime!! -> {
                    Prefs.putBoolean(APP_OPEN_FIRST_TIME, false)
                    startActivity(Intent(applicationContext, RegistrationActivity::class.java))
                    finish()
                }
                auth.currentUser == null -> {
                    startActivity(Intent(applicationContext, RegistrationActivity::class.java))
                    finish()
                }
                auth.currentUser != null ->{
                    val userRepo = FirebaseRepository()
                    App.applicationScope.launch {
                        withContext(Dispatchers.IO){
                            val userResource = userRepo.getUser()
                            App.appUser = when(userResource){
                                is Resource.Success -> userResource.data
                                else -> null
                            }
                            if(App.appUser?.accountSetup == 0){
                                startActivity(Intent(applicationContext, CategoryActivity::class.java))
                                finish()
                            }else{
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            }
        }, 1000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ignoreHandler = true
    }
}
