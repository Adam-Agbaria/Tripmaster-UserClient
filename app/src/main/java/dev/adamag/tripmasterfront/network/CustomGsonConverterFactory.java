package dev.adamag.tripmasterfront.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import dev.adamag.tripmasterfront.model.Flight;
import dev.adamag.tripmasterfront.model.FlightResponse;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomGsonConverterFactory extends Converter.Factory {
    public static GsonConverterFactory create() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FlightResponse.class, (JsonDeserializer<FlightResponse>) (json, typeOfT, context) -> {
                    if (json.isJsonArray()) {
                        Type listType = new TypeToken<List<Flight>>() {}.getType();
                        List<Flight> flights = new Gson().fromJson(json.getAsJsonArray(), listType);
                        FlightResponse flightResponse = new FlightResponse();
                        flightResponse.setFlights(flights);
                        return flightResponse;
                    } else {
                        throw new JsonParseException("Expected a JSON array");
                    }
                }).create();

        return GsonConverterFactory.create(gson);
    }
}
