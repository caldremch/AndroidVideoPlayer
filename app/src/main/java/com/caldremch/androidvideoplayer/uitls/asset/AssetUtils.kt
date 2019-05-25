package com.caldremch.androidvideoplayer.uitls.asset

import android.content.Context
import com.caldremch.androidvideoplayer.uitls.CLog
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import java.lang.RuntimeException

class AssetUtils {

    companion object {

        fun copy(context: Context, fileName: String) {

            var inputStream: InputStream? = null
            var fileOutputStream: FileOutputStream? = null

            try {

                var cacheDir = context.cacheDir
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }

                var outFile = File(cacheDir, fileName)

                if (!outFile.exists()) {
                    outFile.createNewFile()
                } else {
                    CLog.d("already exit")
                    return
                }

                inputStream = context.assets.open(fileName)

                fileOutputStream = FileOutputStream(outFile)
                var buffer = ByteArray(1024)
                var byteCount = inputStream.read(buffer)

                while (byteCount != -1) {
                    CLog.d("writing....")
                    fileOutputStream.write(buffer, 0, byteCount)
                    byteCount = inputStream.read(buffer)
                }
                CLog.d("writing.... done")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

                if (fileOutputStream != null) {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                }

                if (inputStream != null) {
                    inputStream.close()
                }

            }
        }


        fun getAssetFile(context: Context, fileName: String): File {
            var file = File(context.cacheDir, fileName)
            if (!file.exists()) {
                throw RuntimeException("unCopy to cache Directory")
            }
            return file;
        }


    }
}