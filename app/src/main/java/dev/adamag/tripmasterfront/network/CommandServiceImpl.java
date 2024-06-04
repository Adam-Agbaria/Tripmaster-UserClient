package dev.adamag.tripmasterfront.network;




import dev.adamag.tripmasterfront.model.BoundaryCommand;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommandServiceImpl {

    private CommandService service;

    public CommandServiceImpl() {
        service = RetrofitClient.getTripMasterClient().create(CommandService.class);
    }

    public void createCommand(String miniAppName, BoundaryCommand boundaryCommand, Callback<BoundaryCommand> callback) {
        Call<BoundaryCommand> call = service.createCommand(miniAppName, boundaryCommand);
        call.enqueue(new Callback<BoundaryCommand>() {
            @Override
            public void onResponse(Call<BoundaryCommand> call, Response<BoundaryCommand> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    callback.onFailure(call, new Throwable("Failed to create command"));
                }
            }

            @Override
            public void onFailure(Call<BoundaryCommand> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
