package com.tlw.androidrecap.basic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tlw.androidrecap.databinding.DummyRecyclerViewBinding

class DummyAdapter : RecyclerView.Adapter<DummyAdapter.DummyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DummyViewHolder {
        val binding =
            DummyRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DummyViewHolder,
        position: Int
    ) {

        holder.binding.apply {

        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class DummyViewHolder(val binding: DummyRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}