package dev.adamag.tripmasterfront.Activity.AuthActivity;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import dev.adamag.tripmasterfront.Activity.HomeActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryCommand;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.CommandServiceImpl;
import dev.adamag.tripmasterfront.network.RetrofitClient;
import dev.adamag.tripmasterfront.network.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private MaterialButton loginButton;
    private TextView signUpButton;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailEditText = findViewById(R.id.LGP_userName_EDT);
        passwordEditText = findViewById(R.id.LGP_password_EDT);
        loginButton = findViewById(R.id.LPG_button_BTN);
        signUpButton = findViewById(R.id.LGP_signUP_BTN);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                login(email, password);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String email, String password) {
        UserService userService = RetrofitClient.getTripMasterClient().create(UserService.class);
        userService.getUserById("tripMaster", email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    if (user.getAvatar() != null && user.getAvatar().startsWith("D")) {
                        showBannedDialog();
                    } else {
                        fetchPasswordAndLogin(email, password, user);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPasswordAndLogin(String email, String password, User user) {
        BoundaryCommand.TargetObject.ObjectId objectId = new BoundaryCommand.TargetObject.ObjectId("tripMaster", "4893f3dd-ceec-4d9e-992a-fae8f08137eb");
        BoundaryCommand.TargetObject targetObject = new BoundaryCommand.TargetObject(objectId);

        BoundaryCommand.InvokedBy.UserId userId = new BoundaryCommand.InvokedBy.UserId("tripMaster", email);
        BoundaryCommand.InvokedBy invokedBy = new BoundaryCommand.InvokedBy(userId);

        Map<String, Object> commandAttributes = new HashMap<>();
        commandAttributes.put("email", email);
        commandAttributes.put("type", "Password");
        commandAttributes.put("page", 0);
        commandAttributes.put("size", 1);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(new Date());

        BoundaryCommand command = new BoundaryCommand(null, "findObjectsByCreatorEmailAndType", targetObject, timestamp, invokedBy, commandAttributes);
        CommandServiceImpl commandService = new CommandServiceImpl();
        commandService.createCommand("userApp", command, new Callback<BoundaryCommand[]>() {
            @Override
            public void onResponse(Call<BoundaryCommand[]> call, Response<BoundaryCommand[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BoundaryCommand[] results = response.body();
                    Log.d("Response", "Results: " + Arrays.toString(results));
                    if (results.length > 0 && results[0].getCommandAttributes() != null && results[0].getCommandAttributes().get("results") != null) {
                        List<Map<String, Object>> resultList = (List<Map<String, Object>>) results[0].getCommandAttributes().get("results");
                        if (!resultList.isEmpty()) {
                            Map<String, Object> passwordResult = resultList.get(0);
                            Log.d("Response", "Password Result: " + passwordResult);
                            Map<String, Object> objectDetails = (Map<String, Object>) passwordResult.get("objectDetails");
                            String fetchedPassword = objectDetails != null ? (String) objectDetails.get("password") : null;
                            Log.d("Response", "Fetched Password: " + fetchedPassword);

                            if (password.equals(fetchedPassword)) {
                                // Password is correct, proceed to next activity
                                BoundaryCommand.InvokedBy.UserId userIdBoundary = new BoundaryCommand.InvokedBy.UserId("tripMaster", email);
                                String userIdBoundaryJson = gson.toJson(userIdBoundary);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("userIdBoundary", userIdBoundaryJson);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect password " + password + " " + fetchedPassword, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No results in command attributes", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to fetch password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BoundaryCommand[]> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBannedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Banned")
                .setMessage("You are banned from accessing the application.")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }
}
