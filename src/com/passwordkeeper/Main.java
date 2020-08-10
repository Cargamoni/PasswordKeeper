package com.passwordkeeper;

import javax.swing.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        AlgorithmAES ObjectAES = new AlgorithmAES();
        Scanner Pass;
        while(true)
        {
            if(!ObjectAES.AreKeysPresent())
            {
                System.out.print("There is no password please enter a password : ");
                Pass = new Scanner(System.in);
                ObjectAES = new AlgorithmAES(Pass.nextLine());
                ObjectAES.MD5PassowrdDocsCreator();
                System.out.print("New password generated.");
            }
            else
            {
                System.out.print("Please enter your Password : ");
                Pass = new Scanner(System.in);
                ObjectAES = new AlgorithmAES(Pass.nextLine());
                if(ObjectAES.MD5PasswordChecker())
                {

                    //String PlainText = ObjectAES.ReadData("files/PlainText.txt");
                    //System.out.println("Plain Text: " + PlainText);

                    System.out.print("Enter a Plain Text : ");
                    Pass = new Scanner(System.in);
                    String Result = ObjectAES.Encryption(Pass.nextLine());
                    System.out.println(Result);

                    //Result = ObjectAES.Decryption("files/CipherText.txt");
                    //System.out.println(Result);
                }
                else
                {
                    System.out.print("You entered wrong password !");
                    return;
                }
            }
        }
    }
}
