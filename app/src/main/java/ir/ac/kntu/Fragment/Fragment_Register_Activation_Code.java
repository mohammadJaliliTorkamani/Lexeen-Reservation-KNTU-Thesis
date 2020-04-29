package ir.ac.kntu.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import ir.ac.kntu.Entity.AuthenticationResponse;
import ir.ac.kntu.Entity.NormalUser;
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

public class Fragment_Register_Activation_Code extends Fragment {
    private static String REGISTER_VIDEO_STREAM_LINK;
    private VideoView videoView;
    private NormalUser normalUser;
    private EditTextPlus code;
    private ConstraintLayout signUpContainer;
    private TextViewPlus signUp;
    private ProgressBar progressbar;
    private TextViewPlus resend;
    private CountDownTimer countDownTimer;
    private boolean allowedToResend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_activation_code, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeOnlineContents(view);
        manageListeners(view);
        return view;
    }

    private void findViews(View view) {
        videoView = view.findViewById(R.id.fragment_register_activation_code_videoview);
        signUpContainer = view.findViewById(R.id.fragment_register_activation_code_sing_up_constainer);
        resend = view.findViewById(R.id.fragment_register_activation_code_resend);
        signUp = view.findViewById(R.id.fragment_register_activation_code_sing_up_text);
        progressbar = view.findViewById(R.id.fragment_register_activation_code_sing_up_progressbar);
        code = view.findViewById(R.id.fragment_register_activation_code_code);
    }

    private void initializeViewContents(View view) {
        signUpContainer.setBackgroundResource(R.drawable.dr_login_orange_btn);
        Helper.getInstance().changeShapeColorToMainAppColor(signUpContainer);
        signUp.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
        resend.setText("00:00");
        allowedToResend = false;
        normalUser = new Gson().fromJson(getArguments().getString("TO_REGISTER_SERIALIZED_OBJECT"), NormalUser.class);
        if (normalUser == null) {
            Helper.getInstance().toast(getString(R.string.error), Constants.ToastMode.ERROR);
            getActivity().onBackPressed();
        }

        REGISTER_VIDEO_STREAM_LINK = "android.resource://" + ContextHelper.retrieveContext().getPackageName() + "/" + R.raw.register_background;
        Uri uri = Uri.parse(REGISTER_VIDEO_STREAM_LINK);
        videoView.setVideoURI(uri);
        videoView.start();

        countDownTimer = new CountDownTimer(Helper.getInstance().GET_RESEND_VERIFICATION_CODE_INTERVAL(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minute = Helper.getInstance().getMinuteFromSecond(millisUntilFinished);
                int second = Helper.getInstance().getSecondFromSeconds(millisUntilFinished);
                resend.setText((minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second);
                allowedToResend = false;
                resend.setClickable(false);
            }

            @Override
            public void onFinish() {
                allowedToResend = !allowedToResend;
                resend.setText(ContextHelper.retrieveContext().getString(R.string.resend));
                resend.setClickable(true);
            }
        };
        countDownTimer.start();
    }

    private void initializeOnlineContents(View view) {
    }

    private void manageListeners(View view) {
        resend.setOnClickListener(v -> Connector.createService(view, Account_Server_API.class, object -> object.sendRegisterVerificationCode(normalUser.getPhone())
                .enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.body() != null) {
                            switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                                case DONE:
                                    Helper.getInstance().toast(getString(R.string.activation_code_sent), Constants.ToastMode.SUCCESS);
                                    countDownTimer.start();
                                    break;
                                case UNKNOWN:
                                case FAILED:
                                    Helper.getInstance().toast(getString(R.string.error) + " : " + response.body().getMessage(), Constants.ToastMode.ERROR);
                            }
                        } else {
                            Helper_Log.errorLog(Fragment_Register_Activation_Code.class);
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Helper_Log.errorLog(t, Fragment_Register_Activation_Code.class);
                    }
                })));
        signUp.setOnClickListener(v -> {
            if (code.getText().length() == 0) {
                Helper.getInstance().toast(getString(R.string.enter_activation_code), Constants.ToastMode.WARNING);
            } else {
                signUp.setVisibility(View.GONE);
                progressbar.setVisibility(View.VISIBLE);
                Connector.createService(view, Account_Server_API.class, object -> {
                    try {
                        object.register(code.getText().toString(), normalUser.getName(), normalUser.getLastName(),
                                normalUser.getPhone(), normalUser.getPassword(),
                                normalUser.isMale()).enqueue(new Callback<AuthenticationResponse>() {
                            @Override
                            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                                if (response.body() != null) {
                                    switch (response.body().getResultCode()) {
                                        case 0:
                                            countDownTimer.cancel();
                                            Fragment fragment = new Fragment_Login();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("USERNAME", normalUser.getPhone().substring(4));
                                            bundle.putString("PASSWORD", normalUser.getPassword());
                                            fragment.setArguments(bundle);
                                            Setting.getInstance().hideKeyboard(getActivity());
                                            getFragmentManager()
                                                    .beginTransaction()
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .addToBackStack("register_activation_code")
                                                    .add(R.id.main_frame, fragment)
                                                    .commit();

                                            break;
                                        case 1:
                                            Helper.getInstance().toast(getString(R.string.error) + " : " + response.body().getMessage(), Constants.ToastMode.ERROR);
                                            break;
                                    }
                                } else
                                    Helper_Log.errorLog(Fragment_Register_Activation_Code.class);
                                signUp.setVisibility(View.VISIBLE);
                                progressbar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                                Helper_Log.errorLog(t, Fragment_Register_Activation_Code.class);
                                signUp.setVisibility(View.VISIBLE);
                                progressbar.setVisibility(View.GONE);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }
}
