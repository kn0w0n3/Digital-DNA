package com.secretcitylabs.digitaldna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class SearchData extends AppCompatActivity {
    String tempStartTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);
        getSupportActionBar().hide();

        Button searchBtn1, searchBtn2, searchBtn3;
        EditText et1, et2, et3;

        searchBtn1 = (Button) findViewById(R.id.sd_searchBtn);
        searchBtn2 = (Button) findViewById(R.id.sd_searchBtn2);
        searchBtn3 = (Button) findViewById(R.id.sd_searchBtn3);

        et1 = (EditText) findViewById(R.id.sd_SearchEditTxt);
        et2 = (EditText) findViewById(R.id.sd_SearchEditTxt2);
        et3 = (EditText) findViewById(R.id.sd_SearchEditTxt3);

        //CLick listeners for buttons
        searchBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(SearchData.this);
                //String url = "http://v3.boldsystems.org/index.php/API_Public/specimen?taxon=Aves&geo=Costa%20Rica&format=xml";
                String url = et1.getText().toString();
                //Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    processParsing(response);
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, error -> Toast.makeText(SearchData.this, "Error occurred with API call", Toast.LENGTH_SHORT).show());
                //Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

        searchBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchData.this, "The text entered is: " + et2.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        searchBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchData.this, "The text entered is: " + et3.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processParsing(String r) throws XmlPullParserException, IOException{
            TextView tV;
            tV =  (TextView) findViewById(R.id.textView12);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            System.out.println("parser implementation class is "+xpp.getClass());

            xpp.setInput ( new StringReader (r) );
            int eventType = xpp.getEventType();

            while (eventType != xpp.END_DOCUMENT) {

                if(eventType == xpp.START_DOCUMENT) {
                    System.out.println("Start document");
                }
                else if(eventType == xpp.END_DOCUMENT) {
                    System.out.println("End document");
                }
                else if(eventType == xpp.START_TAG) {
                    tempStartTag = xpp.getName().toString();
                    System.out.println("Start tag "+xpp.getName());
                }
                else if(eventType == xpp.END_TAG) {
                    System.out.println("End tag "+xpp.getName());
                }
                else if(eventType == xpp.TEXT) {
                    System.out.println("Text " + xpp.getText());
                    if(xpp.getText().toString().isEmpty()){
                        Toast.makeText(SearchData.this, "The text is empty: " , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        tV.append(tempStartTag + " " + xpp.getText());
                        tempStartTag = "";
                    }
                    //Toast.makeText(SearchData.this, "The text is: " + xpp.getText(), Toast.LENGTH_SHORT).show();
                }
                eventType = xpp.next();
            }
    }
}