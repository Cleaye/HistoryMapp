package com.example.icalvin.historymapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DatabaseInterface {
    private Context context;

    public DatabaseInterface(Context context) {
        this.context = context;
    }

    public Map<String, LatLng> getLocations() throws IOException {
        String url = "http://rosegarden.eu/getLocation.php";
        Document doc = null;

        try {
            doc = getDoc(url);
        } catch(Exception e) {
            e.printStackTrace();
        }

        Map<String, LatLng> map = locationsToList(doc);

        return map;
    }

    public List<FindingContent.FindingItem> getFindingsFromPlace(String place) throws ExecutionException, InterruptedException {
        List<FindingContent.FindingItem> findings = new ArrayList<>();
        String urlString = "http://rosegarden.eu/specificLocation.php?location=" + place.trim();
        Document doc = null;

        try {
            doc = getDoc(urlString);
            findings = resultsToList(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return findings;
    }

    public List<FindingContent.FindingItem> getFindingsByPeriod(String period) throws ExecutionException, InterruptedException {
        List<FindingContent.FindingItem> findings = new ArrayList<>();
        String urlString = "http://rosegarden.eu/specificPeriod.php?period=" + period.trim();
        Document doc = null;
        try {
            doc = getDoc(urlString);
            findings = resultsToList(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return findings;
    }

    public List<FindingContent.FindingItem> getFindingsBySearch(String query) {
        List<FindingContent.FindingItem> findings = new ArrayList<>();
        String urlString = "http://rosegarden.eu/specificObject.php?object=" + query.trim();
        Document doc = null;

        try {
            doc = getDoc(urlString);
            findings = resultsToList(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return findings;
    }

    private Document getDoc(String urlString) throws ExecutionException, InterruptedException, MalformedURLException, URISyntaxException {
        URL url = new URL(urlString);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        Document doc = new RequestXMLTask().execute(url.toString()).get();

        return doc;
    }

    private Map<String, LatLng> locationsToList(Document doc) {
        Map<String, LatLng> map = new HashMap<>();
        NodeList locationNodes = doc.getElementsByTagName("Location");

        for (int i = 0; i < locationNodes.getLength(); i++) {
            Node locations = locationNodes.item(i);
            if (locations.getNodeType() == Node.ELEMENT_NODE) {
                NodeList attributes = locations.getChildNodes();
                String name = null;
                LatLng coordinate = null;

                for(int j = 0; j < attributes.getLength(); j++) {
                    String attributeName = null;
                    try {
                        attributeName = attributes.item(j).getNodeName();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    if (attributeName != null) {
                        switch (attributeName) {
                            case "place-name":
                                name = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "latlong":
                                String[] coordinateString = attributes.item(j).getFirstChild().getNodeValue().split(",");
                                coordinate = new LatLng(Double.parseDouble(coordinateString[0]), Double.parseDouble(coordinateString[1]));
                                break;
                        }
                    }
                }
                map.put(name, coordinate);
            }
        }

        return map;
    }

    private List<FindingContent.FindingItem> resultsToList(Document doc) {
        NodeList findingNodes = doc.getElementsByTagName("Object");
        List<FindingContent.FindingItem> findings = new ArrayList<>();

        for (int i = 0; i < findingNodes.getLength(); i++) {
            Node finding = findingNodes.item(i);
            if (finding.getNodeType() == Node.ELEMENT_NODE) {
                NodeList attributes = finding.getChildNodes();
                String id = null;
                String name = null;
                String description = null;
                String period = null;
                String imageURL = null;
                String coordinate = null;
                String finder = null;

                for (int j = 0; j < attributes.getLength(); j++) {
                    String attributeName = null;
                    try {
                        attributeName = attributes.item(j).getNodeName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (attributeName != null && attributes.item(j).hasChildNodes()) {
                        switch (attributeName) {
                            case "id":
                                id = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "description":
                                name = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "period":
                                period = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "image":
                                imageURL = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "coordinate":
                                coordinate = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "vinder":
                                finder = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                        }
                    }
                }
                findings.add(FindingContent.createFindingItem(id, name, description, period, imageURL, coordinate, finder));
            }
        }

        return findings;
    }

    private List<FindingContent.FindingItem> findingsToList(Document doc) {
        NodeList findingNodes = doc.getElementsByTagName("PZHoai");
        List<FindingContent.FindingItem> findings = new ArrayList<>();

        for (int i = 0; i < findingNodes.getLength(); i++) {
            Node finding = findingNodes.item(i);
            if (finding.getNodeType() == Node.ELEMENT_NODE) {
                NodeList attributes = finding.getChildNodes();
                String id = null;
                String name = null;
                String description = null ;
                String period = null;
                String imageURL = null;

                for(int j = 0; j < attributes.getLength(); j++) {
                    String attributeName = null;
                    try {
                        attributeName = attributes.item(j).getNodeName();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    if (attributeName != null) {
                        switch (attributeName) {
                            case "ID":
                                id = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "Description":
                                name = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "Periode":
                                period = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                            case "Image":
                                imageURL = attributes.item(j).getFirstChild().getNodeValue();
                                break;
                        }
                    }
                }
                //TODO: Add coordinates en finder
                findings.add(FindingContent.createFindingItem(id, name, description, period, imageURL, null, null));
            }
        }

        return findings;
    }

    class RequestXMLTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... url) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document doc = null;
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new URL(url[0]).openStream());
            }
            catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Connectie mislukt...", Toast.LENGTH_SHORT).show();
            }

            return doc;
        }
    }
}
