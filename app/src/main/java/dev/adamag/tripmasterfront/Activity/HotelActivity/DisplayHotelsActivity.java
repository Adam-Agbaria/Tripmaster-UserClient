package dev.adamag.tripmasterfront.Activity.HotelActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.adamag.tripmasterfront.Activity.Adapter.HotelAdapter;
import dev.adamag.tripmasterfront.Activity.FlightActivity.MenuBarActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryCommand;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.Hotel;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.CommandServiceImpl;
import dev.adamag.tripmasterfront.network.ObjectServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayHotelsActivity extends MenuBarActivity {
    private RecyclerView recyclerViewHotels;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private boolean isReturningFromBooking = false;

    private String selectedHotelName;
    private String selectedCity;
    private String selectedCheckInDate;
    private String selectedCheckOutDate;
    private String selectedUrl;
    private String selectedCost;
    private int selectedAdults;
    private int selectedChildren;
    private int selectedRooms;
    private User.UserIdBoundary userIdBoundary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_results);

        // Retrieve the data from the Intent
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String checkInDate = intent.getStringExtra("checkInDate");
        String checkOutDate = intent.getStringExtra("checkOutDate");
        int adultCount = intent.getIntExtra("adultCount", 1);
        int childCount = intent.getIntExtra("childrenCount", 0);
        int roomCount = intent.getIntExtra("roomCount", 1);

        String hotelsJson = intent.getStringExtra("hotel_info");

        Log.d("IntentData", "city: " + city);
        Log.d("IntentData", "checkInDate: " + checkInDate);
        Log.d("IntentData", "checkOutDate: " + checkOutDate);
        Log.d("IntentData", "adultCount: " + adultCount);
        Log.d("IntentData", "childrenCount: " + childCount);
        Log.d("IntentData", "roomCount: " + roomCount);
        Log.d("IntentData", "hotelsJson: " + hotelsJson);
        String userIdBoundaryJson = intent.getStringExtra("userIdBoundary");

        Log.d("IntentData", "userIdBoundaryJson: " + userIdBoundaryJson);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
        List<Map<String, Object>> hotelResponse = gson.fromJson(hotelsJson, listType);

        // Deserialize userIdBoundaryJson to UserIdBoundary object
        if (userIdBoundaryJson != null) {
            userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
            Log.d("UserIdBoundary", "userIdBoundary: " + userIdBoundary);
        } else {
            Log.e("UserIdBoundary", "userIdBoundaryJson is null");
        }

        if (city == null) city = "N/A";
        if (checkInDate == null) checkInDate = "N/A";
        if (checkOutDate == null) checkOutDate = "N/A";
        if (hotelResponse == null) hotelResponse = new ArrayList<>();
        hotelList = parseHotelInfo(hotelResponse, city, checkInDate, checkOutDate, adultCount, childCount, roomCount);

        recyclerViewHotels = findViewById(R.id.recyclerViewHotels);
        recyclerViewHotels.setLayoutManager(new LinearLayoutManager(this));
        hotelAdapter = new HotelAdapter(hotelList, this);
        recyclerViewHotels.setAdapter(hotelAdapter);
        setupBottomNavigationBar();
    }

    private List<Hotel> parseHotelInfo(List<Map<String, Object>> hotelData, String city, String checkInDate, String checkOutDate, int adults, int children, int rooms) {
        List<Hotel> hotels = new ArrayList<>();

        for (Map<String, Object> hotelDetails : hotelData) {
            String hotelName = hotelDetails.get("hotel_name") != null ? (String) hotelDetails.get("hotel_name") : "N/A";
            String url = hotelDetails.get("url") != null ? (String) hotelDetails.get("url") : "N/A";
            String price = hotelDetails.get("price") != null ? (String) hotelDetails.get("price") : "N/A";

            // Creating a new Hotel object
            Hotel newHotel = new Hotel(
                    city,
                    checkInDate,
                    checkOutDate,
                    adults,
                    children,
                    rooms,
                    hotelName,
                    url,
                    price
            );
            hotels.add(newHotel);
        }
        return hotels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReturningFromBooking) {
            showBookingConfirmationDialog();
            isReturningFromBooking = false;
        }
    }

    public void setReturningFromBooking(boolean returningFromBooking, String hotelName, String city, String checkInDate, String checkOutDate, String url, String cost, int adults, int children, int rooms) {
        isReturningFromBooking = returningFromBooking;
        selectedHotelName = hotelName;
        selectedCity = city;
        selectedCheckInDate = checkInDate;
        selectedCheckOutDate = checkOutDate;
        selectedUrl = url;
        selectedCost = cost;
        selectedAdults = adults;
        selectedChildren = children;
        selectedRooms = rooms;
    }

    private void showBookingConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Did you successfully book the hotel?")
                .setPositiveButton("Yes", (dialog, id) -> handleHotelBooking())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void handleHotelBooking() {
        Hotel hotel = new Hotel(
                selectedCity,
                selectedCheckInDate,
                selectedCheckOutDate,
                selectedAdults,
                selectedChildren,
                selectedRooms,
                selectedHotelName,
                selectedUrl,
                selectedCost
        );

        BoundaryObject boundaryObject = hotel.toBoundaryObject();

        boundaryObject.getCreatedBy().getUserId().setEmail(userIdBoundary.getEmail());
        boundaryObject.getCreatedBy().getUserId().setSuperapp(userIdBoundary.getSuperapp());

        ObjectServiceImpl serviceUtil = new ObjectServiceImpl();
        serviceUtil.createObject(boundaryObject, new Callback<BoundaryObject>() {
            @Override
            public void onResponse(Call<BoundaryObject> call, Response<BoundaryObject> response) {
                if (response.isSuccessful()) {
                    Log.d("HotelCreated", "Object created: " + response.body());
                    // Create and send the command after successfully creating the object
//                    createHotelBookingCommand(response.body());
                } else {
                    Log.e("HotelCreateError", "Create object failed: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BoundaryObject> call, Throwable t) {
                Log.e("HotelCreateError", "Create object error: " + t.getMessage());
            }
        });

        Toast.makeText(this, "Hotel booked!", Toast.LENGTH_LONG).show();
    }


}
