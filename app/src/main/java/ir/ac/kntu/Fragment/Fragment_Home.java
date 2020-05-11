package ir.ac.kntu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Adapter.Adapter_ByFieldFood;
import ir.ac.kntu.Adapter.Adapter_ByFieldList;
import ir.ac.kntu.Adapter.Adapter_FoodWeLoveFood;
import ir.ac.kntu.Adapter.Adapter_OurOfferFood;
import ir.ac.kntu.Entity.FoodMenu;
import ir.ac.kntu.Entity.Offer;
import ir.ac.kntu.Entity.WeLove;
import ir.ac.kntu.Interface.Retrofit.Food_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {
    private static SlideInRightAnimator slideInRightAnimator;
    private static List<WeLove> _list_foods_we_love = new LinkedList<>();
    private static List<FoodMenu> _list_by_field = new LinkedList<>();
    private static List<Offer> _list_our_offer = new LinkedList<>();

    private RecyclerView food_we_love_rv;
    private RecyclerView.LayoutManager food_we_love_rv_layout_manager;
    private RecyclerView.Adapter food_we_love_rv_adapter;

    private RecyclerView by_field_list_rv;
    private RecyclerView.LayoutManager by_field_list_rv_layout_manager;
    private RecyclerView.Adapter by_field_list_rv_adapter;

    private RecyclerView by_field_content_rv;
    private RecyclerView.Adapter by_field_content_rv_adapter;
    private RecyclerView.LayoutManager by_field_content_rv_layout_manager;

    private RecyclerView our_offers_rv;
    private RecyclerView.Adapter our_offers_rv_adapter;
    private RecyclerView.LayoutManager our_offers_rv_layoutmanager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);
        initializeContents(view);
        initializeServerSupplied(view);
        manageListeners(view);
        return view;
    }

    private void manageListeners(View view) {
        food_we_love_rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN && rv.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                    rv.stopScroll();
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        by_field_list_rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN && rv.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                    rv.stopScroll();
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        by_field_content_rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN && rv.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                    rv.stopScroll();
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        our_offers_rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN && rv.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                    rv.stopScroll();
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void initializeServerSupplied(View view) {
        Connector.createService(view, Food_Server_API.class, object -> {
            Call<List<Offer>> call1 = object.getOurOffer();
            call1.enqueue(new Callback<List<Offer>>() {
                @Override
                public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                    if (response.body() != null) {
                        _list_our_offer.clear();
                        _list_our_offer.addAll(response.body());
                        our_offers_rv_adapter.notifyDataSetChanged();
                    } else
                        Helper_Log.errorLog(Fragment_Home.class);
                }

                @Override
                public void onFailure(Call<List<Offer>> call, Throwable t) {
                    Helper_Log.errorLog(t, Fragment_Home.class);
                }
            });
        });

        Connector.createService(view, Food_Server_API.class, object -> {
            Call<List<WeLove>> call = object.getFoodsWeLove();
            call.enqueue(new Callback<List<WeLove>>() {
                @Override
                public void onResponse(Call<List<WeLove>> call, Response<List<WeLove>> response) {
                    if (response.body() != null) {
                        _list_foods_we_love.clear();
                        _list_foods_we_love.addAll(response.body());
                        food_we_love_rv_adapter.notifyDataSetChanged();
                    } else
                        Helper_Log.errorLog(Fragment_Home.class);
                }

                @Override
                public void onFailure(Call<List<WeLove>> call, Throwable t) {
                    Helper_Log.errorLog(Fragment_Home.class);
                }
            });
        });
        Connector.createService(view, Food_Server_API.class, object -> {
            Call<List<FoodMenu>> call = object.getFoodMenu();
            call.enqueue(new Callback<List<FoodMenu>>() {
                @Override
                public void onResponse(Call<List<FoodMenu>> call, Response<List<FoodMenu>> response) {
                    if (response.body() != null) {
                        _list_by_field.clear();
                        _list_by_field.addAll(response.body());
                        by_field_list_rv_adapter.notifyDataSetChanged();
                        by_field_content_rv_adapter.notifyDataSetChanged();
                    } else
                        Helper_Log.errorLog(Fragment_Home.class);
                }

                @Override
                public void onFailure(Call<List<FoodMenu>> call, Throwable t) {
                    Helper_Log.errorLog(t, Fragment_Home.class);
                }
            });
        });
    }

    private void initializeContents(View view) {

        ((Fragment_Main) getFragmentManager().findFragmentById(R.id.main_frame)).updateBadge();


        slideInRightAnimator = new SlideInRightAnimator();
        slideInRightAnimator.setChangeDuration(1000);
        slideInRightAnimator.setMoveDuration(1500);

        _list_by_field.clear();
        _list_our_offer.clear();
        _list_foods_we_love.clear();

        food_we_love_rv.setHasFixedSize(true);
        food_we_love_rv_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.HORIZONTAL, false);
        food_we_love_rv.setLayoutManager(food_we_love_rv_layout_manager);
        food_we_love_rv_adapter = new Adapter_FoodWeLoveFood(view, getFragmentManager(), _list_foods_we_love);
        food_we_love_rv.setItemAnimator(slideInRightAnimator);
        food_we_love_rv.setAdapter(food_we_love_rv_adapter);

        by_field_content_rv.setHasFixedSize(true);
        by_field_content_rv_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.HORIZONTAL, false);
        by_field_content_rv.setLayoutManager(by_field_content_rv_layout_manager);
        by_field_content_rv_adapter = new Adapter_ByFieldFood(view, getFragmentManager(), _list_by_field);
        by_field_content_rv.setItemAnimator(slideInRightAnimator);
        by_field_content_rv.setAdapter(by_field_content_rv_adapter);

        by_field_list_rv.setHasFixedSize(true);
        by_field_list_rv_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.HORIZONTAL, false);
        by_field_list_rv.setLayoutManager(by_field_list_rv_layout_manager);
        by_field_list_rv_adapter = new Adapter_ByFieldList(view, getActivity(), _list_by_field, 0, (Adapter_ByFieldFood) by_field_content_rv_adapter);
        by_field_list_rv.setItemAnimator(slideInRightAnimator);
        by_field_list_rv.setAdapter(by_field_list_rv_adapter);

        our_offers_rv.setHasFixedSize(true);
        our_offers_rv_layoutmanager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.HORIZONTAL, false);
        our_offers_rv.setLayoutManager(our_offers_rv_layoutmanager);
        our_offers_rv_adapter = new Adapter_OurOfferFood(view, getFragmentManager(), _list_our_offer);
        our_offers_rv.setItemAnimator(slideInRightAnimator);
        our_offers_rv.setAdapter(our_offers_rv_adapter);
    }

    private void findViews(View view) {
        food_we_love_rv = view.findViewById(R.id.fragment_home_food_we_love_rv);
        by_field_list_rv = view.findViewById(R.id.fragment_home_by_field_list_rv);
        by_field_content_rv = view.findViewById(R.id.fragment_home_by_field_content_rv);
        our_offers_rv = view.findViewById(R.id.fragment_our_offers_rv);

    }
}
