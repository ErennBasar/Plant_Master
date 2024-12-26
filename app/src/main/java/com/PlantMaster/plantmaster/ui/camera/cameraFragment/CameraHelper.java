package com.PlantMaster.plantmaster.ui.camera.cameraFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
public class CameraHelper {

    private final android.content.Context context;
    private final LifecycleOwner lifecycleOwner;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    public CameraHelper(Context context, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
    }
    public CameraHelper(Context context, LifecycleOwner lifecycleOwner, ActivityResultLauncher<Intent> galleryActivityResultLauncher) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.galleryActivityResultLauncher = galleryActivityResultLauncher;
    }

    /**
     * CameraX kütüphanesini kullanarak kamerayı başlatır ve görüntüyü ekrana yansıtır.
     *
     * Bu metot, kamerayı verilen `LifecycleOwner` ile ilişkilendirir, bir önizleme yapılandırması oluşturur
     * ve kameradan gelen görüntüyü belirtilen `PreviewView` üzerinde gösterir.
     *
     * @param previewView Kamera önizleme görüntüsünün gösterileceği View.
     *
     * Kullanım:
     * 1. Bu metodu çağırmadan önce gerekli izinlerin (örn. CAMERA) verildiğinden emin olun.
     * 2. Metot, UI tamamen oluşturulduktan sonra çağrılmalıdır (örn. `onCreateView` veya `onViewCreated` içinde).
     *
     * Notlar:
     * - Varsayılan olarak arka kamera (back-facing lens) kullanılır.
     * - Kamera başlatılamazsa hata yakalanır ve "CameraHelper" etiketiyle loglanır.
     *
     * Bağımlılıklar:
     * - AndroidX CameraX Kütüphanesi.
     * - ContextCompat (ana thread executor için).
     * - LifecycleOwner (kameranın yaşam döngüsünü bağlamak için).
     *
     * @throws Exception Eğer kamera sağlayıcısı başlatılamaz veya bağlanamazsa hata fırlatılır.
     */
    public void startCamera(PreviewView previewView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                // CameraProvider al
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview konfigurasyon
                Preview preview = new Preview.Builder().build();

                // Kamera secimi
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                // Preview -> PreviewView baglanti
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Kamera ve Preview baglanti
                Camera camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                );

            } catch (Exception e) {
                Log.e("CameraHelper", "Kamera başlatılamadı", e);
            }
        }, ContextCompat.getMainExecutor(context));
    }

    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(galleryIntent);
    }

}
