package ir.ac.kntu.Interface.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Technical.Other.Other.Constants;

@Dao
public interface Bill_Database_API {
    /**
     * retrieves list of all Bills with restaurantQrCode
     *
     * @param restaurantQrCode restaurant qr code to get it's bills
     * @return list of bills
     */
    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE restaurantQRCode=:restaurantQrCode")
    List<Bill> getAll(String restaurantQrCode);

    /**
     * count number of bills in restaurant QR Code
     *
     * @param restaurantQrCode
     * @return number of bills
     */
    @Query("SELECT COUNT(*) from " + Constants._DATABASE_BILL_TABLE + " WHERE restaurantQRCode=:restaurantQrCode")
    int countBills(String restaurantQrCode);

    /**
     * inserts bills into DB
     *
     * @param bills bills to be inserted
     */
    @Insert
    void insert(Bill... bills);

    /**
     * deletes Bill with lexeen table ID from restaurant QR Code from DB
     *
     * @param lexinTableID     lexeen table ID
     * @param restaurantQrCode restaurant QR Code
     * @return id of the deleted Bill
     */
    @Query("DELETE FROM " + Constants._DATABASE_BILL_TABLE + " WHERE lexinTableID=:lexinTableID AND restaurantQRCode=:restaurantQrCode")
    int deleteWithLexinTableID(int lexinTableID, String restaurantQrCode);

    /**
     * deletes all the bills from restaurant qr code
     *
     * @param restaurantQrCode restaurant qr code
     */
    @Query("DELETE FROM " + Constants._DATABASE_BILL_TABLE + " WHERE id>=0 AND restaurantQRCode =:restaurantQrCode")
    void clearAll(String restaurantQrCode);

    /**
     * get all hte bills having foodID in the selected restaurant
     *
     * @param foodID           food id to search amount bills
     * @param restaurantQrCode selected restaurant qr code
     * @return list of bills having foodID
     */
    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE foodID = :foodID AND restaurantQRCode=:restaurantQrCode")
    List<Bill> getWithFoodID(int foodID, String restaurantQrCode);

    /**
     * changes number of ordered food (with id ID) in the selected restaurant
     *
     * @param foodID           food ID to update number of ordered items
     * @param finalValue       value (food number) to be changed with the current one
     * @param restaurantQrCode selected restaurant QR Code
     */
    @Query("UPDATE " + Constants._DATABASE_BILL_TABLE + " SET counter = :finalValue WHERE foodID = :foodID AND restaurantQRCode=:restaurantQrCode")
    void updateCounterOfBillByFoodID(int foodID, int finalValue, String restaurantQrCode);

    /**
     * get bill with ID
     *
     * @param id               bill ID
     * @param restaurantQrCode restaurant QR Code
     * @return bill with passed ID in the selected restaurant
     */
    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE id = :id AND restaurantQRCode=:restaurantQrCode")
    List<Bill> getWithBillID(int id, String restaurantQrCode);

    /**
     * get number of bills with id
     *
     * @param id               bill id
     * @param restaurantQrCode restaurant QR Code
     * @return number of bills
     */
    @Query("SELECT COUNT(*) FROM " + Constants._DATABASE_BILL_TABLE + " WHERE id = :id AND restaurantQRCode=:restaurantQrCode")
    int getCountOfBillID(int id, String restaurantQrCode);

    /**
     * deletes all the bills who have foodID in the selected restaurant
     *
     * @param foodID           food ID
     * @param restaurantQrCode restaurant QR Code
     */
    @Query("DELETE FROM " + Constants._DATABASE_BILL_TABLE + " WHERE foodID = :foodID AND restaurantQRCode=:restaurantQrCode")
    void deleteWithFoodID(int foodID, String restaurantQrCode);

    /**
     * get bill having lexeen table ID in the selected restaurant
     *
     * @param lexinTableID     lexeen table ID
     * @param restaurantQrCode restaurant QR Code
     * @return bills which have lexeen table ID in the selected QR Code
     */
    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE lexinTableID = :lexinTableID AND restaurantQRCode=:restaurantQrCode")
    List<Bill> getWithLexinTableID(int lexinTableID, String restaurantQrCode);

    /**
     * get all the bills in the selected restaurant which are added to cart
     *
     * @param restaurantQrCode restaurant QR Code
     * @return
     */
    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE lexinTableID!= -1 AND foodID == -1 AND restaurantQRCode=:restaurantQrCode")
    List<Bill> getToReserveLexinTables(String restaurantQrCode);
}