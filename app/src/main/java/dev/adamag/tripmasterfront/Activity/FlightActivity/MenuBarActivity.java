package dev.adamag.tripmasterfront.Activity.FlightActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import dev.adamag.tripmasterfront.Activity.AuthActivity.LoginActivity;
import dev.adamag.tripmasterfront.Activity.CheckListActivity.DisplayCheckListActivity;
import dev.adamag.tripmasterfront.Activity.HomeActivity;
import dev.adamag.tripmasterfront.Activity.NotificationActivity.NotificationActivity;
import dev.adamag.tripmasterfront.Activity.VacationPackageActivity.DisplayAllVacationPackagesActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.RetrofitClient;
import dev.adamag.tripmasterfront.network.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MenuBarActivity extends AppCompatActivity {

    private User.UserIdBoundary userIdBoundary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not setContentView here

        // Get the userIdBoundary from the intent extras
        String userIdBoundaryJson = getIntent().getStringExtra("userIdBoundary");
        if (userIdBoundaryJson != null) {
            userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
            fetchUserAndCheckAvatar(userIdBoundary.getSuperapp(), userIdBoundary.getEmail());
        } else {
            Toast.makeText(this, "User information is missing", Toast.LENGTH_SHORT).show();
            // Redirect to login if user info is missing
            redirectToLogin();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupBottomNavigationBar();
    }

    protected void setupBottomNavigationBar() {
        // Make sure to set the content view to include the layout with the bottom bar
        findViewById(R.id.homeButton).setOnClickListener(v -> navigateTo(HomeActivity.class));
        findViewById(R.id.ticketButton).setOnClickListener(v -> navigateTo(SearchFlightsActivity.class));
        findViewById(R.id.notificationButton).setOnClickListener(v -> navigateTo(NotificationActivity.class));
        findViewById(R.id.bagButton).setOnClickListener(v -> navigateTo(DisplayCheckListActivity.class));
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

    private void fetchUserAndCheckAvatar(String superapp, String email) {
        UserService userService = RetrofitClient.getTripMasterClient().create(UserService.class);
        userService.getUserById(superapp, email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    if (checkUserAvatarAndRedirect(user.getAvatar())) {
                        setupBottomNavigationBar();
                    }
                } else {
                    Toast.makeText(MenuBarActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MenuBarActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                redirectToLogin();
            }
        });
    }

    private boolean checkUserAvatarAndRedirect(String userAvatar) {
        if (userAvatar != null && userAvatar.startsWith("D")) {
            // Show dialog and redirect to LoginActivity if the avatar starts with "D"
            new AlertDialog.Builder(this)
                    .setTitle("Banned")
                    .setMessage("You are banned from accessing the application.")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        dialog.dismiss();
                        redirectToLogin();
                    })
                    .setCancelable(false)
                    .show();
            return false; // Return false to indicate that further setup should not continue
        }
        return true; // Return true to indicate that further setup can continue
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}
