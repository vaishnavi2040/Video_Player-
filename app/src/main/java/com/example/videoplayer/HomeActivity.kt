package com.example.videoplayer


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videoplayer.Adapter.VideoItemAdapter
import com.example.videoplayer.databinding.ActivityHomeBinding
import com.example.videoplayer.model.VideoModel
import com.google.android.material.snackbar.Snackbar
import java.io.File


class HomeActivity : AppCompatActivity() {

lateinit var binding: ActivityHomeBinding
companion object{
    lateinit var tempList:ArrayList<VideoModel>
}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var actionBar: ActionBar? = supportActionBar

        // providing title for the ActionBar

        // providing title for the ActionBar
        if (actionBar != null) {
            actionBar.setTitle("  All Videos")
        }

        val sharedPrefManager = SharedPrefManager(this)
        if (!sharedPrefManager.isLoggedIn()) {



            startActivity(Intent(this@HomeActivity, IntroActivity::class.java))
            finish()
        }



        tempList= ArrayList()


        binding.recyclerviewHome.setHasFixedSize(true)

        binding.recyclerviewHome.setItemViewCacheSize(10)
        binding.recyclerviewHome.layoutManager=GridLayoutManager(this,3)
        binding.recyclerviewHome.adapter= VideoItemAdapter(this,tempList)


        if(requestRuntimePermission()){

            getAllVideos(this)


        }

        binding.recyclerviewHome.setHasFixedSize(true)
        binding.recyclerviewHome.setItemViewCacheSize(10)
        binding.recyclerviewHome.layoutManager=GridLayoutManager(this,3)
        binding.recyclerviewHome.adapter= VideoItemAdapter(this,tempList)





    }

    private fun requestRuntimePermission(): Boolean{
        //android 13 permission request
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_VIDEO), 13)

                setLayout()
//                getAllVideos(this)
//
//                binding.recyclerviewHome.setHasFixedSize(true)
//
//                binding.recyclerviewHome.setItemViewCacheSize(10)
//                binding.recyclerviewHome.layoutManager=GridLayoutManager(this,3)
//                binding.recyclerviewHome.adapter= VideoItemAdapter(this,tempList)
                return false
            }
            return true
        }

        //for  devices less than api 28
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            if(ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
                setLayout()
//                getAllVideos(this)
//                binding.recyclerviewHome.setHasFixedSize(true)
//
//                binding.recyclerviewHome.setItemViewCacheSize(10)
//                binding.recyclerviewHome.layoutManager=GridLayoutManager(this,3)
//                binding.recyclerviewHome.adapter= VideoItemAdapter(this,tempList)
                return false
            }
        }else{
            // devices higher than  api 29
            if(ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE),14)
                setLayout()
//                getAllVideos(this)
//                binding.recyclerviewHome.setHasFixedSize(true)
//
//                binding.recyclerviewHome.setItemViewCacheSize(10)
//                binding.recyclerviewHome.layoutManager=GridLayoutManager(this,3)
//                binding.recyclerviewHome.adapter= VideoItemAdapter(this,tempList)
                return false
            }
        }
        return true
    }
    fun setLayout(){
        getAllVideos(this)
        binding.recyclerviewHome.setHasFixedSize(true)

        binding.recyclerviewHome.setItemViewCacheSize(10)
        binding.recyclerviewHome.layoutManager=GridLayoutManager(this,3)
        binding.recyclerviewHome.adapter= VideoItemAdapter(this,tempList)


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 13) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                setLayout()

//                getAllVideos(this)
//                binding.recyclerviewHome.setHasFixedSize(true)
//
//                binding.recyclerviewHome.setItemViewCacheSize(10)
//                binding.recyclerviewHome.layoutManager=GridLayoutManager(this,3)
//                binding.recyclerviewHome.adapter= VideoItemAdapter(this,tempList)

            }
            else Snackbar.make(binding.root, "Storage Permission Needed!!", 5000)
                .setAction("OK"){
                    ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
                }
                .show()
//                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
        }

        //for read external storage permission
        if(requestCode == 14) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                setLayout()
//                getAllVideos(this)
//                binding.recyclerviewHome.setHasFixedSize(true)
//
//                binding.recyclerviewHome.setItemViewCacheSize(10)
//                binding.recyclerviewHome.layoutManager=GridLayoutManager(this,3)
//                binding.recyclerviewHome.adapter= VideoItemAdapter(this,tempList)
            }
            else Snackbar.make(binding.root, "Storage Permission Needed!!", 5000)
                .setAction("OK"){
                    ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE),14)
                }
                .show()
//            else
//                ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE),14)
        }
    }


    fun getAllVideos(context: Context){




        val projection = arrayOf(
            MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_ID)
        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,
            MediaStore.Video.Media.DATE_ADDED+" DESC")
        if(cursor != null)
            if(cursor.moveToNext())
                do {

                    val titleC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))?:"Unknown"
                    val idC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))?:"Unknown"
                    val folderC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))?:"Internal Storage"
                    val folderIdC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID))?:"Unknown"
                    val sizeC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))?:"0"
                    val pathC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))?:"Unknown"
                     val durationC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))?.toLong()?:0L

                    try {
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        val video = VideoModel(videoName = titleC, id = idC,
                            videoPath =  pathC, artUri = artUriC)
                        if(file.exists()) tempList.add(video)



                    }catch (e:Exception){}
                }while (cursor.moveToNext())
        cursor?.close()
//        return tempList
    }
    private var exit = false
    override fun onBackPressed() {
        if (exit) {
            finish() // finish activity
        } else {
            Toast.makeText(
                this, "Press Back again to Exit.",
                Toast.LENGTH_SHORT
            ).show()
            exit = true
            Handler().postDelayed({ exit = false }, (3 * 1000).toLong())
        }
    }


}
