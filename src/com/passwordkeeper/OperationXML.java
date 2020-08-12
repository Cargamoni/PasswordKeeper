package com.passwordkeeper;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class OperationXML {

    // Yeni Oluşturacak
    AlgorithmAES ObjectAES;
    public void NewXMLCreator(String MD5FromPassword, String XMLPath)
    {
        try {
            //XML Dosyasının oluşturulması
            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder Builder = Factory.newDocumentBuilder();
            Document NewXMLDocument = Builder.newDocument();

            // Kök
            Element RootElement = NewXMLDocument.createElement("user");
            NewXMLDocument.appendChild(RootElement);

            // Kökün Belirleyicisi
            Attr RootAttribute = NewXMLDocument.createAttribute("userPass");
            RootAttribute.setValue(MD5FromPassword);
            RootElement.setAttributeNode(RootAttribute);

            // XML dosyasını oluştur.
            TransformerFactory TFacktory = TransformerFactory.newInstance();
            Transformer OptimusPrime = TFacktory.newTransformer();
            DOMSource Source = new DOMSource(NewXMLDocument);
            StreamResult Result = new StreamResult(new File(XMLPath));
            OptimusPrime.transform(Source, Result);

            // Output to console for testing
//            StreamResult consoleResult = new StreamResult(System.out);
//            OptimusPrime.transform(Source, consoleResult);


        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    // XML'den Root userPass içerisiğini döndürür.
    public String PasswordReaderXML (String Path) throws IOException, SAXException, ParserConfigurationException
    {
        Document ReadXMLDocument = ReadFromXML(Path);
        ReadXMLDocument.getDocumentElement().normalize();
        return ReadXMLDocument.getDocumentElement().getAttributeNode("userPass").getValue();
    }

    public Boolean CategoryCheckerXML(String Path) throws IOException, SAXException, ParserConfigurationException
    {
        return ReadFromXML(Path).getDocumentElement().getChildNodes().getLength() > 0;
    }

    /// XML Dosyası İçerisine Category Ekleme İşlemini Yapar.
    public Boolean AddCategoryXML(String Path)
    {
        try
        {
            System.out.print("Set a Category Name : ");
            Scanner CategoryChooser = new Scanner(System.in);
            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
            Document ReadXMLDocument = Factory.newDocumentBuilder().parse(Path);

            Element GetRoot = ReadXMLDocument.getDocumentElement();

            Element CategoryElement = ReadXMLDocument.createElement("userPass");
            GetRoot.appendChild(CategoryElement);

            Attr CategoryAttribute = ReadXMLDocument.createAttribute("category");
            CategoryAttribute.setValue(CategoryChooser.nextLine());
            CategoryElement.setAttributeNode(CategoryAttribute);

            TransformerFactory TFacktory = TransformerFactory.newInstance();
            Transformer OptimusPrime = TFacktory.newTransformer();
            DOMSource Source = new DOMSource(ReadXMLDocument);
            StreamResult Result = new StreamResult(new File(Path));
            OptimusPrime.transform(Source, Result);

            return true;
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean AddPasswordToCategoryXML(String Path, String Category, String ThePassword)
    {
        try
        {
            ObjectAES = new AlgorithmAES(ThePassword);

            System.out.print("Set a Password Name : ");
            Scanner PasswordChooser = new Scanner(System.in);
            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
            Document ReadXMLDocument = Factory.newDocumentBuilder().parse(Path);

            // Root Elementi
            Element GetRoot = ReadXMLDocument.getDocumentElement();

            // Seçilen kategorinin bulunması
            int IndexOfCategory = 0;
            for(int i = 0; i < GetRoot.getElementsByTagName("userPass").getLength(); i++)
            {
                Element FindTheIndex = (Element) GetRoot.getElementsByTagName("userPass").item(i);
                if(FindTheIndex.hasAttribute("category") && FindTheIndex.getAttribute("category").equals(Category))
                    IndexOfCategory = i;
            }
            Element CategoryElement = (Element) GetRoot.getElementsByTagName("userPass").item(IndexOfCategory);
            GetRoot.appendChild(CategoryElement);

            // Element İçerisine Yeni Parolanın Eklenmesi
            Element PasswordElement = ReadXMLDocument.createElement("cipherText");
            CategoryElement.appendChild(PasswordElement);

            Attr CategoryAttribute = ReadXMLDocument.createAttribute("type");
            CategoryAttribute.setValue(PasswordChooser.nextLine());
            System.out.print("Enter a Password : ");
            PasswordElement.appendChild(ReadXMLDocument.createTextNode(ObjectAES.Encryption(PasswordChooser.nextLine())));
            PasswordElement.setAttributeNode(CategoryAttribute);

            TransformerFactory TFacktory = TransformerFactory.newInstance();
            Transformer OptimusPrime = TFacktory.newTransformer();
            DOMSource Source = new DOMSource(ReadXMLDocument);
            StreamResult Result = new StreamResult(new File(Path));
            OptimusPrime.transform(Source, Result);

            return true;
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }

    // XML'den Okuyacak
    public Document ReadFromXML(String Path) throws ParserConfigurationException, IOException, SAXException
    {
        File InputFile = new File(Path);
        DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder Builder = Factory.newDocumentBuilder();
        return Builder.parse(InputFile);
    }

    // XML'e Yazacak
    public void WriteToXML()
    {

    }
}
