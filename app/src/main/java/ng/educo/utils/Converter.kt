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
        9 -> "Agricuture"
        else -> "Pharmacy"
    }
}
