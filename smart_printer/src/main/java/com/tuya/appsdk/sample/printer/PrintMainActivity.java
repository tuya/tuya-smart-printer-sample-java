package com.tuya.appsdk.sample.printer;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.tuya.appsdk.sample.R;
import com.tuya.appsdk.sample.printer.tools.BitmapConvertor;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.androiddefaultpanelbase.common.utils.ToastUtil;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.richeditor.RichEditor;

public class PrintMainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PrintMainActivity";

    private RichEditor etContent;
    private AppCompatEditText etCopies;
    private View clTextSetContainer;
    private ITuyaDevice mDevice;
    private ProgressDialog progressDialog;
    private int copies = 0;

    private static final int BITMAP_WIDTH = 384;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer_main_activity);
        AppCompatButton btnPrint = findViewById(R.id.btnPrint);
        clTextSetContainer = findViewById(R.id.clTextSetContainer);
        etCopies = findViewById(R.id.etCopies);
        etContent = findViewById(R.id.etContent);
        etContent.setEditorFontSize(16);
        etContent.setPlaceholder("Insert text here...");
        etContent.focusEditor();
        etContent.setPadding(5, 5, 5, 5);
        etContent.setEditorFontColor(ContextCompat.getColor(this, R.color.black));
        btnPrint.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        findViewById(R.id.tvFontSizeSmall).setOnClickListener(this);
        findViewById(R.id.tvFontSizeMiddle).setOnClickListener(this);
        findViewById(R.id.tvFontSizeLarge).setOnClickListener(this);
        findViewById(R.id.tvBold).setOnClickListener(this);
        findViewById(R.id.tvUnderline).setOnClickListener(this);
        findViewById(R.id.tvTilt).setOnClickListener(this);
        findViewById(R.id.tvLeft).setOnClickListener(this);
        findViewById(R.id.tvMiddle).setOnClickListener(this);
        findViewById(R.id.tvRight).setOnClickListener(this);
        findViewById(R.id.btnText).setOnClickListener(this);
        findViewById(R.id.rlPrintContainer).setOnClickListener(this);
        findViewById(R.id.ivClose).setOnClickListener(this);
        findViewById(R.id.btnClear).setOnClickListener(this);
        initDevice();
    }

    @Override
    protected void onStop() {
        mDevice.unRegisterDevListener();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mDevice.unRegisterDevListener();
        super.onDestroy();
    }

    /**
     * init device and set listener
     */
    private void initDevice() {
        String deviceId = getIntent().getStringExtra("deviceId");
        mDevice = TuyaHomeSdk.newDeviceInstance(deviceId);
        mDevice.registerDevListener(devListener);
        DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(deviceId);
        if (deviceBean != null) {
            getSupportActionBar().setTitle(deviceBean.name);
        }
        L.d(TAG, "etContent.getHtml():" + etContent.getHtml());
    }

    private Bitmap viewToBitmap() {
        etContent.buildDrawingCache();
        return etContent.getDrawingCache();
    }

    private Bitmap zoomBitmap(Bitmap bitmap, int newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = ((float) newWidth) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private final IDevListener devListener = new IDevListener() {
        @Override
        public void onDpUpdate(String devId, String dpStr) {
            L.d(TAG, "IDevListener onDpUpdate devId = " + devId + ", dpStr = " + dpStr);
            operDeviceDp(devId, dpStr);
        }

        @Override
        public void onRemoved(String devId) {

        }

        @Override
        public void onStatusChanged(String devId, boolean online) {

        }

        @Override
        public void onNetworkStatusChanged(String devId, boolean status) {

        }

        @Override
        public void onDevInfoUpdate(String devId) {

        }
    };

    private void operDeviceDp(String devId, String dpStr) {
        if (dpStr == null || dpStr.length() == 0) {
            return;
        }
        try {
            Map<String, Object> dpMap = JSONObject.parseObject(dpStr);
            if (dpMap == null || dpMap.size() == 0) {
                L.d(TAG, "operDeviceDp dpMap is null dpStr:" + dpStr);
                return;
            }

            Object value;
            for (String dpKey : dpMap.keySet()) {
                if ("104".equals(dpKey) && (value = dpMap.get(dpKey)) != null) {
                    if (value.toString().length() > 0) {
                        ToastUtil.showCommonToast(this, value.toString());
                    }
                } else if ("105".equals(dpKey) && (value = dpMap.get(dpKey)) != null) {
                    if (Boolean.getBoolean(value.toString())) {
                        ToastUtil.showCommonToast(this, "No more printing paper.");
                    }
                } else if ("107".equals(dpKey) && (value = dpMap.get(dpKey)) != null) {
                    if (Boolean.getBoolean(value.toString())) {
                        ToastUtil.showCommonToast(this, "Low Power Warning.");
                    }
                } else if ("108".equals(dpKey) && (value = dpMap.get(dpKey)) != null) {
                    if (value.toString().length() > 0) {
                        ToastUtil.showCommonToast(this, "Printing the " + value.toString() + "th.");
                    }
                } else if ("109".equals(dpKey) && (value = dpMap.get(dpKey)) != null) {
                    if (value.toString().length() > 0) {
                        progressDialog.dismiss();
                        etContent.focusEditor();
                        ToastUtil.showCommonToast(this, value.toString());
                    }
                }
            }
        } catch (Exception e) {
            L.e(TAG, "operDeviceDp JSON parse Exception. devId = " + devId + ", dpStr = " + dpStr);
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPrint) {
            L.d(TAG, "etContent.getHtml():" + etContent.getHtml());
            getCopies();
            if (copies == 0) {
                return;
            }
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(this, "Printing", "Please Wait", true, true, null);
            } else {
                progressDialog.show();
            }
            etContent.clearFocusEditor();
            // view to 1-bit bmp file
            Bitmap originalBitmap = viewToBitmap();
            Bitmap newBitmap = zoomBitmap(originalBitmap, BITMAP_WIDTH);
            BitmapConvertor convertor = new BitmapConvertor(this);
            convertor.convertBitmap(newBitmap, "test");
            File file = new File(getCacheDir(), "test.bmp");
            // upload file
            AbsFileUploader uploader = new FileUploader();
            uploader.setFile(file);
            uploader.setCallback(url -> {
                L.d(TAG, "upload success. publicUrl:" + url);
                String targetUrl = "https://storage-proxy.tuyacn.com:7779/dst=" + url;
                String url1 = targetUrl.substring(0, 254);
                String url2 = targetUrl.substring(254);
                L.d(TAG, "url1+url2. url:" + url1 + url2);
                Map<String, Object> map = new HashMap<>();
                map.put("101", copies);
                map.put("102", url1);
                map.put("110", url2);
                map.put("112", file.length());
                map.put("111", "");
                mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        L.d(TAG, "onError code:" + code + ", error:" + error);
                    }

                    @Override
                    public void onSuccess() {
                        L.d(TAG, "onSuccess");
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("103", true);
                        mDevice.publishDps(JSONObject.toJSONString(map1), new IResultCallback() {
                            @Override
                            public void onError(String code, String error) {
                                L.d(TAG, "onError code:" + code + ", error:" + error);
                            }

                            @Override
                            public void onSuccess() {
                                L.d(TAG, "onSuccess");
                            }
                        });
                    }
                });
            });
            boolean flag = uploader.uploadStart();
            if (!flag) {
                ToastUtil.showCommonToast(this, "Need to implement your own logic to upload files and get download links");
                progressDialog.dismiss();
            }
        } else if (v.getId() == R.id.tvFontSizeSmall) {
            etContent.setEditorFontSize(16);
        } else if (v.getId() == R.id.tvFontSizeMiddle) {
            etContent.setEditorFontSize(24);
        } else if (v.getId() == R.id.tvFontSizeLarge) {
            etContent.setEditorFontSize(32);
        } else if (v.getId() == R.id.tvBold) {
            etContent.setBold();
        } else if (v.getId() == R.id.tvUnderline) {
            etContent.setUnderline();
        } else if (v.getId() == R.id.tvTilt) {
            etContent.setItalic();
        } else if (v.getId() == R.id.tvLeft) {
            etContent.setAlignLeft();
        } else if (v.getId() == R.id.tvMiddle) {
            etContent.setAlignCenter();
        } else if (v.getId() == R.id.tvRight) {
            etContent.setAlignRight();
        } else if (v.getId() == R.id.btnText) {
            clTextSetContainer.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.rlPrintContainer || v.getId() == R.id.ivClose) {
            clTextSetContainer.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnClear) {
            etContent.setHtml("");
        }
    }

    private void getCopies() {
        copies = 0;
        try {
            copies = Integer.parseInt(etCopies.getText().toString().trim());
        } catch (Exception ex) {
            ToastUtil.showCommonToast(this, "Please enter the number of copies to be printed.");
        }
    }
}
