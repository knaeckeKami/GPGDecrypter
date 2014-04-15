package org.kami.gpgdecrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public String decrypt(String msg) throws IOException {
        Process gpgProcess = null;
        InputStream in = null;
        InputStream error = null;
        Writer out = null;

        boolean isOutClosed = false;

        try {

            //run gpg process
            gpgProcess = Runtime.getRuntime().exec(executable);
            //get in, out and error stream from gpg process
            in = gpgProcess.getInputStream();
            error = gpgProcess.getErrorStream();
            out = (new OutputStreamWriter(gpgProcess.getOutputStream(), "US-ASCII"));
            //write message, newline and STRG-Z (EOF) to gpg
            out.write(msg);
            out.write(System.lineSeparator());
            out.write(EOF);
            out.flush();
            //close the stream, in order to be able to read
            out.close();
            isOutClosed = true;


            //build decrypted output string
            String result = convertStreamToString(in);
            //if output string is empty, build error string
            if (result.isEmpty()) {

                result = convertStreamToString(error);
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
            if (gpgProcess != null) {
                gpgProcess.destroy();
            }
        }
    }


}
