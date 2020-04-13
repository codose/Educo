package ng.educo.models

data class User(
    var firstName: String = "",
    var lastName : String = "",
    var email : String = "",
    var phone : String = "",
    var accountSetup : Int = 0,
    var interest : MutableList<Long> = mutableListOf(),
    var state : String = "Lagos"
)