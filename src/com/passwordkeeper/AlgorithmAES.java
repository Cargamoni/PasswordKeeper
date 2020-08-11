package com.passwordkeeper;

import java.io.*;
import java.nio.charset.Charset;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AlgorithmAES
{
    // Yapılacaklar
    // Bu Dosya yoksa yeni parola belirlenecek
    // Encyript ve Decyript sadece Parola ile Yapılacak.
    // Encyript edilen dosyaya yazılacak, Decyript edilen Clipboard üzerine kopyalanacak.
    // XML üzerinden okuma yapılacak, parola artık xml'den çekilecek
    // Parola ekleme işlemi yapılacak.

    // Ekstralar
    // Yeni oluşturmada çift parola istenecek


    public static SecretKey KeyAES = null;
    public static final String Algorithm = "AES";
    private static String ThePassword;
    private static String EnteredPassword;
    private static final String KeyFile = "files/secret";
    private static final String PasswordDocsPath = "files/password.xml";
    OperationXML XMLOperations;

    public AlgorithmAES()
    {
        XMLOperations = new OperationXML();
    }

    public AlgorithmAES(String Password)
    {
        XMLOperations = new OperationXML();
        EnteredPassword = Password;
        GenerateKeyAES(Password);
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
    public void MD5PassowrdDocsCreator()
    {
        File PasswordFile;
//        try {
////            PasswordFile = CreateFile(KeyFile);
////            OutputStreamWriter PasswordFileWriter = new OutputStreamWriter(new FileOutputStream(PasswordFile), StandardCharsets.UTF_8);
////            PasswordFileWriter.write(MD5HashGenerator(EnteredPassword));
////            PasswordFileWriter.close();
//        } catch (IOException e) { e.printStackTrace();}
        XMLOperations.NewXMLCreator(MD5HashGenerator(EnteredPassword), PasswordDocsPath);
    }

    public Boolean MD5PasswordChecker()
    {
        String HashedPassword = XMLOperations.PasswordReaderXML(PasswordDocsPath);
        if(HashedPassword.length() > 0 && HashedPassword.contentEquals(MD5HashGenerator(EnteredPassword)))
            return true;
        return false;
    }


    public String ReadData(String FileAddress)
    {
        StringBuilder OutFile = new StringBuilder();
        InputStream InFile;
        try {
            InFile = new FileInputStream(FileAddress);
            BufferedReader Reader = new BufferedReader(new InputStreamReader(InFile));
            String Line;

            while((Line = Reader.readLine()) != null)
                OutFile.append(Line);

            Reader.close();

        } catch (IOException e) { e.printStackTrace(); }
        return OutFile.toString();
    }

    public boolean AreKeysPresent()
    {
        //File DesKey = new File(KeyFile);
        File DesKey = new File(PasswordDocsPath);
        if(DesKey.exists() && DesKey.length() != 0)
            return true;
        return false;
    }

    public SecretKey GenerateKeyAES(String Password)
    {
//        File FileAES;
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
            Decryption(encoded);

            return "Encryption Complete";

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
            System.out.println("Decryped Data : " +ExportData);

            // Bu veri Clipboard'a yazdırılacak
            return ExportData;


        } catch (Exception e) {
            e.printStackTrace();
            return "Error on Decryption";
        }
    }

}
