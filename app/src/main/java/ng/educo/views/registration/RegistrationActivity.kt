package ng.educo.views.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.R
import ng.educo.di.components.RegistrationComponent
import ng.educo.utils.App
import ng.educo.views.registration.viewModels.RegistrationViewModel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class RegistrationActivity : AppCompatActivity() {

    lateinit var registrationComponent: RegistrationComponent

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Registration component by grabbing the factory from the app graph
        registrationComponent = (application as App).appComponent.registrationComponent().create()
        // Injects this activity to the just created registration component
        registrationComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }
}
