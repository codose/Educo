package ng.educo.DataStoreArchitecture

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Constants

class UserRepo {
    private val auth = FirebaseAuth.getInstance()
    private val database = App.firestore
    private val userRef = database.collection(Constants.COLLECTION_USERS)
    suspend fun updateUser(user : User) : Boolean{
        return try {
            userRef.document(auth.currentUser!!.uid).set(user, SetOptions.merge()).await()
            true
        } catch (e : Exception){
            false
        }
    }

    suspend fun logOut() : Boolean {
           return try {
               auth.signOut()
               App.appUser = null
               true
           } catch (e : Exception){

               false
           }
    }

    suspend fun getUser() : User?{
        return try {
            val user = userRef.document(auth.currentUser!!.uid).get().await()
            user.toObject<User>()
        } catch (e: Exception){
            return null
        }
    }
}