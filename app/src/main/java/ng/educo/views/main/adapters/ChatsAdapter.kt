package ng.educo.views.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ng.educo.databinding.ItemChatListItemBinding

import ng.educo.databinding.ItemRequestsReceivedSentItemBinding
import ng.educo.models.Active
import ng.educo.models.Request
import ng.educo.utils.*


class ChatsAdapter(val context : Context, val clickListener: ChatsClickListener) : ListAdapter<Active , ChatsAdapter.MyViewHolder>(ChatsDiffCallback()) {

    class MyViewHolder(val binding: ItemChatListItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(active: Active , clickListener: ChatsClickListener){
            binding.clickListener = clickListener
            binding.active = active
            binding.nameText.text = "${active.user.firstName} ${active.user.lastName}"
            binding.lastMessage.text = active.message.messageText
            binding.lastMessageTime.text = getTimeAgo(active.lastMsg!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return from(parent)
    }
    private fun from(parent: ViewGroup) : MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemChatListItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val request = getItem(position)
            holder.bind(request, clickListener)
    }
}

class ChatsClickListener(val clickListener: (active: Active) -> Unit){
    fun onClick(active: Active) = clickListener(active)
}

class ChatsDiffCallback : DiffUtil.ItemCallback<Active>(){
    override fun areItemsTheSame(oldItem: Active, newItem: Active): Boolean {
        return oldItem.user == newItem.user
    }

    override fun areContentsTheSame(oldItem: Active, newItem: Active): Boolean {
        return oldItem.user == newItem.user
    }
}
