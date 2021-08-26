package com.abxh.utils.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.abxh.utils.R;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SelectVideoDialog extends AppCompatActivity implements OnClickListener {

    public static final String DATA = "path";
    private static final int SHOOTING = 0;
    private static final int CHOOSE = 1;
    private File tempFile;//临时文件

    protected int setContentLayout() {
        return R.layout.take_photo_popupwindow;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentLayout());
        initView();

    }

    //初始化视图
    protected void initView() {
        getWindow().setGravity(Gravity.BOTTOM);

        Button btnTakePhoto = (Button) findViewById(android.R.id.button1);
        btnTakePhoto.setText("拍摄");
        Button btnSelectPhoto = (Button) findViewById(android.R.id.button2);
        btnSelectPhoto.setText("从手机文件选取");
        Button btnCancel = (Button) findViewById(android.R.id.button3);

        btnTakePhoto.setOnClickListener(this);
        btnSelectPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1: {
                String imageFileName = "VID_" + System.currentTimeMillis() + ".mp4";
                String picdir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + getPackageName() + "/cache/";

                tempFile = new File(picdir, imageFileName);

                Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                Uri uri1 = Uri.fromFile(tempFile);
                intent1.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                if (Build.VERSION.SDK_INT < 24) {
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
                } else {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Video.Media.DATA, tempFile.getAbsolutePath());
                    Uri insert = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, insert);
                }
                startActivityForResult(intent1, SHOOTING);
            }
            break;
            // 相册选取
            case android.R.id.button2: {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, CHOOSE);

            }
            break;
            //按钮以外区域
//		case R.id.view_outside:
            // 取消
            case android.R.id.button3:
                try {
                    //直接调用返回键事件
                    onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (data == null || data.getData() == null) {
                android.widget.Toast.makeText(this, "无法识别的错误", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();
            switch (requestCode) {
                case SHOOTING:
                    String path = getPath(uri);
                    Intent intent = new Intent();
                    intent.putExtra(DATA, path);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;

                case CHOOSE:
                    //选择文件
                    String path1 = SelectPhotoDialog.getFileFromUri(this, uri);
//                    String path1 = getPath(data.getData());
                    if (path1 == null) {
                        android.widget.Toast.makeText(this, "无法识别的视频的路径！", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int typeIndex = path1.lastIndexOf(".");
                    if (typeIndex == -1) {
                        android.widget.Toast.makeText(this, "无法识别的视频类型！", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String fileType = path1.substring(typeIndex + 1).toLowerCase(Locale.CHINA);
                    if (Objects.equals(fileType, "mp4") || Objects.equals(fileType, "3gp")) {

                        Intent intent1 = new Intent();
                        intent1.putExtra(DATA, path1);
                        setResult(RESULT_OK, intent1);
                        finish();
                    } else {
                        android.widget.Toast.makeText(this, "无法识别的视频类型！", android.widget.Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    }

    public String getPath(Uri uri) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri,则通过document id来处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents" == uri.getAuthority()) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Video.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, selection);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri,则使用普通的方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，则直接获取视频路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = "";
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//            size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
//            duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
        }
        cursor.close();
        return path;
    }

}
