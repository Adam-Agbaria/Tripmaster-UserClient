package dev.adamag.tripmasterfront.network;

import java.util.List;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Retrofit;

public class ScrapingHotelServiceImpl {
    private static final Retrofit retrofit = RetrofitClient.getHotelScrapingClient();
    private static final ScrapingHotelService service = retrofit.create(ScrapingHotelService.class);

    public static void getHotelInfo(String city, String checkInDate, String checkOutDate, int adults, int children, int rooms, Callback<List<Map<String, Object>>> callback) {
        service.getHotelInfo(city, checkInDate, checkOutDate, adults, children, rooms).enqueue(callback);
    }
}
