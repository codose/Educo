package ng.educo.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.di.modules.AppModule
import ng.educo.di.modules.ViewModelModule
import javax.inject.Singleton


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [AppSubComponents::class, AppModule::class, ViewModelModule::class, AppModule::class])
interface AppComponent{
    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun registrationComponent() : RegistrationComponent.Factory
    fun categoryComponent() : CategoryComponent.Factory
    fun mainComponent() : MainComponent.Factory

}