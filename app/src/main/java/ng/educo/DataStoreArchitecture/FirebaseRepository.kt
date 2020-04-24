package ng.educo.DataStoreArchitecture

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import id.zelory.compressor.Compressor
import kotlinx.coroutines.tasks.await
import ng.educo.models.*
import ng.educo.utils.*
import ng.educo.utils.Constants.COLLECTION_EDUCO
import ng.educo.utils.Constants.COLLECTION_RECEIVED
import ng.educo.utils.Constants.COLLECTION_SENT
import ng.educo.utils.Constants.COLLECTION_USERS
import javax.inject.Inject

class FirebaseRepository @Inject constructor() {
    private val auth = FirebaseAuth.getInstance()
    private val database = App.firestore
    private val userRef = database.collection(COLLECTION_USERS)
    private val educoRef = database.collection(COLLECTION_EDUCO)
    private val receivedRef = database.collection(COLLECTION_RECEIVED)
    private val sentRef = database.collection(COLLECTION_SENT)
    private val storageRef = App.storage.reference


    suspend fun updateUser(user : User) : Resource<String> {
        return try {
            userRef.document(auth.currentUser!!.uid).set(user, SetOptions.merge()).await()
            App.appUser = user
            Resource.Success("Account Updated Successfully")
        } catch(e : Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun registerUser(email: String, password : String) : Resource<Boolean>{
        return try{
            auth.createUserWithEmailAndPassword(email,password).await()
            Resource.Success(true)
        }catch (e : Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun loginUser(email: String, password : String) : Resource<Boolean>{
        return try{
            auth.signInWithEmailAndPassword(email,password).await()
            Resource.Success(true)
        }catch (e : Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun  createNewRequest(educo : Educo) : Resource<Boolean>{
        return try{
            val documentId = educoRef.document().id
            educo.id = documentId
            educoRef.document(documentId).set(educo, SetOptions.merge()).await()
            Resource.Success(true)
        }catch (e : Exception){
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
        }catch(e : Exception){
            Resource.Failure("Error occurred")
        }
    }

    suspend fun getAllStudyGroups() : Resource<List<Educo>>{
        return try{
            val educo = educoRef.get().await()
            val allPartners = educo.toObjects<Educo>()
            val otherPartners : ArrayList<Educo> = ArrayList()
            for(i in allPartners){
                if(checkGroupEduco(i)){
                    otherPartners.add(i)
                }
            }
            Resource.Success(otherPartners)
        }catch(e : Exception){
            Resource.Failure("Error occurred")
        }

    }

    suspend fun uploadImage(uri: Uri, context: Context) : Resource<String>{
        return try {

            val compressedImageFile = Compressor.compress(context, uri.toFile())
            val uploadTask = storageRef.child("displayImages/${auth.currentUser?.uid}.jpg").putFile(compressedImageFile.toUri())
            val downloadUrl = uploadTask.await().storage.downloadUrl.await()
            App.appUser?.imageUrl = downloadUrl.toString()
            updateUser(App.appUser!!)
            Resource.Success("Upload Successful")
        }catch (e : Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun getSingleStudy(id: String) : Resource<Educo>{
        return try{
            val educo = educoRef.document(id).get().await()
            val mEduco = educo.toObject<Educo>()
            Resource.Success(mEduco!!)
        }catch(e:Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun getMyStudies() : Resource<List<Educo>>{
        return try{
            val educo = educoRef.get().await()
            val allPartners = educo.toObjects<Educo>()
            val otherPartners : ArrayList<Educo> = ArrayList()
            for(i in allPartners){
                if(checkUserEduco(i)){
                    otherPartners.add(i)
                }
            }
            Resource.Success(otherPartners)
        }catch(e : Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun applyForStudy(receiverId : String, request: Request) : Resource<String>{
        return try {

            val sent = sentRef.document(App.appUser?.uid!!).collection("Requests").document(request.educo.id).get().await()
            sent.let {
                if(it.exists()){
                    Resource.Success("Request already exists")
                }else{
                    val id = receivedRef.document(receiverId).collection("Requests").document().id
                    request.id = id
                    receivedRef.document(receiverId).collection("Requests").document(id).set(request).await()
                    sentRef.document(App.appUser?.uid!!).collection("Requests").document(request.educo.id).set(request).await()
                    Resource.Success("Request sent Successfully.")
                }
            }
        }catch (e : Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun getReceivedRequests() : Resource<List<Request>>{
        return try{
            val requests = receivedRef.document(App.appUser?.uid!!).collection("Requests").get().await()
            val allRequest = requests.toObjects<Request>()
            Resource.Success(allRequest)
        }catch(e:Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun deleteReceivedRequests(request: Request) : Resource<String>{
        return try{
            receivedRef.document(App.appUser?.uid!!).collection("Requests").document(request.id).delete().await()
            sentRef.document(request.user.uid).collection("Requests").document(request.educo.id).delete().await()
            Resource.Success("Request Accepted")
        }catch(e:Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun getSentRequests() : Resource<List<Request>>{
        return try{
            val requests = sentRef.document(App.appUser?.uid!!).collection("Requests").get().await()
            val allRequest = requests.toObjects<Request>()
            Resource.Success(allRequest)
        }catch(e:Exception){
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

    suspend fun getOtherUser(uid : String) : Resource<User>{
        return try {
            val user = userRef.document(uid).get().await()
            val mUser = user.toObject<User>()
            Resource.Success(mUser!!)
        } catch (e: Exception){
            return Resource.Failure("Error Occurred")
        }
    }

    suspend fun getUser() : Resource<User>{
        return try {
            val user = userRef.document(auth.currentUser!!.uid).get().await()
            val appUser = user.toObject<User>()
            Resource.Success(appUser!!)
        } catch (e: Exception){
            return Resource.Failure(e.message!!)
        }
    }

    suspend fun getRequest(id: String) : Resource<Request>{
        return try{
            val request = receivedRef.document(App.appUser?.uid!!).collection("Requests").document(id).get().await()
            val mRequest = request.toObject<Request>()
            Resource.Success(mRequest!!)
        }catch (e: Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun sendMessage(docId : String, message : Message, user : User) : Resource<String>{
        return try{
            val id = database.collection("Chats").document(docId).collection("Chats").document().id
            val active1 = Active(user, message)
            val active2 = Active(App.appUser!!, message)
            message.id = id
            database.collection("Active Chats").document(message.senderId).collection("Chats").document(message.receiverId).set(active1).await()
            database.collection("Active Chats").document(message.receiverId).collection("Chats").document(message.senderId).set(active2).await()
            database.collection("Chats").document(docId).collection("Chats").document(id).set(message).await()
            Resource.Success("Message sent")
        }catch(e: Exception){
            Resource.Failure(e.message!!)
        }
    }

    suspend fun getActiveChats(uid : String) : Resource<List<Active>>{
        return try{
            val message = database.collection("Active Chats").document(uid).collection("Chats").get().await()
            val active = message.toObjects<Active>()
            Resource.Success(active)
        }catch (e : Exception){
            Resource.Failure(e.message!!)
        }
    }

}