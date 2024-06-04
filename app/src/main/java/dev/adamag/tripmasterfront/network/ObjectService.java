package dev.adamag.tripmasterfront.network;


import java.util.List;

import dev.adamag.tripmasterfront.model.BoundaryObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ObjectService {

    @POST("tripMaster/objects")
    Call<BoundaryObject> createObject(@Body BoundaryObject boundaryObject);

    @GET("tripMaster/objects/tripMaster/{id}")
    Call<BoundaryObject> getObjectById(@Path("id") String id);

    @PUT("tripMaster/objects/tripMaster/{id}")
    Call<Void> updateObject(@Path("id") String id, @Body BoundaryObject boundaryObject);

    @GET("tripMaster/objects")
    Call<List<BoundaryObject>> getAllObjects();
}
