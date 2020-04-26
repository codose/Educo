package ng.educo.di.components

import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.di.scope.ActivityScope
import ng.educo.views.categories.CategoryActivity
import ng.educo.views.categories.ui.InterestsFragment
import ng.educo.views.categories.ui.LocationSelectFragment


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ActivityScope
@Subcomponent
interface CategoryComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create() : CategoryComponent
    }

    fun inject(activity: CategoryActivity)
    fun inject(fragment: InterestsFragment)
    fun inject(fragment: LocationSelectFragment)
}