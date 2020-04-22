package ir.ac.kntu.Interface.Retrofit;

import java.util.List;

import ir.ac.kntu.Entity.ChairSet;
import ir.ac.kntu.Entity.Desk;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Operable_Table {
    @GET("table_api/desks.php")
    Call<List<Desk>> getDesks(@Query("roof") int roof, @Query("selected_date") String persianDate);

    @GET("table_api/chairs.php")
    Call<ChairSet> getChairs(@Query("desk_id") int deskID);

    @GET("table_api/max_columns_of_tables.php")
    Call<Integer> getMaxColumnsOf();
}
