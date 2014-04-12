package org.kami.gpgdecrypt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by knaeckeKami on 11.04.2014.
 */


/**
 * quick, hackish script which decrypts a string in the clipboard using gpg,
 * replacing it with the decrypted string
 * assumes, that an executable named gpg is in the path
 */
public class Main {



    public static void main(String[] args)  {

        //read encrypted message from clipboard
        String input = new TextTransfer().getClipboardContents();
        //decrypt message
        try {
            String decryptedMessage = new GPGDecrypter().decrypt(input);

            //paste result to clipboard and print it to STDOUT
            new TextTransfer().setClipboardContents(decryptedMessage);

            System.out.println(decryptedMessage);
        }catch(Exception e){
            System.err.println("error: "+ e.toString());
        }
    }


}
