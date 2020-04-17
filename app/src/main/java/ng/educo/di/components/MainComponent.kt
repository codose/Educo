package ng.educo.di.components

import dagger.Subcomponent
import ng.educo.di.scope.ActivityScope
import ng.educo.views.main.MainActivity
import ng.educo.views.main.ui.ProfileFragment
import ng.educo.views.main.ui.RequestStudyFragment
import ng.educo.views.main.ui.SettingsFragment
import ng.educo.views.main.ui.StudyPartnerFragment


@ActivityScope
@Subcomponent
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(fragment : ProfileFragment)
    fun inject(activity: MainActivity)
    fun inject(fragment: RequestStudyFragment)
    fun inject(fragment: StudyPartnerFragment)
    fun inject(fragment: SettingsFragment)

}