package com.example.sachi.booklistingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Activity context, ArrayList<Book> earthquakes) {
        super(context, 0, earthquakes);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list, parent, false);
        }

        Book currentBookItem = getItem(position);

        TextView textViewtitle = (TextView) listItemView.findViewById(R.id.responseViewtitle);
        textViewtitle.setText(currentBookItem.mTitle);

        TextView textViewauthor = (TextView) listItemView.findViewById(R.id.responseViewauthor);
        textViewauthor.setText(currentBookItem.mAuthor);

        return listItemView;
    }
}