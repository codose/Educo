package ng.educo.DataStoreArchitecture

import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import ng.educo.models.Educo
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Constants
import ng.educo.utils.Resource
import ng.educo.utils.checkPartnerEduco
import javax.inject.Inject

class FirebaseRepository @Inject constructor() {
    private val auth = FirebaseAuth.getInstance()
    private val database = App.firestore
    private val userRef = database.collection(Constants.COLLECTION_USERS)
    private val educoRef = database.collection(Constants.COLLECTION_REQUESTS)
    suspend fun updateUser(user : User) : Resource<Boolean> {
        return try {
            userRef.document(auth.currentUser!!.uid).set(user, SetOptions.merge()).await()
            App.appUser = user
            Resource.Success(true)
        } catch(e : FirebaseFirestoreException){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun registerUser(email: String, password : String) : Resource<Boolean>{
        return try{
            auth.createUserWithEmailAndPassword(email,password).await()
            Resource.Success(true)
        }catch (e : FirebaseAuthException){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun loginUser(email: String, password : String) : Resource<Boolean>{
        return try{
            auth.signInWithEmailAndPassword(email,password).await()
            Resource.Success(true)
        }catch (e : FirebaseAuthException){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun  createNewRequest(educo : Educo) : Resource<Boolean>{
        return try{
            educoRef.document().set(educo, SetOptions.merge()).await()
            Resource.Success(true)
        }catch (e : FirebaseFirestoreException){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun getAllStudyPartners() : Resource<List<Educo>>{
        return try{
            val educo = educoRef.get().await()
            val allPartners = educo.toObjects<Educo>()
            val otherPartners : ArrayList<Educo> = ArrayList()
            for(i in allPartners){
                if(checkPartnerEduco(i)){
                    otherPartners.add(i)
                }
            }
            Resource.Success(otherPartners)
        }catch(e : FirebaseFirestoreException){
            Resource.Failure(e.message!!)
        }

    }

    fun logOut() : Boolean {
           return try {
               auth.signOut()
               App.appUser = null
               true
           } catch (e : Exception){

               false
           }
    }

    suspend fun getUser() : Resource<User>{
        return try {
            val user = userRef.document(auth.currentUser!!.uid).get().await()
            val appUser = user.toObject<User>()
            Resource.Success(appUser!!)
        } catch (e: FirebaseFirestoreException){
            return Resource.Failure(e.message!!)
        }
    }
}