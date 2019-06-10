package com.caldremch.democommom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * @author Caldremch
 * @date 2019-06-06 15:46
 * @email caldremch@163.com
 * @describe
 **/
public class MutiLineSendEditText extends AppCompatEditText {


    public MutiLineSendEditText(Context context) {
        super(context);
    }

    public MutiLineSendEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MutiLineSendEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        if(inputConnection != null){
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return inputConnection;
    }
}
