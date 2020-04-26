package ng.educo.di.components

import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.di.scope.ActivityScope
import ng.educo.views.main.MainActivity
import ng.educo.views.main.ui.*


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
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
    fun inject(fragment: SingleRequestFragment)
    fun inject(fragment: ChatsFragment)
    fun inject(fragment: SingleChatsFragment)
}