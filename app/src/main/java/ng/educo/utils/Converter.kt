package ng.educo.utils

import ng.educo.models.Educo
import ng.educo.models.User
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun convertInterestList(user: User) : List<String>{
    val list = ArrayList<String>()
    for (i in user.interest){
        val data = longInterestToString(i.toInt())
        list.add(data)
    }
    return list
}

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

fun typeIntToString(id : Int) : String {
    return when(id){
        1 -> "Study Partner"
        else -> "Study Group"
    }
}

fun typeStringToInt(id : String) : Int {
    return when(id){
        "Study Partner" -> 1
        else -> 2
    }
}

fun yearToString(id : String) : String {
    return when(id){
        "1" -> "100 Lvl"
        "2" -> "200 Lvl"
        "3" -> "300 Lvl"
        "4" -> "400 Lvl"
        "5" -> "500 Lvl"
        else -> "600 Lvl"
    }
}

fun formatDateJoined(date : Date) : String {
    val formattedDate : String
    val sdf = SimpleDateFormat("LLLL MM, YYYY")
    formattedDate = sdf.format(date)
    return "Joined $formattedDate"
}

fun checkPartnerEduco(educo: Educo) : Boolean{
    if (educo.uid != App.auth.currentUser?.uid
        && App.appUser?.interest!!.contains(educo.category.toLong())
        && App.appUser!!.state == educo.location
        && educo.type == 1){
        return true
    }
    return false
}

fun checkGroupEduco(educo: Educo) : Boolean{
    if (educo.uid != App.auth.currentUser?.uid
        && App.appUser?.interest!!.contains(educo.category.toLong())
        && App.appUser!!.state == educo.location
        && educo.type == 2){
        return true
    }
    return false
}
