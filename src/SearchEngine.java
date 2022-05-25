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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchEngine implements ISearchEngine{

    private BTree<String, SearchResult> docTree = new BTree<>(8);

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

            // create list of doc nodes
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

    // maps doc to SearchResult object
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

        // iterate over directory
        File dir = new File(directoryPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try {


                    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

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
        }
    }

    public void deleteWebPage(String filePath){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

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
        List<ISearchResult> results = new ArrayList<>();

        modifiedDFS(word, docTree.getRoot(), results);

        return results;
    }


    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence){
        List<ISearchResult> results = new ArrayList<>();

        modifiedDFS(sentence, docTree.getRoot(), results);

        return results;
    }

    // traverse tree to compare between values
    private void modifiedDFS(String text, IBTreeNode<String, SearchResult> node, List<ISearchResult> results)
    {
        for (SearchResult value : node.getValues()) {

            String[] words = text.split(" ");

            SearchResult temp = new SearchResult();
            temp.setId(value.getId());

            int min_rank = Integer.MAX_VALUE;

            String content = value.content.toLowerCase();

            for (String word : words) {
                if (content.contains(word.toLowerCase())) {
                    int matches = countMatches(content, word.toLowerCase());
                    if (matches < min_rank)
                        min_rank = matches;
                }
                temp.setRank(min_rank);
            }
            if(min_rank != Integer.MAX_VALUE)
                results.add(temp);
        }



        if (!node.isLeaf()) {
            for (IBTreeNode<String,SearchResult> child: node.getChildren())
                modifiedDFS(text, child, results);
        }
    }


    private static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /* Counts how many times the substring appears in the larger string. */
    private static int countMatches(String text, String str)
    {
        if (isEmpty(text) || isEmpty(str)) {
            return 0;
        }

        int index = 0, count = 0;
        while (true)
        {
            index = text.indexOf(str, index);
            if (index != -1)
            {
                count ++;
                index += str.length();
            }
            else {
                break;
            }
        }

        return count;
    }

}
