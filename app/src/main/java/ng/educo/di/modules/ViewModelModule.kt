package ng.educo.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.di.providers.ViewModelFactory
import ng.educo.di.providers.ViewModelKey
import ng.educo.di.scope.ActivityScope
import ng.educo.views.categories.CategoryViewModel
import ng.educo.views.main.viewmodels.MainViewModel
import ng.educo.views.main.viewmodels.ProfileViewModel
import ng.educo.views.main.viewmodels.RequestStudyViewModel


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    internal abstract fun categoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RequestStudyViewModel::class)
    internal abstract fun requestStudyViewModel(viewModel: RequestStudyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel


}