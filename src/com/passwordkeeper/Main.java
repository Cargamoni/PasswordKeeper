package com.passwordkeeper;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String EnteredPassword;
        Scanner Pass = new Scanner(System.in);
        AlgorithmAES ObjectAES = new AlgorithmAES(Pass.nextLine());

        String PlainText = ObjectAES.ReadData("files/PlainText.txt");
        System.out.println("Plain Text: " + PlainText);

//        if(!ObjectAES.AreKeysPresent())
//            ObjectAES.GenerateKeyAES();

        String Result = ObjectAES.Encryption("files/PlainText.txt", "files/CipherText.txt");
        System.out.println(Result);

        Result = ObjectAES.Decryption("files/CipherText.txt", "files/PlainText.txt");
        System.out.println(Result);
    }
}
