
import java.io.*;

import java.net.*;


public class WIMI {
    public final static String WIP_SITE = "http://whatsmyip.net";
    private final static String REPLACE_PATTERN = "(?s).*?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*";

    // For demo
    public static void main(String[] args) {
        System.out.println(WIMI.whatIsMyIp());
    }

    public static String whatIsMyIp() {
        String result = null;
        InputStream in = null;

        try {
            URLConnection conn = new URL(WIP_SITE).openConnection();
	    System.out.println("Does it work?");
            //int length = Integer.valueOf(conn.getHeaderField("Content-Length"));
	    int length = 1000;
            byte[] buf = new byte[length];
            in = conn.getInputStream();
            in.read(buf);
            result = new String(buf);
            result = result.replaceAll(REPLACE_PATTERN, "$1");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) { /* ignore */
                }
            }
        }
        return result;
    }
}
