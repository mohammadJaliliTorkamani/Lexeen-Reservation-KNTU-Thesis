package ir.ac.kntu.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Adapter.Adapter_OpenSource;
import ir.ac.kntu.Entity.License;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Setting;

public class Fragment_Setting extends Fragment {
    private ImageView back;
    private TextViewPlus groupName;
    private CoordinatorLayout openSourceCont;
    private CoordinatorLayout contactUsCont;
    private RecyclerView open_source_layout_recyclerview;
    private RecyclerView.LayoutManager open_source_layout_recyclerview_layout_manager;
    private RecyclerView.Adapter open_source_recyclerview_adapter;
    private List<License> licenses = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        findViews(view);
        initializeViewContents(view);
        manageListeners(view);
        return view;
    }

    private void findViews(View view) {
        back = view.findViewById(R.id.setting_back);
        openSourceCont = view.findViewById(R.id.setting_open_source_licences_container);
        contactUsCont = view.findViewById(R.id.setting_contact_us_container);
        groupName = view.findViewById(R.id.setting_group_name);
    }

    private void initializeViewContents(View view) {

    }

    private void manageListeners(View view) {
        back.setOnClickListener(v -> getActivity().onBackPressed());
        groupName.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.ABAN_WEBSITE));
            startActivity(browserIntent);
        });
        openSourceCont.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ContextHelper.retrieveContext());
            View inflateView = LayoutInflater.from(ContextHelper.retrieveContext())
                    .inflate(R.layout.layout_open_source, null, false);
            builder.setView(inflateView);
            builder.setCancelable(true);
            try {

                String[] fileNames = ContextHelper.retrieveContext().getAssets().list("licenses");
                for (String fileName : fileNames)
                    licenses.add(new License(fileName, Setting.getInstance().readAssetFile(fileName)));

            } catch (IOException e) {
                e.printStackTrace();
            }

            open_source_layout_recyclerview = inflateView.findViewById(R.id.open_source_layout_recycler);
            open_source_recyclerview_adapter = new Adapter_OpenSource(licenses);
            open_source_layout_recyclerview_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false);
            open_source_layout_recyclerview.setLayoutManager(open_source_layout_recyclerview_layout_manager);
            open_source_layout_recyclerview.setHasFixedSize(true);
            open_source_layout_recyclerview.setAdapter(open_source_recyclerview_adapter);
            builder.create().show();
        });
        contactUsCont.setOnClickListener(v -> Snackbar.make(view, getString(R.string.phone_colon) + " " + getString(R.string.aban_phone_number), Snackbar.LENGTH_SHORT).setAction(R.string.call, v1 -> Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + getString(R.string.aban_phone_number)));
                        ContextHelper.retrieveContext().startActivity(intent);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check()).show());
    }
}
