package org.kami.gpgdecrypt;

/**
 * Created by knaeckeKami on 11.04.2014.
 */


import java.io.File;

/**
 * quick, hackish script which decrypts a string in the clipboard using gpg,
 * replacing it with the decrypted string
 * assumes, that an executable named gpg is in the path
 */
public class Main {

    private static final String SHORT_COMMANDLINE_CUSTOM_EXECUTABLE_PATH = "-p";
    private static final String LONG_COMMANDLINE_CUSTOM_EXECUTABLE_PATH = "--path-gpg";

    private static final String HELP = "Decrypts the pgp encrypted text in the clipboard using gpg. If gpg is not in the PATH, then the argument -p or --path-gpg must be used to set the path to the gpg executable.";
    private static String gpgExecutablePath = null;


    public static void main(String[] args) {

        if (!parseCommandLineArguments(args)) {
            printHelp();
            return;
        }

        //read encrypted message from clipboard
        String input = new TextTransfer().getClipboardContents();
        //decrypt message
        try {

            String decryptedMessage = new GPGDecrypter(gpgExecutablePath).decrypt(input);

            //paste result to clipboard and print it to STDOUT
            new TextTransfer().setClipboardContents(decryptedMessage);

            System.out.println(decryptedMessage);
        } catch (Exception e) {
            System.err.println("error: " + e.toString());
            printHelp();
        }

    }

    private static void printHelp() {
        System.out.println(HELP);
    }

    private static boolean parseCommandLineArguments(String[] args) {
        //parse CL arguments
        try {
            if (args.length >= 2) {
                for (int i = 0; i < args.length; i++) {
                    switch (args[i]) {
                        case SHORT_COMMANDLINE_CUSTOM_EXECUTABLE_PATH:
                        case LONG_COMMANDLINE_CUSTOM_EXECUTABLE_PATH:
                            gpgExecutablePath = args[++i];
                            gpgExecutablePath = new File(gpgExecutablePath).getAbsolutePath();
                            break;
                        default:
                            return false;
                    }
                }
            }

        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        if (gpgExecutablePath == null) {
            gpgExecutablePath = GPGDecrypter.DEFAULT_GPG_EXECUTABLE;
        }

        return true;
    }
}
