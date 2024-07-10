package dev.adamag.tripmasterfront.Activity;

import android.content.Intent;
import android.os.Bundle;

import dev.adamag.tripmasterfront.Activity.CheckListActivity.DisplayCheckListActivity;
import dev.adamag.tripmasterfront.Activity.FlightActivity.DisplayUserFlightsActivity;
import dev.adamag.tripmasterfront.Activity.FlightActivity.MenuBarActivity;
import dev.adamag.tripmasterfront.Activity.FlightActivity.SearchFlightsActivity;
import dev.adamag.tripmasterfront.Activity.HotelActivity.DisplayUserHotelsActivity;
import dev.adamag.tripmasterfront.Activity.HotelActivity.SearchHotelsActivity;
import dev.adamag.tripmasterfront.Activity.VacationPackageActivity.DisplayAllVacationPackagesActivity;
import dev.adamag.tripmasterfront.R;

public class HomeActivity extends MenuBarActivity {
    private String userIdBoundaryJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");

        findViewById(R.id.bookFlightIcon).setOnClickListener(v -> openActivity(SearchFlightsActivity.class));
        findViewById(R.id.bookHotelIcon).setOnClickListener(v -> openActivity(SearchHotelsActivity.class));
        findViewById(R.id.bookPackageIcon).setOnClickListener(v -> openActivity(DisplayAllVacationPackagesActivity.class));
        findViewById(R.id.myFlightsIcon).setOnClickListener(v -> openActivity(DisplayUserFlightsActivity.class));
        findViewById(R.id.myHotelsIcon).setOnClickListener(v -> openActivity(DisplayUserHotelsActivity.class));
        findViewById(R.id.myChecklistIcon).setOnClickListener(v -> openActivity(DisplayCheckListActivity.class));
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("userIdBoundary", userIdBoundaryJson);
        startActivity(intent);
    }
}
