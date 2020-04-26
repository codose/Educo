package ng.educo.di.components

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module(subcomponents = [RegistrationComponent::class, CategoryComponent::class, MainComponent::class])
class AppSubComponents {
}