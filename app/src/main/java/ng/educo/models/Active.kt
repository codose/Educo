package ng.educo.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Active(
    var chatId : String? = "",
    var user : User = User(),
    var message : Message = Message(),
    @ServerTimestamp
    var lastMsg : Date? = null
)