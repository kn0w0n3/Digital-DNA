package com.secretcitylabs.digitaldna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class ViewData extends AppCompatActivity {
    DatabaseHelper vd_InputDatabaseView;

    Button viewAllBtn,databyIdBtn, dataLimitedBtn;
    TextView entryIdToGet, totalRecordsTextView;
    boolean okToProceed = false;
    Spinner spinner3;
    private String spinnerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        getSupportActionBar().hide();

        vd_InputDatabaseView = new DatabaseHelper(this);
        viewAllBtn = (Button) findViewById(R.id.vd_viewAllDataBtn);
        databyIdBtn = (Button) findViewById(R.id.vd_DataByIDBtn);
        dataLimitedBtn = (Button) findViewById(R.id.vd_viewDataLimitedBtn );
        entryIdToGet = (TextView) findViewById(R.id.vd_DbIDEditTxt);
        totalRecordsTextView = (TextView) findViewById(R.id.totalRecordsTxtView);


        spinner3 = (Spinner) findViewById(R.id.spinner3);
        List<String> whatToExport = Arrays.asList(" Select Limit", " 10 Records", " 20 Records"," 30 Records", " 50 Records", " 100 Records" );
        ArrayAdapter adapter3 = new ArrayAdapter(getApplicationContext(), R.layout.spinner_text_color, whatToExport);
        adapter3.setDropDownViewResource(R.layout.spinner_drop_down);
        spinner3.setAdapter(adapter3);

        viewAll();
        viewEntryById();
        showDatabaseEntryCount();
        listenForSpinnerChange();
        getLimitedDbEntries();
    }

    //Get and view data
    public void viewAll(){
        viewAllBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor res = vd_InputDatabaseView.getAllData();
                        if(res.getCount() == 0){
                            //Show message
                            showMessage("Error", "No data to display");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("id: " + res.getString(0) + "\n"); buffer.append("Sample ID: " + res.getString(1) + "\n");
                            buffer.append("Field ID: " + res.getString(2) + "\n"); buffer.append("Museum ID: " + res.getString(3) + "\n");
                            buffer.append("Collection Code: " + res.getString(4) + "\n"); buffer.append("Deposited In: " + res.getString(5) + "\n");
                            buffer.append("Phylum: " + res.getString(6) + "\n"); buffer.append("Class: " + res.getString(7) + "\n");
                            buffer.append("Order: " + res.getString(8) + "\n"); buffer.append("Family: " + res.getString(9) + "\n");
                            buffer.append("Subfamily: " + res.getString(10) + "\n"); buffer.append("Genus: " + res.getString(11) + "\n");
                            buffer.append("Species: " + res.getString(12) + "\n"); buffer.append("Subspecies: " + res.getString(13) + "\n");
                            buffer.append("Bin ID: " + res.getString(14) + "\n"); buffer.append("Voucher Status: " + res.getString(15) + "\n");
                            buffer.append("Tissue Descriptor: " + res.getString(16) + "\n"); buffer.append("Brief Note: " + res.getString(17) + "\n");
                            buffer.append("Reproduction: " + res.getString(18) + "\n"); buffer.append("Sex: " + res.getString(19) + "\n");
                            buffer.append("Life Stage: " + res.getString(20) + "\n"); buffer.append("Detailed Note: " + res.getString(21) + "\n");
                            buffer.append("Country: " + res.getString(22) + "\n"); buffer.append("Province/State: " + res.getString(23) + "\n");
                            buffer.append("Province/State: " + res.getString(24) + "\n"); buffer.append("Sector: " + res.getString(25) + "\n");
                            buffer.append("Exact Site: " + res.getString(26) + "\n");buffer.append("Latitude: " + res.getString(27) + "\n");
                            buffer.append("Longitude: " + res.getString(28) + "\n");buffer.append("Coord Source: " + res.getString(29) + "\n");
                            buffer.append("Coord Accuracy: " + res.getString(30) + "\n");buffer.append("Date Collected: " + res.getString(31) + "\n");
                            buffer.append("Collectors: " + res.getString(32) + "\n"); buffer.append("Elevation: " + res.getString(33) + "\n");
                            buffer.append("Elev Accuracy: " + res.getString(34) + "\n"); buffer.append("Depth: " + res.getString(35) + "\n");
                            buffer.append("Depth Accuracy: " + res.getString(36) + "\n"); buffer.append("Image Name: " + res.getString(38) + "\n\n");
                        }
                        //Show all Data
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void viewEntryById(){
        databyIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_String = entryIdToGet.getText().toString();
                //int intToCheck = Integer.parseInt(id_String);
                try {
                    // checking valid integer using parseInt()
                    // method
                    Integer.parseInt(id_String);
                    //System.out.println(id_String+ " is a valid integer number");
                    okToProceed = true;
                }
                catch (NumberFormatException e) {
                    //System.out.println(id_String + " is not a valid integer number");
                    Toast.makeText(getApplicationContext(), "Enter numbers only", Toast.LENGTH_LONG).show();
                    okToProceed = false;
                }

                if (okToProceed == false){
                    return;
                }

                if(entryIdToGet.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter an ID" , Toast.LENGTH_LONG).show();
                    return;
                }
                else if(entryIdToGet.getText().toString().isEmpty() == false){
                    int i_Value =  Integer.parseInt(entryIdToGet.getText().toString());
                    if(i_Value < 0){
                        Toast.makeText(getApplicationContext(), "Incorrect character entered. Try again" , Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(i_Value > 0){
                        if(entryIdToGet.getText().toString().trim().equals("")) {
                            //Show message
                            showMessage("Error", "No data to display");
                            return;
                        }
                        else{
                            //get the data here
                            Cursor res = vd_InputDatabaseView.getEntryById(entryIdToGet.getText().toString());
                            if(res.getCount() == 0){
                                //Show message
                                showMessage("Error", "No data to display");
                                return;
                            }
                            StringBuffer buffer = new StringBuffer();
                            while(res.moveToNext()){
                                buffer.append("id: " + res.getString(0) + "\n"); buffer.append("Sample ID: " + res.getString(1) + "\n");
                                buffer.append("Field ID: " + res.getString(2) + "\n"); buffer.append("Museum ID: " + res.getString(3) + "\n");
                                buffer.append("Collection Code: " + res.getString(4) + "\n"); buffer.append("Deposited In: " + res.getString(5) + "\n");
                                buffer.append("Phylum: " + res.getString(6) + "\n"); buffer.append("Class: " + res.getString(7) + "\n");
                                buffer.append("Order: " + res.getString(8) + "\n"); buffer.append("Family: " + res.getString(9) + "\n");
                                buffer.append("Subfamily: " + res.getString(10) + "\n"); buffer.append("Genus: " + res.getString(11) + "\n");
                                buffer.append("Species: " + res.getString(12) + "\n"); buffer.append("Subspecies: " + res.getString(13) + "\n");
                                buffer.append("Bin ID: " + res.getString(14) + "\n"); buffer.append("Voucher Status: " + res.getString(15) + "\n");
                                buffer.append("Tissue Descriptor: " + res.getString(16) + "\n"); buffer.append("Brief Note: " + res.getString(17) + "\n");
                                buffer.append("Reproduction: " + res.getString(18) + "\n"); buffer.append("Sex: " + res.getString(19) + "\n");
                                buffer.append("Life Stage: " + res.getString(20) + "\n"); buffer.append("Detailed Note: " + res.getString(21) + "\n");
                                buffer.append("Country: " + res.getString(22) + "\n"); buffer.append("Province/State: " + res.getString(23) + "\n");
                                buffer.append("Province/State: " + res.getString(24) + "\n"); buffer.append("Sector: " + res.getString(25) + "\n");
                                buffer.append("Exact Site: " + res.getString(26) + "\n");buffer.append("Latitude: " + res.getString(27) + "\n");
                                buffer.append("Longitude: " + res.getString(28) + "\n");buffer.append("Coord Source: " + res.getString(29) + "\n");
                                buffer.append("Coord Accuracy: " + res.getString(30) + "\n");buffer.append("Date Collected: " + res.getString(31) + "\n");
                                buffer.append("Collectors: " + res.getString(32) + "\n"); buffer.append("Elevation: " + res.getString(33) + "\n");
                                buffer.append("Elev Accuracy: " + res.getString(34) + "\n"); buffer.append("Depth: " + res.getString(35) + "\n");
                                buffer.append("Depth Accuracy: " + res.getString(36) + "\n"); buffer.append("Image Name: " + res.getString(38) + "\n\n");
                            }
                            //Show all Data
                            showMessage("Requested Data", buffer.toString());
                        }
                    }
                }
            }
        });
    }

    //Get text selection from spinner
    public void getLimitedDbEntries(){
        dataLimitedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "The text in spinner is: " + spinnerText, Toast.LENGTH_LONG).show();
                if(spinnerText.trim().equals("Select Limit")){
                    Toast.makeText(getApplicationContext(), "Please make a selection ", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(spinnerText.trim().equals("10 Records")){
                    displayLimitedDbEntries("10");
                }
                else if(spinnerText.trim().equals("20 Records")){
                    displayLimitedDbEntries("20");
                }
                else if(spinnerText.trim().equals("30 Records")){
                    displayLimitedDbEntries("30");
                }
                else if(spinnerText.trim().equals("50 Records")){
                    displayLimitedDbEntries("50");
                }
                else if(spinnerText.trim().equals("100 Records")){
                    displayLimitedDbEntries("100");
                }
            }
        });
    }

    public void displayLimitedDbEntries(String number){
        Cursor res = vd_InputDatabaseView.getSpecifiedNumOfEntries(number);
        if(res.getCount() == 0){
            //Show message
            showMessage("Error", "No data to display");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append("id: " + res.getString(0) + "\n"); buffer.append("Sample ID: " + res.getString(1) + "\n");
            buffer.append("Field ID: " + res.getString(2) + "\n"); buffer.append("Museum ID: " + res.getString(3) + "\n");
            buffer.append("Collection Code: " + res.getString(4) + "\n"); buffer.append("Deposited In: " + res.getString(5) + "\n");
            buffer.append("Phylum: " + res.getString(6) + "\n"); buffer.append("Class: " + res.getString(7) + "\n");
            buffer.append("Order: " + res.getString(8) + "\n"); buffer.append("Family: " + res.getString(9) + "\n");
            buffer.append("Subfamily: " + res.getString(10) + "\n"); buffer.append("Genus: " + res.getString(11) + "\n");
            buffer.append("Species: " + res.getString(12) + "\n"); buffer.append("Subspecies: " + res.getString(13) + "\n");
            buffer.append("Bin ID: " + res.getString(14) + "\n"); buffer.append("Voucher Status: " + res.getString(15) + "\n");
            buffer.append("Tissue Descriptor: " + res.getString(16) + "\n"); buffer.append("Brief Note: " + res.getString(17) + "\n");
            buffer.append("Reproduction: " + res.getString(18) + "\n"); buffer.append("Sex: " + res.getString(19) + "\n");
            buffer.append("Life Stage: " + res.getString(20) + "\n"); buffer.append("Detailed Note: " + res.getString(21) + "\n");
            buffer.append("Country: " + res.getString(22) + "\n"); buffer.append("Province/State: " + res.getString(23) + "\n");
            buffer.append("Province/State: " + res.getString(24) + "\n"); buffer.append("Sector: " + res.getString(25) + "\n");
            buffer.append("Exact Site: " + res.getString(26) + "\n");buffer.append("Latitude: " + res.getString(27) + "\n");
            buffer.append("Longitude: " + res.getString(28) + "\n");buffer.append("Coord Source: " + res.getString(29) + "\n");
            buffer.append("Coord Accuracy: " + res.getString(30) + "\n");buffer.append("Date Collected: " + res.getString(31) + "\n");
            buffer.append("Collectors: " + res.getString(32) + "\n"); buffer.append("Elevation: " + res.getString(33) + "\n");
            buffer.append("Elev Accuracy: " + res.getString(34) + "\n"); buffer.append("Depth: " + res.getString(35) + "\n");
            buffer.append("Depth Accuracy: " + res.getString(36) + "\n"); buffer.append("Image Name: " + res.getString(38) + "\n\n");
        }
        //Show requested Data
        showMessage("Requested Data", buffer.toString());
    }

    //Listen for spinner text change
    //https://stackoverflow.com/questions/63211653/get-text-of-selected-spinner-in-android
    public void listenForSpinnerChange(){
        if (spinner3 != null) {
            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item_position = String.valueOf(position);
                    int itemPosition = Integer.parseInt(item_position);
                    String selected = String.valueOf(spinner3.getAdapter().getItem(position));
                    spinnerText = selected;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        else{
            Log.e("Selected item :","NULL:");
        }
    }

    public void showDatabaseEntryCount(){
        String currentEntryCount = vd_InputDatabaseView.getDatabaseEntryCount();
        totalRecordsTextView.setText(currentEntryCount);
    }

    //Show database contents
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}