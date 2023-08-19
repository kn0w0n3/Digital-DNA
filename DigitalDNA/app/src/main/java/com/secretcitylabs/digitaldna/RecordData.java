package com.secretcitylabs.digitaldna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordData extends AppCompatActivity {
    DatabaseHelper inputDatabase;

    EditText sampleID, fieldID, museumID, collectionCode, depositIn, phylum, classTxt,
             order, family, subfamily, genus, species, subspecies, binId, vStatus, tDescriptor,
             briefNote, reproduction, sex, lifeStage, detailedNote, country, province_State, region_Country,
             sector, exactSite, latitude, longitude, cordSource, cordAccuracy, dateCollected, collectors, elevation,
             elevAccuracy, depth, depthAccuracy;

    Button saveBtn, viewAllBtn, clearBtn;
    TextView rd_ImageSelectTxtView, imgNameTxt, imgPreviewTxt;
    Location gps_loc, network_loc, final_loc, gpsInfo ;
    double iCurrentAltitude_GPS, dGpsAccuracy, longitude_i, latitude_i;
    ImageButton selectImgBtn;
    ImageView IVPreviewImage;

    // constant to compare the activity result code
    int SELECT_PICTURE = 200;
    byte[] img;

    private String userCountry, userAddress, sCordSource, imageName;
    private Bitmap imageToStore;
    private ByteArrayOutputStream objectByteArrayOutPutStream;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_data);
        getSupportActionBar().hide();

        inputDatabase = new DatabaseHelper(this);

        imgNameTxt = (TextView) findViewById(R.id.imgSelectTxtView);
        imgPreviewTxt = (TextView) findViewById(R.id.imagePreviewText);

        saveBtn = (Button) findViewById(R.id.rd_SaveBtn);
        viewAllBtn = (Button) findViewById(R.id.viewDataBtn);
        clearBtn = (Button) findViewById(R.id.rd_ClearBtn);
        selectImgBtn = (ImageButton) findViewById(R.id.imageButton);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        rd_ImageSelectTxtView = findViewById(R.id.imgSelectTxtView);

        sampleID = (EditText) findViewById(R.id.sampleIDEditTxt); fieldID = (EditText) findViewById(R.id.fieldIDEditTxt); museumID = (EditText) findViewById(R.id.museumIDEditTxt);
        collectionCode = (EditText) findViewById(R.id.collectionCodeEditTxt); depositIn = (EditText) findViewById(R.id.depositInEditTxt); phylum = (EditText) findViewById(R.id.phylumEditTxt);
        classTxt = (EditText) findViewById(R.id.classEditTxt);

        order = (EditText) findViewById(R.id.orderEditTxt); family = (EditText) findViewById(R.id.familyEditTxt); subfamily = (EditText) findViewById(R.id.subfamilyEditTxt);
        genus = (EditText) findViewById(R.id.genusEditTxt); species = (EditText) findViewById(R.id.speciesEditTxt); subspecies = (EditText) findViewById(R.id.subspeciesEditTxt);
        binId = (EditText) findViewById(R.id.binIdEditTxt);

        vStatus = (EditText) findViewById(R.id.voucherStatusEditTxt); tDescriptor = (EditText) findViewById(R.id.tissueDescriptorEditTxt); briefNote = (EditText) findViewById(R.id.briefNoteEditTxt);
        reproduction = (EditText) findViewById(R.id.reproductionEditTxt); sex = (EditText) findViewById(R.id.sexEditTxt); lifeStage = (EditText) findViewById(R.id.lifeStageEditTxt);
        detailedNote = (EditText) findViewById(R.id.detailNoteEditTxt);

        country = (EditText) findViewById(R.id.countryEditTxt); province_State = (EditText) findViewById(R.id.province_StateEditTxt); region_Country = (EditText) findViewById(R.id.region_CountryEditTxt);
        sector = (EditText) findViewById(R.id.sectorEditTxt); exactSite = (EditText) findViewById(R.id.exactSiteEditTxt); latitude = (EditText) findViewById(R.id.latitudeEditTxt);
        longitude = (EditText) findViewById(R.id.logitudeEditTxt); cordSource = (EditText) findViewById(R.id.cordSourceEditTxt); cordAccuracy = (EditText) findViewById(R.id.cordAccuracyEditTxt);
        dateCollected = (EditText) findViewById(R.id.dateCollectedEditTxt); collectors = (EditText) findViewById(R.id.collectorsEditTxt); elevation = (EditText) findViewById(R.id.elevationEditTxt);
        elevAccuracy = (EditText) findViewById(R.id.elevAccuracyEditTxt); depth = (EditText) findViewById(R.id.depthEditTxt); depthAccuracy = (EditText) findViewById(R.id.depthAccuracyEditTxt);

        //**Start Gps**
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            gpsInfo = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Check GPS first and use if available
        if (gps_loc != null) {
            final_loc = gps_loc;

            //Get latitude and longitude and auto fill form
            latitude_i = final_loc.getLatitude();
            longitude_i = final_loc.getLongitude();
            latitude.setText(String.valueOf(latitude_i));
            longitude.setText(String.valueOf(longitude_i));

            //Get the altitude and auto fill form
            iCurrentAltitude_GPS = gpsInfo.getAltitude();
            elevation.setText(String.valueOf(iCurrentAltitude_GPS));
            depth.setText(String.valueOf(iCurrentAltitude_GPS));

            //Get coordinate source and auto fill form
            sCordSource = gpsInfo.getProvider();
            cordSource.setText(sCordSource);

            //Get accuracy info and auto fill form
            dGpsAccuracy = gpsInfo.getAccuracy();
            elevAccuracy.setText(String.valueOf(dGpsAccuracy));
            depthAccuracy.setText(String.valueOf(dGpsAccuracy));
            cordAccuracy.setText(String.valueOf(dGpsAccuracy));
            //Toast.makeText(RecordData.this, "Depth accuracy test: " + dGpsAccuracy, Toast.LENGTH_LONG).show();
        }
        //Check Network if no GPS. If there is network connectivity, get the data.
        else if (network_loc != null) {
            final_loc = network_loc;

            latitude_i = final_loc.getLatitude();
            longitude_i = final_loc.getLongitude();

            latitude.setText(String.valueOf(latitude_i));
            longitude.setText(String.valueOf(longitude_i));
        }
        else {
            latitude_i = 0.0;
            longitude_i = 0.0;

            latitude.setText(String.valueOf(latitude_i));
            longitude.setText(String.valueOf(longitude_i));
        }

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                                                                     Manifest.permission.ACCESS_COARSE_LOCATION,
                                                                     Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude_i, longitude_i, 1);
            if (addresses != null && addresses.size() > 0) {
                userCountry = addresses.get(0).getCountryName();
                userAddress = addresses.get(0).getAddressLine(0);
                country.setText(userCountry);
                exactSite.setText(userAddress);
            }
            else {
                userCountry = "Unknown";
                country.setText(userCountry);
                exactSite.setText(userAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    //End gps

        //Get the current date and auto fill the text for the user
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        dateCollected.setText(todayString);

        AddData();
        viewAll();
        selectPicture();
        clearText();
        getLocationDetails();
    }

    //Insert Data
    public void AddData(){
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       boolean isInserted =  inputDatabase.insertData(sampleID.getText().toString(), fieldID.getText().toString(), museumID.getText().toString(),
                               collectionCode.getText().toString(), depositIn.getText().toString(), phylum.getText().toString(), subfamily.getText().toString(),
                               classTxt.getText().toString(), order.getText().toString(), family.getText().toString(), genus.getText().toString(),
                               species.getText().toString(), subspecies.getText().toString(), binId.getText().toString(), vStatus.getText().toString(),
                               tDescriptor.getText().toString(), briefNote.getText().toString(),reproduction.getText().toString(), sex.getText().toString(),
                               lifeStage.getText().toString(), detailedNote.getText().toString(), country.getText().toString(), province_State.getText().toString(),
                               region_Country.getText().toString(), sector.getText().toString(), exactSite.getText().toString(), latitude.getText().toString(),
                               longitude.getText().toString(), cordSource.getText().toString(), cordAccuracy.getText().toString(), dateCollected.getText().toString(),
                               collectors.getText().toString(), elevation.getText().toString(), elevAccuracy.getText().toString(), depth.getText().toString(),
                               depthAccuracy.getText().toString(), img, imgNameTxt.getText().toString());

                       if(isInserted){
                               Toast.makeText(RecordData.this, "Data Inserted", Toast.LENGTH_LONG).show();
                               sampleID.setText(""); fieldID.setText(""); museumID.setText(""); collectionCode.setText(""); depositIn.setText(""); phylum.setText(""); classTxt.setText("");
                               order.setText(""); family.setText(""); subfamily.setText(""); genus.setText(""); species.setText(""); subspecies.setText("");binId.setText("");
                               vStatus.setText(""); tDescriptor.setText(""); briefNote.setText(""); reproduction.setText(""); sex.setText(""); lifeStage.setText(""); detailedNote.setText("");
                               country.setText(""); province_State.setText(""); region_Country.setText(""); sector.setText(""); exactSite.setText(""); latitude.setText(""); longitude.setText("");
                               cordSource.setText(""); cordAccuracy.setText(""); dateCollected.setText(""); collectors.setText(""); elevation.setText(""); elevAccuracy.setText("");depth.setText("");
                               depthAccuracy.setText("");imgNameTxt.setText("");imgPreviewTxt.setVisibility(View.INVISIBLE);IVPreviewImage.setImageResource(0);
                       }
                       else{
                           Toast.makeText(RecordData.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                       }
                    }
                }
        );
    }

    //Get and view data
    public void viewAll(){
        viewAllBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor res = inputDatabase.getAllData();
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

    //Show database contents
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //https://stackoverflow.com/questions/48194733/whats-the-way-to-pick-images-from-gallery-on-android-in-2018
    public void selectPicture(){
        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RecordData.this, "Select Image button pressed", Toast.LENGTH_LONG).show();
                imageChooser();
            }
        });
    }

    // this function is triggered when the Select Image Button is clicked
    void imageChooser() {
        // create an instance of the intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    //This function is triggered when user selects the image from the imageChooser
    //https://developer.android.com/reference/android/graphics/BitmapFactory
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                try {
                    imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                // Get the file's content URI from the incoming Intent,
                // then query the server app to get the file's display name and size
                Cursor returnCursor = getContentResolver().query(selectedImageUri , null, null, null, null);

                //Get the column indexes of the data in the Cursor,
                //move to the first row in the Cursor, get the data, and display it.
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();

                //Store the image name
                imageName = returnCursor.getString(nameIndex);

                //Set image name in text view
                imgNameTxt.setText(" " + imageName);

                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imgPreviewTxt.setVisibility(View.VISIBLE);
                    IVPreviewImage.setImageURI(selectedImageUri);
                    storeImage(new ModelClass(imageName, imageToStore));
                }
            }
        }
    }

    //New Method: https://www.youtube.com/watch?v=OBtEwSe4LEQ 12-25-22
    //When the user selects a picture, it gets displayed in the image view and then gets converted
    //to a byte array here. When the user clicks save, the image Byte array should be sent over to the database helper to be inserted
    public void storeImage(ModelClass objectModelClass){
        try {
            Bitmap imageToStoreBitmap = objectModelClass.getImage();
            objectByteArrayOutPutStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutPutStream);
            img = objectByteArrayOutPutStream.toByteArray();
        }
        catch (Exception e) {
            Toast.makeText(RecordData.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //Clear all text when the clear button is pressed
    public void clearText(){
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sampleID.setText(""); fieldID.setText(""); museumID.setText(""); collectionCode.setText(""); depositIn.setText(""); phylum.setText(""); classTxt.setText("");
                order.setText(""); family.setText(""); subfamily.setText(""); genus.setText(""); species.setText(""); subspecies.setText("");binId.setText("");
                vStatus.setText(""); tDescriptor.setText(""); briefNote.setText(""); reproduction.setText(""); sex.setText(""); lifeStage.setText(""); detailedNote.setText("");
                country.setText(""); province_State.setText(""); region_Country.setText(""); sector.setText(""); exactSite.setText(""); latitude.setText(""); longitude.setText("");
                cordSource.setText(""); cordAccuracy.setText(""); dateCollected.setText(""); collectors.setText(""); elevation.setText(""); elevAccuracy.setText("");depth.setText("");
                depthAccuracy.setText("");imgNameTxt.setText("");imgPreviewTxt.setVisibility(View.INVISIBLE);IVPreviewImage.setImageResource(0);
            }
        });
    }

    //Get specific location details to auto fill the form
    public void getLocationDetails(){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address>addresses = geocoder.getFromLocation(latitude_i = final_loc.getLatitude(),longitude_i = final_loc.getLongitude(),1);
            if (geocoder.isPresent()) {
                StringBuilder stringBuilder = new StringBuilder();
                if (addresses.size()>0) {
                    Address returnAddress = addresses.get(0);

                    /*
                    Other Options
                    String _localityString = returnAddress.getLocality();
                    String _name = returnAddress.getFeatureName();
                    String _subLocality = returnAddress.getSubLocality();
                    String _zipcode = returnAddress.getPostalCode();
                    */

                    String _country = returnAddress.getCountryName();
                    country.setText(_country);

                    String _region_code = returnAddress.getCountryCode();
                    region_Country.setText(_region_code);

                    String _state = returnAddress.getAdminArea();
                    province_State.setText(_state);
                }
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}