package ir.ac.kntu.Interface.Retrofit;

import java.util.List;

import ir.ac.kntu.Entity.ChairSet;
import ir.ac.kntu.Entity.Desk;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Table_Server_API {
    /**
     * get all the desks in the selected roof with the selected jalali date
     * (for status marking when returned)
     *
     * @param roof        roof of the restaurant to get roofs
     * @param persianDate jalali date to search for desks
     * @return list of desks
     */
    @GET("table_api/desks.php")
    Call<List<Desk>> getDesks(@Query("roof") int roof, @Query("selected_date") String persianDate);

    /**
     * get chairs of a desk
     *
     * @param deskID desk id
     * @return list of chairs(chairset)
     */
    @GET("table_api/chairs.php")
    Call<ChairSet> getChairs(@Query("desk_id") int deskID);

    /**
     * get columns number of table architecture
     *
     * @return number of columns (used in architecture)
     */
    @GET("table_api/max_columns_of_tables.php")
    Call<Integer> getMaxColumnsOf();
}
