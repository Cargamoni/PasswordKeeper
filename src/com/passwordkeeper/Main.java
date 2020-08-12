package com.passwordkeeper;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
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
                    if(ObjectAES.CategoryChecker)
                    {
//                        System.out.print("Enter a Plain Text : ");
//                        Pass = new Scanner(System.in);
//                        String Result = ObjectAES.Encryption(Pass.nextLine());
//                        System.out.println(Result);

                        ///Buraya Kategori Listeleyici ve Seçici gelecek

                        ObjectAES.NewPasswordAdder();
                    }
                    else
                    {
                        System.out.println("Category Not Found, Add a new One !");
                        ObjectAES.NewCategoryAdder();
                    }

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
