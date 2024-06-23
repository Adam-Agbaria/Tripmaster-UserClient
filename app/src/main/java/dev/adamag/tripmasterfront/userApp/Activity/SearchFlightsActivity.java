package dev.adamag.tripmasterfront.userApp.Activity;

import dev.adamag.tripmasterfront.model.User.UserIdBoundary;
import dev.adamag.tripmasterfront.model.Flight;
import java.util.List;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.FlightResponse;
import dev.adamag.tripmasterfront.network.ScrapingServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFlightsActivity extends MenuBarActivity {
    private MaterialButton takeoffDateButton;
    private MaterialButton landDateButton;
    private AutoCompleteTextView textViewTakeoff;
    private AutoCompleteTextView textViewLanding;

    private TextView adultNumLabel;
    private TextView babyNumLabel;
    private String tripType = "Round Trip";
    private String userIdBoundaryJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);
        setupBottomNavigationBar();

        setupTripTypeButtons();
        setupLocationPicker();
        setupDatePickers();
        setupPassengerCounters();
        setupSearchButton();

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");
    }

    private void setupSearchButton() {
        MaterialButton searchFlightsButton = findViewById(R.id.Sf_search_BTN);
        searchFlightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFlightQueryToServer();
            }
        });
    }

    private void setupTripTypeButtons() {
        MaterialButton oneWayButton = findViewById(R.id.SF_One_way_BTN);
        MaterialButton roundTripButton = findViewById(R.id.SF_Round_Trip_BTN);

        updateButtonStyles(oneWayButton, roundTripButton);

        oneWayButton.setOnClickListener(v -> {
            tripType = "One Way";
            updateButtonStyles(oneWayButton, roundTripButton);
            landDateButton.setVisibility(View.GONE);
        });

        roundTripButton.setOnClickListener(v -> {
            tripType = "Round Trip";
            updateButtonStyles(oneWayButton, roundTripButton);
            landDateButton.setVisibility(View.VISIBLE);
        });
    }

    private void updateButtonStyles(MaterialButton oneWayButton, MaterialButton roundTripButton) {
        if (tripType.equals("One Way")) {
            oneWayButton.setBackgroundColor(getResources().getColor(R.color.mainColor));
            oneWayButton.setTextColor(getResources().getColor(R.color.white));
            roundTripButton.setBackgroundColor(getResources().getColor(R.color.white));
            roundTripButton.setTextColor(getResources().getColor(R.color.black));
        } else {
            roundTripButton.setBackgroundColor(getResources().getColor(R.color.mainColor));
            roundTripButton.setTextColor(getResources().getColor(R.color.white));
            oneWayButton.setBackgroundColor(getResources().getColor(R.color.white));
            oneWayButton.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void setupLocationPicker() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        textViewTakeoff = findViewById(R.id.SF_takeoff_field);
        textViewLanding = findViewById(R.id.SF_land_field);

        textViewTakeoff.setAdapter(adapter);
        textViewLanding.setAdapter(adapter);
    }

    private void setupDatePickers() {
        takeoffDateButton = findViewById(R.id.SF_takeoff_date_LBL);
        landDateButton = findViewById(R.id.SF_land_date_lbl);

        Calendar today = Calendar.getInstance();
        String todayDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.MONTH) + 1,
                today.get(Calendar.YEAR));
        takeoffDateButton.setText(todayDate);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrowDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                tomorrow.get(Calendar.DAY_OF_MONTH),
                tomorrow.get(Calendar.MONTH) + 1,
                tomorrow.get(Calendar.YEAR));
        landDateButton.setText(tomorrowDate);

        takeoffDateButton.setOnClickListener(v -> showDatePickerDialog(takeoffDateButton));
        landDateButton.setOnClickListener(v -> showDatePickerDialog(landDateButton));
    }

    private void showDatePickerDialog(MaterialButton dateButton) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                    dateButton.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void setupPassengerCounters() {
        ImageView addAdult = findViewById(R.id.SF_addAdult_IMG);
        ImageView removeAdult = findViewById(R.id.SF_removeAdult_IMG);
        ImageView addBaby = findViewById(R.id.SF_addBaby_IMG);
        ImageView removeBaby = findViewById(R.id.SF_removeBaby_IMG);

        adultNumLabel = findViewById(R.id.SF_adultNym_LBL);
        babyNumLabel = findViewById(R.id.SF_babyNum_LBL);

        addAdult.setOnClickListener(v -> incrementCounter(adultNumLabel));
        removeAdult.setOnClickListener(v -> decrementCounter(adultNumLabel));

        addBaby.setOnClickListener(v -> incrementCounter(babyNumLabel));
        removeBaby.setOnClickListener(v -> decrementCounter(babyNumLabel));
    }

    private void incrementCounter(TextView counterLabel) {
        int count = Integer.parseInt(counterLabel.getText().toString());
        count++;
        counterLabel.setText(String.valueOf(count));
    }

    private void decrementCounter(TextView counterLabel) {
        int count = Integer.parseInt(counterLabel.getText().toString());
        if (count > 0) {
            count--;
        }
        counterLabel.setText(String.valueOf(count));
    }

    private String extractCity(String location) {
        if (location != null) {
            Pattern pattern = Pattern.compile("\\b[A-Z]{3}\\b");
            Matcher matcher = pattern.matcher(location);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return location;
    }


    private void sendFlightQueryToServer() {
        String departureDate = takeoffDateButton.getText().toString();
        String returnDate = landDateButton.getText().toString();
        String departureAirport = textViewTakeoff.getText().toString();
        String arrivalAirport = textViewLanding.getText().toString();
        int adultCount = Integer.parseInt(adultNumLabel.getText().toString());
        int babyCount = Integer.parseInt(babyNumLabel.getText().toString());
        String departureCity = extractCity(departureAirport);
        String arrivalCity = extractCity(arrivalAirport);
        ScrapingServiceImpl.getFlightInfo(tripType, departureDate, returnDate, departureCity, arrivalCity, adultCount, babyCount, new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (response.isSuccessful()) {
                        Log.d("FlightSearchResponse", "Response: " + response);
                        Log.d("FlightSearchResponse", "Response Body: " + response.body().toString());  // Log the body of the response

                        List<Map<String, Object>> flights = response.body();

                        Intent intent = new Intent(SearchFlightsActivity.this, DisplayFlightsActivity.class);
                        String departureCity = extractCity(departureAirport);
                        String arrivalCity = extractCity(arrivalAirport);

                        intent.putExtra("tripType", tripType);
                        intent.putExtra("departureDate", departureDate);
                        intent.putExtra("returnDate", returnDate);
                        intent.putExtra("departureAirport", departureCity);
                        intent.putExtra("arrivalAirport", arrivalCity);
                        intent.putExtra("adultCount", adultCount);
                        intent.putExtra("babyCount", babyCount);
                        intent.putExtra("userIdBoundary", userIdBoundaryJson);

                        Gson gson = new Gson();
                        String flightsJson = gson.toJson(flights);
                        Log.d("FlightSearchResponse", "Flights JSON: " + flightsJson);  // Log the JSON string of flights
                        intent.putExtra("flight_info", flightsJson);

                        startActivity(intent);
                    } else {
                        Log.d("FlightSearchError", "Error Response: " + response.errorBody().toString());
                    }
                }, 1000); // 1000 milliseconds = 1 second
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.d("FlightSearchError", "Network Error: " + t.getMessage());
            }
        });
    }
}