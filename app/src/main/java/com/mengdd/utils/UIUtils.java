package com.mengdd.utils;

import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Utility Class for UI related work
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class UIUtils {

    /**
     * detach View from its parent
     * 
     */
    public static void detachViewFromParent(View view) {
        Method method;
        try {
            method = ViewGroup.class.getDeclaredMethod("detachViewFromParent",
                    View.class);
            method.setAccessible(true);

            ViewParent viewGroup = view.getParent();
            method.invoke(viewGroup, view);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enableView(View view) {
        try {
            view.setFocusableInTouchMode(true);
            if (view.isInTouchMode()) {
                Method method = View.class.getDeclaredMethod("getViewRootImpl");
                method.setAccessible(true);
                Object object = method.invoke(view);
                if (object != null) {
                    Method m = object.getClass().getDeclaredMethod(
                            "ensureTouchMode", boolean.class);
                    m.setAccessible(true);
                    m.invoke(object, false);
                }
            }
            view.clearFocus();
            view.setSelected(false);
            view.setPressed(false);
        }
        catch (Exception e) {
        }
    }

    public static AlertDialog showDialog(Context context,
            OnClickListener listener, int titleId, int positiveButtonId,
            int neutralButtonId, int negativeButtonId, View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (titleId > 0) {
            builder.setTitle(titleId);
        }

        if (positiveButtonId > 0) {
            builder.setPositiveButton(positiveButtonId, listener);
        }
        if (neutralButtonId > 0) {
            builder.setNeutralButton(neutralButtonId, listener);
        }
        if (negativeButtonId > 0) {
            builder.setNegativeButton(negativeButtonId, listener);
        }
        builder.setView(view);
        try {
            AlertDialog dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static AlertDialog showDialog(Context context,
            OnClickListener listener, CharSequence title, int positiveButtonId,
            int neutralButtonId, int negativeButtonId, View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (positiveButtonId > 0) {
            builder.setPositiveButton(positiveButtonId, listener);
        }
        if (neutralButtonId > 0) {
            builder.setNeutralButton(neutralButtonId, listener);
        }
        if (negativeButtonId > 0) {
            builder.setNegativeButton(negativeButtonId, listener);
        }
        builder.setView(view);
        try {
            AlertDialog dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }
        catch (Exception e) {
            return null;
        }

    }

    public static AlertDialog showDialog(Context context,
            OnClickListener listener, View title, int positiveButtonId,
            int neutralButtonId, int negativeButtonId, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(title);
        if (positiveButtonId > 0) {
            builder.setPositiveButton(positiveButtonId, listener);
        }
        if (neutralButtonId > 0) {
            builder.setNeutralButton(neutralButtonId, listener);
        }
        if (negativeButtonId > 0) {
            builder.setNegativeButton(negativeButtonId, listener);
        }
        builder.setView(view);
        try {
            AlertDialog dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 从view 得到图片
     * 
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    public static void showToast(Context context, CharSequence text,
            boolean longToast) {

        Toast toast = null;
        if (longToast) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);

        }
        else {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }

        toast.show();
    }
}
