package dev.adamag.tripmasterfront.Activity.NotificationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import dev.adamag.tripmasterfront.Activity.Adapter.NotificationAdapter;
import dev.adamag.tripmasterfront.Activity.FlightActivity.MenuBarActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.CommandServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends MenuBarActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private TextView noNotificationsTextView;
    private String userIdBoundaryJson;
    private User.UserIdBoundary userIdBoundary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notifications);
        setupBottomNavigationBar();

        recyclerView = findViewById(R.id.recycler_view);
        noNotificationsTextView = findViewById(R.id.no_notifications_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");
        if (userIdBoundaryJson != null) {
            userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
        } else {
            Toast.makeText(this, "User information is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        fetchNotifications();
    }

    private void fetchNotifications() {
        CommandServiceImpl commandService = new CommandServiceImpl();

        commandService.sendFindObjectsAttributeEmailAndTypeCommand(
                userIdBoundary.getEmail(),
                userIdBoundary.getSuperapp(),
                userIdBoundary.getEmail(),
                "Notification",
                0,
                100,
                new Callback<List<BoundaryObject>>() {
                    @Override
                    public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<BoundaryObject> notifications = response.body();
                            updateRecyclerView(notifications);
                        } else {
                            Toast.makeText(NotificationActivity.this, "Failed to fetch notifications", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                        Toast.makeText(NotificationActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void updateRecyclerView(List<BoundaryObject> notifications) {
        if (notifications.isEmpty()) {
            noNotificationsTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noNotificationsTextView.setVisibility(View.GONE);
            notificationAdapter = new NotificationAdapter(notifications);
            recyclerView.setAdapter(notificationAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
