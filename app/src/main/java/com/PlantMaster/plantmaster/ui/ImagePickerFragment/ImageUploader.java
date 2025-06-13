package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUploader {

    private static final String SERVER_URL = "http://89.252.184.118:5000/predict";
    private static final OkHttpClient client = new OkHttpClient();


    public interface UploadCallback {
        void onSuccess(Bundle resultData);
        void onError(String errorMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void uploadImage(Uri imageUri, Context context, UploadCallback callback) {
        new UploadTask(context, imageUri, callback).execute();
    }

    private static class UploadTask extends AsyncTask<Void, Void, String> {
        private final Context context;
        private final Uri imageUri;
        private final UploadCallback callback;

        public UploadTask(Context context, Uri imageUri, UploadCallback callback) {
            this.context = context;
            this.imageUri = imageUri;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                if (inputStream == null) {
                    return "Dosya açılamadı!";
                }

                File file = createTempFile(context, inputStream);
                if (file == null) {
                    return "Geçici dosya oluşturulamadı!";
                }

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(
                                "file",
                                file.getName(),
                                RequestBody.create(file, MediaType.parse("image/*")))
                        .build();

                Request request = new Request.Builder()
                        .url(SERVER_URL)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    return "Sunucu hatası: " + response.code();
                }

                return response.body() != null ? response.body().string() : "Boş yanıt";

            } catch (IOException e) {
                return "Yükleme hatası: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("ImageUpload", "Sunucu yanıtı: " + result);

            try {
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.has("error")) {
                    String errorMessage = "Hata: " + jsonResponse.getString("error");
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    callback.onError(errorMessage);
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("imageUri", imageUri.toString());
                bundle.putString("plant", jsonResponse.getString("plant"));
                bundle.putString("disease", jsonResponse.getString("disease"));
                bundle.putDouble("confidence", jsonResponse.getDouble("confidence"));

                callback.onSuccess(bundle);
            } catch (JSONException e) {
                String errorMessage = "Yanıt işlenemedi: " + result;
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                Log.e("ImageUpload", "JSON ayrıştırma hatası", e);
                callback.onError(errorMessage);
            }
        }

        private File createTempFile(Context context, InputStream inputStream) {
            try {
                File file = File.createTempFile("upload", ".jpg", context.getCacheDir());
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4 * 1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                }
                return file;
            } catch (IOException e) {
                Log.e("ImageUpload", "Geçici dosya oluşturma hatası", e);
                return null;
            }
        }
    }
}
