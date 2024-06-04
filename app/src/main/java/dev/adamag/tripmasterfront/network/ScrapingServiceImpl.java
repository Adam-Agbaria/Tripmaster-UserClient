package dev.adamag.tripmasterfront.network;

import dev.adamag.tripmasterfront.model.FlightResponse;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ScrapingServiceImpl {
    private static final Retrofit retrofit = RetrofitClient.getScrapingClient();
    private static final ScrapingService service = retrofit.create(ScrapingService.class);

    public static void getFlightInfo(String tripType, String departureDate, String returnDate, String origin, String destination, int adults, int children, Callback<FlightResponse> callback) {
        service.getFlightInfo(tripType, departureDate, returnDate, origin, destination, adults, children).enqueue(callback);
    }
}
