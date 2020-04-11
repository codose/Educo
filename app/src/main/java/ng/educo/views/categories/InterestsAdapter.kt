package ng.educo.views.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ng.educo.databinding.ItemInterestFragmentItemBinding

class InterestsAdapter : ListAdapter<String, InterestsAdapter.ViewHolder>(InterestsDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    var tracker : SelectionTracker<Long>? = null

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(parent)
    }
    private fun from(parent: ViewGroup) : ViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInterestFragmentItemBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val interest = getItem(position)
        tracker?.let {
            holder.bind(interest, it.isSelected(position.toLong()))
        }
    }

    inner class ViewHolder (val binding: ItemInterestFragmentItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(interests: String, isActivated : Boolean){
            binding.itemFragmentInterestTextView.text = interests
            binding.selectedView.isVisible = isActivated
            binding.executePendingBindings()
        }

        fun getItemDetails() : ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>(){
                override fun getSelectionKey(): Long? = itemId

                override fun getPosition(): Int = adapterPosition

            }
    }
}

class InterestsDiffCallback : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem[1] == newItem[1]
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>(){
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if(view != null){
            return (recyclerView.getChildViewHolder(view) as InterestsAdapter.ViewHolder).getItemDetails()
        }
        return null
    }

}

class MyItemKeyProvider(private val recyclerView: RecyclerView) : ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_CACHED){
    override fun getKey(position: Int): Long? {
        return recyclerView.adapter?.getItemId(position)
    }

    override fun getPosition(key: Long): Int {
        val viewHolder = recyclerView.findViewHolderForItemId(key)
        return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION
    }

}