package com.karmadeliworks.paymentprocessing;import android.os.Bundle;import android.support.v7.app.AppCompatActivity;import android.view.View;import android.widget.Button;import android.widget.ProgressBar;import android.widget.TextView;import com.android.volley.Request;import com.android.volley.RequestQueue;import com.android.volley.Response;import com.android.volley.VolleyError;import com.android.volley.toolbox.StringRequest;import com.android.volley.toolbox.Volley;import java.math.BigDecimal;import java.util.HashMap;import java.util.Map;public class PayActivity extends AppCompatActivity {    BigDecimal amt;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_pay);        TextView amount = findViewById(R.id.finalAmount);        TextView email = findViewById(R.id.finalEmail);        final Button send = findViewById(R.id.sendBtn);        final ProgressBar spinner = findViewById(R.id.progressBar2);        spinner.setVisibility(View.GONE);        final Bundle extras = getIntent().getExtras();        final Bundle donationInfo = (Bundle) extras.get("donationInfo");        amt = (BigDecimal) donationInfo.get("amount");        Utility.amountSent = amt.floatValue();        amount.setText( "$"+ Utility.amountSent + " USD");        email.setText( donationInfo.get("email").toString());        send.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                spinner.setVisibility(View.VISIBLE);                String chargeURL = "YOUR_BASEURL" + "/charge";// for server validation of payment                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());                StringRequest stringRequest =  new StringRequest(Request.Method.POST, chargeURL, new Response.Listener<String>() {                    @Override                    public void onResponse(String response) {                        System.out.println("SUCCESS");                        spinner.setVisibility(View.GONE);                        if(Utility.alertSummoned == false){                            Utility.flag = "moneySent";                            alert();                        }                    }                },                        new Response.ErrorListener() {                            @Override                            public void onErrorResponse(VolleyError error) {                                spinner.setVisibility(View.GONE);                                System.out.println("FAILURE");                                error.getLocalizedMessage();                                if(Utility.alertSummoned == false){                                    Utility.flag = "failure";                                    alert();                                }                            }                        }) {                    @Override                    protected Map<String, String> getParams() {                        Map<String, String> params = new HashMap<>();                        Float realAmount =  amt.floatValue() * 100;                        Integer intAmount = realAmount.intValue();                        params.put("amount", intAmount.toString());                        params.put("email", donationInfo.get("email").toString());                        params.put("currency", "USD");                        params.put("source", extras.get("token").toString());                        params.put("description", "***");// enter a description of transaction                        return params;                    }                };                queue.add(stringRequest);            }        });    }    void alert(){        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();        android.support.v4.app.Fragment  fragment = new AlertFragment();        transaction.replace(R.id.frag, fragment);        transaction.addToBackStack(null);        transaction.commit();        Utility.alertSummoned = true;    }}