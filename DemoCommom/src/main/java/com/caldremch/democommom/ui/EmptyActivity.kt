package com.caldremch.democommom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caldremch.common.base.BaseActivity
import com.caldremch.democommom.R

class EmptyActivity : BaseActivity() {

    override fun isPrintLifeCycle(): Boolean {
        return true
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_empty
    }
}
