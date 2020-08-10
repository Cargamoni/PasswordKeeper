package com.passwordkeeper;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
            Attr RootAttribute = NewXMLDocument.createAttribute("userpass");
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

    // XML'den Okuyacak
    public void ReadFromXML()
    {

    }

    // XML'e Yazacak
    public void WriteToXML()
    {

    }
}
