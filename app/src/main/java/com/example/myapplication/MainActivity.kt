package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karumi.dexter.Dexter
import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myapplication.Utils.MyEncryptor
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    lateinit var myDir:File
    companion object
    {
        private val FILE_NAME_ENC = "ronaldo_enc"
        private val FILE_NAME_DEC = "ronaldo_dec.png"

        private val key = "4R81VIRKI2V5TT36"
        private val specString="85PW4OIQZYKC93XC"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this).withPermissions(*arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            .withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    btn_enc.isEnabled = true
                    btn_dec.isEnabled = true


                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    Toast.makeText(this@MainActivity,"You should accept permission",Toast.LENGTH_SHORT).show()
                }

            }).check()

            val root = Environment.getExternalStorageDirectory().toString()
        myDir= File("$root/save_images")
        if(!myDir.exists())
            myDir.mkdirs()

        btn_enc.setOnClickListener {
            val drawable = ContextCompat.getDrawable(this@MainActivity,R.drawable.ronaldo)
            val bitmapDrawable = drawable as BitmapDrawable
            val bitmap  = bitmapDrawable.bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
            val input = ByteArrayInputStream(stream.toByteArray())

            val outputFileEnc = File(myDir, FILE_NAME_ENC)
            try {
                MyEncryptor.encryptToFile(key, specString,input,FileOutputStream(outputFileEnc))

                Toast.makeText(this@MainActivity,"Encrypted",Toast.LENGTH_SHORT).show()
            }catch (e:Exception)
            {
                e.printStackTrace()
            }

        }

        btn_dec.setOnClickListener {
            val outputFileDec = File(myDir, FILE_NAME_DEC)
            val encFile = File(myDir, FILE_NAME_ENC)
            try {
                MyEncryptor.decryptToFile(key, specString,FileInputStream(encFile),FileOutputStream(outputFileDec))

                imageView.setImageURI(Uri.fromFile(outputFileDec))
                Toast.makeText(this@MainActivity,"Decrypted",Toast.LENGTH_SHORT).show()
            }catch (e:java.lang.Exception)
            {
                e.printStackTrace()

            }

        }

    }
}