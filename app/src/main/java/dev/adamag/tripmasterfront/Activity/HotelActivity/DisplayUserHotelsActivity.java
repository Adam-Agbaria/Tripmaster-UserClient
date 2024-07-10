package dev.adamag.tripmasterfront.Activity.HotelActivity;

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

import dev.adamag.tripmasterfront.Activity.Adapter.HotelAdapterNoButton;
import dev.adamag.tripmasterfront.Activity.FlightActivity.MenuBarActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.Hotel;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.ObjectService;
import dev.adamag.tripmasterfront.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayUserHotelsActivity extends MenuBarActivity {

    private RecyclerView recyclerViewHotels;
    private HotelAdapterNoButton hotelAdapter;
    private TextView noHotelsTextView;
    private String userIdBoundaryJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_hotels);
        setupBottomNavigationBar();

        recyclerViewHotels = findViewById(R.id.recyclerViewHotels);
        noHotelsTextView = findViewById(R.id.noHotelsText);

        recyclerViewHotels.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");

        fetchUserHotels();
    }

    private void fetchUserHotels() {
        if (userIdBoundaryJson == null) {
            Toast.makeText(this, "User information is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        User.UserIdBoundary userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);

        ObjectService objectService = RetrofitClient.getTripMasterClient().create(ObjectService.class);

        Call<List<BoundaryObject>> call = objectService.searchObjectsByType(
                "Hotel",
                userIdBoundary.getSuperapp(),
                userIdBoundary.getEmail(),
                100,
                0
        );

        call.enqueue(new Callback<List<BoundaryObject>>() {
            @Override
            public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BoundaryObject> hotels = response.body();

                    List<BoundaryObject> userHotels = new ArrayList<>();
                    for (BoundaryObject hotelObj : hotels) {
                        if (hotelObj.getCreatedBy().getUserId().getEmail().equals(userIdBoundary.getEmail())) {
                            userHotels.add(hotelObj);
                        }
                    }

                    if (userHotels.isEmpty()) {
                        noHotelsTextView.setVisibility(View.VISIBLE);
                        recyclerViewHotels.setVisibility(View.GONE);
                    } else {
                        noHotelsTextView.setVisibility(View.GONE);
                        List<Hotel> hotelList = parseHotelInfo(userHotels);
                        hotelAdapter = new HotelAdapterNoButton(hotelList, DisplayUserHotelsActivity.this);
                        recyclerViewHotels.setAdapter(hotelAdapter);
                        recyclerViewHotels.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(DisplayUserHotelsActivity.this, "Failed to fetch hotels", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                Toast.makeText(DisplayUserHotelsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Hotel> parseHotelInfo(List<BoundaryObject> hotelData) {
        List<Hotel> hotels = new ArrayList<>();

        for (BoundaryObject boundaryObject : hotelData) {
            Map<String, Object> hotelDetails = boundaryObject.getObjectDetails();
            Hotel newHotel = Hotel.fromJson(hotelDetails);
            hotels.add(newHotel);
        }
        return hotels;
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
