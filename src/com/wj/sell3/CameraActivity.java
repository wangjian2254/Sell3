package com.wj.sell3;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.*;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.*;
import com.lidroid.xutils.http.RequestParams;
import com.wj.sell.db.models.ChatMsgEntity;
import com.wj.sell.db.models.Shiming;
import com.wj.sell.util.*;
import org.json.JSONException;
import org.json.JSONObject;

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

    private ImageView rentou;

    private Context context;
    Camera.PictureCallback tmppictureCallback;
    public boolean photo_runing=true;

    private Thread photothread = new Thread(){



        public byte[] rotaingImageView(int angle , byte[] bytes) {
            //旋转图片 动作
            Bitmap bitmap =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        bitmap = scaleBtmap(bitmap, 800);
            if(angle!=0){
                Matrix matrix = new Matrix();
                matrix.postRotate(angle);
                // 创建新的图片
                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bs = baos.toByteArray();
            return bs;
//        return bytes;
        }

        public void run(){
            while (photo_runing||imageslist.size()==0){
                try {
                    sleep(2000);
                    try{
                        camera.takePicture(null,null,tmppictureCallback);

                    }catch (Exception e){


                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //todo:
            final SavePictureTask task = new SavePictureTask();
            task.setContext(context);
            startBtn.post(new Runnable() {
                @Override
                public void run() {
                    byte[] bytes = rotaingImageView(90, imageslist.get(imageslist.size() - 1));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    rentou.setImageBitmap(bitmap);
                    task.execute(imageslist);
                    startBtn.setEnabled(false);
                }
            });


        }
    };

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
        try {
            camera = Camera.open(0);
        }catch (Exception e){

            CameraActivity.this.finish();
            Toast.makeText(context, "您已经禁止使用摄像头了，请打开权限后再操作。", Toast.LENGTH_LONG).show();
            return;
        }
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

        tmppictureCallback = new Camera.PictureCallback() {

            private Bitmap scaleBtmap(Bitmap a, int width) {
                // TODO Auto-generated method stub

                float scale_x = (float)width/a.getWidth();
                Matrix matrix = new Matrix();
                matrix.postScale(scale_x, scale_x);

                Bitmap aa = Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);

                return aa;
            }

            //@Override
            public void onPictureTaken(byte[] data, Camera camera) {


                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if(bitmap.getWidth()>1000){

                    bitmap = scaleBtmap(bitmap, 800);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    try {
                        baos.flush();
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    data = baos.toByteArray();

                }


                imageslist.add(data);
                if(imageslist.size()>image_count){
                    imageslist.remove(0);
                }
                camera.startPreview();
            }
        };

        camera.setDisplayOrientation(result);

//        camera.autoFocus(null);
        try{
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        initCamera();
                        camera.cancelAutoFocus();
                    }
                }
            });
        }catch (Exception e){

        }

        Camera.Parameters parameters = camera.getParameters();
//        parameters.setRotation(270);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        parameters.setPictureSize(800, 600);
        List<Camera.Size> cl = parameters.getSupportedPictureSizes();
        int w=0,h=0;
        for(Camera.Size size:cl){
            if(size.width>=800){
                w=size.width;
                h=size.height;
            }else{
                break;
            }
        }
        if(w>0){
            parameters.setPictureSize(w,h);
        }

        try{
            camera.setParameters(parameters);
        }catch (Exception e){

        }


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

        context = this;

        setContentView(R.layout.camera_layout);

//        shiming = (Shiming)savedInstanceState.getSerializable("shiming");

        // initCamera();
        this.powerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        this.wakeLock = this.powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK, "My Lock");
        startBtn = (Button) findViewById(R.id.Tack);
        rentou = (ImageView) findViewById(R.id.rentou);

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
                task.setContext(context);
                task.execute(imageslist);


                startBtn.setEnabled(false);
            }
        };
        // save pic

        startBtn.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                photo_runing = false;

            }
        });


        imageslist.clear();
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
        photo_runing = false;
        imageslist.clear();

        this.wakeLock.release();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {

        initCamera();
        photothread.start();
        try {
            camera.cancelAutoFocus();
        }catch (Exception e){

        }
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


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context=null;

    private Dialog dialog;


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setTitle("正在上传图片");
        dialog.show();

    }

    /** *//**
     * 把字节数组保存为一个文件

     */
    public int getDegreeFromBytes(byte[] b){
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = File.createTempFile("img", ".jpg");
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (stream != null){
                try {
                    stream.close();
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        }
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        return degree;
    }

    private Bitmap scaleBtmap(Bitmap a, int width) {
        // TODO Auto-generated method stub

        float scale_x = (float)width/a.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scale_x, scale_x);

        Bitmap aa = Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);

        return aa;
    }

    public byte[] rotaingImageView(int angle , byte[] bytes) {
        //旋转图片 动作
        Bitmap bitmap =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        bitmap = scaleBtmap(bitmap, 800);
        if(angle!=0){
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            // 创建新的图片
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bs = baos.toByteArray();
        return bs;
//        return bytes;
    }


    @Override
    protected String doInBackground(List<byte[]>... params) {
        String fname = DateFormat.format("yyyyMMddhhmmss", new Date()).toString()+".jpg";

        for(int i=0;i<params[0].size();i++){
            InputStream inputStream = null;
            int r = getDegreeFromBytes(params[0].get(i));
            byte[] bytes = rotaingImageView(90,params[0].get(i));
            inputStream = new ByteArrayInputStream(bytes);

//            inputStream = new ByteArrayInputStream(params[0].get(i));
            RequestParams httpparams = new RequestParams();
            httpparams.addBodyParameter("file", inputStream,bytes.length,"image.jpg");
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

        RequestParams params2 = new RequestParams();
//        params2.addBodyParameter("text", "图片上传完成");
//        params.addBodyParameter("request_id", String.valueOf(shiming.getS_id()));

        HttpCallResultBackAutoShiming httpCallResultBackSendChat = new HttpCallResultBackAutoShiming(new HttpCallResultBack() {
            @Override
            public void doresult(HttpResult result) {
                result.isSuccess();
            }

            @Override
            public void dofailure() {

            }
        });
        httpCallResultBackSendChat.setParams(params2);
        String resultstr = SellApplication.post_sync(httpCallResultBackSendChat);

        try {
            JSONObject r = new JSONObject(resultstr);
            r.has("res");
        } catch (JSONException e) {
            e.printStackTrace();
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
                Intent mainIntent2 = new Intent(context, ChatActivity.class);
                Bundle extras2 = new Bundle();
                mainIntent2.putExtras(extras2);
                context.startActivity(mainIntent2);
                ((Activity)context).finish();

                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void dofailure() {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        httpCallResultBackSendChat.setParams(params);
        SellApplication.post(httpCallResultBackSendChat);

    }


}