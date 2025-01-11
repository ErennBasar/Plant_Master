import os
from PIL import Image

def get_image_count(directory):
    image_extensions = ('.png', '.jpg', '.jpeg')
    image_count = 0
    for root, dirs, files in os.walk(directory):
        image_count += sum(1 for file in files if file.lower().endswith(image_extensions))
    return image_count

def resize_images(input_directory, output_directory, target_size=(224, 224)):
    image_files = [f for f in os.listdir(input_directory) if f.lower().endswith(('png', 'jpg', 'jpeg'))]

    if not image_files:
        print(f"{input_directory} dizininde resim bulunamadı.")
        return

    if not os.path.exists(output_directory):
        os.makedirs(output_directory)

    for image_file in image_files:
        image_path = os.path.join(input_directory, image_file)
        output_path = os.path.join(output_directory, image_file)

        try:
            with Image.open(image_path) as img:
                img = img.convert("RGB")
                img_resized = img.resize(target_size, Image.Resampling.LANCZOS)
                img_resized.save(output_path)
                print(f"Kaydedildi: {output_path}")
        except Exception as e:
            print(f"Hata: {image_path} - {e}")

def process_directory(base_directory):
    base_directory_224 = base_directory + "_224" 
    if not os.path.exists(base_directory_224):
        os.makedirs(base_directory_224)

    for root, dirs, files in os.walk(base_directory):
        for subdir in dirs:
            subdir_path = os.path.join(root, subdir)
            total_images = get_image_count(subdir_path)
            print(f"{subdir_path} dizininde toplam {total_images} resim bulundu.")

            if total_images > 0:
                relative_path = os.path.relpath(subdir_path, base_directory)
                output_directory = os.path.join(base_directory_224, relative_path)
                
                resize_images(subdir_path, output_directory)
            else:
                print(f"{subdir_path} dizininde resim bulunamadı.")

def main():
    base_directory = input("Lütfen ana dizin yolunu girin: ").replace('"', '')
    
    if not os.path.exists(base_directory):
        print("Geçerli bir dizin giriniz.")
        return

    process_directory(base_directory)

if __name__ == "__main__":
    main()
