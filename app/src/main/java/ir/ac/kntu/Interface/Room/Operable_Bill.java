package ir.ac.kntu.Interface.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Technical.Other.Other.Constants;

@Dao
public interface Operable_Bill {
    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE restaurantQRCode=:restaurantQrCode")
    List<Bill> getAll(String restaurantQrCode);

    @Query("SELECT COUNT(*) from " + Constants._DATABASE_BILL_TABLE + " WHERE restaurantQRCode=:restaurantQrCode")
    int countBills(String restaurantQrCode);

    @Insert
    void insert(Bill... bills);

    @Query("DELETE FROM " + Constants._DATABASE_BILL_TABLE + " WHERE lexinTableID=:lexinTableID AND restaurantQRCode=:restaurantQrCode")
    int deleteWithLexinTableID(int lexinTableID, String restaurantQrCode);

    @Query("DELETE FROM " + Constants._DATABASE_BILL_TABLE + " WHERE id>=0 AND restaurantQRCode =:restaurantQrCode")
    void clearAll(String restaurantQrCode);

    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE foodID = :foodID AND restaurantQRCode=:restaurantQrCode")
    List<Bill> getWithFoodID(int foodID, String restaurantQrCode);

    @Query("UPDATE " + Constants._DATABASE_BILL_TABLE + " SET counter = :finalValue WHERE foodID = :foodID AND restaurantQRCode=:restaurantQrCode")
    void updateCounterOfBillByFoodID(int foodID, int finalValue, String restaurantQrCode);

    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE id = :id AND restaurantQRCode=:restaurantQrCode")
    List<Bill> getWithBillID(int id, String restaurantQrCode);

    @Query("SELECT COUNT(*) FROM " + Constants._DATABASE_BILL_TABLE + " WHERE id = :id AND restaurantQRCode=:restaurantQrCode")
    int getCountOfBillID(int id, String restaurantQrCode);

    @Query("DELETE FROM " + Constants._DATABASE_BILL_TABLE + " WHERE foodID = :foodID AND restaurantQRCode=:restaurantQrCode")
    void deleteWithFoodID(int foodID, String restaurantQrCode);

    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE lexinTableID = :lexinTableID AND restaurantQRCode=:restaurantQrCode")
    List<Bill> getWithLexinTableID(int lexinTableID, String restaurantQrCode);

    @Query("SELECT * FROM " + Constants._DATABASE_BILL_TABLE + " WHERE lexinTableID!= -1 AND foodID == -1 AND restaurantQRCode=:restaurantQrCode")
    List<Bill> getToReserveLexinTables(String restaurantQrCode);
}