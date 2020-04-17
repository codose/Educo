package ng.educo.views.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ng.educo.databinding.ItemInterestItemBinding
import ng.educo.databinding.ItemRequestItemBinding
import ng.educo.models.Educo
import ng.educo.utils.longInterestToString
import ng.educo.utils.typeIntToString


class MainAdapter : ListAdapter<Educo, MainAdapter.MyViewHolder>(MainDiffCallback()) {

    class MyViewHolder(val binding: ItemRequestItemBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(educo: Educo){
            binding.titleTxtview.text = educo.title
            binding.descTextView.text = educo.description
            binding.catTextView.text =  longInterestToString(educo.category)
            binding.typeTextView.text = typeIntToString(educo.type) + " : " + "${educo.users}"
            binding.locationTextView.text = educo.location
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return from(parent)
    }
    private fun from(parent: ViewGroup) : MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRequestItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val id = getItem(position)
        holder.bind(id)
    }
}

class MainDiffCallback : DiffUtil.ItemCallback<Educo>(){
    override fun areItemsTheSame(oldItem: Educo, newItem: Educo): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Educo, newItem: Educo): Boolean {
        return oldItem == newItem
    }
}
