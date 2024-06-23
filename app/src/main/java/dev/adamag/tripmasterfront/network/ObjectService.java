package dev.adamag.tripmasterfront.network;

import java.util.List;

import dev.adamag.tripmasterfront.model.BoundaryObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ObjectService {

    @POST("superapp/objects")
    Call<BoundaryObject> createObject(@Body BoundaryObject boundaryObject);

    @GET("superapp/objects/{superapp}/{id}")
    Call<BoundaryObject> getObjectById(
            @Path("superapp") String superapp,
            @Path("id") String id,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail
    );

    @PUT("superapp/objects/{superapp}/{id}")
    Call<Void> updateObject(
            @Path("superapp") String superapp,
            @Path("id") String id,
            @Body BoundaryObject boundaryObject,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail
    );

    @GET("superapp/objects")
    Call<List<BoundaryObject>> getAllObjects(
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/objects/search/byType/{type}")
    Call<List<BoundaryObject>> searchObjectsByType(
            @Path("type") String type,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/objects/search/byAlias/{alias}")
    Call<List<BoundaryObject>> searchObjectsByAlias(
            @Path("alias") String alias,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/objects/search/byAliasPattern/{pattern}")
    Call<List<BoundaryObject>> searchObjectsByAliasPattern(
            @Path("pattern") String pattern,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/objects/search/byLocation/{lat}/{lng}/{distance}")
    Call<List<BoundaryObject>> searchObjectsByLocation(
            @Path("lat") double lat,
            @Path("lng") double lng,
            @Path("distance") double distance,
            @Query("units") String units,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail,
            @Query("size") int size,
            @Query("page") int page
    );
}
