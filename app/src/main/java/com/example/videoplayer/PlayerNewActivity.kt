package com.example.videoplayer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.videoplayer.databinding.ActivityPlayerBinding
import com.example.videoplayer.model.VideoModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView


class PlayerNewActivity : AppCompatActivity() {
     private lateinit var playPauseBtn: ImageButton
    private lateinit var fullScreenBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var nextBtn: ImageButton
    private lateinit var prevBtn: ImageButton
    private lateinit var repeatBtn: ImageButton
    private lateinit var shareBtn: ImageButton
    private lateinit var videoTitle: TextView
    private lateinit var playerView:StyledPlayerView

    private lateinit var binding: ActivityPlayerBinding

    companion object {


        private lateinit var player: ExoPlayer
        lateinit var playerList: ArrayList<VideoModel>
        var position: Int = -1
        var repeat:Boolean=false


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player_new)
        videoTitle=findViewById(R.id.videoTitle)
//        binding = ActivityPlayerBinding.inflate(layoutInflater)
//        setContentView(binding.root)


        playerView = findViewById<StyledPlayerView>(R.id.player_view)
        videoTitle = findViewById(R.id.videoTitle)
        playPauseBtn = findViewById(R.id.playPauseBtn)
        fullScreenBtn = findViewById(R.id.fullScreenBtn)
        backBtn = findViewById(R.id.backBtn_v)
        nextBtn=findViewById(R.id.nextBtn)
        prevBtn=findViewById(R.id.prevBtn)
        repeatBtn=findViewById(R.id.repeatBtn)
        shareBtn=findViewById(R.id.shareBtn)





        playerList= ArrayList()
        playerList=HomeActivity.tempList

//        player = ExoPlayer.Builder(this@PlayerNewActivity).build()
//        playerView.player = player
//        videoTitle.text= playerList[position].videoName
//        videoTitle.isSelected=true
//
//        val mediaItem: MediaItem = MediaItem.fromUri(playerList[position].artUri)
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        playVideo()

        if (intent.extras != null) {
            if (intent.getStringExtra("LINK") != null) {
                val i = intent
                val url = i.getStringExtra("link")
                createplayerurl(url)
            }
        }

       createPlayer()
        initialiseBinding()
//        for (i in playerList.indices){
//            var songPath = playerList.get(i).artUri
//            val item: MediaItem = MediaItem.fromUri(songPath)
//            player.addMediaItem(item)
//        }
//        player.prepare()
//        player.setPlayWhenReady(true)






    }

    private fun createplayerurl(url: String?) {try{
        player.release()
    }
    catch (e: Exception){}
        player = ExoPlayer.Builder(this@PlayerNewActivity).build()
        playerView.player = player
        videoTitle.text= "Notification"
//        videoTitle.isSelected=true

        val mediaItem: MediaItem = MediaItem.fromUri(Uri.parse(url))
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()


    }

    fun initialiseBinding(){


        backBtn.setOnClickListener {
            finish()
        }
        playPauseBtn.setOnClickListener {
            if(player.isPlaying) pauseVideo()
            else playVideo()
        }
        nextBtn.setOnClickListener { nextPrevVideo() }
        prevBtn.setOnClickListener { nextPrevVideo(false) }

        repeatBtn.setOnClickListener {
            if(repeat){
                repeat = false
                player.repeatMode=Player.REPEAT_MODE_OFF
                repeatBtn.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_off)

            }
            else
            {
                repeat = true
                player.repeatMode=Player.REPEAT_MODE_ONE
                repeatBtn.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_all)


            }
        }
        shareBtn.setOnClickListener {
            shareVideoWithShareCompat(playerList[position].artUri)
        }



//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "video/*"
//        intent.putExtra(Intent.EXTRA_STREAM, playerList[position].artUri)
//
//        startActivity(intent)

    }
    fun shareVideoWithShareCompat(videoUri: Uri) {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            val intent = Intent(Intent.ACTION_SEND)
        intent.type = "video/*"
        intent.putExtra(Intent.EXTRA_STREAM, playerList[position].artUri)

        startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


    }



    private fun playVideo(){
        playPauseBtn.setImageResource(R.drawable.pause_icon)
        player.play()
    }
    private fun pauseVideo(){
        playPauseBtn.setImageResource(R.drawable.play_arrow)
        player.pause()
    }
    private fun nextPrevVideo(isNext: Boolean = true){
        if(isNext) setPosition()
        else setPosition(isIncrement = false)
        createPlayer()
    }
    private fun setPosition(isIncrement: Boolean = true){
        if(!repeat){
            if(isIncrement){
                if(playerList.size -1 == position)
                    position = 0
                else ++position
            }else{
                if(position  == 0)
                    position = playerList.size - 1
                else --position
            }
        }}

    fun createPlayer(){
        try{
            player.release()
        }
        catch (e: Exception){}
                player = ExoPlayer.Builder(this@PlayerNewActivity).build()
        playerView.player = player
        videoTitle.text= playerList[position].videoName
        videoTitle.isSelected=true

        val mediaItem: MediaItem = MediaItem.fromUri(playerList[position].artUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()

        player.addListener(object :Player.Listener
        {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if(playbackState== Player.STATE_ENDED) nextPrevVideo()
            }
        })

    }

    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
        player.release()

    }
}