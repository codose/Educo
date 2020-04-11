package ng.educo.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.pixplicity.easyprefs.library.Prefs
import ng.educo.R
import ng.educo.databinding.ActivitySplashBinding
import ng.educo.utils.App
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
                App.appUser == null -> {
                    startActivity(Intent(applicationContext, RegistrationActivity::class.java))
                    finish()
                }
                App.appUser!!.accountSetup == 1 ->{
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
                else -> { //Should goto MainActivity
                    startActivity(Intent(applicationContext, CategoryActivity::class.java))
                    finish()
                }
            }
        }, 3000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ignoreHandler = true
    }
}
