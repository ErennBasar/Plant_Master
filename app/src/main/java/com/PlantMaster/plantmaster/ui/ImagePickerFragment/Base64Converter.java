package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

public class Base64Converter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String uriToBase64(Context context, Uri uri){
        try{
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead=inputStream.read(buffer))!= -1){
                byteArrayOutputStream.write(buffer,0,bytesRead);
            }

            byte[] bytes = byteArrayOutputStream.toByteArray();

            inputStream.close();

            return Base64.getEncoder().encodeToString(bytes);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap base64ToBitmap(String base64str){
        try{
            byte[] decodeBytes = Base64.getDecoder().decode(base64str);

            return BitmapFactory.decodeByteArray(decodeBytes,0,decodeBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
