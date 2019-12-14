package com.boroday.webserver;

import org.junit.Test;

import java.io.*;

public class ServerTest {

    @Test
    public void startServer() throws IOException {
        Server server = new Server();
        server.setWebAppPath("resources/webapp");
        server.setPort(3000);
        server.start();
    }
}
