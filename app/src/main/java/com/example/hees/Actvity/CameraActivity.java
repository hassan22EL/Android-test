package com.example.hees.Actvity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hees.R;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class CameraActivity extends AppCompatActivity {

    //Requset
    private static final int REQUEST_PERMISSION_fOR_CAMERA = 1;
    //View
    private TextureView CameraPerview, CameraPerview2;
    private Button Flash;
    private Button TakeCapture;
    private Button CameraSwap;


    //requriment open Camera to  and previewCamera ;
    private CameraDevice mDevice, mDevice2;
    private String CameraID, CameraID2;
    private CaptureRequest mRequest, Request2; //Requset a Capture at a
    private CaptureRequest.Builder mBuilder, mBuilder2; //Buiderl a capture after Capture Requset
    private CameraCaptureSession mSession, mSession2; // open a capture seeion
    private Size mSize, mSize2;  //size of preview to open camera
    private Handler mHandler, mHandler2;
    private HandlerThread mThread, mThread2;
    private Semaphore mCameraOPenCloseLock = new Semaphore(1);
    private boolean isPressed;


    private CameraDevice.StateCallback mCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraOPenCloseLock.release();
            mDevice = cameraDevice;
            try {
                Create_PerviewCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOPenCloseLock.release();
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            mCameraOPenCloseLock.release();
            cameraDevice.close();
            mDevice = null;
        }
    };
    private TextureView.SurfaceTextureListener mTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            try {
                Open_Camera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };
    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            try {
                Create_PerviewCamera();
//                Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    };


    private CameraDevice.StateCallback mCallback2 = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraOPenCloseLock.release();
            mDevice2 = cameraDevice;
            try {
                Create_PerviewCamera2();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOPenCloseLock.release();
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            mCameraOPenCloseLock.release();
            cameraDevice.close();
            mDevice2 = null;
        }
    };
    private TextureView.SurfaceTextureListener mTextureListener2 = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            try {
                Open_Camera2();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };
    private CameraCaptureSession.CaptureCallback mCaptureCallback2 = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            try {
                Create_PerviewCamera2();
//                Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    };


    private void startBackgroundThread() {
        mThread = new HandlerThread("Camera BackGround");
        mThread.start();
        mHandler = new Handler(mThread.getLooper());

    }

    private void stopBackgroundThread() {
        mThread.quitSafely();
        try {
            mThread.join();
            mThread = null;
            mHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread2() {
        mThread2 = new HandlerThread("Camera BackGround");
        mThread2.start();
        mHandler2 = new Handler(mThread2.getLooper());

    }

    private void stopBackgroundThread2() {
        mThread2.quitSafely();
        try {
            mThread2.join();
            mThread2 = null;
            mHandler2 = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraPerview = (TextureView) findViewById(R.id.camera_perview);
        CameraPerview2 = (TextureView) findViewById(R.id.camera_front_perview);
        Flash = (Button) findViewById(R.id.flash_on);
        TakeCapture = (Button) findViewById(R.id.take_picture);
        CameraSwap = (Button) findViewById(R.id.swap_camera);
        CameraPerview.setSurfaceTextureListener(mTextureListener);
        CameraSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPressed) {
                    isPressed = true;
                    CloseCamera();
                    CameraPerview2.setVisibility(View.VISIBLE);
                    CameraPerview.setVisibility(View.GONE);
                    CameraPerview2.setSurfaceTextureListener(mTextureListener2);
                    Toast.makeText(CameraActivity.this, "Front Camera ", Toast.LENGTH_SHORT).show();
                } else {
                    CloseCamera2();
                    CameraPerview.setVisibility(View.VISIBLE);
                    CameraPerview2.setVisibility(View.GONE);
                    CameraPerview.setSurfaceTextureListener(mTextureListener);
                    Toast.makeText(CameraActivity.this, "Back Camera ", Toast.LENGTH_SHORT).show();
                    isPressed = false;
                }
            }
        });


    }

    private boolean Check_Permssion_For_Camera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }

    }

    private boolean Check_Permission_ExtranalWriteStorge() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void Requset_Permssion_For_WriteExtranalStorge() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_fOR_CAMERA);
    }

    private void Request_Permssion_For_Camera() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_fOR_CAMERA);
    }

    private void Open_Camera() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //getId from camera
        CameraID = manager.getCameraIdList()[0];
        CameraCharacteristics mCharacteristics = manager.getCameraCharacteristics(CameraID);
        //get Dimenssion of a Camera
        StreamConfigurationMap map = mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        mSize = map.getOutputSizes(SurfaceTexture.class)[0];
        if (Check_Permssion_For_Camera() && Check_Permission_ExtranalWriteStorge()) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            manager.openCamera(CameraID, mCallback, mHandler);
        } else {
            Requset_Permssion_For_WriteExtranalStorge();
            Request_Permssion_For_Camera();
            manager.openCamera(CameraID, mCallback, mHandler);
        }

    }

    private void Create_PerviewCamera() throws CameraAccessException {
        //get texture surface and into a surface
        SurfaceTexture texture = CameraPerview.getSurfaceTexture();
        assert texture != null;
        Surface surface = new Surface(texture);
        // Requset a Capture and Bulid a Capture
        mBuilder = mDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mBuilder.addTarget(surface);

        //Create Capture Seesion
        mDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                if (mDevice == null) {
                    return;

                }
                mSession = cameraCaptureSession;
                try {
                    UpdatePerview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                Toast.makeText(CameraActivity.this, "Confication Filaed", Toast.LENGTH_SHORT).show();
            }
        }, null);


    }

    private void UpdatePerview() throws CameraAccessException {
        //RequsetCapture
        mBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        //set bulid
        mSession.setRepeatingRequest(mBuilder.build(), null, mHandler);

    }


    private void Open_Camera2() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //getId from camera
        CameraID2 = manager.getCameraIdList()[1];
        CameraCharacteristics mCharacteristics = manager.getCameraCharacteristics(CameraID2);
        //get Dimenssion of a Camera
        StreamConfigurationMap map = mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        mSize2 = map.getOutputSizes(SurfaceTexture.class)[0];
        if (Check_Permssion_For_Camera() && Check_Permission_ExtranalWriteStorge()) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            manager.openCamera(CameraID2, mCallback2, mHandler2);
        } else {
            Requset_Permssion_For_WriteExtranalStorge();
            Request_Permssion_For_Camera();
            manager.openCamera(CameraID2, mCallback2, mHandler2);
        }

    }

    private void Create_PerviewCamera2() throws CameraAccessException {
        //get texture surface and into a surface
        SurfaceTexture texture = CameraPerview2.getSurfaceTexture();
        assert texture != null;
        Surface surface = new Surface(texture);
        // Requset a Capture and Bulid a Capture
        mBuilder2 = mDevice2.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mBuilder2.addTarget(surface);

        //Create Capture Seesion
        mDevice2.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                if (mDevice2 == null) {
                    return;

                }
                mSession2 = cameraCaptureSession;
                try {
                    UpdatePerview2();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                Toast.makeText(CameraActivity.this, "Confication Filaed", Toast.LENGTH_SHORT).show();
            }
        }, null);


    }

    private void UpdatePerview2() throws CameraAccessException {
        //RequsetCapture
        mBuilder2.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        //set bulid
        mSession2.setRepeatingRequest(mBuilder2.build(), null, mHandler);

    }

    private void CloseCamera() {
        try {
            mCameraOPenCloseLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (null != mSession) {
            mSession.close();
            mSession = null;
        }
        if (null != mDevice) {
            mDevice.close();
            mDevice = null;
        }
    }

    private void CloseCamera2() {
        try {
            mCameraOPenCloseLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (null != mDevice2) {
            mDevice2.close();
            mDevice2 = null;
        }
        if (null != mSession2) {
            mSession2.close();
            mSession2 = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        startBackgroundThread2();
        if (CameraPerview.isAvailable()) {
            try {
                Open_Camera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            CameraPerview.setSurfaceTextureListener(mTextureListener);
        }


        if (CameraPerview2.isAvailable()) {
            try {
                Open_Camera2();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            CameraPerview2.setSurfaceTextureListener(mTextureListener2);
        }

    }

    @Override
    protected void onPause() {
        stopBackgroundThread();
        stopBackgroundThread2();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_fOR_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void useFlash() {

    }

}
