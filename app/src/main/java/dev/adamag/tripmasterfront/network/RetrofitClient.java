package dev.adamag.tripmasterfront.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {

    private static Retrofit tripMasterRetrofit = null;
    private static Retrofit chatGptRetrofit = null;
    private static Retrofit scrapingRetrofit = null;

    private static final String TRIP_MASTER_BASE_URL = "http://10.0.2.2:8084/";
    private static final String CHAT_GPT_BASE_URL = "https://api.openai.com/v1/";
    private static final String SCRAPING_BASE_URL = "http://10.0.2.2:8085/";

    public static Retrofit getTripMasterClient() {
        if (tripMasterRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            tripMasterRetrofit = new Retrofit.Builder()
                    .baseUrl(TRIP_MASTER_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return tripMasterRetrofit;
    }



    public static Retrofit getScrapingClient() {
        if (scrapingRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            scrapingRetrofit = new Retrofit.Builder()
                    .baseUrl(SCRAPING_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return scrapingRetrofit;
    }

}
