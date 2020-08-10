package com.passwordkeeper;

public class Main {

    public static void main(String[] args) {
	// write your code here
        AlgorithmAES ObjectAES = new AlgorithmAES();

        String PlainText = ObjectAES.ReadData("files/PlainText.txt");
        System.out.println("Plain Text: " + PlainText);

        if(!ObjectAES.AreKeysPresent())
            ObjectAES.GenerateKeyAES();

        String Result = ObjectAES.Encryption("files/PlainText.txt", "files/CipherText.txt");
        System.out.println(Result);

        Result = ObjectAES.Decryption("files/CipherText.txt", "files/PlainText.txt");
        System.out.println(Result);
    }
}
