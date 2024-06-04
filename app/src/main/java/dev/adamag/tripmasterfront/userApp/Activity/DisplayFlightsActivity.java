package dev.adamag.tripmasterfront.userApp.Activity;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

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
import dev.adamag.tripmasterfront.model.FlightResponse;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.CommandServiceImpl;
import dev.adamag.tripmasterfront.network.ObjectServiceImpl;
import dev.adamag.tripmasterfront.userApp.Adapter.FlightAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayFlightsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFlights;
    private FlightAdapter flightAdapter;
    private List<Flight> flightList;
    private boolean isReturningFromBooking = false;

    private String selectedAirline;
    private String selectedDepartureTime;
    private String selectedArrivalTime;
    private String selectedDepartureAirport;
    private String selectedArrivalAirport;
    private int selectedAdults;
    private int selectedChildren;
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
        FlightResponse flightResponse = (FlightResponse) intent.getSerializableExtra("flight_info");
        String userIdBoundaryJson = intent.getStringExtra("userIdBoundary");

        // Deserialize userIdBoundaryJson to UserIdBoundary object
        if (userIdBoundaryJson != null) {
            userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
        }

        if (tripType == null) tripType = "N/A";
        if (departureDate == null) departureDate = "N/A";
        if (returnDate == null) returnDate = "N/A";
        if (departureAirport == null) departureAirport = "N/A";
        if (arrivalAirport == null) arrivalAirport = "N/A";
        if (flightResponse == null) flightResponse = new FlightResponse();

        flightList = parseFlightInfo(flightResponse, departureDate, returnDate, extractCity(departureAirport), extractCity(arrivalAirport), adultCount, babyCount);

        recyclerViewFlights = findViewById(R.id.recyclerViewFlights);
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));
        flightAdapter = new FlightAdapter(flightList, this);
        recyclerViewFlights.setAdapter(flightAdapter);
    }

    private List<Flight> parseFlightInfo(FlightResponse flightResponse, String departureDate, String returnDate, String departureAirport, String arrivalAirport, int adults, int children) {
        List<Flight> flights = new ArrayList<>();
        List<String> airlines = flightResponse.getAirlines();
        List<String> urls = flightResponse.getUrls();

        for (int i = 0; i < airlines.size(); i++) {
            String airline = airlines.get(i);
            String url = urls.get(i);
            Flight flight = new Flight(
                    "flightID" + i,
                    airline,
                    departureDate,
                    returnDate,
                    departureAirport,
                    arrivalAirport,
                    "",
                    adults,
                    children
            );
            flight.getObjectDetails().put("url", url);
            flights.add(flight);
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

    public void setReturningFromBooking(boolean returningFromBooking, String airline, String departureTime, String arrivalTime, String departureAirport, String arrivalAirport, int adults, int children) {
        isReturningFromBooking = returningFromBooking;
        selectedAirline = airline;
        selectedDepartureTime = departureTime;
        selectedArrivalTime = arrivalTime;
        selectedDepartureAirport = departureAirport;
        selectedArrivalAirport = arrivalAirport;
        selectedAdults = adults;
        selectedChildren = children;
    }

    private void showBookingConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Did you successfully book the flight?")
                .setPositiveButton("Yes", (dialog, id) -> showCostInputDialog())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void showCostInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the cost of the flight");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, id) -> {
            String cost = input.getText().toString();
            handleFlightBooking(cost);
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    private void handleFlightBooking(String cost) {
        Flight flight = new Flight(
                "1",  // Placeholder ID
                selectedAirline,
                selectedDepartureTime,
                selectedArrivalTime,
                selectedDepartureAirport,
                selectedArrivalAirport,
                cost,
                selectedAdults,
                selectedChildren
        );

        BoundaryObject boundaryObject = flight.toBoundaryObject();

        boundaryObject.getCreatedBy().getUserId().setEmail(userIdBoundary.getEmail());
        boundaryObject.getCreatedBy().getUserId().setSuperapp(userIdBoundary.getSuperapp());

        ObjectServiceImpl serviceUtil = new ObjectServiceImpl();
        serviceUtil.createObject(boundaryObject, new Callback<BoundaryObject>() {
            @Override
            public void onResponse(Call<BoundaryObject> call, Response<BoundaryObject> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Object created: " + response.body());
                    // Create and send the command after successfully creating the object
                    createFlightBookingCommand(response.body(), cost);
//                    Intent intent = new Intent(DisplayFlightsActivity.this, DisplayBookedFlights.class);
//                    intent.putExtra("userIdBoundary", new Gson().toJson(userIdBoundary));
//                    startActivity(intent);
                } else {
                    Log.e(TAG, "Create object failed: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BoundaryObject> call, Throwable t) {
                Log.e(TAG, "Create object error: " + t.getMessage());
            }
        });

        Toast.makeText(this, "Flight booked: " + boundaryObject.toString(), Toast.LENGTH_LONG).show();
    }

    private void createFlightBookingCommand(BoundaryObject boundaryObject, String cost) {
        BoundaryCommand boundaryCommand = new BoundaryCommand();
        boundaryCommand.setCommandId(new BoundaryCommand.CommandId("yourSuperApp", "yourMiniApp", "1"));
        boundaryCommand.setCommand("Book a flight");

        BoundaryCommand.TargetObject targetObject = new BoundaryCommand.TargetObject();
        targetObject.setObjectId(new BoundaryCommand.TargetObject.ObjectId(boundaryObject.getObjectId().getSuperapp(), boundaryObject.getObjectId().getId()));
        boundaryCommand.setTargetObject(targetObject);

        boundaryCommand.setInvocationTimestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(new Date()));

        BoundaryCommand.InvokedBy invokedBy = new BoundaryCommand.InvokedBy();
        invokedBy.setUserId(new BoundaryCommand.InvokedBy.UserId(userIdBoundary.getSuperapp(), userIdBoundary.getEmail()));
        boundaryCommand.setInvokedBy(invokedBy);

        Map<String, Object> commandAttributes = new HashMap<>();
        commandAttributes.put("cost", cost);
        commandAttributes.put("airline", selectedAirline);
        commandAttributes.put("departureTime", selectedDepartureTime);
        commandAttributes.put("arrivalTime", selectedArrivalTime);
        commandAttributes.put("departureAirport", selectedDepartureAirport);
        commandAttributes.put("arrivalAirport", selectedArrivalAirport);
        commandAttributes.put("adults", selectedAdults);
        commandAttributes.put("children", selectedChildren);
        boundaryCommand.setCommandAttributes(commandAttributes);

        CommandServiceImpl commandService = new CommandServiceImpl();
        commandService.createCommand("userApp", boundaryCommand, new Callback<BoundaryCommand>() {
            @Override
            public void onResponse(Call<BoundaryCommand> call, Response<BoundaryCommand> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Command created: " + response.body());
                } else {
                    Log.e(TAG, "Create command failed: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BoundaryCommand> call, Throwable t) {
                Log.e(TAG, "Create command error: " + t.getMessage());
            }
        });
    }

}
