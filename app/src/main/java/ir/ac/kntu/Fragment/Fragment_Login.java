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

import java.io.IOException;

import ir.ac.kntu.Entity.AuthenticationResponse;
import ir.ac.kntu.Interface.Retrofit.Account_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.EditTextPlus;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Login extends Fragment {
    private static String LOGIN_VIDEO_STREAM_LINK;
    private String initedUsername;
    private String initedPassword;
    private VideoView videoView;
    private EditTextPlus phone;
    private EditTextPlus password;
    private ConstraintLayout loginContainer;
    private ProgressBar progressBar;
    private TextViewPlus loginText;
    private TextViewPlus register;
    private TextViewPlus forget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        findViews(view);
        initialize(view);
        manageListeners(view);
        if (initedUsername != null && initedPassword != null) {
            phone.setText(initedUsername);
            password.setText(initedPassword);
            loginContainer.callOnClick();
        }
        return view;
    }

    private void findViews(View view) {
        videoView = view.findViewById(R.id.fragment_login_videoview);
        loginContainer = view.findViewById(R.id.fragment_login_login);
        progressBar = view.findViewById(R.id.fragment_login_login_progressbar);
        loginText = view.findViewById(R.id.fragment_login_login_text);
        register = view.findViewById(R.id.fragment_login_register);
        phone = view.findViewById(R.id.fragment_login_phone_number);
        password = view.findViewById(R.id.fragment_login_password);
        forget = view.findViewById(R.id.fragment_login_forget);
    }

    private void initialize(View view) {
        Helper.getInstance().changeShapeColorToMainAppColor(loginContainer);
        Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_FIRST_USE_STATE, "FALSE");
        progressBar.setVisibility(View.GONE);
        if (getArguments() != null) {
            initedUsername = getArguments().getString("USERNAME");
            initedPassword = getArguments().getString("PASSWORD");
            if (initedUsername != null) {
                Helper.getInstance().info("You have a login load");
            }
        }
        onResume();
    }

    @Override
    public void onResume() {
        LOGIN_VIDEO_STREAM_LINK = "android.resource://" + ContextHelper.retrieveContext().getPackageName() + "/" + R.raw.log_in_background;
        Uri uri = Uri.parse(LOGIN_VIDEO_STREAM_LINK);
        videoView.setVideoURI(uri);
        videoView.start();
        super.onResume();
    }

    private void manageListeners(View view) {
        loginContainer.setOnClickListener(v -> {
            String phone = this.phone.getText().toString().trim();
            String password = this.password.getText().toString().trim();
            if (phone.isEmpty()) {
                Helper.getInstance().toast(getString(R.string.enter_phone), Constants.ToastMode.WARNING);
            } else if (password.isEmpty()) {
                Helper.getInstance().toast(getString(R.string.empty_password), Constants.ToastMode.WARNING);
            } else {
                try {
                    String hashedPassword = Helper.getInstance().hash(password);
                    loginText.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    Connector.createService(view, Account_Server_API.class, loginObject -> {
                        loginObject.login(Helper.getInstance().getDefaultPrePhone() + phone, hashedPassword).enqueue(new Callback<AuthenticationResponse>() {
                            @Override
                            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                                loginText.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                if (response.body() != null) {
                                    AuthenticationResponse authenticationResponse = response.body();
                                    switch (AuthenticationResponse.ResultCode.getResult(authenticationResponse.getResultCode())) {
                                        case ERROR:
                                            Helper.getInstance().toast(authenticationResponse.getMessage(), Constants.ToastMode.ERROR);
                                            break;
                                        case SUCCESSFUL:
                                            try {
                                                Setting.getInstance().saveSetting(Constants._TABLE_PROFILE, Constants._KEY_TOKEN, authenticationResponse.getToken());
                                                exchangeKeys(getActivity().getWindow().getDecorView().findViewById(R.id.main_act), object -> {
                                                    Helper.getInstance().toast(getString(R.string.welcome) + " !", Constants.ToastMode.SUCCESS);
                                                    Setting.getInstance().hideKeyboard(getActivity());
                                                    Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_LOGIN_STATE, "USER");
                                                    //DONED
                                                    getFragmentManager()
                                                            .beginTransaction()
                                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                            .replace(R.id.main_frame, new Fragment_LandingPage())
                                                            .commit();
                                                });
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                            break;
                                    }
                                } else {
                                    Helper_Log.errorLog(Fragment_Login.class);
                                }
                            }

                            @Override
                            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                                loginText.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Helper_Log.errorLog(t, Fragment_Login.class);
                            }
                        });
                    });
                } catch (Exception e) {
                    Helper_Log.errorLog(e, Fragment_Login.class);
                }
            }
        });
        register.setOnClickListener(v ->
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack("register")
                        .add(R.id.main_frame, new Fragment_Register())
                        .commit());
        forget.setOnClickListener(v ->
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack("forget")
                        .add(R.id.main_frame, new Fragment_Forget())
                        .commit());
    }

    private void exchangeKeys(View view, Runnable_SingleArg<Long> runnable) throws IOException {
        Encryption.getInstance().generateG();
        Encryption.getInstance().generatePrime();
        long privateKey = Encryption.getInstance().selectPrivateKey();
        Encryption.getInstance().calculateX(privateKey);
        Encryption.getInstance().computeSharedSecretKey(view, privateKey, runnable);
    }
}
