package dev.adamag.tripmasterfront.network;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScrapingHotelService {

    @GET("scrape")
    Call<List<Map<String, Object>>> getHotelInfo(
            @Query("city") String city,
            @Query("checkInDate") String checkInDate,
            @Query("checkOutDate") String checkOutDate,
            @Query("adults") int adults,
            @Query("children") int children,
            @Query("rooms") int rooms
    );
}
