package edu.android.teamproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {

    private String mCurrentPhotoPath;
    public static final int REQ_CODE_CAMERA = 1000; // 카메라 키값
    public static final int PICK_FROM_GALLERY = 1; // 갤러리 키값
    public static final int PERMISTION_GALLERY = 2;
    public static final int PERMISTION_CAMERA = 3;
    public static final int PERMISTION_WRITE = 4;

    private static Uri imageUri;
    public static Bitmap bit = null; // 그림을 저장할 공간.

    private String filePath;
    private String folderName = "TeamProject";// 폴더명
    private String fileName = "Image"; // 파일명

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
    }//end onCreate();


    // startActivityForResult 할때의 기능 override
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== REQ_CODE_CAMERA && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
                try {
           //         bit = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                    bit = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    Log.i("edu.android", "mCurrentPhotoPath :: " + mCurrentPhotoPath);
                    // 이미지를 상황에 맞게 회전시킴
                    ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    Log.i("edu.android", "exifDegree" + exifDegree);
                    bit = rotate(bit, exifDegree);

                    Intent nextI = new Intent(this, MainActivity.class);
                    startActivity(nextI);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
                }


        }else{

        }//end Camera

        if (requestCode== PICK_FROM_GALLERY && resultCode == RESULT_OK ) {
            Uri uri = data.getData();
            if(uri!=null) {
                try {
                    bit = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    Intent nextI = new Intent(this, MainActivity.class);
                    startActivity(nextI);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "에러 발생", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }
        }else{

        }//end Gallery

    }//end onActivityResult


    public static int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    public static Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }



    // 카메라로 찍어서 값을 넘겨줌
    public void startCamera(View view) {
        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int check2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(check == PackageManager.PERMISSION_GRANTED&& check2== PackageManager.PERMISSION_GRANTED){
            try {
                File photoFile = null;
                photoFile = createImageFile();
                imageUri = FileProvider.getUriForFile(this,"edu.android.teamproject",photoFile);

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQ_CODE_CAMERA);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PERMISTION_CAMERA);
            ActivityCompat.requestPermissions(this, permissions, PERMISTION_WRITE);
        }

    }//end startCamera

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SCR_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }//end createImageFile

    public void startGallery(View view) {
        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (check == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // 갤러리 인텐트 생성
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY); //갤러리 인텐트 실행
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PERMISTION_GALLERY);
        }
    }

}//end class SecondActivity