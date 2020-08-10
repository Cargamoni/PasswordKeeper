package com.passwordkeeper;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AlgorithmAES
{
    public static SecretKey KeyAES = null;
    public static final String Algorithm = "AES";
    public static final String KeyFile = "files/secret.key";

    public File CreateFile(String FileName)
    {
        File ReadFile = new File(FileName);
        try {
            if (ReadFile.getParentFile() != null) {
                ReadFile.getParentFile().mkdir();
                ReadFile.createNewFile();
            }
        }catch (IOException e) { e.printStackTrace(); }

        return ReadFile;
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
        File DesKey = new File(KeyFile);
        if(DesKey.exists())
            return true;
        return false;
    }

    public void GenerateKeyAES()
    {
        File FileAES;
        KeyGenerator KeyMaker;
        SecureRandom seed = new SecureRandom();
        KeySpec spec;
        //byte [] salt = seed.generateSeed(64);
        byte [] salt = {1};
        String password = "Asshole";
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            //spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            spec = new PBEKeySpec(password.toCharArray(), salt, 1, 256);
            SecretKey tmp = factory.generateSecret(spec);
            KeyAES = new SecretKeySpec(tmp.getEncoded(), Algorithm);

//            KeyMaker = KeyGenerator.getInstance(Algorithm);
//            KeyMaker.init(256);
//            KeyAES = KeyMaker.generateKey();

            FileAES = CreateFile(KeyFile);
            ObjectOutputStream SecretKeyOut = new ObjectOutputStream(new FileOutputStream(FileAES));
            SecretKeyOut.writeObject(KeyAES);
            SecretKeyOut.close();

        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) { e.printStackTrace();}
    }

    public String Encryption (String PlainText, String CipherText)
    {
        try {
            ObjectInputStream InputStream = new ObjectInputStream(new FileInputStream(KeyFile));
            final SecretKey KeyAES = (SecretKey) InputStream.readObject();
            InputStream.close();

            Cipher CipherAES = Cipher.getInstance(Algorithm);
            CipherAES.init(Cipher.ENCRYPT_MODE,KeyAES);

            FileInputStream IStreamFile = new FileInputStream(PlainText);
            FileOutputStream OStreamFile = new FileOutputStream(CipherText);
            CipherOutputStream CStreamFile = new CipherOutputStream(OStreamFile, CipherAES);

            byte[] block = new byte[64];
            int i;
            while((i = IStreamFile.read(block)) != -1)
                CStreamFile.write(block,0,i);

            CStreamFile.close();
            IStreamFile.close();
            CStreamFile.close();

            return "Encryption Complete";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error on Encryption";
        }
    }

    public String Decryption (String CipherText, String PlainText)
    {
        try {
            ObjectInputStream InputStream = new ObjectInputStream(new FileInputStream(KeyFile));
            final SecretKey KeyAES = (SecretKey)InputStream.readObject();
            InputStream.close();

            Cipher CipherAES = Cipher.getInstance(Algorithm);
            CipherAES.init(Cipher.DECRYPT_MODE, KeyAES);

            FileInputStream IStreamFile = new FileInputStream(CipherText);
            FileOutputStream OStreamFile = new FileOutputStream(PlainText);
            CipherInputStream CStreamFile = new CipherInputStream(IStreamFile,CipherAES);

            byte[] block = new byte[64];
            int i;

            while((i = CStreamFile.read(block)) != -1)
                OStreamFile.write(block,0,i);

            CStreamFile.close();
            IStreamFile.close();
            CStreamFile.close();

            String PlainTextAgain = ReadData(PlainText);
            return "Decryption = " + PlainTextAgain;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error on Decryption";
        }
    }

}
