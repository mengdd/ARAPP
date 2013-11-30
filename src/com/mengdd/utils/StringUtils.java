package com.mengdd.utils;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class StringUtils {

    public static void validateChineseStringLength(Context context,
            EditText input, String str, int maxCount) {
        if (null == str) {
            return;
        }
        str = str.trim();
        int len = 0;
        try {
            len = str.getBytes("gbk").length;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (len > maxCount * 2) {
            int num = str.length();
            String valid = str.substring(0, num - 1);
            input.setText(valid);
            input.setSelection(input.getText().length());// 光标定位到末尾

            String toast = String.format("不能超过%s个汉字", maxCount);
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show();

        }
    }
}
