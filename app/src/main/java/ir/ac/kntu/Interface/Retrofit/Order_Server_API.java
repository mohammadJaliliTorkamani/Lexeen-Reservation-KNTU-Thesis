package ir.ac.kntu.Interface.Retrofit;

import java.util.List;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Entity.RefactoredBills;
import ir.ac.kntu.Entity.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Order_Server_API {
    /**
     * gets list of user orders
     *
     * @return list ot orders
     */
    @GET("order_api/orders.php")
    Call<List<Order>> getOrders();

    /**
     * updates list of bills (updating price and bills etc with the latest info before final order)
     *
     * @param list list of incomplete (or old) orders
     * @return list of new updated bills
     */
    @POST("order_api/food_bill.php")
    Call<List<Bill>> completeBills(@Body List<Bill> list);

    /**
     * checks whether assigned bills are good for order or no
     *
     * @param refactoredBills list of bills (completed object)
     * @return check result
     */
    @POST("order_api/is_good_order.php")
    Call<ServerResponse> isGoodOrder(@Body RefactoredBills refactoredBills);

    /**
     * order food for serve
     *
     * @param order order to be served
     * @return result of serve order
     */
    @POST("order_api/order.php")
    Call<ServerResponse> order(@Body Order order);

    /**
     * order food for delivery
     *
     * @param order order to be delivered
     * @return result of delivery order
     */
    @POST("order_api/deliver.php")
    Call<ServerResponse> deliver(@Body Order order);

}
