package dev.adamag.tripmasterfront.Activity.AuthActivity;

import static android.content.ContentValues.TAG;

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
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryCommand;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.Password;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.CommandServiceImpl;
import dev.adamag.tripmasterfront.network.ObjectService;
import dev.adamag.tripmasterfront.network.RetrofitClient;
import dev.adamag.tripmasterfront.network.UserServiceImpl;
import dev.adamag.tripmasterfront.Activity.FlightActivity.SearchFlightsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
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
        ObjectService objectService = RetrofitClient.getTripMasterClient().create(ObjectService.class);

        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.getUserById("tripMaster", email, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    // Fetch all objects
                    Call<List<BoundaryObject>> allObjectsCall = objectService.getAllObjects(
                            "tripMaster",
                            email,
                            10,
                            0
                    );                    allObjectsCall.enqueue(new Callback<List<BoundaryObject>>() {
                        @Override
                        public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<BoundaryObject> allObjects = response.body();

                                // Find the password object for the user
                                BoundaryObject passwordBoundaryObject = null;
                                for (BoundaryObject obj : allObjects) {
                                    if ("Password".equals(obj.getType()) &&
                                            user.getUserId().getEmail().equals(((BoundaryObject.CreatedByBoundary.UserIdBoundary) obj.getCreatedBy().getUserId()).getEmail())) {
                                        passwordBoundaryObject = obj;
                                        break;
                                    }
                                }

                                if (passwordBoundaryObject != null) {
                                    Password passwordObject = Password.fromBoundaryObject(passwordBoundaryObject);

                                    if (passwordObject.getObjectDetails().get("password").equals(password)) {
                                        // Password is correct, proceed to next activity
                                        String role = user.getRole().name();
                                        User.UserIdBoundary userIdBoundary = user.getUserId();
                                        String userIdBoundaryJson = gson.toJson(userIdBoundary);

//                                        saveLoginCommand(user);

                                        // Based on role, redirect to respective activity
                                        Intent intent;
                                        if (role.equals("ADMIN")) {
                                            intent = new Intent(LoginActivity.this, SearchFlightsActivity.class);
                                        } else if (role.equals("MINIAPP_USER")) {
                                            intent = new Intent(LoginActivity.this, SearchFlightsActivity.class);
                                        } else {
                                            intent = new Intent(LoginActivity.this, SearchFlightsActivity.class);
                                        }
                                        intent.putExtra("userIdBoundary", userIdBoundaryJson);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Password fetch failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Password fetch failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Network error during password fetch", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error during user fetch", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveLoginCommand(User user) {
        CommandServiceImpl commandService = new CommandServiceImpl();

        BoundaryCommand command = new BoundaryCommand();
        command.setCommandId(new BoundaryCommand.CommandId("tripMaster", "yourMiniApp", "1"));
        command.setCommand("Login");

        BoundaryCommand.TargetObject targetObject = new BoundaryCommand.TargetObject();
        targetObject.setObjectId(new BoundaryCommand.TargetObject.ObjectId("NoObjectRelated", "NoObjectRelated")); // Use appropriate object ID
        command.setTargetObject(targetObject);

        command.setInvocationTimestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(new Date()));

        BoundaryCommand.InvokedBy invokedBy = new BoundaryCommand.InvokedBy();
        invokedBy.setUserId(new BoundaryCommand.InvokedBy.UserId(user.getUserId().getSuperapp(), user.getUserId().getEmail()));
        command.setInvokedBy(invokedBy);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", user.getUserId().getEmail());
        command.setCommandAttributes(attributes);

        commandService.createCommand("userApp", command, new Callback<BoundaryCommand>() {
            @Override
            public void onResponse(Call<BoundaryCommand> call, Response<BoundaryCommand> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Command created: " + response.body());
                } else {
                    Log.e(TAG, "Create command failed: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BoundaryCommand> call, Throwable t) {
                Log.e(TAG, "Create command error: " + t.getMessage());
            }
        });
    }
}
