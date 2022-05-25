import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class SearchEngine implements ISearchEngine{

    BTree<String, SearchResult> docTree = new BTree<>(8);
    public void indexWebPage(String filePath)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(filePath));

            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("doc");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    SearchResult convertedDoc = convertDoc(element);
                    docTree.insert(convertedDoc.id, convertedDoc);
                }

            }


            } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private SearchResult convertDoc(Element x) {
        SearchResult temp = new SearchResult();

        temp.id = x.getAttribute("id");
        temp.url = x.getAttribute("url");
        temp.title = x.getAttribute("title");
        temp.content = x.getFirstChild().getTextContent();

        return temp;
    }



    public void indexDirectory(String directoryPath){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        File dir = new File(directoryPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try {

                    // optional, but recommended
                    // process XML securely, avoid attacks like XML External Entities (XXE)
                    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

                    // parse XML file
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    Document doc = db.parse(new File(child.toURI()));

                    doc.getDocumentElement().normalize();

                    NodeList list = doc.getElementsByTagName("doc");

                    for (int temp = 0; temp < list.getLength(); temp++) {
                        Node node = list.item(temp);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {

                            Element element = (Element) node;
                            String id = element.getAttribute("id");
                            SearchResult convertedDoc = convertDoc(element);
                            docTree.insert(convertedDoc.id, convertedDoc);
                        }

                    }


                } catch (ParserConfigurationException | IOException | SAXException e) {
                    e.printStackTrace();
                }
            }
        } else {
           return;
        }


    }

    public void deleteWebPage(String filePath){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(filePath));

            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("doc");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    docTree.delete(id);
                }

            }


        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }


    public List<ISearchResult> searchByWordWithRanking(String word){

        return null;
    }


    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence){return null;}

//    private void modifiedDFS(String word, IBTreeNode<String, SearchResult> node, List<SearchResult> results)
//    {
//        for (String key : node.getKeys())
//        {
//            if()
//        }
//
//
//        if (!node.isLeaf()) {
//            for (IBTreeNode<String,SearchResult> child: node.getChildren())
//                modifiedDFS(word, child, results);
//        }
//    }

}
