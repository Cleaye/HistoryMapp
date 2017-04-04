package com.example.icalvin.historymapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DatabaseInterface {
    private Context Context;
    public Map<String, LatLng> mDataMap;

    public DatabaseInterface(Context context) {
        Context = context;
        mDataMap = new HashMap<>();
    }

    public Map<String, LatLng> getMData() throws ExecutionException, InterruptedException {
        createMarkerURL(1, 100);
        return mDataMap;
    }

    public List<FindingContent.FindingItem> getFindingsFromPlace(String place, int start, int end, List<FindingContent.FindingItem> findings) throws ExecutionException, InterruptedException {
        String urlString = "http://62.221.199.184:5842/action=get&command=search&query=and(Vindplaats="+place+";not(isnull(Image)))&range="+start+"-"+end+"&fields=*";
        Document doc = null;
        try {
            doc = getDoc(urlString);
            List<FindingContent.FindingItem> newFindings = findingsToList(doc);

            if(newFindings.size() > 0) {
                for (FindingContent.FindingItem item : newFindings) {
                    findings.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(doc != null) {
            NodeList countList = doc.getElementsByTagName("count");
            int count = Integer.parseInt(countList.item(0).getFirstChild().getNodeValue());
            if (count > end) {
                return getFindingsFromPlace(place, start + 100, end + 100, findings);
            }
        }
        return findings;
    }

    public List<FindingContent.FindingItem> getFindingsByPeriod(String period, int start, int end, List<FindingContent.FindingItem> findings) throws ExecutionException, InterruptedException {
        String urlString = "http://62.221.199.184:5842/action=get&command=search&query=and(Periode_sort="+period+";not(isnull(Image)))&range="+start+"-"+end+"&fields=*";
        Document doc = null;
        try {
            doc = getDoc(urlString);
            List<FindingContent.FindingItem> newFindings = findingsToList(doc);

            if(newFindings.size() > 0) {
                for (FindingContent.FindingItem item : newFindings) {
                    findings.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(doc != null) {
            NodeList countList = doc.getElementsByTagName("count");
            int count = Integer.parseInt(countList.item(0).getFirstChild().getNodeValue());
            if (count > end) {
                return getFindingsByPeriod(period, start + 100, end + 100, findings);
            }
        }
        return findings;
    }

    private void createMarkerURL(int start, int end) throws ExecutionException, InterruptedException {
        String url = "http://62.221.199.184:5842/action=get&command=search&query=and(not(isnull(Locatie));not(isnull(Image)))&range="+start+"-"+end+"&fields=Locatie";
        Document doc = null;

        try {
            doc = getDoc(url);
        } catch(Exception e) {
            e.printStackTrace();
        }

        locationsToList(doc);

        NodeList countList = doc.getElementsByTagName("count");
        int count = Integer.parseInt(countList.item(0).getFirstChild().getNodeValue());
        if (count > end) {
            createMarkerURL(start + 100, end + 100);
        }
    }

    private Document getDoc(String urlString) throws ExecutionException, InterruptedException, MalformedURLException, URISyntaxException {
        URL url = new URL(urlString);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        Document doc = new RequestXMLTask().execute(url.toString()).get();

        return doc;
    }

    private LatLng getCoordinatesFromPlaces(String place) {
        Geocoder geocoder = new Geocoder(Context, Locale.US);
        List<Address> listOfAddress;

        try {
            listOfAddress = geocoder.getFromLocationName(place, 1);
            if(listOfAddress != null && !listOfAddress.isEmpty()){
                Address address = listOfAddress.get(0);

                double lat = address.getLatitude();
                double lon = address.getLongitude();
                return new LatLng(lat, lon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void locationsToList(Document doc) {
        NodeList locationNodes = doc.getElementsByTagName("Locatie");

        for (int i = 0; i < locationNodes.getLength(); i++) {
            Node location = locationNodes.item(i);
            if (location.getNodeType() == Node.ELEMENT_NODE) {
                String[] fullPlace = location.getFirstChild().getNodeValue().split("\\\\");
                String place = fullPlace[fullPlace.length - 1] + ", " + fullPlace[fullPlace.length - 2];
                if (!mDataMap.containsKey(place)) {
                    LatLng coordinates = getCoordinatesFromPlaces(place);
                    mDataMap.put(place, coordinates);
                }
            }
        }
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

                findings.add(FindingContent.createFindingItem(id, name, description, period, imageURL));
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
                Toast.makeText(Context, "Database connection failed!", Toast.LENGTH_SHORT).show();
            }

            return doc;
        }
    }
}
