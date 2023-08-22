package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {


    Spinner spinnerfrom , spinnerto;
    EditText amount_edittext;
    TextView result_textview;
    ImageView convert_btn_image , reset_btn;

    String Base_URL = "https://api.currencyapi.com/v3/latest";
    String API_Key = "cur_live_8gtOOvrBLB3i2RcWJ1H5AbazrofNSj6cp99n1z4M";
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);



        spinnerfrom = findViewById(R.id.spinner_from);
        spinnerto = findViewById(R.id.spinner_to);
        amount_edittext = findViewById(R.id.amount_edittext);
        result_textview = findViewById(R.id.result_textview);
        convert_btn_image = findViewById(R.id.convert_btn);
        reset_btn = findViewById(R.id.reset_btn);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.currencies_array,
                android.R.layout.simple_spinner_dropdown_item);

        spinnerfrom.setAdapter(adapter);
        spinnerto.setAdapter(adapter);

        spinnerfrom.setSelection(0);
        spinnerto.setSelection(127);







        convert_btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                corrency_Converter();
            }
        });


        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Data();
            }
        });

    }


    private void Reset_Data() {
        amount_edittext.setText("");
        result_textview.setText("");
    }





    private void corrency_Converter() {

        String currency_from = spinnerfrom.getSelectedItem().toString();
        String currency_to = spinnerto.getSelectedItem().toString();
        double amount = Double.parseDouble(amount_edittext.getText().toString());


        String URL = Base_URL + "?apikey=" + API_Key;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                String currencyData = "{\"meta\":{\"last_updated_at\":\"2023-08-16T23:59:59Z\"},\"data\":{...}}";

                try {
                    JSONObject dataObject = response.getJSONObject("data");

                    double fromRate = dataObject.getJSONObject(currency_from).getDouble("value");
                    double toRate = dataObject.getJSONObject(currency_to).getDouble("value");


                    double convertedAmount = (amount / fromRate) * toRate;

                    result_textview.setText(decimalFormat.format(amount) + " " + currency_from
                            + " = " + decimalFormat.format(convertedAmount) + " " +currency_to);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(objectRequest);

    }
}