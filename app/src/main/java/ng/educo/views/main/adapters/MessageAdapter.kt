package ng.educo.views.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.R
import ng.educo.databinding.ItemSingleMessageItemBinding
import ng.educo.models.Active
import ng.educo.models.Message
import ng.educo.utils.App
import ng.educo.utils.getChatTime


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MessageAdapter(val context : Context, val clickListener: MessageClickListener) : ListAdapter<Message , MessageAdapter.MyViewHolder>(MessageDiffCallback()) {

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    class MyViewHolder(val binding: ItemSingleMessageItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(
            message: Message,
            clickListener: MessageClickListener,
            message2 : Message?
        ){
            val time1 = when {
                message.timestamp != null -> getChatTime(message.timestamp!!)
                else -> getChatTime(message.localTime!!)
            }
            val time2 = when {
                message2!!.timestamp != null -> getChatTime(message2.timestamp!!)
                else -> getChatTime(message2.localTime!!)
            }
            val endParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
            }
            if(time1 == time2){
                binding.timeStampTextView.visibility = GONE
            }else{
                binding.timeStampTextView.visibility = VISIBLE
                binding.timeStampTextView.text = time1
            }

            val startParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.START
            }
            binding.messageTextView.text = message.messageText


            if(App.appUser?.uid == message.senderId){
                binding.apply {
                    messageTextView.background = ContextCompat.getDrawable(context, R.drawable.btn_curves)
                    messageTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                    messageTextView.layoutParams = endParams
                    timeStampTextView.layoutParams = endParams
                }
            }else{
                binding.apply {
                    messageTextView.background = ContextCompat.getDrawable(context, R.drawable.btn_curves_ii)
                    messageTextView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
                    messageTextView.layoutParams = startParams
                    timeStampTextView.layoutParams = startParams
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return from(parent)
    }
    private fun from(parent: ViewGroup) : MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSingleMessageItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val request = getItem(position)
        var message2 : Message? = Message()
        if(position - 1 > -1){
            if(getItem(position - 1) != null){
                message2 = getItem(position-1)
            }
        }
        holder.bind(request, clickListener, message2)
    }
}

class MessageClickListener(val clickListener: (active: Active) -> Unit){
    fun onClick(active: Active) = clickListener(active)
}

class MessageDiffCallback : DiffUtil.ItemCallback<Message>(){
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }
}
