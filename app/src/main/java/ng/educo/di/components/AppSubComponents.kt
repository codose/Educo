package ng.educo.di.components

import dagger.Module


@Module(subcomponents = [RegistrationComponent::class, CategoryComponent::class, ProfileComponent::class])
class AppSubComponents {
}