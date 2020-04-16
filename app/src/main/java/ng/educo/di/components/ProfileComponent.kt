package ng.educo.di.components

import dagger.Subcomponent
import ng.educo.di.scope.ActivityScope
import ng.educo.views.main.MainActivity
import ng.educo.views.main.ui.ProfileFragment
import javax.inject.Singleton


@ActivityScope
@Subcomponent
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }

    fun inject(fragment : ProfileFragment)
    fun inject(activity: MainActivity)
}