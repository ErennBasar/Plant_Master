{
 "cells": [
  {
   "cell_type": "code",
   "execution_count": 8,
   "metadata": {},
   "outputs": [
    {
     "name": "stdout",
     "output_type": "stream",
     "text": [
      "Toplamda 500 görüntü işlendi ve kaydedildi.\n"
     ]
    }
   ],
   "source": [
    "import os\n",
    "from PIL import Image\n",
    "import imgaug.augmenters as iaa\n",
    "import numpy as np\n",
    "\n",
    "def augment_images_in_directory(directory_path, target_count=500):\n",
    "    # Veri artırma işlemleri için bir imgaug augmenter seti oluşturuyoruz\n",
    "    augmenter = iaa.Sequential([\n",
    "        iaa.Fliplr(0.5),  # Görüntüyü yatay olarak çevirme\n",
    "        iaa.Affine(rotate=(-15, 15)),  # Görüntüyü döndürme\n",
    "        iaa.Multiply((0.8, 1.2)),  # Parlaklık artırma/azaltma\n",
    "        iaa.GaussianBlur(sigma=(0, 1.0)),  # Hafif bulanıklık ekleme\n",
    "        iaa.AdditiveGaussianNoise(scale=(0, 0.1 * 255)),  # Gürültü ekleme\n",
    "        iaa.ContrastNormalization((0.8, 1.2)),  # Kontrast ayarlama\n",
    "        iaa.Affine(scale=(0.8, 1.2)),  # Ölçekleme\n",
    "        iaa.PerspectiveTransform(scale=(0.01, 0.1)),  # Perspektif kaydırma\n",
    "        iaa.LinearContrast((0.75, 1.5)),  # Kontrastı lineer olarak ayarlama\n",
    "        iaa.Crop(percent=(0, 0.1)),  # Görüntüyü kesme\n",
    "        iaa.Sharpen(alpha=(0.0, 1.0)),  # Keskinlik artırma\n",
    "        iaa.ElasticTransformation(alpha=(0.1, 0.5), sigma=0.25)  # Elastik dönüşüm\n",
    "    ])\n",
    "\n",
    "    if not os.path.exists(directory_path):\n",
    "        print(f\"Dizin bulunamadı: {directory_path}\")\n",
    "        return\n",
    "\n",
    "    \n",
    "    image_files = [f for f in os.listdir(directory_path) if f.lower().endswith(('png', 'jpg', 'jpeg'))]\n",
    "\n",
    "    if not image_files:\n",
    "        print(\"Dizinde görüntü dosyası bulunamadı.\")\n",
    "        return\n",
    "\n",
    "    total_images = len(image_files)  \n",
    "    augmented_count = 0 \n",
    "    index = 1\n",
    "\n",
    "    \n",
    "    augmented_files = image_files.copy()  \n",
    "\n",
    "    while augmented_count + total_images < target_count:\n",
    "        for image_file in augmented_files:  \n",
    "            image_path = os.path.join(directory_path, image_file)\n",
    "            with Image.open(image_path) as img:\n",
    "                img = img.convert(\"RGB\") \n",
    "\n",
    "                \n",
    "                img_array = np.array(img)\n",
    "\n",
    "                \n",
    "                augmented_images = augmenter(images=[img_array] * 3)  \n",
    "\n",
    "                for i, aug_img in enumerate(augmented_images):\n",
    "                    aug_image = Image.fromarray(aug_img)\n",
    "\n",
    "                   \n",
    "                    save_path = os.path.join(directory_path, f\"{index}_aug_{i + 1}.jpg\")\n",
    "                    aug_image.save(save_path)\n",
    "                    print(f\"Kaydedildi: {save_path}\")\n",
    "\n",
    "                    augmented_count += 1\n",
    "                    augmented_files.append(f\"{index}_aug_{i + 1}.jpg\")  \n",
    "\n",
    "                    if augmented_count + total_images >= target_count:\n",
    "                        break  \n",
    "\n",
    "                index += 1\n",
    "\n",
    "            if augmented_count + total_images >= target_count:\n",
    "                break  \n",
    "\n",
    "    print(f\"Toplamda {augmented_count + total_images} görüntü işlendi ve kaydedildi.\")\n",
    "\n",
    "\n",
    "augment_images_in_directory(r\"C:\\Users\\Batuhan\\Desktop\\image\", target_count=500)\n"
   ]
  }
 ],
 "metadata": {
  "kernelspec": {
   "display_name": "Python 3",
   "language": "python",
   "name": "python3"
  },
  "language_info": {
   "codemirror_mode": {
    "name": "ipython",
    "version": 3
   },
   "file_extension": ".py",
   "mimetype": "text/x-python",
   "name": "python",
   "nbconvert_exporter": "python",
   "pygments_lexer": "ipython3",
   "version": "3.11.9"
  }
 },
 "nbformat": 4,
 "nbformat_minor": 2
}
