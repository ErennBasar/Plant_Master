import os
from PIL import Image
import imgaug.augmenters as iaa
import numpy as np
import cv2

def get_image_count(directory):
    image_extensions = ('.png', '.jpg', '.jpeg')
    image_count = 0
    for root, dirs, files in os.walk(directory):
        image_count += sum(1 for file in files if file.lower().endswith(image_extensions))
    return image_count

# Canny kenar algılama fonksiyonu
def canny_augmentation(images, random_state, parents, hooks):
    augmented_images = []
    for image in images:
        # OpenCV ile Canny kenar algılama
        gray_image = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)  # RGB'den gri tonlama
        edges = cv2.Canny(gray_image, 100, 200)  # Canny kenar algılama
        augmented_images.append(edges)  # Kenarları döndür
    return augmented_images

def shorten_filename(filename, max_length=255):
    if len(filename) > max_length:
        base, ext = os.path.splitext(filename)
        base = base[:max_length - len(ext)]
        return base + ext
    return filename

# Augmentasyon tekniklerini artırma
augmenters = [
    iaa.Fliplr(0.5),  # Görüntüyü yatay olarak çevirme
    iaa.Affine(rotate=(-15, 15)),  # Görüntüyü döndürme
    iaa.Multiply((0.8, 1.2)),  # Parlaklık artırma/azaltma
    iaa.GaussianBlur(sigma=(0, 1.0)),  # Hafif bulanıklık ekleme
    iaa.AdditiveGaussianNoise(scale=(0, 0.1 * 255)),  # Gürültü ekleme
    iaa.LinearContrast((0.8, 1.2)),  # Kontrast ayarlama
    iaa.Affine(scale=(0.8, 1.2)),  # Ölçekleme
    iaa.PerspectiveTransform(scale=(0.01, 0.1)),  # Perspektif kaydırma
    iaa.Sharpen(alpha=(0.0, 1.0)),  # Keskinlik artırma
    iaa.Crop(percent=(0, 0.1)),  # Görüntüyü kesme
    iaa.Affine(translate_percent={"x": (-0.2, 0.2), "y": (-0.2, 0.2)}),  # Kaydırma
    iaa.LinearContrast((0.6, 1.4)),  # Kontrast artırma
    iaa.Resize({"height": (0.8, 1.2), "width": (0.8, 1.2)}),  # Yeniden boyutlandırma
    iaa.Rot90(1),  # 90 derece döndürme
    iaa.Fliplr(0.7),  # Daha fazla yatay çevirme
    iaa.Multiply((0.5, 1.5)),  # Parlaklık artışı/azaltma
    iaa.ElasticTransformation(alpha=(0.1, 0.3), sigma=0.25),  # Elastik dönüşüm
    iaa.PiecewiseAffine(scale=(0.01, 0.05)),  # Parçalı affine dönüşümü
    iaa.Invert(0.1),  # Renkleri tersine çevirme
    iaa.Lambda(func_images=canny_augmentation),  # Canny kenar algılama (per_channel parametresiz)
    iaa.Dropout((0.01, 0.1)),  # Rastgele pikselleri silme
    iaa.Solarize(0.5),  # Görüntü solarizasyonu
    iaa.MotionBlur(k=5),  # Hareket bulanıklığı
    iaa.ElasticTransformation(alpha=(0.3, 0.6), sigma=0.2),  # Elastik dönüşüm
    iaa.Superpixels(p_replace=(0.1, 0.5), n_segments=(100, 1000)),  # Süper piksel
    # OpenCV augmentasyonları ekleme
    iaa.Lambda(func_images=lambda images, random_state, parents, hooks: [
        cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE) for image in images]),  # 90 derece döndürme
    iaa.Lambda(func_images=lambda images, random_state, parents, hooks: [
        # Perspektif dönüşümünü düzgün bir şekilde uygulamak
        cv2.warpPerspective(image, cv2.getPerspectiveTransform(np.array([[0, 0], [1, 0], [1, 1], [0, 1]], dtype=np.float32),
                                                              np.array([[0, 0], [1, 0], [1, 1], [0, 1]], dtype=np.float32)), 
                            (image.shape[1], image.shape[0])) for image in images]),  # Perspektif değişikliği
    iaa.Lambda(func_images=lambda images, random_state, parents, hooks: [
        cv2.cvtColor(image, cv2.COLOR_BGR2HSV) for image in images]),  # Renk dönüşümü
    iaa.Lambda(func_images=lambda images, random_state, parents, hooks: [
    cv2.equalizeHist(cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)) for image in images]),  # Histogram eşitleme
    iaa.ContrastNormalization((0.8, 1.2))  # Kontrast normalizasyonu
]

def augment_images(directory, num_images_to_augment):
    image_files = [f for f in os.listdir(directory) if f.lower().endswith(('png', 'jpg', 'jpeg'))]

    if not image_files:
        print(f"{directory} dizininde resim bulunamadı.")
        return

    for i, image_file in enumerate(image_files[:num_images_to_augment]):
        image_path = os.path.join(directory, image_file)
        with Image.open(image_path) as img:
            img = img.convert("RGB")
            img_array = np.array(img)

            for j, augmenter in enumerate(augmenters):
                augmented_image = augmenter(image=img_array)
                aug_image = Image.fromarray(augmented_image)

                filename, ext = os.path.splitext(image_file)
                save_path = os.path.join(directory, f"{filename}_aug_{j + 1}{ext}")
                aug_image.save(save_path)
                print(f"Kaydedildi: {save_path}")

def process_directory(base_directory, desired_total=4000):
    for root, dirs, files in os.walk(base_directory):
        for subdir in dirs:
            subdir_path = os.path.join(root, subdir)
            total_images = get_image_count(subdir_path)
            print(f"{subdir_path} dizininde toplam {total_images} resim bulundu.")

            if total_images >= desired_total:
                print(f"{subdir_path} dizinindeki resim sayısı zaten yeterli.")
                continue

            needed_images = desired_total - total_images
            augment_per_image = len(augmenters)
            images_to_augment = int(np.ceil(needed_images / augment_per_image))

            if images_to_augment * augment_per_image < needed_images:
                images_to_augment += 1 

            print(f"{subdir_path} dizininde {images_to_augment} orijinal resim augmentasyon için seçilecek.")

            augment_images(subdir_path, images_to_augment)

def main():
    base_directory = input("Lütfen ana dizin yolunu girin: ").replace('"', '')
    if not os.path.exists(base_directory):
        print("Geçerli bir dizin giriniz.")
        return

    process_directory(base_directory)

if __name__ == "__main__":
    main()
