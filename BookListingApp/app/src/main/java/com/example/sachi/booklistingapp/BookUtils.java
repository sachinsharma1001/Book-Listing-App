package com.example.sachi.booklistingapp;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class BookUtils {

    private BookUtils() {
    }

    public static List<Book> fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

        }

        List<Book> books = extractFeatureFromJson(jsonResponse);

        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> booklist = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            for(int i=0; i<itemsArray.length(); i++) {

                JSONObject firstFeature = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = firstFeature.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");

                if (volumeInfo.has("authors")) {
                    JSONArray authorArray = volumeInfo.getJSONArray("authors");

                    String authorName = authorArray.getString(0);

                    Book book = new Book(title, authorName);

                    booklist.add(book);
                }
                else {
                    Book book  = new Book(title);

                    booklist.add(book);
                }
            }

        } catch (JSONException e) {

            Log.e("BookUtils", "Problem parsing the book JSON results", e);
        }

        return booklist;
    }
}
