package ir.ac.kntu.Fragment;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.Result;

import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.ButtonPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Setting;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * scanner fragment ables user to scan QR code and enter to the restaurant world
 */
public class Fragment_Scanner extends Fragment implements ZXingScannerView.ResultHandler {
    private ViewGroup contentFrame;
    private ZXingScannerView scannerView;
    private ButtonPlus scan;

    // Required empty public constructor
    public Fragment_Scanner() {
    }

    /**
     * fragment entry point which finds views, initializes UI elements, loads data and declares
     * listeners
     *
     * @param inflater           inflater
     * @param container          container view
     * @param savedInstanceState saved instance bundle
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeOnlineContents(view);
        manageListeners(view);
        return view;
    }

    /**
     * delcares listeners for some UI elements
     *
     * @param view
     */
    private void manageListeners(View view) {
        scan.setOnClickListener(v -> Helper.getInstance().toast(R.string.place_barcode_on_frame, Constants.ToastMode.NORMAL));
    }

    /**
     * loads data from server
     *
     * @param view view to work
     */
    private void initializeOnlineContents(View view) {
    }

    /**
     * initializes UI elements
     *
     * @param view view to work
     */
    private void initializeViewContents(View view) {
        scan.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        if (getArguments() != null && getArguments().getString("CODE") != null) {
            Fragment scanningResultFragment = new Fragment_ScanningResult();
            Bundle bundle = new Bundle();
            bundle.putString("CODE", getArguments().getString("CODE"));
            scanningResultFragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack("scanning_result")
                    .add(R.id.main_frame, scanningResultFragment)
                    .commitAllowingStateLoss();
        }
        scannerView = new ZXingScannerView(ContextHelper.retrieveContext()) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };

        // this parameter will make your HUAWEI phone works great!
        scannerView.setAspectTolerance(0.5f);
        contentFrame.addView(scannerView);
    }

    /**
     * assign view objects to view elements
     *
     * @param view to find views with it
     */
    private void findViews(View view) {
        contentFrame = view.findViewById(R.id.fragment_scanner_content_frame);
        scan = view.findViewById(R.id.fragment_scanner_scan);
    }

    /**
     * stars camera and other system services
     */
    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        scannerView.resumeCameraPreview(this);
    }

    /**
     * pauses camera and other system services
     */
    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    /**
     * stops camera and passes the result to the scanning result fragment
     *
     * @param rawResult
     */
    @Override
    public void handleResult(Result rawResult) {
        scannerView.stopCamera();
        scannerView.stopCameraPreview();
        Log.v(Constants.TAG, rawResult.getText() + "  " + rawResult.getBarcodeFormat().toString());
        Setting.getInstance().vibrate(175);
        MediaPlayer.create(getActivity().getBaseContext(), R.raw.barcodescanbeep).start();
        Fragment scanningResultFragment = new Fragment_ScanningResult();
        Bundle bundle = new Bundle();
        bundle.putString("CODE", rawResult.getText());
        scanningResultFragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("scanning_result")
                .add(R.id.main_frame, scanningResultFragment)
                .commit();
    }

    /**
     * used to draw corners of a rectangle at the center of screen (adapted from https://stackoverflow.com/questions/43799287/how-to-make-area-for-scan-barcode-using-library-zbar-more-smaller-android)
     */
    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 0;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        @Override
        public void setBorderColor(int borderColor) {
            mBorderPaint.setColor(borderColor);
        }

        @Override
        public void setBorderStrokeWidth(int borderStrokeWidth) {
            mBorderPaint.setStrokeWidth(borderStrokeWidth);
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
            setBorderColor(Color.WHITE);
            setBorderStrokeWidth(5);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }
    }
}
