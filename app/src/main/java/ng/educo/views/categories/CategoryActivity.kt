package ng.educo.views.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.R
import ng.educo.di.components.CategoryComponent
import ng.educo.utils.App
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CategoryActivity : AppCompatActivity() {

    @Inject
    lateinit var factory : ViewModelProvider.Factory

    lateinit var categoryComponent: CategoryComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        categoryComponent = (application as App).appComponent.categoryComponent().create()
        categoryComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
    }
}
