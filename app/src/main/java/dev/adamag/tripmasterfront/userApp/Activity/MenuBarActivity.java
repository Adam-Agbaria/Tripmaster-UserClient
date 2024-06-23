package dev.adamag.tripmasterfront.userApp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import dev.adamag.tripmasterfront.R;

public abstract class MenuBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNavigationBar() {
        // Setup navigation buttons
        findViewById(R.id.homeButton).setOnClickListener(v -> navigateTo(SearchFlightsActivity.class));
        findViewById(R.id.ticketButton).setOnClickListener(v -> navigateTo(SearchFlightsActivity.class));
        findViewById(R.id.notificationButton).setOnClickListener(v -> navigateTo(SearchFlightsActivity.class));
        findViewById(R.id.bagButton).setOnClickListener(v -> navigateTo(SearchFlightsActivity.class));
    }

    private void navigateTo(Class<?> activityClass) {
        if (this.getClass() != activityClass) {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        }
    }
}
