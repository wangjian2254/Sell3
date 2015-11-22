package com.wj.sell3;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.*;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import com.lidroid.xutils.http.RequestParams;
import com.wj.sell.db.models.ChatMsgEntity;
import com.wj.sell.db.models.Shiming;
import com.wj.sell.util.HttpCallResultBack;
import com.wj.sell.util.HttpCallResultBackSendChat;
import com.wj.sell.util.HttpCallResultBackSendImage;
import com.wj.sell.util.HttpResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjian on 15/11/22.
 */
public class CameraActivity extends Activity  implements SurfaceHolder.Callback {

    private MediaRecorder mediaRecorder = null;
    private Camera camera;
    private static final String OUTPUT_FILE = "/sdcard/videooutput.mp4";
    private static final String TAG = "RecordVideo";
    private VideoView vv = null;
    private Button startBtn = null;
    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;
    boolean surfaceInited = false;
    boolean cameraInited = false;
    boolean startPreviewed = false;

//    public Shiming getShiming() {
//        return shiming;
//    }

//    private Shiming shiming=null;

    private List<byte[]>  imageslist = new ArrayList<byte[]>();
    private static int image_count = 5;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void onOrientationChanged(int orientation) {

        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(0, info);
        orientation = (orientation + 45) / 90 * 90;
        int rotation = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation - orientation + 360) % 360;
        } else { // back-facing camera
            rotation = (info.orientation + orientation) % 360;
        }
        Camera.Parameters parameters = camera.getParameters();
        parameters.setRotation(90);
        camera.setParameters(parameters);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    void initCamera() {
        camera = Camera.open(0);
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(0, info);
        int rotation = this.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        // onOrientationChanged(degrees);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            //@Override
            public void onPictureTaken(byte[] data, Camera camera) {

                imageslist.add(data);
                if(imageslist.size()>image_count){
                    imageslist.remove(0);
                }
                camera.startPreview();
            }
        };

        camera.setDisplayOrientation(result);

        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    initCamera();
                    camera.cancelAutoFocus();

                    camera.takePicture(null, null, pictureCallback);
                }

            }
        });
        Camera.Parameters parameters = camera.getParameters();
        parameters.setRotation(270);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setPictureSize(800, 600);
        camera.setParameters(parameters);

        cameraInited = true;
        if (surfaceInited && !startPreviewed) {
            try {
                camera.setPreviewDisplay(vv.getHolder());
                camera.startPreview();
                startPreviewed = true;
            } catch (Exception e) {

            }

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camera_layout);

//        shiming = (Shiming)savedInstanceState.getSerializable("shiming");

        // initCamera();
        this.powerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        this.wakeLock = this.powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK, "My Lock");
        startBtn = (Button) findViewById(R.id.Tack);

        vv = (VideoView) findViewById(R.id.videoview);

        final SurfaceHolder holder = vv.getHolder();
        holder.addCallback(this);
        // holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            //@Override
            public void onPictureTaken(byte[] data, Camera camera) {

                imageslist.add(data);
                if(imageslist.size()>image_count){
                    imageslist.remove(0);
                }
                camera.startPreview();

                SavePictureTask task = new SavePictureTask();
//                task.setShiming(shiming);
                task.execute(imageslist);

                startBtn.setEnabled(false);
            }
        };
        // save pic

        startBtn.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        startBtn.setEnabled(true);
        try {
            surfaceInited = true;
            if (cameraInited && !startPreviewed) {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
                startPreviewed = true;
            }
        } catch (Exception e) {

        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        Log.v(TAG, "Width x Height = " + width + "x" + height);
        // camera.stopPreview();
        // camera.startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceInited = false;

    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    protected void beginRecording(SurfaceHolder holder) throws Exception {
        // TODO Auto-generated method stub
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        File outFile = new File(OUTPUT_FILE);
        if (outFile.exists()) {
            outFile.delete();
        }

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.reset();
            // camera.stopPreview();
            camera.unlock();

            mediaRecorder.setCamera(camera);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoSize(320, 240);

            mediaRecorder.setVideoFrameRate(15);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            mediaRecorder.setMaxDuration(30000);
            mediaRecorder.setPreviewDisplay(holder.getSurface());
            mediaRecorder.setOutputFile(OUTPUT_FILE);
            mediaRecorder.setOrientationHint(270);
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    protected void stopRecording() {
        // TODO Auto-generated method stub
        if (mediaRecorder != null) {
            mediaRecorder.stop();
        }
        try {
            camera.reconnect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void playRecording() {
        // camera.unlock();
        // camera.stopPreview();
        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);
        vv.setVideoPath(OUTPUT_FILE);
        vv.start();
    }

    @Override
    protected void onPause() {

        super.onPause();
        camera.stopPreview();
        camera.release();
        cameraInited = false;
        startPreviewed = false;

        this.wakeLock.release();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {

        initCamera();
        camera.cancelAutoFocus();
        super.onResume();
        this.wakeLock.acquire();
    }

    protected void stopPlayingRecording() throws Exception {
        // TODO Auto-generated method stub
        vv.stopPlayback();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//            return true;
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//            return true;
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_B:
//                Intent callIntent = new Intent(Intent.ACTION_CALL,
//                        Uri.parse("tel:13901959653"));
//                startActivity(callIntent);
//                return true;
//
//        }
//        return super.onKeyUp(keyCode, event);
//    }
}


class SavePictureTask extends AsyncTask<List<byte[]>, String, String> {


//    public Shiming getShiming() {
//        return shiming;
//    }

//    public void setShiming(Shiming shiming) {
//        this.shiming = shiming;
//    }

//    private Shiming shiming=null;


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(List<byte[]>... params) {
        String fname = DateFormat.format("yyyyMMddhhmmss", new Date()).toString()+".jpg";

        for(int i=0;i<params[0].size();i++){
            InputStream inputStream = new ByteArrayInputStream(params[0].get(i));
            RequestParams httpparams = new RequestParams();
            httpparams.addBodyParameter("file", inputStream,params[0].get(i).length,"image.jpg");
//            httpparams.addBodyParameter("request_id", String.valueOf(shiming.getS_id()));
            HttpCallResultBackSendImage httpCallResultBackSendImage = new HttpCallResultBackSendImage(new HttpCallResultBack() {
                @Override
                public void doresult(HttpResult result) {
//                    image_list_count-=1;
                }

                @Override
                public void dofailure() {
//                    image_list_count-=1;
                }
            });
            httpCallResultBackSendImage.setParams(httpparams);
            SellApplication.post_sync(httpCallResultBackSendImage);
        }



        return null;
    }

    //doInBackground执行完后由UI线程调用，用于更新界面操作
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub


        //textView.setText(result);
        super.onPostExecute(result);

        RequestParams params = new RequestParams();
        params.addBodyParameter("text", "图片上传完成");
//        params.addBodyParameter("request_id", String.valueOf(shiming.getS_id()));

        HttpCallResultBackSendChat httpCallResultBackSendChat = new HttpCallResultBackSendChat(new HttpCallResultBack() {
            @Override
            public void doresult(HttpResult result) {

            }

            @Override
            public void dofailure() {

            }
        });
        httpCallResultBackSendChat.setParams(params);
        SellApplication.post(httpCallResultBackSendChat);

    }
}