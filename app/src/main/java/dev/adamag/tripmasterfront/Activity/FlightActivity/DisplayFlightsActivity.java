package dev.adamag.tripmasterfront.Activity.FlightActivity;

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

import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryCommand;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.Flight;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.CommandServiceImpl;
import dev.adamag.tripmasterfront.network.ObjectServiceImpl;
import dev.adamag.tripmasterfront.Activity.Adapter.FlightAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayFlightsActivity extends MenuBarActivity {
    private RecyclerView recyclerViewFlights;
    private FlightAdapter flightAdapter;
    private List<Flight> flightList;
    private boolean isReturningFromBooking = false;

    private String selectedAirline;
    private String selectedOutboundDeparture;
    private String selectedOutboundArrival;
    private String selectedReturnDeparture;
    private String selectedReturnArrival;
    private String selectedDepartureAirport;
    private String selectedArrivalAirport;
    private String selectedCost;
    private int selectedAdults;
    private int selectedChildren;
    private String selectedDepartureDate;
    private String selectedReturnDate;
    private User.UserIdBoundary userIdBoundary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_results);

        // Retrieve the data from the Intent
        Intent intent = getIntent();
        String tripType = intent.getStringExtra("tripType");
        String departureDate = intent.getStringExtra("departureDate");
        String returnDate = intent.getStringExtra("returnDate");
        String departureAirport = intent.getStringExtra("departureAirport");
        String arrivalAirport = intent.getStringExtra("arrivalAirport");
        int adultCount = intent.getIntExtra("adultCount", 1);
        int babyCount = intent.getIntExtra("babyCount", 0);

        String flightsJson = intent.getStringExtra("flight_info");

        Log.d("IntentData", "tripType: " + tripType);
        Log.d("IntentData", "departureDate: " + departureDate);
        Log.d("IntentData", "returnDate: " + returnDate);
        Log.d("IntentData", "departureAirport: " + departureAirport);
        Log.d("IntentData", "arrivalAirport: " + arrivalAirport);
        Log.d("IntentData", "adultCount: " + adultCount);
        Log.d("IntentData", "babyCount: " + babyCount);
        Log.d("IntentData", "flightsJson: " + flightsJson);
        String userIdBoundaryJson = intent.getStringExtra("userIdBoundary");


        Log.d("IntentData", "userIdBoundaryJson: " + userIdBoundaryJson);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
        List<Map<String, Object>> flightResponse = gson.fromJson(flightsJson, listType);

        // Deserialize userIdBoundaryJson to UserIdBoundary object
        if (userIdBoundaryJson != null) {
            userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
            Log.d("UserIdBoundary", "userIdBoundary: " + userIdBoundary);
        } else {
            Log.e("UserIdBoundary", "userIdBoundaryJson is null");
        }

        if (tripType == null) tripType = "N/A";
        if (departureDate == null) departureDate = "N/A";
        if (returnDate == null) returnDate = "N/A";
        if (departureAirport == null) departureAirport = "N/A";
        if (arrivalAirport == null) arrivalAirport = "N/A";
        if (flightResponse == null) flightResponse = new ArrayList<>();
        flightList = parseFlightInfo(flightResponse, extractCity(departureAirport), extractCity(arrivalAirport), adultCount, babyCount, departureDate, returnDate);

        recyclerViewFlights = findViewById(R.id.recyclerViewFlights);
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));
        flightAdapter = new FlightAdapter(flightList, this);
        recyclerViewFlights.setAdapter(flightAdapter);
        setupBottomNavigationBar();
    }

    private List<Flight> parseFlightInfo(List<Map<String, Object>> flightData, String departureAirport, String arrivalAirport, int adults, int children, String departureDate, String returnDate) {
        List<Flight> flights = new ArrayList<>();

        for (Map<String, Object> flightDetails : flightData) {
            // Creating a new Flight object using the fromJson method
            Flight newFlight = new Flight(
                    (String) flightDetails.get("link"), // Use the link as the flight ID
                    (String) flightDetails.get("airline"),
                    (String) flightDetails.get("outboundDeparture"),
                    (String) flightDetails.get("outboundArrival"),
                    (String) flightDetails.get("returnDeparture"),
                    (String) flightDetails.get("returnArrival"),
                    departureAirport, // Override with the parameter
                    arrivalAirport,  // Override with the parameter
                    (String) flightDetails.get("price"),
                    adults, // Override with the parameter
                    children, // Override with the parameter
                    departureDate,
                    returnDate
            );
            flights.add(newFlight);
        }
        return flights;
    }

    private String extractCity(String location) {
        if (location != null && location.contains(",")) {
            return location.split(",")[0];
        }
        return location;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReturningFromBooking) {
            showBookingConfirmationDialog();
            isReturningFromBooking = false;
        }
    }

    public void setReturningFromBooking(boolean returningFromBooking, String airline, String outboundDeparture, String outboundArrival,
                                        String returnDeparture, String returnArrival, String departureAirport, String arrivalAirport,
                                        String cost, int adults, int children, String departureDate, String returnDate) {
        isReturningFromBooking = returningFromBooking;
        selectedAirline = airline;
        selectedOutboundDeparture = outboundDeparture;
        selectedOutboundArrival = outboundArrival;
        selectedReturnDeparture = returnDeparture;
        selectedReturnArrival = returnArrival;
        selectedDepartureAirport = departureAirport;
        selectedArrivalAirport = arrivalAirport;
        selectedCost = cost;
        selectedAdults = adults;
        selectedChildren = children;
        selectedDepartureDate = departureDate;
        selectedReturnDate = returnDate;
    }

    private void showBookingConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Did you successfully book the flight?")
                .setPositiveButton("Yes", (dialog, id) -> handleFlightBooking())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void handleFlightBooking() {
        Flight flight = new Flight(
                "1",  // Placeholder ID
                selectedAirline,
                selectedOutboundDeparture,
                selectedOutboundArrival,
                selectedReturnDeparture,
                selectedReturnArrival,
                selectedDepartureAirport,
                selectedArrivalAirport,
                selectedCost,
                selectedAdults,
                selectedChildren,
                selectedDepartureDate,
                selectedReturnDate
        );

        BoundaryObject boundaryObject = flight.toBoundaryObject();

        boundaryObject.getCreatedBy().getUserId().setEmail(userIdBoundary.getEmail());
        boundaryObject.getCreatedBy().getUserId().setSuperapp(userIdBoundary.getSuperapp());

        ObjectServiceImpl serviceUtil = new ObjectServiceImpl();
        serviceUtil.createObject(boundaryObject, new Callback<BoundaryObject>() {
            @Override
            public void onResponse(Call<BoundaryObject> call, Response<BoundaryObject> response) {
                if (response.isSuccessful()) {
                    Log.d("FlightCreated", "Object created: " + response.body());
                    // Create and send the command after successfully creating the object
                } else {
                    Log.e("FlightCreateError", "Create object failed: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BoundaryObject> call, Throwable t) {
                Log.e("FlightCreateError", "Create object error: " + t.getMessage());
            }
        });

        Toast.makeText(this, "Flight booked!", Toast.LENGTH_LONG).show();
    }



}
