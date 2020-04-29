package ir.ac.kntu.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Adapter.Adapter_Search;
import ir.ac.kntu.Interface.Retrofit.Food_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.EditTextPlus;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment {
    private EditTextPlus searchBox;
    private TextViewPlus cancel;
    private TextViewPlus empty;
    private RecyclerView search_rv;
    private RecyclerView.Adapter searchList_adapter;
    private RecyclerView.LayoutManager searchList_layout_manager;
    private List<ir.ac.kntu.Entity.Food> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        findViews(view);
        initialize(view);
        manageListeners(view);
        return view;
    }

    private void findViews(View view) {
        searchBox = view.findViewById(R.id.fragment_search_searchbox);
        cancel = view.findViewById(R.id.fragment_search_cancel);
        empty = view.findViewById(R.id.fragment_search_rv_empty_text);
        search_rv = view.findViewById(R.id.fragment_search_rv);
    }

    private void initialize(View view) {
        list = new LinkedList<>();
        searchList_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false);
        search_rv.setLayoutManager(searchList_layout_manager);
        search_rv.setHasFixedSize(true);
        searchList_adapter = new Adapter_Search(getFragmentManager(), getActivity(), list);
        search_rv.setAdapter(searchList_adapter);
    }

    private void manageListeners(View view) {
        cancel.setOnClickListener(v -> getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("home")
                .add(R.id.main_fragment_top_bb_frame, new Fragment_Home())
                .commit());
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String text = s.toString();
                    Connector.createService(view, Food_Server_API.class, object -> object.getFoodsContaining(text).enqueue(new Callback<List<ir.ac.kntu.Entity.Food>>() {
                        @Override
                        public void onResponse(Call<List<ir.ac.kntu.Entity.Food>> call, Response<List<ir.ac.kntu.Entity.Food>> response) {
                            if (response.body() != null) {
                                empty.setVisibility(response.body().isEmpty() ? View.VISIBLE : View.GONE);
                                list.clear();
                                list.addAll(response.body());
                                searchList_adapter.notifyDataSetChanged();
                            } else {
                                Helper_Log.errorLog(Fragment_Search.class);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ir.ac.kntu.Entity.Food>> call, Throwable t) {
                            Helper_Log.errorLog(t, Fragment_Search.class);
                        }
                    }));
                }
            }
        });
    }
}
