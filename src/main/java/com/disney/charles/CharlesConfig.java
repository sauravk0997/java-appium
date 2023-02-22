package com.disney.charles;

import com.disney.exceptions.charles.CharlesConfigException;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class CharlesConfig {

    private Document finalizedDocument = null;

    public CharlesConfig(String filePath, String targetFile) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
            File file = new File(targetFile);
            FileUtils.copyInputStreamToFile(Objects.requireNonNull(inputStream), file);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            if (finalizedDocument == null) {
                finalizedDocument = documentBuilder.parse(file);
                finalizedDocument.getDocumentElement().normalize();
            }
        } catch (Exception e) {
            throw new CharlesConfigException(e);
        }
    }

    public void updateIp(String ip) {
        String[] ipArray = ip.split("\\.");
        NodeList ipElement = finalizedDocument.getElementsByTagName("ip");
        NodeList ipChildren = ipElement.item(0).getChildNodes();
        int counter = 0;
        for (int k = 0; k < ipChildren.getLength(); k++) {
            if ("int".equalsIgnoreCase(ipChildren.item(k).getNodeName())) {
                ipChildren.item(k).setTextContent(ipArray[counter]);
                counter++;
            }
        }
    }

    public void updatePort(int port) {
        NodeList portElement = finalizedDocument.getElementsByTagName("port");
        portElement.item(0).setTextContent(String.valueOf(port));
    }

    public void updateProxyHost(String server) {
        NodeList hostElement = finalizedDocument.getElementsByTagName("host");
        hostElement.item(0).setTextContent(server);
    }

    public void updateUserName(String username) {
        NodeList hostElement = finalizedDocument.getElementsByTagName("username");
        hostElement.item(0).setTextContent(username);
    }

    public void generateConfigFile(String filePath) {
        DOMSource source = new DOMSource(finalizedDocument);
        try {
            FileWriter writer = new FileWriter(filePath);
            StreamResult streamResult = new StreamResult(writer);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            Properties properties = new Properties();
            properties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperties(properties);
            transformer.transform(source, streamResult);
        } catch (Exception e) {
            throw new CharlesConfigException(e);
        }
    }

    public Document getFinalizedDocument() {
        return finalizedDocument;
    }
}