package dev.adamag.tripmasterfront.Activity.FlightActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.adamag.tripmasterfront.Activity.Adapter.FlightAdapterNoButton;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.Flight;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.ObjectService;
import dev.adamag.tripmasterfront.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayUserFlightsActivity extends MenuBarActivity {

    private RecyclerView recyclerViewFlights;
    private FlightAdapterNoButton flightAdapter;
    private TextView noFlightsTextView;
    private String userIdBoundaryJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_flights);
        setupBottomNavigationBar();

        recyclerViewFlights = findViewById(R.id.recyclerViewFlights);
        noFlightsTextView = findViewById(R.id.noFlightsText);

        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");

        fetchUserFlights();
    }

    private void fetchUserFlights() {
        if (userIdBoundaryJson == null) {
            Toast.makeText(this, "User information is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        User.UserIdBoundary userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);

        ObjectService objectService = RetrofitClient.getTripMasterClient().create(ObjectService.class);

        Call<List<BoundaryObject>> call = objectService.searchObjectsByType(
                "Flight",
                userIdBoundary.getSuperapp(),
                userIdBoundary.getEmail(),
                100,
                0
        );

        call.enqueue(new Callback<List<BoundaryObject>>() {
            @Override
            public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BoundaryObject> flights = response.body();

                    List<BoundaryObject> userFlights = new ArrayList<>();
                    for (BoundaryObject flightObj : flights) {
                        if (flightObj.getCreatedBy().getUserId().getEmail().equals(userIdBoundary.getEmail())) {
                            userFlights.add(flightObj);
                        }
                    }

                    if (userFlights.isEmpty()) {
                        noFlightsTextView.setVisibility(View.VISIBLE);
                        recyclerViewFlights.setVisibility(View.GONE);
                    } else {
                        noFlightsTextView.setVisibility(View.GONE);
                        List<Flight> flightList = parseFlightInfo(userFlights);
                        flightAdapter = new FlightAdapterNoButton(flightList, DisplayUserFlightsActivity.this);
                        recyclerViewFlights.setAdapter(flightAdapter);
                        recyclerViewFlights.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(DisplayUserFlightsActivity.this, "Failed to fetch flights", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                Toast.makeText(DisplayUserFlightsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Flight> parseFlightInfo(List<BoundaryObject> flightData) {
        List<Flight> flights = new ArrayList<>();

        for (BoundaryObject boundaryObject : flightData) {
            Map<String, Object> flightDetails = boundaryObject.getObjectDetails();
            Flight newFlight = Flight.fromJson(flightDetails);
            flights.add(newFlight);
        }
        return flights;
    }

    private int getIntValue(Map<String, Object> details, String key) {
        Object value = details.get(key);
        if (value instanceof Double) {
            return ((Double) value).intValue();
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return 0;
        }
    }
}
