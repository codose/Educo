package ng.educo.views.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import ng.educo.databinding.ItemRequestsReceivedSentItemBinding
import ng.educo.models.Request
import ng.educo.utils.*


class RequestAdapter(val context : Context, val clickListener: RequestClickListener) : ListAdapter<Request, RequestAdapter.MyViewHolder>(RequestDiffCallback()) {

    class MyViewHolder(val binding: ItemRequestsReceivedSentItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(request: Request, clickListener: RequestClickListener){
            binding.clickListener = clickListener
            binding.request = request
            binding.sentNameTxtView.text = "${request.user.firstName} ${request.user.lastName}"
            binding.dateTextView.text = formatDateCreated(request.timeSent!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return from(parent)
    }
    private fun from(parent: ViewGroup) : MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRequestsReceivedSentItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val request = getItem(position)
            holder.bind(request, clickListener)
    }
}

class RequestClickListener(val clickListener: (request: Request) -> Unit){
    fun onClick(request: Request) = clickListener(request)
}

class RequestDiffCallback : DiffUtil.ItemCallback<Request>(){
    override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
        return oldItem.id == newItem.id
    }
}
