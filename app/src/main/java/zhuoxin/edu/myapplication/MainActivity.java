package zhuoxin.edu.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button mBtn1;
    Button mBtn2;
    ImageView mImg1;
    ImageView mImg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mImg1 = (ImageView) findViewById(R.id.img1);
        mImg2 = (ImageView) findViewById(R.id.img2);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
    }

    File file;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1: {
                //调用系统相机
                //  MediaStore.ACTION_IMAGE_CAPTURE  调用系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //照相需要一个路径来保存照片
                //String path = Environment.getExternalStorageDirectory().getPath();
                if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
                    String path = Environment.getExternalStorageDirectory().getPath();
                }
                file = new File(path + File.separator + System.currentTimeMillis() + ".jpg");
                //设置保存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                //启动回调
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.btn2: {
                //从图库中获取手机
                // Intent.ACTION_PICK 进入图库获取照片意图
                Intent intent = new Intent(Intent.ACTION_PICK);
                //设置类型
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: {
                   /* Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                    mImg1.setImageBitmap(bitmap);*/
                    Bitmap bitmap = data.getParcelableExtra("data");
                    mImg1.setImageBitmap(bitmap);
                }
                case 2: {
                    //通过内容提供者获取系统中的数据
                    ContentResolver contentResolver = getContentResolver();
                    //根据地制值拿数据
                    Uri uri = data.getData();
                    String[] array = {MediaStore.Images.Media.DATA};
                    Cursor cursor = contentResolver.query(uri, array, null, null, null);
                    //将游标移到第一位
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor.getColumnIndex(array[0]));
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    mImg2.setImageBitmap(bitmap);

                    break;
                }
            }
        }
    }
}
