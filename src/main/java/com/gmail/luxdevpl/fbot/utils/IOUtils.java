package com.gmail.luxdevpl.fbot.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class IOUtils {

    public static String getContent(String s) {
        String body = null;
        InputStream in = null;

        try {
            URLConnection con = new URL(s).openConnection();

            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            in = con.getInputStream();

            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;

            body = IOUtils.toString(in, encoding);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(in);
        }

        return body;
    }

    private static String toString(InputStream in, String encoding) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int len = 0;

        while ((len = in.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }

        return new String(baos.toByteArray(), encoding);
    }

    private static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
