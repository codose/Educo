package ng.educo.views.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ng.educo.databinding.ItemInterestFragmentItemBinding
import ng.educo.databinding.ItemInterestItemBinding
import ng.educo.utils.longInterestToString
import ng.educo.views.categories.InterestsAdapter
import ng.educo.views.main.adapters.ProfileAdapter.*

class ProfileAdapter : ListAdapter<Long, MyViewHolder>(InterestsDiffCallback()) {

    class MyViewHolder(val binding: ItemInterestItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(interests: String){
            binding.interestText.text = interests
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return from(parent)
    }
    private fun from(parent: ViewGroup) : MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInterestItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val id = getItem(position)
        val interests = longInterestToString(id.toInt())
        holder.bind(interests)
    }
}

class InterestsDiffCallback : DiffUtil.ItemCallback<Long>(){
    override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }
}
