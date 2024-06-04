package dev.adamag.tripmasterfront.userApp.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.Flight;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.ObjectServiceImpl;
import dev.adamag.tripmasterfront.userApp.Adapter.FlightAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayBookedFlights extends AppCompatActivity {
    private RecyclerView recyclerViewFlights;
    private FlightAdapter flightAdapter;
    private List<Flight> flightList = new ArrayList<>();
    private User.UserIdBoundary userIdBoundary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_flights);

        Intent intent = getIntent();
        String userIdBoundaryJson = intent.getStringExtra("userIdBoundary");

        if (userIdBoundaryJson != null) {
            userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
        }

        recyclerViewFlights = findViewById(R.id.recyclerViewFlights);
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));
        flightAdapter = new FlightAdapter(flightList, this);
        recyclerViewFlights.setAdapter(flightAdapter);

        fetchBookedFlights();
    }

    private void fetchBookedFlights() {
        ObjectServiceImpl serviceUtil = new ObjectServiceImpl();
        serviceUtil.getAllObjects(new Callback<List<BoundaryObject>>() {
            @Override
            public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (BoundaryObject obj : response.body()) {
                        if ("Flight".equals(obj.getType()) &&
                                userIdBoundary.getEmail().equals(((BoundaryObject.CreatedByBoundary.UserIdBoundary) obj.getCreatedBy().getUserId()).getEmail())) {
                            Flight flight = Flight.fromBoundaryObject(obj);
                            flightList.add(flight);
                        }
                    }
                    flightAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DisplayBookedFlights.this, "Failed to fetch booked flights", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                Toast.makeText(DisplayBookedFlights.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
