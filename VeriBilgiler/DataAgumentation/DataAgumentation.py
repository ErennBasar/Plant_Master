import os
from PIL import Image
import imgaug.augmenters as iaa
import numpy as np

def augment_images_in_directory(directory_path, target_count):
    # Veri artırma işlemleri için bir imgaug augmenter seti oluşturuyoruz
    augmenter = iaa.Sequential([
        iaa.Fliplr(0.5),  # Görüntüyü yatay olarak çevirme
        iaa.Affine(rotate=(-15, 15)),  # Görüntüyü döndürme
        iaa.Multiply((0.8, 1.2)),  # Parlaklık artırma/azaltma
        iaa.GaussianBlur(sigma=(0, 1.0)),  # Hafif bulanıklık ekleme
        iaa.AdditiveGaussianNoise(scale=(0, 0.1 * 255)),  # Gürültü ekleme
        iaa.ContrastNormalization((0.8, 1.2)),  # Kontrast ayarlama
        iaa.Affine(scale=(0.8, 1.2)),  # Ölçekleme
        iaa.PerspectiveTransform(scale=(0.01, 0.1)),  # Perspektif kaydırma
        iaa.LinearContrast((0.75, 1.5)),  # Kontrastı lineer olarak ayarlama
        iaa.Crop(percent=(0, 0.1)),  # Görüntüyü kesme
        iaa.Sharpen(alpha=(0.0, 1.0)),  # Keskinlik artırma
        iaa.ElasticTransformation(alpha=(0.1, 0.5), sigma=0.25)  # Elastik dönüşüm
    ])

    if not os.path.exists(directory_path):
        print(f"Dizin bulunamadı: {directory_path}")
        return

    image_files = [f for f in os.listdir(directory_path) if f.lower().endswith(('png', 'jpg', 'jpeg'))]

    if not image_files:
        print("Dizin içinde görüntü dosyası bulunamadı.")
        return

    total_images = len(image_files)
    print(f"Başlangıçta {total_images} adet görüntü bulunuyor.")

    if total_images >= target_count:
        print(f"Target dosya sayısı {target_count} mevcut dosya sayısından ({total_images}) küçük. Veri artırmaya gerek yok.")
        return

    augmented_count = 0
    index = 1
    augmented_files = image_files.copy()

    while augmented_count + total_images < target_count:
        for image_file in augmented_files:
            image_path = os.path.join(directory_path, image_file)
            with Image.open(image_path) as img:
                img = img.convert("RGB") 

                img_array = np.array(img)

                augmented_images = augmenter(images=[img_array] * 3)

                for i, aug_img in enumerate(augmented_images):
                    aug_image = Image.fromarray(aug_img)

                    
                    safe_file_name = f"{index}_aug_{i + 1}.jpg"
                    save_path = os.path.join(directory_path, safe_file_name)

                   
                    if len(save_path) > 255:
                        print(f"Uyarı: Dosya yolu çok uzun, kısaltılıyor: {save_path}")
                        save_path = save_path[:255]

                    aug_image.save(save_path)
                    print(f"Kaydedildi: {save_path}")

                    augmented_count += 1
                    augmented_files.append(safe_file_name)

                    if augmented_count + total_images >= target_count:
                        break

                index += 1

            if augmented_count + total_images >= target_count:
                break

    print(f"Toplamda {augmented_count + total_images} görüntü işlendi ve kaydedildi.")


directory_input = input("Lütfen dizin yolunu girin: ")
directory_input = directory_input.replace('"', '')

target_count_input = input("Hedef görüntü sayısını girin: ")
try:
    target_count = int(target_count_input)
except ValueError:
    print("Hedef görüntü sayısı geçerli bir sayı olmalıdır.")
    exit()

augment_images_in_directory(directory_input, target_count)
