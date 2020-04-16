package ng.educo.utils


fun longInterestToString(id : Int) : String {
    return when(id){
        0 -> "Technology"
        1 -> "Arts"
        2 -> "Science"
        3 -> "Law"
        4 -> "Social Science"
        5 -> "Architecture"
        6 -> "Economics"
        7 -> "Clinical Science"
        8 -> "Education"
        9 -> "Agriculture"
        else -> "Pharmacy"
    }
}

fun longStringToInt(id : String) : Int {
    return when(id){
        "Technology" -> 0
        "Arts" -> 1
        "Science" -> 2
        "Law" -> 3
        "Social Science" -> 4
        "Architecture" -> 5
        "Economics" -> 6
        "Clinical Science" -> 7
        "Education" -> 8
        "Agriculture" -> 9
        else -> 10
    }
}
