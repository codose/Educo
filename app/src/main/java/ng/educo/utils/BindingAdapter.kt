package ng.educo.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("interest")
fun TextView.setInterestText(item: String?){
    item?.let {
        text = item
    }
}