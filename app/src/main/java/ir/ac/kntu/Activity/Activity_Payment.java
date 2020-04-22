package ir.ac.kntu.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import ir.ac.kntu.Entity.ServerResponse;
import ir.ac.kntu.Interface.Retrofit.Operable_User;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Payment;
import ir.ac.kntu.Technical.Other.Other.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Payment extends AppCompatActivity {
    private static long priceToman;
    private static String mode;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        findViews();
        handleStatus();
    }

    private void handleStatus() {
        if (getIntent().getData() != null) {  //payment done(whether failed or succeed)
            ZarinPal.getPurchase(ContextHelper.retrieveContext()).verificationPayment(getIntent().getData(), (isPaymentSuccess, refID, paymentRequest) -> {
                if (refID == null || !isPaymentSuccess) {
                    Helper.getInstance().toast(R.string.payment_failed, Constants.ToastMode.ERROR);
                    startActivity(new Intent(Activity_Payment.this, MainActivity.class));
                    finish();
                } else {
                    Connector.createService(constraintLayout, Operable_User.class, object -> {
                        try {
                            object.chargeWallet(Encryption.getInstance().encrypt(refID), (float) paymentRequest.getAmount()).enqueue(new Callback<ServerResponse>() {
                                @Override
                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                    if (response.body() != null) {
                                        switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                                            case DONE:
                                                try {
                                                    openSuccessfulPaymentDialog(Encryption.getInstance().decrypt(response.body().getMessage()));
                                                } catch (Exception e) {
                                                    Helper_Log.errorLog(e, Activity_Payment.class);
                                                }
                                                break;
                                            case FAILED:
                                                Helper.getInstance().toast(R.string.payment_failed, Constants.ToastMode.ERROR);
                                                startActivity(new Intent(Activity_Payment.this, MainActivity.class));
                                                finish();
                                                break;
                                            case UNKNOWN:
                                                Helper.getInstance().toast(R.string.unknown_error, Constants.ToastMode.ERROR);
                                                startActivity(new Intent(Activity_Payment.this, MainActivity.class));
                                                finish();
                                                break;
                                        }
                                    } else {
                                        Helper.getInstance().toast(R.string.error, Constants.ToastMode.ERROR);
                                        startActivity(new Intent(Activity_Payment.this, MainActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ServerResponse> call, Throwable t) {
                                    Helper.getInstance().toast("NOOOO", Constants.ToastMode.SUCCESS);
                                    Helper_Log.errorLog(t, Activity_Payment.class);
                                    startActivity(new Intent(Activity_Payment.this, MainActivity.class));
                                    finish();
                                }
                            });
                        } catch (Exception e) {
                            Helper_Log.errorLog(e, Activity_Payment.class);
                        }
                    });
                }
            });
        } else {  //do a payment operation
            priceToman = Helper.getInstance().getCostCeilOf(getIntent().getDoubleExtra("PRICE", -1));
            if (priceToman != -1) {
                Payment.getInstance().pay(constraintLayout, priceToman, objects -> {
                    Intent intent = (Intent) objects[0];
                    startActivity(intent);
                });
            }
        }
    }

    public void openSuccessfulPaymentDialog(String issueTrackingNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflateView = LayoutInflater.from(this).inflate(R.layout.dialog_qr_purchased, null, false);
        builder.setView(inflateView);
        builder.setCancelable(false);
        Dialog dialog = builder.create();
        TextViewPlus topTitle = inflateView.findViewById(R.id.dialog_qr_purchased_top_title);
        ImageView qrImage = inflateView.findViewById(R.id.dialog_qr_purchased_image);
        TextViewPlus code = inflateView.findViewById(R.id.dialog_qr_purchased_code);
        TextViewPlus share = inflateView.findViewById(R.id.dialog_qr_purchased_share);
        TextViewPlus save = inflateView.findViewById(R.id.dialog_qr_purchased_save);
        TextViewPlus navigateToHome = inflateView.findViewById(R.id.dialog_qr_purchased_navigate);
        Helper.getInstance().changeShapeColorToMainAppColor(topTitle);
        Helper.getInstance().changeShapeColorToMainAppColor(navigateToHome);
        share.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        save.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));

        code.setText(String.valueOf(issueTrackingNo));
        try {
            Bitmap bitmap = new QRGEncoder(issueTrackingNo, null, QRGContents.Type.TEXT, 480).encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
            share.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, Helper.getInstance().bitmapToUri(bitmap));
                startActivity(intent);
            });
            save.setOnClickListener(v -> Dexter.withActivity(this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        try {
                            Setting.getInstance().saveBitmap("order_" + Helper.getInstance().getTodaysTime(null, "Y-m-d H:i", true), bitmap, Constants.BITMAPS_DIRECTORY_NAME);
                            Helper.getInstance().toast(R.string.saved, Constants.ToastMode.SUCCESS);
                        } catch (Exception e) {
                            Helper.getInstance().toast(R.string.error_while_saving, Constants.ToastMode.ERROR);
                            Helper_Log.errorLog(e, Activity_Payment.class);
                        }
                    } else {
                        Helper.getInstance().toast(R.string.no_permission_granted, Constants.ToastMode.ERROR);
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).withErrorListener(error -> {
                Helper.getInstance().toast(R.string.error, Constants.ToastMode.ERROR);
                Helper_Log.errorLog(Activity_Payment.class);
            }).check());
        } catch (Exception e) {
            Helper_Log.errorLog(e, Activity_Payment.class);
        }
        navigateToHome.setOnClickListener(v -> startActivity(new Intent(Activity_Payment.this, MainActivity.class)));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void findViews() {
        constraintLayout = findViewById(R.id.activity_payment_whole);
    }
}
