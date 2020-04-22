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
import ir.ac.kntu.Interface.Retrofit.Operable_General;
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

public class Fragment_Enter_Activation_Code extends Fragment {
    private static String LOGIN_VIDEO_STREAM_LINK;
    private VideoView videoView;
    private ConstraintLayout resetContainer;
    private TextViewPlus reset;
    private ProgressBar progressBar;
    private EditTextPlus activationCode;

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

    private void findViews(View view) {
        videoView = view.findViewById(R.id.fragment_forget_enter_code_videoview);
        resetContainer = view.findViewById(R.id.fragment_forget_enter_code_reset);
        reset = view.findViewById(R.id.fragment_forget_enter_code_text);
        progressBar = view.findViewById(R.id.fragment_forget_enter_code_progressbar);
        activationCode = view.findViewById(R.id.fragment_forget_enter_code_code);
    }

    private void initializeViewContents(View view) {
        resetContainer.setBackgroundResource(R.drawable.dr_login_orange_btn);
        Helper.changeShapeColorToMainAppColor(resetContainer);
        LOGIN_VIDEO_STREAM_LINK = "android.resource://" + ContextHelper.retrieveContext().getPackageName() + "/" + R.raw.log_in_background;
        Uri uri = Uri.parse(LOGIN_VIDEO_STREAM_LINK);
        videoView.setVideoURI(uri);
        videoView.start();
        progressBar.setVisibility(View.GONE);
        reset.setVisibility(View.VISIBLE);
    }

    private void initializeServerSupplied(View view) {
    }

    private void manageListeners(View view) {
        reset.setOnClickListener(v -> {
            if (activationCode.getText().length() == 0) {
                Helper.toast(getString(R.string.enter_activation_code), Constants.ToastMode.WARNING);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                reset.setVisibility(View.GONE);
                Connector.createService(view, Operable_General.class, object -> object.sendForgetnessVerificationCode(activationCode.getText().toString()).enqueue(new Callback<ServerResponse>() {
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
                                    Helper.toast(getString(R.string.error) + " : " + response.body().getMessage(), Constants.ToastMode.ERROR);
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
