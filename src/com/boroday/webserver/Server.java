package com.boroday.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final static byte[] LINE_END = "\n".getBytes();
    private String path;
    private int port;

    public void setWebAppPath(String path) {
        this.path = path;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        if (path == null || port == 0) {
            throw new IOException("Port or/and path is not defined");
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     DataOutputStream socketWriter = new DataOutputStream(socket.getOutputStream())) {
                    // изначально хотела использовать BufferedWriter для страниц с html, scc, js и DataOutputStream
                    // для всего остального, но, видимо, я не могу использвать один и тот же сокет
                    // для нескольких OutputStream, так как при такой реализации вторая часть
                    // (для побайтного чтения) не работала. Поэтому сейчас пишу всё побайтно

                    System.out.println("Connection accepted.");
                    String[] strings;
                    while (true) {
                        String lineFromClient = socketBufferedReader.readLine();
                        System.out.println(lineFromClient);
                        strings = lineFromClient.split(" ");
                        if (lineFromClient.isEmpty()) {
                            break;
                        }
                        if (strings[0].equals("GET")) {
                            break;
                        }
                    }

                    if (strings[0].equals("GET")) {
                        byte[] response200Ok = "HTTP/1.1 200 OK".getBytes();
                        socketWriter.write(response200Ok);
                        socketWriter.write(LINE_END);
                        socketWriter.write(LINE_END);

                        try (InputStream inputStream = new FileInputStream(path + strings[1])) {
                            int count;
                            byte[] buffer = new byte[8196];
                            while ((count = inputStream.read(buffer)) != -1) {
                                socketWriter.write(buffer, 0, count);
                            }
                        } catch (FileNotFoundException ex) {
                            System.out.println("File was not sent. File not found by path: " + ex.getMessage());
                        }
                    } else {
                        byte[] response404NotFound = "HTTP/1.1 404 Not Found".getBytes();
                        socketWriter.write(response404NotFound);
                        socketWriter.write(LINE_END);
                        socketWriter.write(LINE_END);
                    }
                }
            }
        }
    }
}