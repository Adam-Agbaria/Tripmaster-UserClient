package dev.adamag.tripmasterfront.Activity.FlightActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import dev.adamag.tripmasterfront.Activity.VacationPackageActivity.DisplayAllVacationPackagesActivity;
import dev.adamag.tripmasterfront.R;

public abstract class MenuBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not setContentView here
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupBottomNavigationBar();
    }

    protected void setupBottomNavigationBar() {
        // Make sure to set the content view to include the layout with the bottom bar
        findViewById(R.id.homeButton).setOnClickListener(v -> navigateTo(DisplayUserFlightsActivity.class));
        findViewById(R.id.ticketButton).setOnClickListener(v -> navigateTo(SearchFlightsActivity.class));
        findViewById(R.id.notificationButton).setOnClickListener(v -> navigateTo(SearchFlightsActivity.class));
        findViewById(R.id.bagButton).setOnClickListener(v -> navigateTo(DisplayAllVacationPackagesActivity.class));
    }

    private void navigateTo(Class<?> activityClass) {
        if (this.getClass() != activityClass) {
            Intent intent = new Intent(this, activityClass);
            // Copy all extras from the current intent to the new intent
            if (getIntent().getExtras() != null) {
                intent.putExtras(getIntent().getExtras());
            }
            startActivity(intent);
        }
    }
}
