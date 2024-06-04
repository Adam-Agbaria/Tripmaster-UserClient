package dev.adamag.tripmasterfront.network;


import dev.adamag.tripmasterfront.model.FlightResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScrapingService {

    @GET("scrape")
    Call<FlightResponse> getFlightInfo(
            @Query("tripType") String tripType,
            @Query("departureDate") String departureDate,
            @Query("returnDate") String returnDate,
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("adults") int adults,
            @Query("children") int children
    );
}
