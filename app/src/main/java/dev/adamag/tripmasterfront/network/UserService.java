package dev.adamag.tripmasterfront.network;


import dev.adamag.tripmasterfront.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface UserService {

    @POST("tripMaster/users")
    Call<User> createUser(@Body User user);

    @PUT("tripMaster/users/{superapp}/{email}")
    Call<Void> updateUser(@Path("superapp") String superapp, @Path("email") String email, @Body User user);

    @GET("tripMaster/users/login/{superapp}/{email}")
    Call<User> getUserById(@Path("superapp") String superapp, @Path("email") String email);

    // Uncomment and add if you need delete functionality
    // @DELETE("yourApplicationName/users/{email}")
    // Call<Void> deleteUserById(@Path("email") String email);
}
