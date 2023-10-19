package com.example.videoplayer.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.videoplayer.PlayerNewActivity
import com.example.videoplayer.R
import com.example.videoplayer.databinding.VideoItemBinding
import com.example.videoplayer.model.VideoModel
import java.util.ArrayList

class VideoItemAdapter(private val context: Context, private var videoList: ArrayList<VideoModel>, private var isFolder: Boolean = false)
    : RecyclerView.Adapter<VideoItemAdapter.MyHolder>() {
    class MyHolder(binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.videoName
        val image=binding.videoThumbnail
        val imagecard=binding.videocard
        val root = binding.root


    }

    private var newPosition = 0
    private lateinit var dialogRF: androidx.appcompat.app.AlertDialog



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(VideoItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.title.text = videoList[position].videoName

        Glide.with(context)
            .asBitmap()
            .load(videoList[position].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_play).centerCrop())
            .into(holder.image)

        holder.root.setOnClickListener {
            Log.d("error","touch nhi chal gaya")
            PlayerNewActivity.position = position

            val intent = Intent(context, PlayerNewActivity::class.java)
//
            ContextCompat.startActivity(context, intent, null)




//            Log.d("error","touch nhi chal gaya")
//                    sendIntent(pos = position, ref = "NowPlaying")
//            Log.d("bhari mistake","touch chal gaya1")



        }
    }


    override fun getItemCount(): Int {
        return videoList.size
    }
    fun sendIntent(pos: Int, ref: String){
        PlayerNewActivity.position = pos
        val intent = Intent(context, PlayerNewActivity::class.java)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }
//    @SuppressLint("NotifyDataSetChanged")
//    fun updateList(searchList: ArrayList<VideoModel>){
//        videoList = ArrayList()
//        videoList.addAll(searchList)
//        notifyDataSetChanged()
//    }
}