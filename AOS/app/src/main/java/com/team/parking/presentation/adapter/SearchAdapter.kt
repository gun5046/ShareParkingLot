package com.team.parking.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.team.parking.data.model.map.Place
import com.team.parking.data.model.map.SearchKeyWordResponse
import com.team.parking.databinding.ItemSearchPlaceBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(){

    private val callback = object : DiffUtil.ItemCallback<Place>(){
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.address_name == newItem.address_name
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,callback)

    inner class SearchViewHolder(val binding : ItemSearchPlaceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : Place){
            binding.tvItemSearchPlaceName.text = data.place_name
            binding.tvItemSearchPlaceAddress.text = data.address_name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchPlaceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}