package com.xixi.protocol;

import com.xixi.common.Invocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {

    public String send(String hostname, Integer port, Invocation invocation){
        try {

            URL url =new URL("http",hostname,port,"/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);

            oos.writeObject(invocation);
            oos.flush();
            oos.close();

            InputStream inputStream = httpURLConnection.getInputStream();
//            String result = IOUtils.toString(inputStream);
            String result = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
