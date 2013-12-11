package com.mengdd.data.json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class JsonUtils {
    public static String createJsonString(String key, Object value) {
        String jsonString = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, value);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject.toString();

        return jsonString;

    }

    public String getJsonFromLocal(Context context, String file)
            throws Exception {
        int len;
        byte buf[] = new byte[1024];
        InputStream inputStream = context.getAssets().open(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
        }
        inputStream.close();
        outputStream.close();
        String string = outputStream.toString();
        return string;
    }

    public String getJsonFromNetwork(String url) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        String string = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            HttpEntity httpEntity = httpResponse.getEntity();
            string = EntityUtils.toString(httpEntity, "UTF8");

        } else {

            // TODO
        }
        return string;
    }

    public void save2SDcardFile(String path, String content) throws IOException {
        File outputFile = new File(path);
        if (!outputFile.exists()) {
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            outputFile.createNewFile();
        }

        stringToFile(path, content);
    }

    /**
     * Writes string to file. Basically same as "echo -n $string > $filename"
     *
     * @param filename
     * @param string
     * @throws IOException
     */
    public static void stringToFile(String filename, String string)
            throws IOException {
        FileWriter out = new FileWriter(filename);
        try {
            out.write(string);
        }
        finally {
            out.close();
        }
    }
}
