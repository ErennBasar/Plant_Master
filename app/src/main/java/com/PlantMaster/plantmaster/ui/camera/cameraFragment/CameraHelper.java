package com.PlantMaster.plantmaster.ui.camera.cameraFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.File;
import androidx.activity.result.ActivityResultLauncher;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageCaptureException;
import com.google.common.util.concurrent.ListenableFuture;
public class CameraHelper {

    private final android.content.Context context;
    private final LifecycleOwner lifecycleOwner;
    private ImageCapture imageCapture;
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

                // ImageCapture konfigurasyonu
                ImageCapture.Builder imageCaptureBuilder = new ImageCapture.Builder();
                imageCapture = imageCaptureBuilder.build();

                // Preview -> PreviewView baglanti
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Kamera ve Preview baglanti
                Camera camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                );

            } catch (Exception e) {
                Log.e("CameraHelper", "Kamera başlatılamadı", e);
            }
        }, ContextCompat.getMainExecutor(context));
    }
    /**
     * Galeri uygulamasını açar ve kullanıcıdan bir görüntü seçmesini ister.
     * Seçilen görüntü, verilen `ActivityResultLauncher` üzerinden işlenir.
     *
     * Kullanım:
     * 1. Bu metodu çağırmadan önce `galleryActivityResultLauncher`'ın uygun şekilde yapılandırıldığından emin olun.
     * 2. Kullanıcı bir görüntü seçtikten sonra, sonuç `ActivityResultLauncher` ile alınabilir.
     *
     * Notlar:
     * - Intent, galeri uygulamasını başlatmak için ACTION_PICK kullanır.
     * - Android `MediaStore` API'sini kullanır.
     */
    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(galleryIntent);
    }


    public void takePhoto(final OnPhotoSavedListener listener) {
        if (imageCapture == null) {
            Log.e("CameraHelper", "ImageCapture is not initialized");
            return;
        }

        // Fotoğrafın geçici olarak saklanacağı dosya
        File photoFile = new File(context.getCacheDir(), "temp_image.jpg");

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // Fotoğraf başarıyla kaydedildi
                if (listener != null) {
                    listener.onPhotoSaved(photoFile.getAbsolutePath());
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exc) {
                Log.e("CameraHelper", "Photo capture failed: " + exc.getMessage(), exc);
            }
        });
    }

    // Callback arayüzü
    public interface OnPhotoSavedListener {
        void onPhotoSaved(String filePath);
    }



}
