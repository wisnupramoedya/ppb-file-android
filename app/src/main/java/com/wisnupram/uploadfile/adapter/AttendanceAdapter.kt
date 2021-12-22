package com.wisnupram.uploadfile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wisnupram.uploadfile.databinding.ListItemBinding
import com.wisnupram.uploadfile.model.Result

class AttendanceAdapter: RecyclerView.Adapter<ListViewHolder>() {
    //inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    var presences = mutableListOf<Result>()

    fun setPresenceList(presences: List<Result>) {
        this.presences = presences.toMutableList()
        //notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return presences.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(presences[position])
    }
}

class ListViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: Result) {
        with(binding) {
            Glide.with(itemView.context).load(data.image_profile).into(fotodiri)
            nama.text = data.name
            nik.text = data.nik

            if (data.mask == 1) {
                masker.text = "PAKAI"
            }

            else {
                masker.text = "TIDAK PAKAI"
            }
        }
    }
}