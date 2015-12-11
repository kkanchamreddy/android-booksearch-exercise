package com.codepath.android.booksearch.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.models.Book;
import com.codepath.android.booksearch.net.BookClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class BookDetailActivity extends ActionBarActivity {
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPageCount;
    private String openBookId;

    private BookClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvPageCount = (TextView) findViewById(R.id.tvPageCount);

        // Extract book object from intent extras
        Book book = getIntent().getParcelableExtra("book");
        client = new BookClient();
        openBookId = book.getOpenLibraryId();
        client.getBookDetails(openBookId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    if(response != null) {

                        JSONObject bookDetails = response.getJSONObject("OLID:" + openBookId);
                        if(bookDetails.has("publishers")) {
                            final JSONArray publsihers = bookDetails.getJSONArray("publishers");
                            JSONObject publisher =  publsihers.getJSONObject(0);
                            tvPublisher.setText("Published by: " + publisher.getString("name"));
                        }

                        if(bookDetails.has("number_of_pages")) {
                            String pageCount = bookDetails.getString("number_of_pages");
                            tvPageCount.setText("Pages: " + pageCount);
                        }
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }
        });


        // Use book object to populate data into views
        Picasso.with(getApplicationContext()).load(Uri.parse(book.getCoverUrl())).placeholder(R.drawable.ic_nocover).into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());

        //update the Actionbar title
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        actionBar.setTitle(book.getTitle());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
