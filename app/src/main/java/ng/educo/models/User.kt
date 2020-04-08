package ng.educo.models

data class User(
    var uId : String,
    var firstName: String,
    var lastName : String,
    var accountType : Int,
    var dob : String,
    var email : String,
    var phone : String
)