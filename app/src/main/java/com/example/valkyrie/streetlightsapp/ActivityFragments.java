package com.example.valkyrie.streetlightsapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityFragments extends Fragment {
    ListView myListview;
    private CountriesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment, container, false);

        dbHelper = new CountriesDbAdapter(getActivity());
        //ArrayList<String> theList=new ArrayList<>();
        //Cursor data=dbHelper.getAllData();

        dbHelper.open();

        //Clean all data
       // dbHelper.deleteAllCountries();
        //Add some data
        //dbHelper.insertSomeCountries();

        myListview = v.findViewById(R.id.listView1);
        //Generate ListView from SQLite Database
        displayListView();
        myListview.setAdapter(dataAdapter);


        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryCode =
                        cursor.getString(cursor.getColumnIndexOrThrow("code"));
                Toast.makeText(getContext(),
                        countryCode, Toast.LENGTH_SHORT).show();

            }
        });

        EditText myFilter = v.findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchCountriesByName(constraint.toString());
            }
        });
        return v;
    }
        private void displayListView () {


            Cursor cursor = dbHelper.fetchAllCountries();

            // The desired columns to be bound
            String[] columns = new String[]{
                    CountriesDbAdapter.KEY_CODE,
                    CountriesDbAdapter.KEY_CONTINENT,
                    CountriesDbAdapter.KEY_NAME,
                    CountriesDbAdapter.KEY_REGION
            };

            // the XML defined views which the data will be bound to
            int[] to = new int[]{
                    R.id.code,
                    R.id.continent,
                    R.id.region,
                    R.id.name,
            };

            // create the adapter using the cursor pointing to the desired data
            //as well as the layout information
            dataAdapter = new SimpleCursorAdapter(
                    getActivity(), R.layout.country_info,
                    cursor,
                    columns,
                    to,
                    0);


            // Assign adapter to ListView

        }
}