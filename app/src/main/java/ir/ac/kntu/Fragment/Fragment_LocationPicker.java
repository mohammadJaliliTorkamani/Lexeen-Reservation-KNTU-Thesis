package ir.ac.kntu.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.LineString;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Entity.ServerResponse;
import ir.ac.kntu.Interface.Retrofit.Account_Server_API;
import ir.ac.kntu.Interface.Retrofit.Order_Server_API;
import ir.ac.kntu.Interface.Retrofit.Restaurant_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import ir.map.sdk_map.MapirStyle;
import ir.map.sdk_map.maps.MapView;
import ir.map.servicesdk.MapService;
import ir.map.servicesdk.ResponseListener;
import ir.map.servicesdk.enums.RouteType;
import ir.map.servicesdk.model.base.MapirError;
import ir.map.servicesdk.request.RouteRequest;
import ir.map.servicesdk.response.RouteResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_VIEWPORT;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class Fragment_LocationPicker extends Fragment {
    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    private static MapboxMap map;
    LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(ContextHelper.retrieveContext())
            .elevation(5)
            .accuracyAlpha(.6f)
            .accuracyColor(Color.RED)
            .build();
    private MapView mapView;
    private MapService mapService = new MapService();
    private EditText blockNo;
    private EditText floor;
    private EditText unit;
    private TextViewPlus selectPlaceText;
    private ProgressBar selectPlacePB;
    private LinearLayout detailAddressFrame;
    private ImageView back;
    private Style mapStyle;
    private Order order;
    private FloatingActionButton myLocation;
    private CoordinatorLayout placeContainer;
    private int step;//0: location selection   1: selection completed & show polygon for final confirmation
    private ImageView hoveringMarker;
    private Symbol restaurantSymbol;
    private Symbol destinationSymbol;
    private SymbolManager sampleSymbolManager;
    private LineManager lineManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.fragment_location_picker, null, false);
        if (getArguments() == null || getArguments().getString("ORDER") == null)
            Helper_Log.errorLog(Fragment_LocationPicker.class);
        else
            order = new Gson().fromJson(getArguments().getString("ORDER"), Order.class);
        findViews(view);
        initializeViewContents(view);
        manageListeners(view);
        return view;
    }

    private void findViews(View view) {
        placeContainer = view.findViewById(R.id.location_picker_select_place_container);
        mapView = view.findViewById(R.id.location_picker_mapview);
        selectPlacePB = view.findViewById(R.id.location_picker_select_place_pb);
        selectPlaceText = view.findViewById(R.id.location_picker_select_place_text);
        back = view.findViewById(R.id.location_picker_back);
        myLocation = view.findViewById(R.id.location_picker_my_location);
        blockNo = view.findViewById(R.id.location_picker_block_no);
        floor = view.findViewById(R.id.location_picker_floor);
        unit = view.findViewById(R.id.location_picker_unit);
        detailAddressFrame = view.findViewById(R.id.location_picker_top_address_detail_frame);
    }

    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        // Add the marker image to map
        loadedMapStyle.addImage("dropped-icon-image", BitmapUtils.getBitmapFromDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pin, null)));
        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                visibility(NONE),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }


    private void initializeViewContents(View view) {
        placeContainer.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        detailAddressFrame.setVisibility(View.GONE);
        selectPlacePB.setVisibility(View.GONE);
        selectPlaceText.setVisibility(View.VISIBLE);
        if (step == 0) {
            selectPlaceText.setText(getString(R.string.select_place));
        }

        mapView.getMapAsync(mapboxMap -> {
            map = mapboxMap;
            map.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), style -> {
                mapStyle = style;
                sampleSymbolManager = new SymbolManager(mapView, map, mapStyle);
                lineManager = new LineManager(mapView, map, mapStyle, "hw-secondary-tertiary");
                Connector.createService(view, Restaurant_Server_API.class, object ->
                        object.getRestaurantInfo(Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_SELECTED_RESTAURANT_QR_CODE, null)).enqueue(new Callback<Restaurant>() {
                            @Override
                            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                                if (response.body() != null) {
                                    enableLocationComponent();
                                    addSymbolToMap(response.body().getAddress().getLatitude(), response.body().getAddress().getLongitude(), false);
                                    hoveringMarker = new ImageView(ContextHelper.retrieveContext());
                                    hoveringMarker.setImageResource(R.drawable.ic_pin);
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                                    hoveringMarker.setLayoutParams(params);
                                    mapView.addView(hoveringMarker);
                                    initDroppedMarker(style);
                                } else {
                                    Helper_Log.errorLog(Fragment_LocationPicker.class);
                                }
                            }

                            @Override
                            public void onFailure(Call<Restaurant> call, Throwable t) {
                                Helper_Log.errorLog(t, Fragment_LocationPicker.class);
                            }
                        }));
            });
        });
    }

    private void addSymbolToMap(double latitude, double longitude, boolean isDestinationSymbol) {
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(ResourcesCompat.getDrawable(getResources(), isDestinationSymbol ? R.drawable.ic_pin : R.drawable.ic_food_store, null));
        mapStyle.addImage("sample_image_id_" + isDestinationSymbol, bitmap);
        sampleSymbolManager.setIconAllowOverlap(true);
        sampleSymbolManager.setIconRotationAlignment(ICON_ROTATION_ALIGNMENT_VIEWPORT);
        SymbolOptions sampleSymbolOptions = new SymbolOptions();
        sampleSymbolOptions.withLatLng(new LatLng(latitude, longitude));
        sampleSymbolOptions.withIconImage("sample_image_id_" + isDestinationSymbol);
        sampleSymbolOptions.withIconSize(1.0f);
        Symbol sampleSymbol = sampleSymbolManager.create(sampleSymbolOptions);
        if (isDestinationSymbol)
            destinationSymbol = sampleSymbol;
        else
            restaurantSymbol = sampleSymbol;
    }

    private void enableLocationComponent() {
        if (PermissionsManager.areLocationPermissionsGranted(ContextHelper.retrieveContext())) {

            LocationComponent locationComponent = map.getLocationComponent();
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(ContextHelper.retrieveContext(), mapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);


        } else {
            PermissionsManager permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                }

                @Override
                public void onPermissionResult(boolean granted) {
                    if (granted)
                        enableLocationComponent();
                    else
                        Helper.getInstance().toast(R.string.location_access_not_permitted, Constants.ToastMode.INFO);
                }
            });
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }


    private void manageListeners(View view) {
        myLocation.setOnClickListener(v -> {
            if (map != null) {
                Location location = map.getLocationComponent().getLastKnownLocation();
                if (location != null)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
            }
        });

        back.setOnClickListener(v -> {
            if (step == 0) {
                getActivity().onBackPressed();
            } else {
                sampleSymbolManager.delete(destinationSymbol);
                lineManager.deleteAll();
                map.getUiSettings().setAllGesturesEnabled(true);
                step = 0;
                selectPlaceText.setText(R.string.select_place);
                detailAddressFrame.setVisibility(View.GONE);
                hoveringMarker.setVisibility(View.VISIBLE);
            }
        });
        selectPlaceText.setOnClickListener(v -> {

            if (step == 0) {
                try {
                    selectPlacePB.setVisibility(View.VISIBLE);
                    selectPlaceText.setVisibility(View.GONE);
                    double latitude = map.getCameraPosition().target.getLatitude();
                    double longitude = map.getCameraPosition().target.getLongitude();
                    Connector.createService(view, Account_Server_API.class, object -> object.getCash().enqueue(new Callback<Double>() {
                        @Override
                        public void onResponse(Call<Double> call, Response<Double> response) {
                            if (response.body() != null) {
                                double currentCash = response.body();
                                if (currentCash >= order.getTotalPrice()) {//enough cash
                                    double restaurantLatitude = restaurantSymbol.getLatLng().getLatitude();
                                    double restaurantLongitude = restaurantSymbol.getLatLng().getLongitude();
                                    RouteRequest requestBody = new RouteRequest.Builder(
                                            latitude, longitude,
                                            restaurantLatitude, restaurantLongitude,
                                            RouteType.WALKING
                                    ).build();
                                    addSymbolToMap(latitude, longitude, true);
                                    mapService.route(requestBody, new ResponseListener<RouteResponse>() {
                                        @Override
                                        public void onSuccess(RouteResponse response) {
                                            if (!response.getRoutes().isEmpty()) {
                                                showRouteOnMap(response.getRoutes().get(0).getGeometry());
                                                step++;
                                                selectPlaceText.setText(getString(R.string.confirm));
                                                Animation animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_down);
                                                animation.setFillAfter(true);
                                                detailAddressFrame.setVisibility(View.INVISIBLE);
                                                detailAddressFrame.startAnimation(animation);
                                                selectPlacePB.setVisibility(View.GONE);
                                                selectPlaceText.setVisibility(View.VISIBLE);

                                                List<LatLng> sampleLatLngList = new ArrayList<>();
                                                sampleLatLngList.add(new LatLng(restaurantLatitude, restaurantLongitude));
                                                sampleLatLngList.add(new LatLng(latitude, longitude));
                                                int samplePadding = 200;
                                                int sampleBearing = 0;
                                                int sampleTilt = 0;
                                                LatLngBounds sampleLatLngBounds = new LatLngBounds.Builder().includes(sampleLatLngList).build();
                                                map.animateCamera(CameraUpdateFactory.newLatLngBounds(sampleLatLngBounds, sampleBearing, sampleTilt, samplePadding));
                                                hoveringMarker.setVisibility(View.INVISIBLE);
                                                map.getUiSettings().setAllGesturesEnabled(false);
                                            } else {
                                                Helper.getInstance().toast(R.string.invalid_destination, Constants.ToastMode.INFO);
                                                back.callOnClick();
                                            }
                                        }

                                        @Override
                                        public void onError(MapirError error) {
                                            Helper_Log.errorLog(Fragment_LocationPicker.class);
                                            selectPlacePB.setVisibility(View.GONE);
                                            selectPlaceText.setVisibility(View.VISIBLE);
                                        }
                                    });


                                } else {//not enough cash
                                    Helper.getInstance().toast(R.string.no_enough_charge, Constants.ToastMode.INFO);
                                    selectPlacePB.setVisibility(View.GONE);
                                    selectPlaceText.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Helper_Log.errorLog(Fragment_LocationPicker.class);
                                selectPlacePB.setVisibility(View.GONE);
                                selectPlaceText.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<Double> call, Throwable t) {
                            Helper_Log.errorLog(t, Fragment_LocationPicker.class);
                            selectPlacePB.setVisibility(View.GONE);
                            selectPlaceText.setVisibility(View.VISIBLE);
                        }
                    }));
                } catch (Exception e) {
                    Helper_Log.errorLog(e, Fragment_LocationPicker.class);
                    selectPlacePB.setVisibility(View.GONE);
                    selectPlaceText.setVisibility(View.VISIBLE);
                }
            } else {
                if (blockNo.getText().length() == 0) {
                    Helper.getInstance().toast(R.string.enter_block_no, Constants.ToastMode.WARNING);
                } else if (floor.getText().length() == 0) {
                    Helper.getInstance().toast(R.string.enter_floor, Constants.ToastMode.WARNING);
                } else if (unit.getText().length() == 0) {
                    Helper.getInstance().toast(R.string.enter_unit, Constants.ToastMode.WARNING);
                } else if (!Helper.getInstance().isInteger(floor.getText().toString(), 10)) {
                    Helper.getInstance().toast(R.string.bad_formatted_floor, Constants.ToastMode.WARNING);
                } else if (!Helper.getInstance().isInteger(unit.getText().toString(), 10)) {
                    Helper.getInstance().toast(R.string.bad_formatted_unit, Constants.ToastMode.WARNING);
                } else {
                    Helper
                            .getInstance()
                            .showBiOptionsDialog(
                                    Fragment_LocationPicker.this,
                                    getString(R.string.accept_order),
                                    getString(R.string.are_you_sure_to_accept_order),
                                    getString(R.string.yes),
                                    getString(R.string.cancel2),
                                    dialog -> {
                                        dialog.dismiss();
                                        if (!Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode()).isEmpty()) {
                                            selectPlacePB.setVisibility(View.VISIBLE);
                                            selectPlaceText.setVisibility(View.GONE);
                                            Connector.createService(view, Order_Server_API.class, object -> {
                                                try {
                                                    double latitude = map.getCameraPosition().target.getLatitude();
                                                    double longitude = map.getCameraPosition().target.getLongitude();

                                                    order.setLatitude(latitude);
                                                    order.setLongitude(longitude);
                                                    order.setFloor(Integer.parseInt(floor.getText().toString()));
                                                    order.setUnit(Encryption.getInstance().encrypt(unit.getText().toString()));
                                                    order.setBlockNo(Encryption.getInstance().encrypt(blockNo.getText().toString()));
                                                    order.setDelivery(true);
                                                    object.deliver(order).enqueue(new Callback<ServerResponse>() {
                                                        @Override
                                                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                                            if (response.body() != null) {
                                                                switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                                                                    case DONE://so we have issue tracking no in message
                                                                        Helper.getInstance().toast(R.string.ordered_successfully, Constants.ToastMode.SUCCESS);
                                                                        order.setOrderID(Integer.parseInt(response.body().getMessage()));
                                                                        Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().clearAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode());
                                                                        Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).orderInterface().clearAll();
                                                                        getFragmentManager().popBackStack();
                                                                        getFragmentManager()
                                                                                .beginTransaction()
                                                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                                                .addToBackStack("user_orders")
                                                                                .add(R.id.main_frame, new Fragment_UserOrders())
                                                                                .commit();
                                                                        break;
                                                                    case FAILED:  //so we have error message in message
                                                                        Helper.getInstance().toast(Encryption.getInstance().decrypt(response.body().getMessage()), Constants.ToastMode.ERROR);
                                                                        openMainFragment();
                                                                        break;
                                                                    default: //so we have null in message
                                                                        Helper.getInstance().toast(getString(R.string.unknown_error), Constants.ToastMode.ERROR);
                                                                        openMainFragment();
                                                                        break;
                                                                }
                                                            } else {
                                                                Helper_Log.errorLog(Fragment_LocationPicker.class);
                                                                openMainFragment();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                                                            Helper_Log.errorLog(t, Fragment_LocationPicker.class);
                                                            openMainFragment();
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    Helper_Log.errorLog(e, Fragment_LocationPicker.class);
                                                }
                                            });
                                        } else
                                            Helper.getInstance().toast(getString(R.string.no_orders_found), Constants.ToastMode.INFO);
                                    },
                                    dialog -> dialog.dismiss(), false);
                }
            }
        });
    }

    private void openMainFragment() {
        Fragment toOpen = new Fragment_Main();
        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.main_frame, toOpen).commit();
    }

    private void showRouteOnMap(String geometry) {
        LineString routeLine = LineString.fromPolyline(geometry, 5); // second parameter must be 5
        LineOptions lineOptions = new LineOptions()
                .withGeometry(routeLine)
                .withLineColor("#469bf0")
                .withLineWidth(5f);
        lineManager.create(lineOptions);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}