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

public class OperationXML {

    // Yeni Oluşturacak
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
            StreamResult consoleResult = new StreamResult(System.out);
            OptimusPrime.transform(Source, consoleResult);


        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public String PasswordReaderXML (String Path)
    {
        File InputFile = new File(Path);
        {
            try {
                DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder Builder = Factory.newDocumentBuilder();
                Document ReadXMLDocument = Builder.parse(InputFile);
                ReadXMLDocument.getDocumentElement().normalize();
                return ReadXMLDocument.getDocumentElement().getAttributeNode("userPass").getValue();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    // XML'den Okuyacak
    public void ReadFromXML()
    {

    }

    // XML'e Yazacak
    public void WriteToXML()
    {

    }
}
