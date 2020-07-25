package ir.ac.kntu.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ir.ac.kntu.Entity.ServerResponse;
import ir.ac.kntu.Interface.Retrofit.Account_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.EditTextPlus;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * reset password fragment ables user to reset password and log into the app
 */
public class Fragment_Forget_Reset_Password extends Fragment {
    private static String LOGIN_VIDEO_STREAM_LINK;
    private VideoView videoView;
    private ConstraintLayout enterContainer;
    private TextViewPlus enter;
    private ProgressBar progressBar;
    private EditTextPlus password;
    private EditTextPlus passwordConfirmed;

    /**
     * fragment entry point, finds views, loads data, initializes UI elements and declares listeners
     *
     * @param inflater           inflater
     * @param container          container view
     * @param savedInstanceState saved instance bundle
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_reset_password, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeServerSupplied(view);
        manageListeners(view);
        return view;
    }

    /**
     * assign view objects to view elements
     *
     * @param view to find views with it
     */
    private void findViews(View view) {
        videoView = view.findViewById(R.id.fragment_forget_reset_password_videoview);
        enter = view.findViewById(R.id.fragment_forget_reset_password_text);
        enterContainer = view.findViewById(R.id.fragment_forget_reset_password_enter);
        progressBar = view.findViewById(R.id.fragment_forget_reset_password_progressbar);
        password = view.findViewById(R.id.fragment_forget_reset_password_password);
        passwordConfirmed = view.findViewById(R.id.fragment_forget_reset_password_password_confirmed);
    }

    /**
     * initializes UI elements and plays videos by calling "onResume"
     *
     * @param view view to work
     */
    private void initializeViewContents(View view) {
        enterContainer.setBackgroundResource(R.drawable.dr_login_orange_btn);
        Helper.getInstance().changeShapeColorToMainAppColor(enterContainer);
        onResume();
    }

    /**
     * changes some UI elements and plays video
     */
    @Override
    public void onResume() {
        LOGIN_VIDEO_STREAM_LINK = "android.resource://" + ContextHelper.retrieveContext().getPackageName() + "/" + R.raw.log_in_background;
        Uri uri = Uri.parse(LOGIN_VIDEO_STREAM_LINK);
        videoView.setVideoURI(uri);
        videoView.start();
        progressBar.setVisibility(View.GONE);
        enter.setVisibility(View.VISIBLE);
        super.onResume();
    }

    /**
     * loads data from server
     *
     * @param view view to work
     */
    private void initializeServerSupplied(View view) {
    }

    /**
     * declares listeners for some UI elements
     *
     * @param view
     */
    private void manageListeners(View view) {
        enter.setOnClickListener(v -> {
            if (password.getText().length() == 0) {
                Helper.getInstance().toast(getString(R.string.enter_password), Constants.ToastMode.INFO);
            } else if (password.getText().length() < Constants.PASSWORD_MINIMUM_LENGTH) {
                Helper.getInstance().toast(getString(R.string.short_password), Constants.ToastMode.INFO);
            } else if (passwordConfirmed.getText().length() == 0) {
                Helper.getInstance().toast(getString(R.string.enter_password_confirmed), Constants.ToastMode.INFO);
            } else if (!passwordConfirmed.getText().toString().equals(password.getText().toString())) {
                Helper.getInstance().toast(getString(R.string.password_not_matched), Constants.ToastMode.ERROR);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                enter.setVisibility(View.GONE);
                Connector.createService(view, Account_Server_API.class, object -> {
                    try {
                        object.resetForgotPassword(Helper.getInstance().hash(password.getText().toString()), getArguments().getString("PHONE")).enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                if (response.body() != null) {
                                    switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                                        case DONE:
                                            Helper.getInstance().toast(R.string.password_successfully_changed, Constants.ToastMode.SUCCESS);
                                            Fragment fragment = new Fragment_Login();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("USERNAME", getArguments().getString("PHONE"));
                                            bundle.putString("PASSWORD", password.getText().toString());
                                            fragment.setArguments(bundle);
                                            Setting.getInstance().hideKeyboard(getActivity());
                                            getFragmentManager().popBackStack();//pop the current fragment
                                            getFragmentManager().popBackStack();//pop the previous fragment
                                            getFragmentManager()
                                                    .beginTransaction()
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .addToBackStack("login")
                                                    .add(R.id.main_frame, fragment)
                                                    .commit();
                                            break;
                                        case FAILED:
                                        case UNKNOWN:
                                            Helper.getInstance().toast(getString(R.string.error) + " : " + response.body().getMessage(), Constants.ToastMode.ERROR);
                                            break;
                                    }
                                } else
                                    Helper_Log.errorLog(Fragment_Forget_Reset_Password.class);
                                progressBar.setVisibility(View.GONE);
                                enter.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                Helper_Log.errorLog(t, Fragment_Forget_Reset_Password.class);
                                progressBar.setVisibility(View.GONE);
                                enter.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (Exception e) {
                        Helper_Log.errorLog(e, Fragment_Forget_Reset_Password.class);
                    }
                });
            }
        });
    }
}
