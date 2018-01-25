package external;

import entity.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * project: Webtest
 *
 * @author YubaiTao on 23/01/2018.
 */
public class TicketMasterAPI {
    private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private static final String DEFAULT_TERM = ""; // no restriction
    private static final String API_KEY = "X1cX0J4TCLAM1lqTjtnNbbyAd6RzRztm";


    public List<Item> search(double lat, double lon, String term) {
        if (term == null) {
            term = DEFAULT_TERM;
        }

        try {
            term = java.net.URLEncoder.encode(term, "UTF-8"); // " " to "%20"
        } catch (Exception e) {
            e.printStackTrace();
        }

        String geoHash = GeoHash.encodeGeohash(lat, lon, 8);
        String query = String.format("apikey=%s&geoPoint=%s&keyword=%s&radius=%s", API_KEY, geoHash, term, 50);

        try {
            HttpURLConnection connection = (HttpURLConnection)new URL(URL + "?" + query).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("---- Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line = "";
            // read response line by line
            while ((line = in.readLine()) != null) {
                responseBuilder.append(line);
            }
            in.close();

            JSONObject responseJSON = new JSONObject(responseBuilder.toString());
            if (responseJSON.isNull("_embedded")) {
                return new ArrayList<>();
            }
            JSONObject embedded = (JSONObject)responseJSON.get("_embedded");
            JSONArray events = (JSONArray)embedded.get("events");

            return getItemList(events);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ArrayList<>();
    }


    /**
     * Helper methods
     */
    private JSONObject getVenue(JSONObject event) throws JSONException {
        return null;
    }

    private String getImageUrl(JSONObject event) throws JSONException {
        return null;
    }

    private Set<String> getCategories(JSONObject event) throws JSONException {
        return null;
    }

    private double getDistance(JSONObject event) throws JSONException {
        return 0.0;
    }

    // Convert JSONArray to a list of item objects.
    private List<Item> getItemList(JSONArray events) throws JSONException {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);

            Item.ItemBuilder itemBuilder = new Item.ItemBuilder();
            if (!event.isNull("name")) {
                itemBuilder.setName(event.getString("name"));
            }
            if (!event.isNull("id")) {
                itemBuilder.setItemId(event.getString("id"));
            }
            if (!event.isNull("url")) {
                itemBuilder.setUrl(event.getString("url"));
            }
            if (!event.isNull("distance")) {
                itemBuilder.setDistance(event.getDouble("distance"));
            }

            JSONObject venue = getVenue(event);
            itemBuilder.setImageUrl(getImageUrl(event));
            itemBuilder.setCategories(getCategories(event));

            Item item = itemBuilder.build();
            itemList.add(item);
        }
        return itemList;
    }



    private void queryAPI(double lat, double lon) {
//        JSONArray events = search(lat, lon, null);
        List<Item> itemList = search(lat, lon, null);
        try {
//            for (int i = 0; i < events.length(); i++) {
//                JSONObject event = events.getJSONObject(i);
//                System.out.println(event);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
	 * Main entry for sample TicketMaster API requests.
	 */
	public static void main(String[] args) {
		TicketMasterAPI tmApi = new TicketMasterAPI();
		// Mountain View, CA
		// tmApi.queryAPI(37.38, -122.08);
		// Houston, TX
		tmApi.queryAPI(29.682684, -95.295410);
	}



}