package com.example.android.newsapp.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class parses generic Atom feeds.
 */
public class FeedParser {

    /**
     * Parse an Atom feed, returning a collection of Entry objects.
     *
     * @param in Atom feed, as a stream.
     * @return List of {@link com.example.android.newsapp.net.FeedParser.Entry} objects.
     * @throws java.io.IOException on I/O error.
     */
    public List<Entry> parse(InputStream in) {

        List<Entry> entryList = new ArrayList<>();

        String serverResponse;

        try {
            serverResponse = readIt(in);
            JSONObject jsonObject = new JSONObject(serverResponse);

            JSONObject response = jsonObject.getJSONObject("response");

            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject entry = results.getJSONObject(i);

                String dateString = entry.getString("webPublicationDate");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                Date date = simpleDateFormat.parse(dateString);
                entryList.add(new Entry(Integer.toString(i), entry.getString("webTitle"), entry.getString("webUrl"), date.getTime()));

            }

        } catch (JSONException | IOException | ParseException e) {
            e.printStackTrace();
        }

        return entryList;
    }


    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream InputStream containing HTML from targeted site.
     * @return String concatenated according to len parameter.
     * @throws java.io.IOException
     */
    private static String readIt(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String webPage = "", data;

        while ((data = reader.readLine()) != null) {
            webPage += data + "\n";
        }

        return webPage;
    }


    /**
     * This class represents a single entry (post) in the XML feed.
     * <p>
     * <p>It includes the data members "title," "link," and "summary."
     */
    public static class Entry {
        public final String id;
        public final String title;
        public final String link;
        public final long published;

        Entry(String id, String title, String link, long published) {
            this.id = id;
            this.title = title;
            this.link = link;
            this.published = published;
        }
    }
}
