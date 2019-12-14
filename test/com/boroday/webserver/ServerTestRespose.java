package com.boroday.webserver;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static junit.framework.TestCase.assertEquals;

public class ServerTestRespose {

    private final static String LINE_END = "\n";

    @Test
    public void checkServerResponse() throws IOException {
        try (Socket socketToServer = new Socket("localhost", 3000);
             BufferedReader bufferedReaderFromServer = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));
             BufferedWriter bufferedWriterToServer = new BufferedWriter(new OutputStreamWriter(socketToServer.getOutputStream()))) {

            bufferedWriterToServer.write("GET /index.html HTTP/1.1");
            bufferedWriterToServer.write(LINE_END);
            bufferedWriterToServer.write(LINE_END);
            bufferedWriterToServer.flush();

            assertEquals("HTTP/1.1 200 OK", bufferedReaderFromServer.readLine());
            assertEquals("", bufferedReaderFromServer.readLine());
            assertEquals("<!DOCTYPE html>", bufferedReaderFromServer.readLine());
        }
    }
}
