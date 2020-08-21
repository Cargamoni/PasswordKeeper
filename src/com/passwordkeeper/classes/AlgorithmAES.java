package com.passwordkeeper.classes;

import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.ParserConfigurationException;

public class AlgorithmAES
{
    // Yapılacaklar
    // Parola silme, güncelleme yapılacak
    // Kategori silme, güncelleme yapılacak
    public static SecretKey KeyAES = null;
    public static final String Algorithm = "AES";
    private static String EnteredPassword;
    private static final String PasswordDocsPath = "files/password.xml";

    public Boolean CategoryChecker;
    OperationXML XMLOperations;


    public AlgorithmAES()
    {
        XMLOperations = new OperationXML();
    }

    public AlgorithmAES(String Password) throws ParserConfigurationException, SAXException, IOException
    {
        XMLOperations = new OperationXML();
        EnteredPassword = Password;
        GenerateKeyAES(Password);

        CategoryChecker = XMLOperations.CategoryCheckerXML(PasswordDocsPath);
    }

    /// XML içerisine Kategori ekleme işlemini yapan Kod burası.
    public void NewCategoryAdder(String CategoryName)
    {
        XMLOperations.AddCategoryXML(PasswordDocsPath, CategoryName);
//        System.out.println("Category Created. 1 for add new one, 2 for add password, 3 for exit.");
//        Scanner Motion = new Scanner(System.in);
//        char MotionChar = Motion.nextLine().toCharArray()[0];
//        if(MotionChar == '1')
//            NewCategoryAdder();
//        else if (MotionChar == '2')
//            NewPasswordAdder();
//        else if (MotionChar == '3')
//            return;
//        else
//            System.out.println("Wrong Input");
    }

    /// XML içerisine kategoriye ait yeni parola ekleme işlemini gerçekleştirir.
    public void NewPasswordAdder(int CategoryID, String PasswordName, String NewPassword)
    {
//        Scanner Motion = new Scanner(System.in);
//        System.out.print("Which Category : ");
//        String CategoryName = Motion.nextLine();
        XMLOperations.AddPasswordToCategoryXML(PasswordDocsPath, CategoryID, EnteredPassword, PasswordName, NewPassword);
//        System.out.println("Category Created. 1 for add new one, 2 for add password, 3 for exit.");
//        char MotionChar = Motion.nextLine().toCharArray()[0];
//        if(MotionChar == '1')
//            NewCategoryAdder();
//        else if (MotionChar == '2')
//            NewPasswordAdder();
//        else if (MotionChar == '3')
//            return;
//        else
//            System.out.println("Wrong Input");
    }

    /// XML içerisinden Kategorileri listeler.
    public void CategoryLister() throws ParserConfigurationException, SAXException, IOException
    {
        XMLOperations.CategoryListerXML(PasswordDocsPath);
    }

    /// XML içerisinden Kategorileri listeler.
    public String[] CategoryListReturner() throws ParserConfigurationException, SAXException, IOException
    {
        return XMLOperations.CategoryListArrayXML(PasswordDocsPath);
    }

    public String[] CategoryPasswordsReturner(int CategoryID) throws ParserConfigurationException, SAXException, IOException
    {
        return XMLOperations.CategoryPasswordArrayXML(PasswordDocsPath, CategoryID);
    }

    public String GetCategoryNameFromID(int CategoryID) throws ParserConfigurationException, SAXException, IOException {
        return XMLOperations.CategoryReturnerFromXML(PasswordDocsPath, CategoryID);
    }

    public void CopyPasswordToClipBoard(int CategoryID, int TypeID) throws IOException, SAXException, ParserConfigurationException
    {
        //System.out.println("Welcome to Reader");
        //CategoryLister();
        //Scanner Input = new Scanner(System.in);
        //System.out.print("Please Select a Category (Just Number): ");
        //int CategoryID = Integer.parseInt(Input.nextLine());

        //XMLOperations.CategoryPasswordListerXML(PasswordDocsPath, CategoryID);
        //System.out.print("Wich Password do you want to Copy (Just Number) : ");
        //int TypeID = Integer.parseInt(Input.nextLine());
        byte[] CipherTextFromXML = XMLOperations.GetChoosenPasswordXML(PasswordDocsPath, CategoryID, TypeID);
        String YourPasswordIs = Decryption(CipherTextFromXML);

        StringSelection SelectPassword = new StringSelection(YourPasswordIs);
        Clipboard ClipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        ClipBoard.setContents(SelectPassword,SelectPassword);
        //System.out.println("Your password copied to clipboard. Press Ctrl+V for paste !");
    }

    /// Parolayı MD5'e dönüştürmek için kullanılır.
    public String MD5HashGenerator(String Text)
    {
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(Text.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            //System.out.println("Your Password was : " + Password + " \n Now : " + ThePassword);
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /// Eğer parola daha önce oluşturulmadıysa bu fonksiyon dosyayı oluşturur
    // parolanın hashlenmiş halini dosya içerisine yazdırır. Burası XML'e
    // yazdırılacak şekilde düzenlenecek !!!
    public void MD5PassowrdDocsCreator(String ThePassword)
    {
        String FolderName = "files";
        if(Files.notExists(Paths.get(FolderName)))
            new File(FolderName).mkdir();
        XMLOperations.NewXMLCreator(MD5HashGenerator(ThePassword), PasswordDocsPath);
    }

    /// Parolayı XML üzerinden kontrol eder ve sonucu döndürür.
    public Boolean MD5PasswordChecker() throws ParserConfigurationException, SAXException, IOException
    {
        String HashedPassword = XMLOperations.PasswordReaderXML(PasswordDocsPath);
        if(HashedPassword.length() > 0 && HashedPassword.contentEquals(MD5HashGenerator(EnteredPassword)))
            return true;
        return false;
    }

    /// Parolaların tutulacağı dosyanın varlığını kontrol eder.
    public boolean AreKeysPresent()
    {
        File Document = new File(PasswordDocsPath);
        if(Document.exists() && Document.length() != 0)
            return true;
        return false;
    }

    /// Parolaya göre PrivateKey oluşturur. Encryption için
    // tekrar tekrar kullanılacağı için, seed aktif değildir.
    // aktif edilirse sürekli farklı key alınacağı için
    // parola Decryption işleminde kullanılamaz.
    public SecretKey GenerateKeyAES(String Password)
    {
        KeyGenerator KeyMaker;
        SecureRandom seed = new SecureRandom();
        KeySpec spec;
        //byte [] salt = seed.generateSeed(64);
        byte [] salt = {1};
        //String password = "Asshole";
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            //spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            spec = new PBEKeySpec(Password.toCharArray(), salt, 1, 256);
            SecretKey tmp = factory.generateSecret(spec);
            KeyAES = new SecretKeySpec(tmp.getEncoded(), Algorithm);

            return KeyAES;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) { e.printStackTrace();}
        return null;
    }

    /// Aldığı veriyi AES Algoritmasını kullanarak şifreleyip, Base64 ile Encode işleminden geçirip
    // daha sonra XML'e göndermek için kullanılacak. Şifrelemeyi sadece parolalar üzerinde yapacaktır.
    // Şifrelemede oluşturulan Key paroladan elde edilir, bu parola kaybedilirse geri döndürme
    // gerçekleştirilemez.
    public String Encryption (String PlainText)
    {
        try {
            Cipher CipherAES = Cipher.getInstance(Algorithm);
            CipherAES.init(Cipher.ENCRYPT_MODE,KeyAES);

            final MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] data;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 DigestOutputStream dos = new DigestOutputStream(bos, digest);
                 CipherOutputStream cos = new CipherOutputStream(dos, CipherAES)) {
                try (ObjectOutputStream oos = new ObjectOutputStream(cos)) { oos.writeObject(PlainText); }
                data = bos.toByteArray();
            }

            //Debug
            //System.out.println(Arrays.toString(data));
            //System.out.println(new String(encoded));

            //Burası XML'e yazdırılacak.
            byte[] encoded = Base64.getEncoder().encode(data);
            return new String(encoded);
            //Decryption(encoded);

            //return "Encryption Complete";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error on Encryption";
        }
    }

    /// XML üzerinden okunan veriyi Decryption işlemine sokup, parolayı açığa çıkarmak için kullanılır.
    // Okunan veri daha sonra clipboard'a kopyalanmak için kullanılacak.
    public String Decryption (byte[] CipherAsBase64)
    {
        try {

            Cipher CipherAES = Cipher.getInstance(Algorithm);
            CipherAES.init(Cipher.DECRYPT_MODE, KeyAES);
            byte[] EncodedKey = KeyAES.getEncoded();
            byte[] DecodedCipher = Base64.getDecoder().decode(CipherAsBase64);
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");

            SecretKeySpec scs = new SecretKeySpec(EncodedKey, Algorithm);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CipherOutputStream CipherOut = new CipherOutputStream(baos, CipherAES);
            CipherOut.write(DecodedCipher);
            CipherOut.close();
            baos.close();

            byte[] Decrypted = baos.toByteArray();

            //Debug
//            System.out.println(Arrays.toString(Decrypted));
//            System.out.println("Decryption : " + new String(Decrypted));

            String ExportData = new String(Decrypted, 7, Decrypted.length -7, "UTF-8");

            //Debug
            //System.out.println("Decryped Data : " +ExportData);

            return ExportData;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error on Decryption";
        }
    }

}
