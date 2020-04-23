package ng.educo.views.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ng.educo.databinding.ItemRequestItemBinding
import ng.educo.databinding.ItemRequestSentItemBinding

import ng.educo.databinding.ItemRequestsReceivedSentItemBinding
import ng.educo.models.Request
import ng.educo.utils.*


class SentRequestAdapter(val context : Context, val clickListener: RequestClickListener) : ListAdapter<Request, SentRequestAdapter.MyViewHolder>(RequestDiffCallback()) {

    class MyViewHolder(val binding: ItemRequestSentItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(request: Request, clickListener: RequestClickListener){
            binding.clickListener = clickListener
            binding.request = request
            binding.sentNameTxtView.text = "${request.educo.user.firstName} ${request.educo.user.lastName}"
            binding.dateTextView.text = formatDateCreated(request.timeSent!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return from(parent)
    }
    private fun from(parent: ViewGroup) : MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRequestSentItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val request = getItem(position)
            holder.bind(request, clickListener)
    }
}

