package com.secretcitylabs.digitaldna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ExportData extends AppCompatActivity {
    DatabaseHelper exportDatabase;
    Button exportDataBtn;
    TextView dbContentsTextView;
    TextView dbCountTextView;
    //Spinner spinner;

    int databaseEntryCount = 0;

    RecyclerView recyclerView;
    ArrayList<String> arr_SampleID, arr_Date_Collected, arr_Exact_Site;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);
        getSupportActionBar().hide();

        exportDatabase = new DatabaseHelper(this);

        arr_SampleID = new ArrayList<>();
        arr_Date_Collected = new ArrayList<>();
        arr_Exact_Site = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(this, arr_SampleID, arr_Date_Collected, arr_Exact_Site);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //displayData();

        dbContentsTextView = findViewById(R.id.scrollViewTextView);
        dbCountTextView = findViewById(R.id.dbEntryCountTxtView);

        exportDataBtn = (Button) findViewById(R.id.exportDataButton);
        exportDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDatabase.exportDB_CSV();
                Toast.makeText(getApplicationContext(), "Exporting to CSV", Toast.LENGTH_LONG).show();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        //Spinner Drop down elements
        List<String> formats = Arrays.asList(" Select Format", "CSV");

        //https://www.youtube.com/watch?v=N8GfosWTt44
        //Create an ArrayAdapter using custom layout
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_text_color, formats);

        //Specify the custom layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_drop_down);

        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> whatToExport = Arrays.asList(" Select Export Type", "Selected Items", "All Items");
        ArrayAdapter adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner_text_color, whatToExport);
        adapter2.setDropDownViewResource(R.layout.spinner_drop_down);
        spinner2.setAdapter(adapter2);

        //Get info from database and populate the scroll view
        getDatabaseEntries();
    }

    //Show database contents
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //Get the db id for each entry and populate the scroll view for now
    public void getDatabaseEntries(){
        Cursor res = exportDatabase.getAllData();
        if(res.getCount() == 0){
            //Show message
            showMessage("Error", "No data to display");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            databaseEntryCount++;
            //buffer.append("Sample id: " + res.getString(1) + "\n" + "Date Collected: " + res.getString(31) + "\n" + "Exact Site: " + res.getString(26) +  "\n\n");
            arr_SampleID.add(res.getString(1));
            arr_Date_Collected.add(res.getString(31));
            arr_Exact_Site.add(res.getString(26));
        }
        dbCountTextView.setText(String.valueOf(databaseEntryCount));
        //dbContentsTextView.append(buffer.toString());
    }
}