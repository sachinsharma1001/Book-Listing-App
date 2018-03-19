package com.example.sachi.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mEmptyStateTextView;

    private BookAdapter bookAdapter;

    private ListView bookListView;

    private EditText editText;

    private static String API_URL;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.searchText);

        bookListView = (ListView) findViewById(R.id.list);

        bookAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(bookAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);


            Button queryButton = (Button) findViewById(R.id.searchButton);
            queryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        String search = editText.getText().toString();
                        API_URL = "https://www.googleapis.com/books/v1/volumes?q=" + search;

                        new RetrieveFeedTask().execute(API_URL);
                    }
                    else {

                        bookAdapter.clear();
                        mEmptyStateTextView.setText("Check Your Internet Connectivity");
                    }
                }
            });
        }


    class RetrieveFeedTask extends AsyncTask<String, Void, List<Book>> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected List<Book> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Book> result = BookUtils.fetchBookData(urls[0]);
            return result;
        }

        protected void onPostExecute(List<Book> data) {
            progressBar.setVisibility(View.GONE);
            bookAdapter.clear();

            mEmptyStateTextView.setText("No Data Found");

            if (data != null && !data.isEmpty()) {
                bookAdapter.addAll(data);
            }

        }

    }
}
