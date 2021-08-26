package com.abxh.utils.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.abxh.utils.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * 选择拍照或相册等dialog
 *
 * @author sinata
 */
public class SelectPhotoDialog extends AppCompatActivity implements OnClickListener {

    private final int CODE_PERMISSION = 10;
    public static final String DATA = "path";
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
        Button btnSelectPhoto = (Button) findViewById(android.R.id.button2);
        Button btnCancel = (Button) findViewById(android.R.id.button3);

        btnTakePhoto.setOnClickListener(this);
        btnSelectPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (tempFile != null && tempFile.exists()) {
                        Intent intent = new Intent();
                        intent.putExtra(DATA, tempFile.getAbsolutePath());
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(0, 0);
                    }

                    break;
                case 1:
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            String path = getFileFromUri(SelectPhotoDialog.this, uri);
                            if (path != null) {
                                int typeIndex = path.lastIndexOf(".");
                                if (typeIndex != -1) {
                                    String fileType = path.substring(typeIndex + 1).toLowerCase(Locale.CHINA);
                                    //某些设备选择图片是可以选择一些非图片的文件。然后发送出去或出错。这里简单的通过匹配后缀名来判断是否是图片文件
                                    //如果是图片文件则发送。反之给出提示
                                    if (Objects.equals(fileType, "jpg") || Objects.equals(fileType, "gif")
                                            || Objects.equals(fileType, "png") || Objects.equals(fileType, "jpeg")
                                            || Objects.equals(fileType, "bmp") || Objects.equals(fileType, "wbmp")
                                            || Objects.equals(fileType, "ico") || Objects.equals(fileType, "jpe")) {
                                        Intent intent = new Intent();
                                        intent.putExtra(DATA, path);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        overridePendingTransition(0, 0);
                                    } else {
                                        Toast.makeText(this, "无法识别的图片类型！", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(this, "无法识别的图片类型！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "无法识别的图片类型或路径！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "无法识别的图片类型！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //拍照
            case android.R.id.button1: {
                String picdir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + getPackageName() + "/cache/";

                //检测路径是否存在，不存在就创建
                File dir = new File(picdir);
                if (!dir.exists()) {//picdir，则进行创建  
                    dir.mkdirs();//创建文件夹  
                }
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = System.currentTimeMillis() + ".jpg";
                tempFile = new File(picdir, fileName);
                Uri u = Uri.fromFile(tempFile);
                //7.0崩溃问题
                if (Build.VERSION.SDK_INT < 24) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                } else {
//                                    String packageResourcePath = getPackageName();
//                                    Uri uri = FileProvider.getUriForFile(this, packageResourcePath + ".fileProvider", tempFile);
//                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                startActivityForResult(intent, 0);


            }
            break;
            // 相册选取
            case android.R.id.button2: {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
                intent.setType("image/*");
                startActivityForResult(intent, 1);
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
    protected void onDestroy() {
        super.onDestroy();

    }

    private static String getFilePathForN(Context context, Uri uri) {
        try {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = (returnCursor.getString(nameIndex));
            File file = new File(context.getFilesDir(), name);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            returnCursor.close();
            inputStream.close();
            outputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFileNamePathForN(Context context, Uri uri) {
        try {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            return (returnCursor.getString(nameIndex));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据uri获取文件路径,是将文件复制到app目录缓存下 在提供路径的
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFileFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String fileName = getFileNamePathForN(context, uri);
        if (TextUtils.isEmpty(fileName)) {
            String lastPathSegment = uri.getLastPathSegment();
            if (TextUtils.isEmpty(lastPathSegment)) return null;
            int beginIndex = lastPathSegment.lastIndexOf("/");
            if (beginIndex > 0) {
                fileName = lastPathSegment.substring(beginIndex);
            } else {
                fileName = lastPathSegment;
            }
        }
        return getPathFromInputStreamUri(context, uri, fileName);
    }


    public static String getPathFromInputStreamUri(Context context, Uri uri, String fileName) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                File file = createTemporalFileFrom(context, inputStream, fileName);
                filePath = file.getPath();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

    private static File createTemporalFileFrom(Context context, InputStream inputStream, String fileName)
            throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];
            //自己定义拷贝文件路径
            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/" + "/cache";
            File cacheDir = new File(root);
            cacheDir.delete();
            targetFile = new File(cacheDir, fileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            cacheDir.mkdirs();
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }


}
