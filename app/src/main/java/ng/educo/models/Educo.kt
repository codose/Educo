package ng.educo.models


data class Educo(
    var name : String,
    var uId : String,
    var accountType: Int,
    var description: String,
    var aoc : List<String>
)