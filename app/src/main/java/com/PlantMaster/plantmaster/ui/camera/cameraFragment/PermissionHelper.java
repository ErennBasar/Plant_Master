package com.PlantMaster.plantmaster.ui.camera.cameraFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

public class PermissionHelper {
    private final Context context;
    private  final ActivityResultLauncher<String> requestPermissionLauncher;

    public PermissionHelper(Context context, ActivityResultLauncher<String> requestpermissionLauncher){
       this.context = context;
       this.requestPermissionLauncher = requestpermissionLauncher;
    }

    /**
     * Kamera izni kontrol edilir ve gerekiyorsa izin istenir.
     *
     * @param startCameraAction Kamera başlatma işlemini gerçekleştiren Runnable.
     */
    public void checkAndRequestCameraPermission(Runnable startCameraAction) {
        // Kamera izni kontrol edilir
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // İzin verilmemişse, izin istenir
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);

            // Tekrar kontrol edilip kamera başlatılır (Kullanıcı izni hemen vermişse)
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                startCameraAction.run();
            }
        } else {
            // İzin zaten verilmişse kamerayı başlat
            startCameraAction.run();
        }
    }

    /**
     * Kamera izni sonucunu işler.
     *
     * @param isGranted Kamera izninin verilip verilmediği.
     * @param startCameraAction Kamera başlatma işlemini gerçekleştiren Runnable.
     */
    public void handleCameraPermissionResult(boolean isGranted, Runnable startCameraAction) {
        if (isGranted) {
            // İzin verildiyse kamerayı başlat
            startCameraAction.run();
        } else {
            // İzin verilmediyse kullanıcıyı bilgilendir
            Toast.makeText(context, "Kamera izni gereklidir", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Galeriye erişim iznini kontrol eder ve gerekiyorsa kullanıcıdan izin ister.
     * İzin verilmişse belirtilen işlemi çalıştırır.
     *
     * @param startGalleryAction Galeriyi açma işlemini gerçekleştiren Runnable.
     */
    public void checkAndRequestGalleryPermission(Runnable startGalleryAction){
        // Galeriye erişim izni kontrolü
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("GalleryButton", "İzin verilmedi, izin isteniyor");
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            Log.d("GalleryButton", "İzin verildi, galeri açılıyor");
            startGalleryAction.run();
        }
    }
    /**
     * Galeri izni sonucunu işler.
     * İzin verilmişse galeriyi açar. İzin reddedilirse (şu an yorumlanmış), bir bilgilendirme yapılabilir.
     *
     * @param isGranted Galeri izninin verilip verilmediği.
     * @param openGalleryAction Galeriyi açma işlemini gerçekleştiren Runnable.
     */
    public void handleGalleryPermissionResult(boolean isGranted, Runnable openGalleryAction) {
        if (isGranted) {
            // İzin verildiyse kamerayı başlat
            openGalleryAction.run();
        } else {
            // İzin verilmediyse kullanıcıyı bilgilendir
            //Toast.makeText(context, "Galeri izni gereklidir", Toast.LENGTH_SHORT).show();
        }
    }
}
