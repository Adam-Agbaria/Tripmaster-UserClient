package dev.adamag.tripmasterfront.network;


import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.model.UserInput;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class UserServiceImpl {

    private UserService service;

    public UserServiceImpl() {
        service = RetrofitClient.getTripMasterClient().create(UserService.class);
    }

    public void createUser(UserInput user, Callback<User> callback) {
        Call<User> call = service.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void updateUser(String superapp, String email, User user, Callback<Void> callback) {
        Call<Void> call = service.updateUser(superapp, email, user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void getUserById(String superapp, String email, Callback<User> callback) {
        Call<User> call = service.getUserById(superapp, email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    // Uncomment and add if you need delete functionality
    // Method to delete a user by email
    // public void deleteUserById(String email, Callback<Void> callback) {
    //     Call<Void> call = service.deleteUserById(email);
    //     call.enqueue(new Callback<Void>() {
    //         @Override
    //         public void onResponse(Call<Void> call, Response<Void> response) {
    //             callback.onResponse(call, response);
    //         }

    //         @Override
    //         public void onFailure(Call<Void> call, Throwable t) {
    //             callback.onFailure(call, t);
    //         }
    //     });
    // }
}
