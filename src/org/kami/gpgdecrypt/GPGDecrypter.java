package org.kami.gpgdecrypt;

import java.io.*;

/**
 * Created by knaeckeKami on 11.04.2014.
 */
public class GPGDecrypter implements Decrypter {

    //assume gpg is in the path
    public static final String DEFAULT_GPG_EXECUTABLE = "gpg";
    private static final String EOF = "\u001A";


    private String executable = null;

    public GPGDecrypter() {
        executable = DEFAULT_GPG_EXECUTABLE;
    }

    public GPGDecrypter(String executable) {
        this.executable = executable;
    }


    public String decrypt(String msg) throws UnsupportedEncodingException, IOException {
        Process p = null;
        BufferedReader in = null;
        BufferedReader error = null;
        Writer out = null;

        boolean isOutClosed = false;

        try {

            //run gpg
            p = Runtime.getRuntime().exec(executable);
            in = new BufferedReader(new InputStreamReader(p.getInputStream(), "US-ASCII"));
            error = new BufferedReader(new InputStreamReader(p.getErrorStream(), "US-ASCII"));

            out = (new OutputStreamWriter(p.getOutputStream(), "US-ASCII"));
            //write message, newline and STRG-Z (EOF) to gpg
            out.write(msg);
            out.write(System.lineSeparator());
            out.write(EOF);
            out.flush();
            //close the stream, in order to be able to read
            out.close();
            isOutClosed = true;

            StringBuilder builder = new StringBuilder();
            //build decrypted output string
            in.lines().forEachOrdered(builder::append);

            String result = builder.toString();
            //if output string is empty, build error string
            if (result.isEmpty()) {
                error.lines().forEachOrdered(builder::append);
                result = builder.toString();
            }
            return result;
        } finally {
            //clean up
            if (out != null && !isOutClosed) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (error != null) {
                error.close();
            }
            if (p != null) {
                p.destroy();
            }
        }
    }


}
