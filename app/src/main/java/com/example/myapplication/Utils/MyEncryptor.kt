package com.example.myapplication.Utils

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.Throws

object MyEncryptor {
    private val DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE=1024
    private val ALGO_IMAGE_ENCRYPTOR="AES/CBC/PKCS5PADDING"
    private val ALGO_SECRET_KEY="AES"

    @Throws(NoSuchAlgorithmException::class,
    NoSuchPaddingException::class,
    InvalidKeyException::class,
    IOException::class,
    InvalidAlgorithmParameterException::class)

    fun encryptToFile(keystr:String,spec:String,input:InputStream,output:OutputStream)
    {
        var output = output
        try {
            val charset: Charset = StandardCharsets.UTF_8

            val iv = IvParameterSpec(charset.encode(spec).array())
            val keySpec = SecretKeySpec(charset.encode(keystr).array(), ALGO_SECRET_KEY)
            val c = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR)
            c.init(Cipher.ENCRYPT_MODE,keySpec,iv)
            output = CipherOutputStream(output,c)

            val buffer = ByteArray(DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE)

            var bytesRead:Int = 0
            while(input.read(buffer).also {
                bytesRead = it
                } > 0 )
                    output.write(buffer,0,bytesRead)
        }finally {
            output.close()
        }
    }

    fun decryptToFile(keystr:String,spec:String,input:InputStream,output:OutputStream)
    {
        var output = output
        try {
            val charset: Charset = StandardCharsets.UTF_8

            val iv = IvParameterSpec(charset.encode(spec).array())
            val keySpec = SecretKeySpec(charset.encode(keystr).array(), ALGO_SECRET_KEY)
            val c = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR)
            c.init(Cipher.DECRYPT_MODE,keySpec,iv)
            output = CipherOutputStream(output,c)

            val buffer = ByteArray(DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE)

            var bytesRead:Int = 0
            while(input.read(buffer).also {
                    bytesRead = it
                } > 0 )
                output.write(buffer,0,bytesRead)
        }finally {
            output.close()
        }
    }

}