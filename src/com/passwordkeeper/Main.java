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
        Scanner Pass = new Scanner(System.in);
        while(true)
        {
            if(!ObjectAES.AreKeysPresent())
            {
                System.out.print("There is no password please enter a password : ");
                ObjectAES = new AlgorithmAES(Pass.nextLine());
                ObjectAES.MD5PassowrdDocsCreator();
                System.out.print("New password generated.");
            }
            else
            {
                System.out.print("Please enter your Password : ");
                ObjectAES = new AlgorithmAES(Pass.nextLine());
                if(ObjectAES.MD5PasswordChecker())
                {
                    if(ObjectAES.CategoryChecker)
                    {
                        System.out.println("Added Categories");
                        ObjectAES.CategoryLister();
                        System.out.print("Adding Category 0, Adding Password 1, Read Password 2 (0/1/2) : ");
                        int Selection = Integer.parseInt(Pass.nextLine());
                        if(Selection == 0)
                            ObjectAES.NewCategoryAdder();
                        else if (Selection == 1)
                            ObjectAES.NewPasswordAdder();
                        else if (Selection == 2)
                            ObjectAES.CopyPasswordToClipBoard();
                        else
                            System.out.print("Wrong input !");
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
