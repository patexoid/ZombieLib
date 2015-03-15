package lib.back.fileParser;

import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 15.03.2015.
 */
public class Fb2FileParser {


    private DocumentBuilder builder;
    private XPath xpath;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");

    public Fb2FileParser() throws ParserConfigurationException {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        xpath = XPathFactory.newInstance().newXPath();
    }

    public Fb2BookDescription parseFile(InputStream file) throws IOException, SAXException, XPathExpressionException, ParseException {
        Document document = builder.parse(file);
        Node description = (Node) xpath.evaluate("/FictionBook/description/title-info", document, XPathConstants.NODE);
        NodeList genreNodeList = (NodeList) xpath.evaluate("genre", description,XPathConstants.NODESET);
        List<String> genres=new ArrayList<>();
        for(int i=0;i<genreNodeList.getLength();i++){
            genres.add(genreNodeList.item(i).getTextContent());
        }

        Node author = (Node) xpath.evaluate("author", description, XPathConstants.NODE);
        String firstName = (String) xpath.evaluate("first-name", author, XPathConstants.STRING);
        String middleName = (String) xpath.evaluate("middle-name", author, XPathConstants.STRING);
        String lastName= (String) xpath.evaluate("last-name", author, XPathConstants.STRING);
        String homePage = (String) xpath.evaluate("home-page", author, XPathConstants.STRING);
        String email = (String) xpath.evaluate("email", author, XPathConstants.STRING);

        String title = (String) xpath.evaluate("book-title", description, XPathConstants.STRING);
        String annotation = (String) xpath.evaluate("annotation", description, XPathConstants.STRING);
        String dateString = (String) xpath.evaluate("date/@value", description, XPathConstants.STRING);


        Date date = simpleDateFormat.parse(dateString);
        return new Fb2BookDescription(genres, new Fb2Author(firstName, middleName, lastName, homePage, email), title, annotation, date, annotation);
    }
}
