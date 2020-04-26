package ng.educo.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class Educo(
    var title : String = "",
    var category : Int = 0,
    var type : Int = 0,
    var users : Int = 0,
    @ServerTimestamp
    var createdAt : Date? = null,
    var description : String = "",
    var location : String = "",
    var user : User = User(),
    var id : String = "",
    var createdAtLocal: Date? = Date(System.currentTimeMillis())
)