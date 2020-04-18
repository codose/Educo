package ng.educo.models

import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.DocumentTransform
import java.util.*

data class User(
    var firstName: String = "",
    var lastName : String = "",
    var email : String = "",
    var phone : String = "",
    var accountSetup : Int = 0,
    @ServerTimestamp
    var accountCreated : Date? = null,
    var interest : MutableList<Long> = mutableListOf(),
    var state : String = "Lagos"
)