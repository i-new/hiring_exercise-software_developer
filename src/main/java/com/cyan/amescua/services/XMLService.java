package com.cyan.amescua.services;

import com.cyan.amescua.model.Feed;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This service will be used to get xml's API URLs and parse them into a Feed object
 */
@Service
public class XMLService {

    /**
     * This method gets a url for XML feeds and retrieve a list of Feed objects from those XML data.
     * @return List<Feed>
     */
    public static List<Feed> parseFeeds(String URL) {

        List<Feed> feeds = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(URL);

            // normalize XML Response
            doc.getDocumentElement().normalize();

            // Retrieve all the feeds
            NodeList nodeList = doc.getElementsByTagName("item");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    feeds.add(
                            new Feed(elem.getElementsByTagName("title").item(0).getTextContent(),
                            elem.getElementsByTagName("link").item(0).getTextContent(),
                            elem.getElementsByTagName("pubDate").item(0).getTextContent().toString(),
                            elem.getElementsByTagName("description").item(0).getTextContent())
                    );
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return feeds;
    }


}
