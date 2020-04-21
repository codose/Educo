package ng.educo.views.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.R

import ng.educo.databinding.ItemRequestItemBinding
import ng.educo.models.Educo
import ng.educo.models.User
import ng.educo.utils.*


class MainAdapter(val context : Context, val clickListener: EducoClickListener) : ListAdapter<Educo, MainAdapter.MyViewHolder>(MainDiffCallback()) {

    class MyViewHolder(val binding: ItemRequestItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(educo: Educo, clickListener: EducoClickListener){
            binding.educo = educo
            binding.catTextView.text = longInterestToString(educo.category)
            if(educo.type == 1){
                binding.typeTextView.text = typeIntToString(educo.type)
            }else{
                binding.typeTextView.text = typeIntToString(educo.type) + " : " + "${educo.users}"
            }
            val firebaseRepository = FirebaseRepository()
            val user = liveData {
                val user = firebaseRepository.getOtherUser(educo.uid)
                emit(user)
            }
            user.observeForever {
                when (it){
                    is Resource.Success -> {
                        Glide.with(context)
                            .load(it.data.imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(binding.profileImage)
                    }
            }
            }
            binding.clickListener = clickListener
            binding.timeStampTxtView.text = getTimeAgo(educo.createdAt!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return from(parent)
    }
    private fun from(parent: ViewGroup) : MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRequestItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val id = getItem(position)
            holder.bind(id, clickListener)
    }
}

class EducoClickListener(val clickListener: (id: String) -> Unit){
    fun onClick(educo: Educo) = clickListener(educo.id)
}

class MainDiffCallback : DiffUtil.ItemCallback<Educo>(){
    override fun areItemsTheSame(oldItem: Educo, newItem: Educo): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Educo, newItem: Educo): Boolean {
        return oldItem == newItem
    }
}
