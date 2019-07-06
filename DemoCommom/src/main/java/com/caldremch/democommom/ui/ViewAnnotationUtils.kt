package com.caldremch.democommom.ui

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.caldremch.common.annotation.BindView
import com.caldremch.common.annotation.ContentView
import java.lang.Exception
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * @author Caldremch
 * @date 2019-06-25 23:00
 * @email caldremch@163.com
 * @describe
 */
class ViewAnnotationUtils{
    companion object{

        /**
         * setContentView
         */
        fun injectContentView(activity: AppCompatActivity){
            //注解
            val annotation = activity.javaClass.getAnnotation(ContentView::class.java)
            annotation?.let {
                    @LayoutRes val layoutId = it.value
                activity.setContentView(layoutId)
                //反射
//                try {
//
//                    val method = activity.javaClass.getDeclaredMethod("setContentView", Int::class.java);
//                    method.invoke(activity, layoutId)
//                }catch (ex:Exception){
//                    ex.printStackTrace()
//                }
            }
        }


        fun injectFindView(activity: AppCompatActivity){

            //获取当前类的所有成员变量
            val fields:Array<Field>  = activity::class.java.declaredFields

            for (field in fields){
                try {

                    field.annotations.let {
                        if (field.isAnnotationPresent(BindView::class.java)){
                            field.isAccessible = true
                            val viewAnno = field.getAnnotation(BindView::class.java)
                            field.set(activity, activity.findViewById(viewAnno.value))
                        }
                    }

                }catch (ex:Exception){
                    ex.printStackTrace()
                }

            }

        }
    }
}
