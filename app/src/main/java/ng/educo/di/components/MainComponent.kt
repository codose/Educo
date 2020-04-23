package ng.educo.di.components

import dagger.Subcomponent
import ng.educo.di.scope.ActivityScope
import ng.educo.views.main.MainActivity
import ng.educo.views.main.ui.*


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
    fun inject(fragment: StudyGroupFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: SingleStudyFragment)
    fun inject(fragment: ReceivedRequestFragment)
    fun inject(fragment: RequestsFragment)
    fun inject(fragment: SentRequestFragment)
}