package xml.android.milos.com.faxtons;

import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Milo on 17/11/2016.
 */

public class RssParser {

    //Declare variable
    NodeList nodelist;

    //Link list to get the link to be parsed
    public LinkedList<ItemRss> getXML(String url) {

        LinkedList<ItemRss> list = null;
        try {

            String result = sourceListingString(url);
            list = parseData(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private String sourceListingString(String Url) throws IOException {
        String result = "";

        try {
            //Get the link Url and download the XML file
            URL url = new URL(Url);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Download the XML file
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            //Get root element of the XML file
            //String firstElement = doc.getDocumentElement().getNodeName();

            // Locate the Tag Name
            nodelist = doc.getElementsByTagName("item");

            //Number of properties in XML file
            //int totalProperties = nodelist.getLength();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return StringEscapeUtils.unescapeXml(result.toString());
    }


    //Parse data using appropriate tags
    public LinkedList<ItemRss> parseData(String dataToParse) {
        ItemRss data;
        LinkedList<ItemRss> slist;

        slist = new LinkedList<ItemRss>();
        try {
            for (int i = 0; i < nodelist.getLength(); i++) {
                Node nNode = nodelist.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    // Get title
                    NodeList titleList = eElement.getElementsByTagName("title");
                    data = new ItemRss();
                    Element titleElement = (Element) titleList.item(0);
                    NodeList textTitleList = titleElement.getChildNodes();
                    data.setTitle((textTitleList.item(0)).getNodeValue().trim());


                    // Get description
                    NodeList descriptionList = eElement.getElementsByTagName("description");
                    Element descriptionElement = (Element) descriptionList.item(0);
                    NodeList textDescriptionList = descriptionElement.getChildNodes();
                    data.setDescription((textDescriptionList.item(0)).getNodeValue().trim());

                    // Get date
                    NodeList dateList = eElement.getElementsByTagName("pubDate");
                    Element dateElement = (Element) dateList.item(0);
                    NodeList textDateList = dateElement.getChildNodes();
                    data.setDate((textDateList.item(0)).getNodeValue().trim());

                    // Get georss points
                    NodeList geoPointList = eElement.getElementsByTagName("georss:point");
                    Element pointElement = (Element) geoPointList.item(0);
                    NodeList textGeoPointList = pointElement.getChildNodes();
                    data.setGeoPoint((textGeoPointList.item(0)).getNodeValue().trim());

                    //add all values into linkList
                    slist.add(data);
                }//end of if clause

            }//end of for loop

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return slist;
        //System exit;
    }
}
