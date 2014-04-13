GPGDecrypter
============

A short utility to decrypt PGP messages in the clipboard. Useful for two factor PGP authentification.

This tool assumes that you already have installed GPG and have an executable named gpg in your PATH.

It just reads the content of your clipboard, attempts to decrypt it with gpg (you may need to enter a passphrase), and writes the decrypted message to your clipboard.

Needs Java 8.

