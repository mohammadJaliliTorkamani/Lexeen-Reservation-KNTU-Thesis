package ir.ac.kntu.Interface.Retrofit;

import java.util.List;

import ir.ac.kntu.Entity.Discount;
import ir.ac.kntu.Entity.FAQ;
import ir.ac.kntu.Entity.LexinMarchant;
import ir.ac.kntu.Entity.ServerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface General_Server_API {
    /**
     * get FAQ list from serve
     *
     * @return list of FAQs
     */
    @GET("general_api/faqs.php")
    Call<List<FAQ>> getFAQs();

    /**
     * gets marchant info for payment
     *
     * @return marchant info
     */
    @GET("general_api/marchant_id.php")
    Call<LexinMarchant> getMarchantInfo();

    /**
     * gets discount detail
     *
     * @param code discount code
     * @return discount info
     */
    @GET("general_api/discount_with_code.php")
    Call<Discount> getDiscountWithCode(@Query("code") String code);

    /**
     * checks whether the current app is at the latest version or no
     *
     * @return is update result
     */
    @GET("general_api/check_for_update.php")
    Call<ServerResponse> isLatestAppVersion();

    /**
     * get list of available amounts to charge wallet
     *
     * @return list of valid to pay values for wallet charge
     */
    @GET("general_api/acceptable_cash_amounts.php")
    Call<List<Double>> getAcceptableCashAmounts();

}