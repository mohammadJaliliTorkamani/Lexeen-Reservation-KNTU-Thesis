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

public class Fragment_Forget extends Fragment {
    private static String LOGIN_VIDEO_STREAM_LINK;
    private VideoView videoView;
    private TextViewPlus recover;
    private ProgressBar progressBar;
    private EditTextPlus phone;
    private ConstraintLayout recoverContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeServerSupplied(view);
        manageListeners(view);
        return view;
    }

    private void findViews(View view) {
        recoverContainer = view.findViewById(R.id.fragment_forget_recover);
        videoView = view.findViewById(R.id.fragment_forget_videoview);
        recover = view.findViewById(R.id.fragment_forget_recover_text);
        progressBar = view.findViewById(R.id.fragment_forget_recover_progressbar);
        phone = view.findViewById(R.id.fragment_forget_phone_phone);
    }

    private void initializeViewContents(View view) {
        recoverContainer.setBackgroundResource(R.drawable.dr_login_orange_btn);
        Helper.getInstance().changeShapeColorToMainAppColor(recoverContainer);
        onResume();
    }

    @Override
    public void onResume() {
        LOGIN_VIDEO_STREAM_LINK = "android.resource://" + ContextHelper.retrieveContext().getPackageName() + "/" + R.raw.log_in_background;
        Uri uri = Uri.parse(LOGIN_VIDEO_STREAM_LINK);
        videoView.setVideoURI(uri);
        videoView.start();
        progressBar.setVisibility(View.GONE);
        recover.setVisibility(View.VISIBLE);
        super.onResume();
    }

    private void initializeServerSupplied(View view) {
    }

    private void manageListeners(View view) {
        recover.setOnClickListener(v -> {
            if (phone.getText().length() == 0) {
                Helper.getInstance().toast(getString(R.string.enter_phone), Constants.ToastMode.WARNING);
            } else if (phone.getText().length() < 10) {
                Helper.getInstance().toast(getString(R.string.enter_phone_correctly), Constants.ToastMode.WARNING);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                recover.setVisibility(View.GONE);
                Connector.createService(view, Account_Server_API.class, object -> object.checkSend(Helper.getInstance().getDefaultPrePhone() + phone.getText().toString()).enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.body() != null) {
                            switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                                case DONE:
                                    Helper.getInstance().toast(getString(R.string.activation_code_sent), Constants.ToastMode.SUCCESS);
                                    Setting.getInstance().hideKeyboard(getActivity());
                                    Fragment fragment = new Fragment_Enter_Activation_Code();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("PHONE", phone.getText().toString());
                                    fragment.setArguments(bundle);
                                    getFragmentManager()
                                            .beginTransaction()
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                            .addToBackStack("enter_activation_code")
                                            .add(R.id.main_frame, fragment)
                                            .commit();
                                    break;
                                case FAILED:
                                case UNKNOWN:
                                    Helper.getInstance().toast(getString(R.string.error) + " : " + response.body().getMessage(), Constants.ToastMode.ERROR);
                                    break;
                            }
                        } else
                            Helper_Log.errorLog(Fragment_Forget.class);
                        progressBar.setVisibility(View.GONE);
                        recover.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Helper_Log.errorLog(t, Fragment_Forget.class);
                        progressBar.setVisibility(View.GONE);
                        recover.setVisibility(View.VISIBLE);
                    }
                }));
            }
        });
    }
}
