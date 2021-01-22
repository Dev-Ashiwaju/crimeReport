package com.example.crimereporting;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressLint("Registered")
public class MainActivity extends AppCompatActivity {

    TextInputEditText case_no, pick_date, reporting_officer, prepared_by, incident;
    TextInputLayout caseText, dateText, reportText, prepareText, incidentText;
    Button report;
    SweetAlertDialog pb;
    String message,caseNo,caseDate,prepareBy, reportingOfficer, Incident;
    String serverUrl = "http://darajephtechnologies.com/accident_report/crime_report/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        case_no = findViewById(R.id.case_no);
        pick_date = findViewById(R.id.pick_date);
        reporting_officer = findViewById(R.id.reporting_officer);
        prepared_by = findViewById(R.id.prepared_by);
        incident = findViewById(R.id.incident);

        caseText = findViewById(R.id.caseText);
        dateText = findViewById(R.id.dateText);
        reportText = findViewById(R.id.reportText);
        prepareText = findViewById(R.id.prepareText);
        incidentText = findViewById(R.id.incidentText);

        report = findViewById(R.id.report);

        case_no.setText(randomAlphaNumeric(20));
        case_no.setFocusable(false);
        case_no.setFocusableInTouchMode(false);
        case_no.setFocusableInTouchMode(false);

        pick_date.setEnabled(true);
        pick_date.setTextIsSelectable(true);
        pick_date.setFocusable(true);
        pick_date.setFocusableInTouchMode(false);
        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_date();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caseNo = case_no.getText().toString();
                caseDate = pick_date.getText().toString();
                prepareBy = prepared_by.getText().toString();
                reportingOfficer = reporting_officer.getText().toString();
                Incident = incident.getText().toString();
                if (validateInput(case_no, caseText) && validateInput(pick_date, dateText) && validateInput(reporting_officer, reportText) && validateInput(prepared_by, prepareText) && validateInput(incident, incidentText)) {
                    pb = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pb.getProgressHelper().setBarColor(Color.parseColor("#3C5BFB"));
                    pb.setTitleText("Saving");
                    pb.setContentText("Please wait while app is sending the information to the server!");
                    pb.setCancelable(false);
                    pb.show();

                    SaveReport();
                }
            }
        });
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#&%";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    private boolean validateInput(TextInputEditText v, TextInputLayout txtView) {
        String mainHint = txtView.getHint().toString();
        String txt = v.getText().toString();
        if (txt.equals("") || txt.length() < 1) {
            txtView.setError("This field cannot be empty!");
            txtView.setErrorTextColor(ColorStateList.valueOf(0xFFB3003B));
            txtView.setBoxStrokeColor(0xFFB3003B);
            v.requestFocus();
            return false;
        } else {
            txtView.setHint(mainHint);
            txtView.setError(null);
            txtView.setErrorEnabled(false);
            txtView.setBoxStrokeColor(0xFF0A6A62);
            return true;
        }
    }

    private void SaveReport(){
        StringRequest request=new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                System.out.println("Update Image Response: "+response);
                pb.dismissWithAnimation();
                System.out.println("Registration Value get from server : " + response);
                int jsonResult = returnParsedJsonObject(response);
                JSONObject resGet = null;
                try {
                    resGet = new JSONObject(response);
                    //loginUser = resGet.getString("email");
                    message = resGet.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonResult == 0) {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(message)
                            .show();
                    return;
                }
                System.out.println("message from server : " + message);
                if (jsonResult == 1) {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(message)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                }
                            }).show();
                }
                System.out.println("Response from Server: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pb != null) {
                    pb.dismissWithAnimation();
                    pb = null;
                }
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Server connection failed " +error.toString())
                        .show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> map=new HashMap<String, String>();
                map.put("case_no",caseNo);
                map.put("case_date",caseDate);
                map.put("prepared_by",prepareBy);
                map.put("reporting_officer",reportingOfficer);
                map.put("incident",Incident);
                return map;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private int returnParsedJsonObject(String result) {
        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }

    private void pick_date() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String d = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                pick_date.setText(d);
            }
        }, year, month, day);
        picker.show();
    }
}
