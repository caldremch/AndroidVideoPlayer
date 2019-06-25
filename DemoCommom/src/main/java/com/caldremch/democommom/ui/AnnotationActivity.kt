package com.caldremch.democommom.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.caldremch.common.annotation.BindView
import com.caldremch.common.annotation.ContentView
import com.caldremch.democommom.R

/**
 * @author Caldremch
 *
 * @date 2019-06-25 22:03
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
@ContentView(R.layout.activity_annotation)
class AnnotationActivity : AppCompatActivity() {

    @Deprecated(message = "过期测试",level = DeprecationLevel.WARNING )
    @BindView(R.id.button4)
    private lateinit var buttonDiy:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewAnnotationUtils.injectContentView(this)
        ViewAnnotationUtils.injectFindView(this)

        buttonDiy.setOnClickListener {
            Toast.makeText(this, "点击有效", Toast.LENGTH_SHORT).show()
        }
    }


}