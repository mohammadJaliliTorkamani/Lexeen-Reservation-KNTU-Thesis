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
 * activation enter fragment ables user to enter sent code and complete sign up operation
 */
public class Fragment_Enter_Activation_Code extends Fragment {
    private static String LOGIN_VIDEO_STREAM_LINK;
    private VideoView videoView;
    private ConstraintLayout resetContainer;
    private TextViewPlus reset;
    private ProgressBar progressBar;
    private EditTextPlus activationCode;

    /**
     * entry point which inflates view, finds views, initialize UI elements from the server
     * and define listeners
     *
     * @param inflater           inflater
     * @param container          container view
     * @param savedInstanceState saved instance bundle
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_activation_code, container, false);
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
        videoView = view.findViewById(R.id.fragment_forget_enter_code_videoview);
        resetContainer = view.findViewById(R.id.fragment_forget_enter_code_reset);
        reset = view.findViewById(R.id.fragment_forget_enter_code_text);
        progressBar = view.findViewById(R.id.fragment_forget_enter_code_progressbar);
        activationCode = view.findViewById(R.id.fragment_forget_enter_code_code);
    }

    /**
     * initializes UI elements
     *
     * @param view view to work
     */
    private void initializeViewContents(View view) {
        resetContainer.setBackgroundResource(R.drawable.dr_login_orange_btn);
        Helper.getInstance().changeShapeColorToMainAppColor(resetContainer);
        LOGIN_VIDEO_STREAM_LINK = "android.resource://" + ContextHelper.retrieveContext().getPackageName() + "/" + R.raw.log_in_background;
        Uri uri = Uri.parse(LOGIN_VIDEO_STREAM_LINK);
        videoView.setVideoURI(uri);
        videoView.start();
        progressBar.setVisibility(View.GONE);
        reset.setVisibility(View.VISIBLE);
    }

    /**
     * initializes some UI elements from the server
     *
     * @param view view to work
     */
    private void initializeServerSupplied(View view) {
    }

    /**
     * declares listeners of UI elements
     *
     * @param view view to work
     */
    private void manageListeners(View view) {
        reset.setOnClickListener(v -> {
            if (activationCode.getText().length() == 0) {
                Helper.getInstance().toast(getString(R.string.enter_activation_code), Constants.ToastMode.WARNING);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                reset.setVisibility(View.GONE);
                Connector.createService(view, Account_Server_API.class, object -> object.sendForgetnessVerificationCode(activationCode.getText().toString()).enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.body() != null) {
                            switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                                case DONE:
                                    Setting.getInstance().hideKeyboard(getActivity());
                                    Fragment fragment = new Fragment_Forget_Reset_Password();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("PHONE", getArguments().getString("PHONE"));
                                    fragment.setArguments(bundle);

                                    getFragmentManager()
                                            .beginTransaction()
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                            .replace(R.id.main_frame, fragment)
                                            .commit();
                                    break;
                                case FAILED:
                                case UNKNOWN:
                                    Helper.getInstance().toast(getString(R.string.error) + " : " + response.body().getMessage(), Constants.ToastMode.ERROR);
                                    break;
                            }
                        } else
                            Helper_Log.errorLog(Fragment_Enter_Activation_Code.class);
                        progressBar.setVisibility(View.GONE);
                        reset.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Helper_Log.errorLog(t, Fragment_Enter_Activation_Code.class);
                        progressBar.setVisibility(View.GONE);
                        reset.setVisibility(View.VISIBLE);
                    }
                }));
            }
        });
    }
}
