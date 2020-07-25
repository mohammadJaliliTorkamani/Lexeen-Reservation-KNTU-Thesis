package ir.ac.kntu.Fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.pushpole.sdk.PushPole;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * register fragment enables user to sign up and create profile or switch to login page
 */
public class Fragment_Register extends Fragment {
    private static String REGISTER_VIDEO_STREAM_LINK;
    private ConstraintLayout signUpContainer;
    private VideoView videoView;
    private boolean isMale = true;
    private EditTextPlus firstName;
    private EditTextPlus lastName;
    private EditTextPlus phoneNumber;
    private TextViewPlus male;
    private TextViewPlus female;
    private EditTextPlus password;
    private TextViewPlus signUp;
    private ProgressBar progressBar;
    private ConstraintLayout phoneContainer;
    private TextViewPlus logIn;

    /**
     * fragment entry point which finds views, initializes UI elements and declares listeners
     *
     * @param inflater           inflated
     * @param container          container view
     * @param savedInstanceState saved instance bundle
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        findViews(view);
        initialize(view);
        manageListeners(view);
        return view;
    }

    /**
     * assign view objects to view elements
     *
     * @param view to find views with it
     */
    private void findViews(View view) {
        signUpContainer = view.findViewById(R.id.fragment_register_sing_up_container);
        videoView = view.findViewById(R.id.fragment_register_video_view);
        firstName = view.findViewById(R.id.fragment_register_first_name);
        lastName = view.findViewById(R.id.fragment_register_last_name);
        phoneNumber = view.findViewById(R.id.fragment_register_phone_number);
        phoneContainer = view.findViewById(R.id.fragment_register_phone_container);
        male = view.findViewById(R.id.fragment_register_male);
        female = view.findViewById(R.id.fragment_register_female);
        password = view.findViewById(R.id.fragment_register_password);
        signUp = view.findViewById(R.id.fragment_register_sing_up_text);
        progressBar = view.findViewById(R.id.fragment_register_sing_up_progressbar);
        logIn = view.findViewById(R.id.fragment_register_log_in);
    }

    /**
     * graphical male(i==0)  or female(i==1) button selects and disables the other button
     *
     * @param i
     */
    private void selectButton(int i) {
        male.setBackgroundResource(R.drawable.dr_register_male_unselected);
        female.setBackgroundResource(R.drawable.dr_register_female_unselected);

        if (i == 0) {//Male{
            isMale = true;
            male.setBackgroundResource(R.drawable.dr_register_male_selected);
            male.setTextColor(Color.WHITE);
            female.setTextColor(Color.BLACK);
        } else {//Female
            isMale = false;
            female.setBackgroundResource(R.drawable.dr_register_female_selected);
            female.setTextColor(Color.WHITE);
            male.setTextColor(Color.BLACK);
        }
    }

    /**
     * initializes some UI contents and calls onResume (to play video)
     *
     * @param view
     */
    private void initialize(View view) {
        signUpContainer.setBackgroundResource(R.drawable.dr_login_orange_btn);
        Helper.getInstance().changeShapeColorToMainAppColor(signUpContainer);
        onResume();
        selectButton(0);
    }

    /**
     * plays video and some UI changes
     */
    @Override
    public void onResume() {
        REGISTER_VIDEO_STREAM_LINK = "android.resource://" + ContextHelper.retrieveContext().getPackageName() + "/" + R.raw.register_background;
        Uri uri = Uri.parse(REGISTER_VIDEO_STREAM_LINK);
        videoView.setVideoURI(uri);
        videoView.start();
        signUp.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        super.onResume();
    }

    /**
     * declares listeners for some UI elements
     *
     * @param view view to work
     */
    private void manageListeners(View view) {
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (firstName.getText() == null || firstName.getText().toString().isEmpty())
                    return;
                if (Helper.getInstance().containsNonPersianLanguage(firstName.getText().toString())) {
                    Helper.getInstance().toast(ContextHelper.retrieveContext().getString(R.string.change_your_keyboard_language_to_persian), Constants.ToastMode.WARNING);
                    firstName.setText(firstName.getText().subSequence(0, firstName.length() - 1));
                }
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (lastName.getText() == null || lastName.getText().toString().isEmpty())
                    return;
                if (Helper.getInstance().containsNonPersianLanguage(lastName.getText().toString())) {
                    Helper.getInstance().toast(ContextHelper.retrieveContext().getString(R.string.change_your_keyboard_language_to_persian), Constants.ToastMode.WARNING);
                    lastName.setText(lastName.getText().subSequence(0, lastName.length() - 1));
                }
            }
        });

        male.setOnClickListener(v -> selectButton(0));
        female.setOnClickListener(v -> selectButton(1));
        logIn.setOnClickListener(v -> getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("login")
                .add(R.id.main_frame, new Fragment_Login())
                .commit());
        signUp.setOnClickListener(v -> {
            if (firstName.getText().length() < 2) {
                Helper.getInstance().toast(getString(R.string.enter_first_name), Constants.ToastMode.WARNING);
            } else if (lastName.getText().length() < 2) {
                Helper.getInstance().toast(getString(R.string.enter_last_name), Constants.ToastMode.WARNING);
            } else if (phoneNumber.getText().length() < 10) {
                Helper.getInstance().toast(getString(R.string.enter_phone_correctly), Constants.ToastMode.WARNING);
            } else if (password.getText().length() < Constants.PASSWORD_MINIMUM_LENGTH) {
                Helper.getInstance().toast(getString(R.string.password_must_contain_at_least) + Constants.PASSWORD_MINIMUM_LENGTH + getString(R.string.be_characters), Constants.ToastMode.WARNING);
            } else {
                String _pushe_id = PushPole.getId(ContextHelper.retrieveContext());
                String _firstName = firstName.getText().toString().trim();
                String _lastName = lastName.getText().toString().trim();
                String _phoneNumber = Helper.getInstance().getDefaultPrePhone() + phoneNumber.getText().toString().trim();
                String _password = password.getText().toString().trim();
                NormalUser normalUser = new NormalUser(_firstName, _lastName, _phoneNumber, null, null, _pushe_id, _password, 0, isMale);
                signUp.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Connector.createService(view, Account_Server_API.class, object -> object.sendRegisterVerificationCode(_phoneNumber)
                        .enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                if (response.body() != null) {
                                    switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                                        case DONE:
                                            Bundle bundle = new Bundle();
                                            bundle.putString("TO_REGISTER_SERIALIZED_OBJECT", new Gson().toJson(normalUser));
                                            Fragment fragment = new Fragment_Register_Activation_Code();
                                            fragment.setArguments(bundle);
                                            getFragmentManager()
                                                    .beginTransaction()
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                    .addToBackStack("register_activation_code")
                                                    .add(R.id.main_frame, fragment)
                                                    .commit();
                                            break;
                                        case UNKNOWN:
                                        case FAILED:
                                            Helper.getInstance().toast(getString(R.string.error) + " : " + response.body().getMessage(), Constants.ToastMode.ERROR);
                                    }
                                    signUp.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Helper_Log.errorLog(Fragment_Register.class);
                                    signUp.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                Helper_Log.errorLog(t, Fragment_Register.class);
                                signUp.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }));
            }
        });
    }
}