package dev.adamag.tripmasterfront.network;

import dev.adamag.tripmasterfront.model.BoundaryCommand;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommandService {

    @POST("superapp/miniapp/{miniAppName}")
    Call<BoundaryCommand[]> createCommand(@Path("miniAppName") String miniAppName, @Body BoundaryCommand boundaryCommand);
}
