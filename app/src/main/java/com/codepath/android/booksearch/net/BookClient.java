package com.codepath.android.booksearch.net;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BookClient {
    private static final String API_BASE_URL = "http://openlibrary.org/";
    private AsyncHttpClient client;

    public BookClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getBooks(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("search.json?q=");
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //Method to get more details about a book

    public void getBookDetails(String openBookId, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("api/books.json?format=json&jscmd=data&bibkeys=OLID:");
            Log.d("Open BookId : ", openBookId);
            Log.d("After Open BookId : ", URLEncoder.encode(openBookId, "utf-8"));
            client.get(url + URLEncoder.encode(openBookId, "utf-8"), handler);
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
