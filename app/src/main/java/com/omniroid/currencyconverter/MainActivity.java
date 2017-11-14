package com.omniroid.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.omniroid.currencyconverter.AppUrls.AppUrl;
import com.omniroid.currencyconverter.model.CurrencyFirebaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextView textView;

    Spinner spinner;
    Spinner spinner_values;

    EditText editTextInput;
    EditText editTextValuesOutput;

    private float CURRENCY_VALUE_FROM = 0;
    private float CURRENCY_VALUE_TO = 0;


    private ToggleButton mToggleButton;

    LinearLayout linearLayout;


    List<Currency> currencies = new ArrayList<>();

    Currency currency;

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        //setTheme(android.R.style.ThemeOverlay_Material_Dark);

        //Bundle bundle = new Bundle();
        String bundle = getIntent().getStringExtra("chooseTheme");

        if (bundle.equals("One")){
            setTheme(R.style.AppTheme);
        }else if (bundle.equals("Two")){
            setTheme(R.style.AppThemeDark);
        }

        setContentView(R.layout.activity_main);



        listView = findViewById(R.id.list_firebase_data);


        linearLayout = findViewById(R.id.ll_main_layout);
        textView = findViewById(R.id.tv_data);

        spinner = findViewById(R.id.spn_key);
        spinner_values = findViewById(R.id.spn_key_value);


        editTextInput = findViewById(R.id.et_keys);
        editTextValuesOutput = findViewById(R.id.et_values);


        mToggleButton = findViewById(R.id.btn_toggle);
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mToggleButton.isChecked()){
                    //Util.changeToTheme(MainActivity.this, Util.THEME_DEFAULT);
                    (MainActivity.this).setTheme(R.style.AppTheme);
                }else {
                    (MainActivity.this).setTheme(R.style.AppThemeDark);
                }

            }
        });


        getCurrencyConvert();



        editTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateUI();
            }
        });





        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(MainActivity.this, ""+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                //CURRENCY_VALUE_FROM = setCurrencyValueOne(""+adapterView.getItemAtPosition(i));
                setCurrencyValueOne(""+adapterView.getItemAtPosition(i));
                Log.v("currFrom",""+CURRENCY_VALUE_FROM);
                updateUI();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_values.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setCurrencyValueTwo(""+adapterView.getItemAtPosition(i));
                Log.v("currTo",""+CURRENCY_VALUE_TO);

                updateUI();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








    }


    private void updateUI(){

        float inputPrice = 0;
        if (editTextInput.getText().toString().trim().equals("")) {
            inputPrice = 0 ;
        }else {
            inputPrice = Float.parseFloat(editTextInput.getText().toString().trim());
        }

        float outputPrice = 0;
        if (CURRENCY_VALUE_TO != 0 && CURRENCY_VALUE_FROM != 0) {
            outputPrice = inputPrice * (CURRENCY_VALUE_TO / CURRENCY_VALUE_FROM);
        }

        editTextValuesOutput.setText(""+ outputPrice);


//        if (spinner.getSelectedItem().toString().equals("") && spinner_values.getSelectedItem().toString().equals("")
//                && editTextInput.getText().toString().trim().equals("") && editTextInput.getText().toString().trim().equals("")) {
//        }else {
//            addToFirebaseDataBase(spinner.getSelectedItem().toString(), Float.parseFloat(editTextInput.getText().toString().trim()), spinner_values.getSelectedItem().toString(), Float.parseFloat(editTextInput.getText().toString().trim()));
//        }
    }

    private void addToFirebaseDataBase(String spinnerFrom,float spinnerFromValue,
                                       String spinnerTo,float spinnerToValue) {

        DatabaseReference myRefUsers = database.getReference().child("OldInputs");
        CurrencyFirebaseData currencyFirebaseData = new CurrencyFirebaseData(spinnerFrom,spinnerFromValue,spinnerTo,spinnerToValue);
        myRefUsers.push().setValue(currencyFirebaseData);

        if (spinner.getSelectedItem().toString().equals("") && spinner_values.getSelectedItem().toString().equals("")) {
        }else {
            populateListViewWithFirebaseData();
        }
    }

    private void populateListViewWithFirebaseData() {

        DatabaseReference myRefUsers = database.getReference().child("OldInputs");

        myRefUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                CurrencyFirebaseData currencyFirebaseData =dataSnapshot.getValue(CurrencyFirebaseData.class);

                List<CurrencyFirebaseData> taskDesList= new ArrayList<>();
                taskDesList.add(currencyFirebaseData);

                List<String> strings = new ArrayList<>();
                String dataList = "";
                for(int i=0;i<taskDesList.size();i++){
                    dataList = taskDesList.get(i).getSpinnerFrom()+taskDesList.get(i).getSpinnerFromValue()+" -- -- "+
                    taskDesList.get(i).getSpinnerTo()+taskDesList.get(i).getSpinnerToValue();

                    strings.add(taskDesList.get(i).getSpinnerFrom()+taskDesList.get(i).getSpinnerFromValue()+" -- -- "+
                            taskDesList.get(i).getSpinnerTo()+taskDesList.get(i).getSpinnerToValue());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_activated_1,strings);
                listView.setAdapter(arrayAdapter);



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void getCurrencyConvert() {

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppUrl.FIXER_BASE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                if (statusCode == 200){


                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));

                        JSONObject jsonObjectReuqest = jsonObject.getJSONObject("rates");

                        System.out.println(""+jsonObjectReuqest);


                        Iterator x = jsonObjectReuqest.keys();
                        JSONArray jsonArray = new JSONArray();
                        HashMap<String, String> map = new HashMap<String, String>();





                        while (x.hasNext()){
//                            String key = (String) x.next();
//                            jsonArray.put(jsonObjectReuqest.get(key));

                            String key = (String)x.next();
                            String value = jsonObjectReuqest.getString(key);

                            currency = new Currency();
                            currency.setCurrencyName(key);
                            currency.setCurrencyValue(value);

                            currencies.add(currency);
                            map.put(key, value);


                           // textView.setText(""+map+"\n");
                        }

                        List<String> keys = new ArrayList<>();

                        for (int i = 0; i < currencies.size();i++){
                            keys.add(currencies.get(i).getCurrencyName());
                        }

                        setCurrencyValueOne(""+currencies.get(0).getCurrencyName());
                        setCurrencyValueTwo(""+currencies.get(0).getCurrencyName());


                        ArrayAdapter<String> adapterOne = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,keys);

                        spinner.setAdapter(adapterOne);




                        ArrayAdapter<String> adapterTwo = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,keys);

                        spinner_values.setAdapter(adapterTwo);



                        Log.v("currFromLoad",""+CURRENCY_VALUE_FROM);
                        Log.v("currToLoad",""+CURRENCY_VALUE_TO);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void setCurrencyValueOne(String currencyName){



        for (int i = 0; i < currencies.size();i++){
            Log.v("currenciesSizeOne",""+currencies.size());
            Log.v("currenciesListOne",""+currencies.get(i).getCurrencyName());
            if (currencies.get(i).getCurrencyName().equals(currencyName)){
                CURRENCY_VALUE_FROM = Float.parseFloat(currencies.get(i).getCurrencyValue());
            }
        }



    }


    private void setCurrencyValueTwo(String currencyName){



        for (int i = 0; i < currencies.size();i++){
            Log.v("currenciesSizeTwo",""+currencies.size());
            Log.v("currenciesListTwo",""+currencies.get(i).getCurrencyName());
            if (currencies.get(i).getCurrencyName().equals(currencyName)){
                CURRENCY_VALUE_TO = Float.parseFloat(currencies.get(i).getCurrencyValue());
            }

        }



    }



}
