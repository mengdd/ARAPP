package com.mengdd.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/**
 * Utility Class for some IO/File related work
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class FileUtils {

    public enum MediaType {
        Image, Video,
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(MediaType mediaType) {
        Log.d(AppConstants.LOG_TAG, "getOutputMediaFile");
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    AppConfig.CAMERA_FILE_DIR);

            Log.d(AppConstants.LOG_TAG,
                    "Successfully created mediaStorageDir: " + mediaStorageDir);

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(AppConstants.LOG_TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.d(AppConstants.LOG_TAG,
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        // create File
        File mediaFile = null;

        switch (mediaType) {
        case Image:
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

            break;

        case Video:
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
            break;
        default:
            break;
        }

        return mediaFile;
    }

    /**
     * Get an InputScrean from a url string which points to a file path
     * 
     * @param urlStr
     * @return
     */
    public static InputStream getInputStream(String urlStr) {
        FileInputStream inputStream = null;

        try {
            if (urlStr.startsWith("file://")) {
                urlStr = urlStr.replace("file://", "");
            }

            inputStream = new FileInputStream(urlStr);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(AppConstants.LOG_TAG, "file not found: " + urlStr);
        }
        return inputStream;

    }

    /**
     * Get a String from a InputScream
     * 
     * @param inputStream
     * @return
     */
    public static String getInputScreamString(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream == null !");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream), 8 * 1024);
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            while (null != (line = reader.readLine())) {
                sb.append(line + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
