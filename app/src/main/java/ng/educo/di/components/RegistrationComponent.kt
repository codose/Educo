package ng.educo.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.di.scope.ActivityScope
import ng.educo.views.registration.RegistrationActivity
import ng.educo.views.registration.ui.LoginFragment
import ng.educo.views.registration.ui.RegisterFragment


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ActivityScope
@Subcomponent
interface RegistrationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RegistrationComponent
    }

    fun inject(activity: RegistrationActivity)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: RegisterFragment)

}