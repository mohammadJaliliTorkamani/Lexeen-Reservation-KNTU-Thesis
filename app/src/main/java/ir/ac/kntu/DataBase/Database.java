package ir.ac.kntu.DataBase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Interface.Room.Bill_Database_API;
import ir.ac.kntu.Interface.Room.Order_Database_API;
import ir.ac.kntu.Interface.Room.Restaurant_Database_API;

@androidx.room.Database(entities = {Restaurant.class, Bill.class, Order.class},
        version = 1, exportSchema = false)

/**
 * Singleton DB Class
 */
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    /**
     * singleton database class
     *
     * @param context to work with it in DB
     * @param db_name name of database to create
     * @return DB Object
     */
    public static Database getInstance(Context context, String db_name) {

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context,
                            Database.class, db_name)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * interface (bill)
     *
     * @return bill interface object
     */
    public abstract Bill_Database_API billInterface();

    /**
     * interface (restaurant)
     *
     * @return restaurant interface object
     */
    public abstract Restaurant_Database_API restaurantInterface();

    /**
     * interface (order)
     *
     * @return order interface object
     */
    public abstract Order_Database_API orderInterface();

}