package com.workon.utils;

import com.workon.controllers.LoginConnectionController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class HttpRequestFile {
    private HttpURLConnection httpConn;
    private DataOutputStream request;
    private final String boundary =  "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    public HttpRequestFile(String requestURL)
            throws IOException {

        // creates a unique boundary based on time stamp
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);

        String value = LoginConnectionController.getUserToken();
        String accessToken = "Bearer ";
        accessToken = accessToken.concat(value);
        httpConn.setRequestProperty("Authorization", accessToken);

        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + this.boundary);

        request =  new DataOutputStream(httpConn.getOutputStream());
    }

    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\";filename=\"" +
                fileName + "\"" + this.crlf);
        request.writeBytes(this.crlf);

        byte[] bytes = Files.readAllBytes(uploadFile.toPath());
        request.write(bytes);
    }

    public void finish() throws IOException {
        request.writeBytes(this.crlf);
        request.writeBytes(this.twoHyphens + this.boundary +
                this.twoHyphens + this.crlf);

        request.flush();
        request.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
    }
}
