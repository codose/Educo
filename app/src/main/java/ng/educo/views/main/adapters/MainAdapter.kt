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
import ng.educo.utils.*


class MainAdapter(val context : Context, val clickListener: EducoClickListener) : ListAdapter<Educo, MainAdapter.MyViewHolder>(MainDiffCallback()) {

    class MyViewHolder(val binding: ItemRequestItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(educo: Educo, clickListener: EducoClickListener){
            val time = when {
                educo.createdAt != null -> getTimeAgo(educo.createdAt!!)
                else -> getTimeAgo(educo.createdAtLocal!!)
            }
            binding.educo = educo
            binding.catTextView.text = longInterestToString(educo.category)
            if(educo.type == 1){
                binding.typeTextView.text = typeIntToString(educo.type)
            }else{
                binding.typeTextView.text = typeIntToString(educo.type) + " : " + "${educo.users}"
            }
            Glide.with(context)
                .load(educo.user.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.profileImage)
            binding.clickListener = clickListener
            binding.timeStampTxtView.text = time
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

class EducoClickListener(val clickListener: (educo: Educo) -> Unit){
    fun onClick(educo: Educo) = clickListener(educo)
}

class MainDiffCallback : DiffUtil.ItemCallback<Educo>(){
    override fun areItemsTheSame(oldItem: Educo, newItem: Educo): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Educo, newItem: Educo): Boolean {
        return oldItem == newItem
    }
}
