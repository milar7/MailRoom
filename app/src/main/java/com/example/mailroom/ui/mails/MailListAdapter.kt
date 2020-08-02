package com.example.mailroom.ui.mails

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.mailroom.R
import com.example.mailroom.data.entity.Mail
import kotlinx.android.synthetic.main.item_mail_list.view.*
import java.text.DateFormat

class MailListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Mail>() {

        override fun areItemsTheSame(oldItem: Mail, newItem: Mail): Boolean {
            return oldItem.mailId == newItem.mailId
        }

        override fun areContentsTheSame(oldItem: Mail, newItem: Mail): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MailListAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_mail_list,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MailListAdapterViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Mail>) {
        differ.submitList(list)
    }

    class MailListAdapterViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Mail) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.apply {
                title.text=item.title
                text.text=item.text
                date.text= DateFormat.getDateInstance().format(item.sendDate)
                type.text=item.type
                sender.text="Send By :${item.sender.name}"
                giver.text="Receive By :${item.receiver.name}"
            }
        }
    }

    fun getItemAt(position: Int):Mail=differ.currentList.get(position)

    interface Interaction {
        fun onItemSelected(position: Int, item: Mail)
    }
}
