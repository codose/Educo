package ng.educo.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Request(
    var message : String = "",
    var status : Int = 0,
    var user : User = User(),
    var educo: Educo = Educo(),
    var id : String = "",
    @ServerTimestamp
    var timeSent : Date? = null
)