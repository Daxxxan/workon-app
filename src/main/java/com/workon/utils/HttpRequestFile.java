package com.workon.utils;

import com.workon.controllers.LoginConnectionController;
import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public class HttpRequestFile {
    private HttpURLConnection httpConn;
    private DataOutputStream request;
    private final String boundary =  "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    public HttpRequestFile(@NoNull String requestURL){
        AnnotationParser.parse(requestURL);
        // creates a unique boundary based on time stamp
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            httpConn = (HttpURLConnection) Objects.requireNonNull(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);

        String value = LoginConnectionController.getUserToken();
        String accessToken = "Bearer ";
        accessToken = accessToken.concat(value);
        httpConn.setRequestProperty("Authorization", accessToken);

        try {
            httpConn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + this.boundary);

        try {
            request =  new DataOutputStream(httpConn.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFilePart(@NoNull String fieldName, @NoNull File uploadFile){
        AnnotationParser.parse(fieldName, uploadFile);
        String fileName = uploadFile.getName();
        try {
            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    fieldName + "\";filename=\"" +
                    fileName + "\"" + this.crlf);
            request.writeBytes(this.crlf);

            byte[] bytes = Files.readAllBytes(uploadFile.toPath());
            request.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        int status = 0;

        try {
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary +
                    this.twoHyphens + this.crlf);

            request.flush();
            request.close();
            // checks server's status code first
            status = httpConn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (status == HttpURLConnection.HTTP_OK) {
            httpConn.disconnect();
        }
    }
}
