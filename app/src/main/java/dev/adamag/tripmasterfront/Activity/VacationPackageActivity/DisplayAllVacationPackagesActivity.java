package dev.adamag.tripmasterfront.Activity.VacationPackageActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import dev.adamag.tripmasterfront.Activity.Adapter.PackagesAdapter;
import dev.adamag.tripmasterfront.Activity.FlightActivity.MenuBarActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.ObjectService;
import dev.adamag.tripmasterfront.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayAllVacationPackagesActivity extends MenuBarActivity {

    private RecyclerView recyclerView;
    private PackagesAdapter packagesAdapter;
    private TextView noPackagesTextView;
    private String userIdBoundaryJson;
    private User.UserIdBoundary userIdBoundary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_vacation_packages);
        setupBottomNavigationBar();

        recyclerView = findViewById(R.id.recycler_view);
        noPackagesTextView = findViewById(R.id.no_packages_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");
        if (userIdBoundaryJson != null) {
            userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
        } else {
            Toast.makeText(this, "User information is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        fetchVacationPackages();
    }

    private void fetchVacationPackages() {
        ObjectService objectService = RetrofitClient.getTripMasterClient().create(ObjectService.class);

        Call<List<BoundaryObject>> call = objectService.searchObjectsByType(
                "VacationPackage",
                userIdBoundary.getSuperapp(),
                userIdBoundary.getEmail(),
                100,
                0
        );

        call.enqueue(new Callback<List<BoundaryObject>>() {
            @Override
            public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BoundaryObject> packages = response.body();

                    if (packages.isEmpty()) {
                        noPackagesTextView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        noPackagesTextView.setVisibility(View.GONE);
                        packagesAdapter = new PackagesAdapter(packages);
                        recyclerView.setAdapter(packagesAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(DisplayAllVacationPackagesActivity.this, "Failed to fetch packages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                Toast.makeText(DisplayAllVacationPackagesActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
