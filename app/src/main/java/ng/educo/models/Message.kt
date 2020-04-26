package ng.educo.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Message(
    var messageText : String = "",
    var receiverId : String = "",
    var senderId : String = "",
    @ServerTimestamp
    var timestamp : Date? = null,
    val localTime : Date? = Date(System.currentTimeMillis()),
    var id : String = ""
)