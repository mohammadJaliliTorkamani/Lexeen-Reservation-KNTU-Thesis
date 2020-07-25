package ir.ac.kntu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import java.util.List;

import ir.ac.kntu.Entity.FoodMenu;
import ir.ac.kntu.Entity.MenuFoodInfoView;
import ir.ac.kntu.Entity.MenuHeadingView;
import ir.ac.kntu.Interface.Retrofit.Food_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Menu fragment ables user to see restaurant foods menu
 */
public class Fragment_Menu extends Fragment {
    private ExpandablePlaceHolderView expandablePlaceHolderView;
    private boolean mIsLoadingMore = false;
    private boolean mNoMoreToLoad = false;

    /**
     * fragment entry point which finds views, loads data, initializes UI elements and declares
     * listeners
     *
     * @param inflater           inflater
     * @param container          container view
     * @param savedInstanceState saved instance bundle
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeServerSupplied(view);
        manageListeners(view);
        return view;
    }

    /**
     * toasts an alarm message if the user wasn't connect to the internet network
     *
     * @param view view to work
     */
    private void initializeServerSupplied(View view) {
        Connector.createService(view, Food_Server_API.class, object -> {
            Call<List<FoodMenu>> call = object.getFoodMenu();
            call.enqueue(new Callback<List<FoodMenu>>() {
                @Override
                public void onResponse(Call<List<FoodMenu>> call, Response<List<FoodMenu>> response) {
                    if (response.body() != null) {
                        addExpandableItems(view, response.body());
                        //setLoadMoreListener(view);
                    } else
                        Helper_Log.errorLog(Fragment_Menu.class);
                }

                @Override
                public void onFailure(Call<List<FoodMenu>> call, Throwable t) {
                    Helper_Log.errorLog(t, Fragment_Menu.class);
                }
            });
        });
    }

    /**
     * adds MenuHeadingView and MenuFoodInfoView for each food menu item
     *
     * @param view  view to work
     * @param items food menu to be traced and to create a corresponding graphic menu from foodMenu
     */
    private void addExpandableItems(View view, List<FoodMenu> items) {
        for (FoodMenu foodMenu : items) {
            expandablePlaceHolderView.addView(new MenuHeadingView(foodMenu.getCategory(), getActivity()));
            for (ir.ac.kntu.Entity.Food food : foodMenu.getFoodList()) {
                expandablePlaceHolderView.addView(new MenuFoodInfoView(ContextHelper.retrieveContext(), getFragmentManager(), Encryption.getInstance().decrypt(foodMenu.getCategory().getColor()), food, foodMenu.getFoodList().size() + 1)); //+1  is for arrow icon
            }
            expandablePlaceHolderView.addView(new MenuFoodInfoView(ContextHelper.retrieveContext(), getFragmentManager(), Encryption.getInstance().decrypt(foodMenu.getCategory().getColor()), null, foodMenu.getFoodList().size() + 1));//+1  is for arrow icon
        }

    }

    /**
     * assign view objects to view elements
     *
     * @param view to find views with it
     */
    private void findViews(View view) {
        expandablePlaceHolderView = view.findViewById(R.id.fragment_menu_expandable_ph);
    }

    /**
     * initializes UI elements (updating badge in here)
     *
     * @param view view to work
     */
    private void initializeViewContents(View view) {
        ((Fragment_Main) getFragmentManager().findFragmentById(R.id.main_frame)).updateBadge();
    }

    /**
     * declares listeners for some UI elements
     *
     * @param view view to work
     */
    private void manageListeners(View view) {
    }
}
