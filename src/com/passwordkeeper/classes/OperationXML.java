package com.passwordkeeper.classes;

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
import java.util.ArrayList;
import java.util.List;
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

    // Kaç adet kategori olduğunu alır ve döndürür.
    public boolean CategoryCheckerXML(String Path) throws IOException, SAXException, ParserConfigurationException
    {
        return ReadFromXML(Path).getDocumentElement().getChildNodes().getLength() > 0;
    }

    /// XML içerisindeni Kategorileri Listeler
    public void CategoryListerXML(String Path) throws IOException, SAXException, ParserConfigurationException
    {
        Document ReadXMLDocument = ReadFromXML(Path);
        ReadXMLDocument.getDocumentElement().normalize();
        NodeList CategoryList = ReadXMLDocument.getElementsByTagName("userPass");

        for (int i = 0; i< CategoryList.getLength(); i++)
        {
            Node CategoryNode = CategoryList.item(i);
            if(CategoryNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element CategoryElement = (Element) CategoryNode;
                System.out.println(String.valueOf(i+1) + "." + CategoryElement.getAttribute("category"));
            }
        }
    }

    /// XML içerisindeni Kategorileri dizi olarak döndürür.
    public String[] CategoryListArrayXML(String Path) throws IOException, SAXException, ParserConfigurationException
    {
        Document ReadXMLDocument = ReadFromXML(Path);
        ReadXMLDocument.getDocumentElement().normalize();
        NodeList CategoryList = ReadXMLDocument.getElementsByTagName("userPass");

        List<String> CategoryArrayList = new ArrayList<>();
        for (int i = 0; i< CategoryList.getLength(); i++)
        {
            Node CategoryNode = CategoryList.item(i);
            if(CategoryNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element CategoryElement = (Element) CategoryNode;
                //System.out.println(String.valueOf(i+1) + "." + CategoryElement.getAttribute("category"));
                CategoryArrayList.add(CategoryElement.getAttribute("category"));
            }
        }

        String[] CategoryArray = new String[CategoryArrayList.size()];
        for(int i = 0; i< CategoryArray.length; i++)
            CategoryArray[i] = CategoryArrayList.get(i);

        return CategoryArray;
    }

    /// XML içerisinden Kategori adını geri döndürür.
    public String CategoryReturnerFromXML(String Path, int CategoryID) throws IOException, SAXException, ParserConfigurationException
    {
        Document ReadXMLDocument = ReadFromXML(Path);
        ReadXMLDocument.getDocumentElement().normalize();
        NodeList CategoryList = ReadXMLDocument.getElementsByTagName("userPass");

        for (int i = 0; i< CategoryList.getLength(); i++)
        {
            Node CategoryNode = CategoryList.item(i);
            if(CategoryNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element CategoryElement = (Element) CategoryNode;
                if(CategoryID == i)
                    return CategoryElement.getAttribute("category");
            }
        }
        return "";

    }

    /// XML içerisinden seçilen kategoriye ait olan parola isimlerini getirir.
    public void CategoryPasswordListerXML(String Path, int CategoryID) throws IOException, SAXException, ParserConfigurationException
    {
        Document ReadXMLDocument = ReadFromXML(Path);
        Element GetRoot = ReadXMLDocument.getDocumentElement();

        String CategoryName = CategoryReturnerFromXML(Path, CategoryID);

        NodeList nList = GetRoot.getElementsByTagName("userPass");
        //System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            //Debug
            //System.out.print("\nCurrent Element :");
            //System.out.println(nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                //Debug
                //System.out.print("Attribute : ");
                //System.out.println(eElement.getAttribute("category"));

                if(eElement.getAttribute("category").equals(CategoryName))
                {
                    NodeList carNameList = eElement.getElementsByTagName("cipherText");

                    for (int count = 0; count < carNameList.getLength(); count++) {
                        Node node1 = carNameList.item(count);

                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                            Element car = (Element) node1;

                            // Debug
                            //System.out.print("Pass value : ");
                            //System.out.println(car.getTextContent());
                            System.out.print(String.valueOf(count+1) + ". Pass : ");
                            System.out.println(car.getAttribute("type"));
                        }
                    }
                }
            }
        }

    }

    /// XML içerisinden seçilen kategoriye ait olan parola isimlerini Dizi olarak döndürür.
    public String[] CategoryPasswordArrayXML(String Path, int CategoryID) throws IOException, SAXException, ParserConfigurationException
    {
        Document ReadXMLDocument = ReadFromXML(Path);
        Element GetRoot = ReadXMLDocument.getDocumentElement();

        String CategoryName = CategoryReturnerFromXML(Path, CategoryID);

        List<String> CategoryPasswordList = new ArrayList<String>();

        NodeList nList = GetRoot.getElementsByTagName("userPass");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node nNode = nList.item(temp);

            //Debug
            //System.out.print("\nCurrent Element :");
            //System.out.println(nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                //Debug
                //System.out.print("Attribute : ");
                //System.out.println(eElement.getAttribute("category"));

                if(eElement.getAttribute("category").equals(CategoryName))
                {
                    NodeList carNameList = eElement.getElementsByTagName("cipherText");

                    for (int count = 0; count < carNameList.getLength(); count++) {
                        Node node1 = carNameList.item(count);

                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                            Element car = (Element) node1;

                            // Debug
                            //System.out.print("Pass value : ");
                            //System.out.println(car.getTextContent());
                            //System.out.print(String.valueOf(count+1) + ". Pass : ");
                            //System.out.println(car.getAttribute("type"));
                            CategoryPasswordList.add(car.getAttribute("type"));
                        }
                    }
                }
            }
        }

        String[] PasswordList = new String[CategoryPasswordList.size()];
        for(int i = 0; i< PasswordList.length; i++)
            PasswordList[i] = CategoryPasswordList.get(i);
        return PasswordList;
    }

    public byte[] GetChoosenPasswordXML(String Path, int CategoryID, int TypeID) throws IOException, SAXException, ParserConfigurationException
    {
        Document ReadXMLDocument = ReadFromXML(Path);
        Element GetRoot = ReadXMLDocument.getDocumentElement();

        String CategoryName = CategoryReturnerFromXML(Path, CategoryID);

        NodeList nList = GetRoot.getElementsByTagName("userPass");
        System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            //Debug
            //System.out.print("\nCurrent Element :");
            //System.out.println(nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                //Debug
                //System.out.print("Attribute : ");
                //System.out.println(eElement.getAttribute("category"));

                if(eElement.getAttribute("category").equals(CategoryName))
                {
                    NodeList carNameList = eElement.getElementsByTagName("cipherText");

                    for (int count = 0; count < carNameList.getLength(); count++) {
                        Node node1 = carNameList.item(count);

                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                            Element car = (Element) node1;

                            // Debug
                            //System.out.print("Pass value : ");
                            if(count == TypeID)
                            {
                                byte[] cipher = car.getTextContent().getBytes();
                                return cipher;
                            }
                            //System.out.println(car.getTextContent());
                            //System.out.print(String.valueOf(count) + ". Pass : ");
                            //System.out.println(car.getAttribute("type"));
                        }
                    }
                }
            }
        }
        return null;
    }

    /// XML Dosyası İçerisine Category Ekleme İşlemini Yapar.
    public void AddCategoryXML(String Path, String CategoryName)
    {
        try
        {
            //System.out.print("Set a Category Name : ");
            //Scanner CategoryChooser = new Scanner(System.in);
            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
            Document ReadXMLDocument = Factory.newDocumentBuilder().parse(Path);

            Element GetRoot = ReadXMLDocument.getDocumentElement();

            Element CategoryElement = ReadXMLDocument.createElement("userPass");
            GetRoot.appendChild(CategoryElement);

            Attr CategoryAttribute = ReadXMLDocument.createAttribute("category");
            CategoryAttribute.setValue(CategoryName);
            CategoryElement.setAttributeNode(CategoryAttribute);

            TransformerFactory TFacktory = TransformerFactory.newInstance();
            Transformer OptimusPrime = TFacktory.newTransformer();
            DOMSource Source = new DOMSource(ReadXMLDocument);
            StreamResult Result = new StreamResult(new File(Path));
            OptimusPrime.transform(Source, Result);

            System.out.println("Category Added.");
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            System.out.println("Category Not Added.");
        }
    }

    /// XML Dosyası içerisine Parola ekler.
    public void AddPasswordToCategoryXML(String Path, int CategoryID, String ThePassword, String PasswordName, String NewPassword)
    {
        try
        {
            ObjectAES = new AlgorithmAES(ThePassword);

            //System.out.print("Set a Password Name : ");
            //Scanner PasswordChooser = new Scanner(System.in);
            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
            Document ReadXMLDocument = Factory.newDocumentBuilder().parse(Path);

            String Category = CategoryReturnerFromXML(Path, CategoryID);

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
            CategoryAttribute.setValue(PasswordName);
            //System.out.print("Enter a Password : ");
            PasswordElement.appendChild(ReadXMLDocument.createTextNode(ObjectAES.Encryption(NewPassword)));
            PasswordElement.setAttributeNode(CategoryAttribute);

            TransformerFactory TFacktory = TransformerFactory.newInstance();
            Transformer OptimusPrime = TFacktory.newTransformer();
            DOMSource Source = new DOMSource(ReadXMLDocument);
            StreamResult Result = new StreamResult(new File(Path));
            OptimusPrime.transform(Source, Result);

            System.out.println("Password Added.");
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            System.out.println("Password Not Added.");
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

}
