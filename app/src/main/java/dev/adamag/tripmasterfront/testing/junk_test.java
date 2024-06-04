package dev.adamag.tripmasterfront.testing;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.Flight;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.model.UserRole;
import dev.adamag.tripmasterfront.network.ObjectServiceImpl;
import dev.adamag.tripmasterfront.network.UserServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class junk_test {
}
//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        ObjectServiceImpl serviceUtil = new ObjectServiceImpl();
//
//        UserServiceImpl userService = new UserServiceImpl();
//
//        // Create a UserBoundary object
//        User newUser = new User();
//        if (newUser.getUserId() == null) {
//            newUser.setUserId(new User.UserIdBoundary());
//        }
//        newUser.getUserId().setEmail("newuser@example.com");
//
//        newUser.setRole(UserRole.SUPERAPP_USER); // Assuming UserRole is an enum
//        newUser.setUsername("newuser");
//        newUser.setAvatar("avatar");
//
//        // Create a Flight object and convert it to BoundaryObject
//        Flight flight = new Flight("12345", "Delta Airlines", "10:00 AM", "1:00 PM", "New York", "Los Angeles", "$300");
//        BoundaryObject boundaryObject = flight.toBoundaryObject();
//        boundaryObject.getCreatedBy().getUserId().setEmail("prosamsungreviews@gmail.com");
////        boundaryObject.getCreatedBy().getUserId().setSuperapp("tripMaster");
//
//
//
//
//        // Create object on server
//        serviceUtil.createObject(boundaryObject, new Callback<BoundaryObject>() {
//            @Override
//            public void onResponse(Call<BoundaryObject> call, Response<BoundaryObject> response) {
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "Object created: " + response.body());
//                } else {
//                    Log.e(TAG, "Create object failed: " + response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BoundaryObject> call, Throwable t) {
//                Log.e(TAG, "Create object error: " + t.getMessage());
//            }
//        });
//
////        // Retrieve object by ID
////        serviceUtil.getObjectById("12345", new Callback<BoundaryObject>() {
////            @Override
////            public void onResponse(Call<BoundaryObject> call, Response<BoundaryObject> response) {
////                if (response.isSuccessful()) {
////                    Log.d(TAG, "Object retrieved: " + response.body());
////                } else {
////                    Log.e(TAG, "Get object failed: " + response.message());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<BoundaryObject> call, Throwable t) {
////                Log.e(TAG, "Get object error: " + t.getMessage());
////            }
////        });
////
////        // Update object
////        boundaryObject.getObjectDetails().put("price", "$350");
////        serviceUtil.updateObject("12345", boundaryObject, new Callback<Void>() {
////            @Override
////            public void onResponse(Call<Void> call, Response<Void> response) {
////                if (response.isSuccessful()) {
////                    Log.d(TAG, "Object updated successfully.");
////                } else {
////                    Log.e(TAG, "Update object failed: " + response.message());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<Void> call, Throwable t) {
////                Log.e(TAG, "Update object error: " + t.getMessage());
////            }
////        });
////
////        // Retrieve all objects
////        serviceUtil.getAllObjects(new Callback<List<BoundaryObject>>() {
////            @Override
////            public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
////                if (response.isSuccessful()) {
////                    Log.d(TAG, "All objects retrieved: " + response.body());
////                } else {
////                    Log.e(TAG, "Get all objects failed: " + response.message());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
////                Log.e(TAG, "Get all objects error: " + t.getMessage());
////            }
////        });
//
//        // Create user on server
//        userService.createUser(newUser, new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "User created: " + response.body());
//                } else {
//                    Log.e(TAG, "Create user failed: " + response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.e(TAG, "Create user error: " + t.getMessage());
//            }
//        });
//
//        // Update the user
//        newUser.setUsername("updateduser");
//        userService.updateUser("newuser@example.com", newUser, new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "User updated successfully");
//                } else {
//                    Log.e(TAG, "Update user failed: " + response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e(TAG, "Update user error: " + t.getMessage());
//            }
//        });
//
//        // Retrieve the user by email
//        userService.getUserById("newuser@example.com", new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "User retrieved: " + response.body());
//                } else {
//                    Log.e(TAG, "Retrieve user failed: " + response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.e(TAG, "Retrieve user error: " + t.getMessage());
//            }
//        });
//
//        // Uncomment and add if you need delete functionality
//        // Delete the user by email
//        // userService.deleteUserById("newuser@example.com", new Callback<Void>() {
//        //     @Override
//        //     public void onResponse(Call<Void> call, Response<Void> response) {
//        //         if (response.isSuccessful()) {
//        //             Log.d(TAG, "User deleted successfully");
//        //         } else {
//        //             Log.e(TAG, "Delete user failed: " + response.toString());
//        //         }
//        //     }
//
//        //     @Override
//        //     public void onFailure(Call<Void> call, Throwable t) {
//        //         Log.e(TAG, "Delete user error: " + t.getMessage());
//        //     }
//        // });
//    }
//
//}
