package dev.adamag.tripmasterfront.Activity.HotelActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.network.ScrapingHotelServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchHotelsActivity extends AppCompatActivity {
    private Button checkInDateButton;
    private Button checkOutDateButton;
    private AutoCompleteTextView cityTextView;
    private TextView adultNumLabel;
    private TextView childrenNumLabel;
    private Spinner roomCountSpinner;
    private String userIdBoundaryJson;
    private ProgressBar loadingIndicator;
    private RelativeLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hotels);

        setupLocationPicker();
        setupDatePickers();
        setupPassengerCounters();
        setupRoomCountSpinner();
        setupSearchButton();

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");

        // Initialize loading overlay and indicator
        loadingOverlay = findViewById(R.id.loadingOverlay);
        loadingIndicator = findViewById(R.id.loadingIndicator);
    }

    private void setupSearchButton() {
        Button searchHotelsButton = findViewById(R.id.SH_search_BTN);
        searchHotelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                sendHotelQueryToServer();
            }
        });
    }

    private void setupLocationPicker() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.destinations_array, android.R.layout.simple_dropdown_item_1line);
        cityTextView = findViewById(R.id.SH_city_field);
        cityTextView.setAdapter(adapter);
    }

    private void setupDatePickers() {
        checkInDateButton = findViewById(R.id.SH_check_in_date_LBL);
        checkOutDateButton = findViewById(R.id.SH_check_out_date_lbl);

        Calendar today = Calendar.getInstance();
        String todayDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.MONTH) + 1,
                today.get(Calendar.YEAR));
        checkInDateButton.setText(todayDate);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrowDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                tomorrow.get(Calendar.DAY_OF_MONTH),
                tomorrow.get(Calendar.MONTH) + 1,
                tomorrow.get(Calendar.YEAR));
        checkOutDateButton.setText(tomorrowDate);

        checkInDateButton.setOnClickListener(v -> showDatePickerDialog(checkInDateButton));
        checkOutDateButton.setOnClickListener(v -> showDatePickerDialog(checkOutDateButton));
    }

    private void showDatePickerDialog(Button dateButton) {
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
        ImageView addAdult = findViewById(R.id.SH_addAdult_IMG);
        ImageView removeAdult = findViewById(R.id.SH_removeAdult_IMG);
        ImageView addChild = findViewById(R.id.SH_addChild_IMG);
        ImageView removeChild = findViewById(R.id.SH_removeChild_IMG);

        adultNumLabel = findViewById(R.id.SH_adultNum_LBL);
        childrenNumLabel = findViewById(R.id.SH_childrenNum_LBL);

        addAdult.setOnClickListener(v -> incrementCounter(adultNumLabel));
        removeAdult.setOnClickListener(v -> decrementCounter(adultNumLabel));

        addChild.setOnClickListener(v -> incrementCounter(childrenNumLabel));
        removeChild.setOnClickListener(v -> decrementCounter(childrenNumLabel));
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

    private void setupRoomCountSpinner() {
        roomCountSpinner = findViewById(R.id.roomCountSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.room_counts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomCountSpinner.setAdapter(adapter);
    }

    private void showLoading() {
        loadingOverlay.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingOverlay.setVisibility(View.GONE);
    }


    private void sendHotelQueryToServer() {
        String checkInDate = checkInDateButton.getText().toString();
        String checkOutDate = checkOutDateButton.getText().toString();
        String city = cityTextView.getText().toString();
        int adultCount = Integer.parseInt(adultNumLabel.getText().toString());
        int childrenCount = Integer.parseInt(childrenNumLabel.getText().toString());
        int roomCount = Integer.parseInt(roomCountSpinner.getSelectedItem().toString());

        ScrapingHotelServiceImpl.getHotelInfo(city, checkInDate, checkOutDate, adultCount, childrenCount, 1, new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (response.isSuccessful()) {
                        Log.d("HotelSearchResponse", "Response: " + response);
                        Log.d("HotelSearchResponse", "Response Body: " + response.body().toString());  // Log the body of the response

                        List<Map<String, Object>> hotels = response.body();

                        Intent intent = new Intent(SearchHotelsActivity.this, DisplayHotelsActivity.class);
                        intent.putExtra("city", city);
                        intent.putExtra("checkInDate", checkInDate);
                        intent.putExtra("checkOutDate", checkOutDate);
                        intent.putExtra("adultCount", adultCount);
                        intent.putExtra("childrenCount", childrenCount);
                        intent.putExtra("roomCount", roomCount);

                        intent.putExtra("userIdBoundary", userIdBoundaryJson);

                        Gson gson = new Gson();
                        String hotelsJson = gson.toJson(hotels);
                        Log.d("HotelSearchResponse", "Hotels JSON: " + hotelsJson);  // Log the JSON string of hotels
                        intent.putExtra("hotel_info", hotelsJson);

                        startActivity(intent);
                    } else {
                        Log.d("HotelSearchError", "Error Response: " + response.errorBody().toString());
                    }
                    hideLoading();
                }, 1000); // 1000 milliseconds = 1 second
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                hideLoading();
                Log.d("HotelSearchError", "Network Error: " + t.getMessage());
            }
        });
    }
}
