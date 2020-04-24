package ng.educo.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Active(
    var user : User = User(),
    var message : Message = Message(),
    @ServerTimestamp
    var lastMsg : Date? = null
)