package com.thomas.mvvmretrofitrecyclerviewkotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thomas.mvvmretrofitrecyclerviewkotlin.databinding.ListItemBinding
import com.thomas.mvvmretrofitrecyclerviewkotlin.model.PresenceResponse
import com.thomas.mvvmretrofitrecyclerviewkotlin.model.Result

class MainAdapter: RecyclerView.Adapter<MainViewHolder>() {

    var presences = mutableListOf<Result>()

    fun setPresenceList(presences: List<Result>) {
        this.presences = presences.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ListItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val presence = presences[position]
        Glide.with(holder.itemView.context).load(presence.image_mask).into(holder.binding.fotodiri)
        holder.binding.nama.text = presence.name
        holder.binding.nik.text = presence.nik

        if (presence.mask == 1) {
            holder.binding.masker.text = "PAKAI"
        }

        else {
            holder.binding.masker.text = "TIDAK PAKAI"
        }

    }

    override fun getItemCount(): Int {
        return presences.size
    }
}

class MainViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

}
