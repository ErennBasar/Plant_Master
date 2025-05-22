from flask import Flask, request, jsonify
import torch
from torchvision import transforms
from PIL import Image
import io
import torch.nn as nn
from transformers import Dinov2Model

app = Flask(__name__)

class DINOv2Classifier(nn.Module):
    def __init__(self, num_classes):
        super().__init__()
        self.dinov2 = Dinov2Model.from_pretrained("facebook/dinov2-base")
        self.classifier = nn.Sequential(
            nn.Linear(768, 512), nn.ReLU(), nn.Dropout(0.3), nn.Linear(512, num_classes)
        )

    def forward(self, x):
        features = self.dinov2(x).last_hidden_state
        pooled = features.mean(dim=1)
        return self.classifier(pooled)

MODEL_PATH = "best_dino2.pth"
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

class_names = [
    'Apple___Apple_scab', 'Apple___Black_rot', 'Apple___Cedar_apple_rust', 'Apple___healthy',
    'Blueberry___healthy', 'Cherry___Powdery_mildew', 'Cherry___healthy',
    'Corn___Cercospora_leaf_spot Gray_leaf_spot', 'Corn___Common_rust_',
    'Corn___Northern_Leaf_Blight', 'Corn___healthy', 'Grape___Black_rot',
    'Grape___Esca_(Black_Measles)', 'Grape___Leaf_blight_(Isariopsis_Leaf_Spot)', 'Grape___healthy',
    'Orange___Haunglongbing_(Citrus_greening)', 'Peach___Bacterial_spot', 'Peach___healthy',
    'Pepper___Bacterial_spot', 'Pepper___healthy', 'Potato___Early_blight',
    'Potato___Late_blight', 'Potato___healthy', 'Raspberry___healthy', 'Soybean___healthy',
    'Squash___Powdery_mildew', 'Strawberry___Leaf_scorch', 'Strawberry___healthy',
    'Tomato___Bacterial_spot', 'Tomato___Early_blight', 'Tomato___Late_blight',
    'Tomato___Leaf_Mold', 'Tomato___Septoria_leaf_spot', 'Tomato___Spider_mites Two-spotted_spider_mite',
    'Tomato___Target_Spot', 'Tomato___Tomato_Yellow_Leaf_Curl_Virus', 'Tomato___Tomato_mosaic_virus',
    'Tomato___healthy'
]

model = DINOv2Classifier(num_classes=len(class_names)).to(device)
model.load_state_dict(torch.load(MODEL_PATH, map_location=device))
model.eval()

def transform_image(image_bytes):
    transform = transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])
    image = Image.open(io.BytesIO(image_bytes)).convert("RGB")
    return transform(image).unsqueeze(0).to(device)

@app.route('/predict', methods=['POST'])
def predict():
    try:
        if 'file' not in request.files:
            return jsonify({"error": "No file uploaded!"}), 400

        file = request.files['file']
        image_bytes = file.read()
        image_tensor = transform_image(image_bytes)

        with torch.no_grad():
            output = model(image_tensor)
            prob = torch.nn.functional.softmax(output[0], dim=0)
            confidence, predicted_idx = torch.max(prob, 0)

        if confidence < 0.1:
            return jsonify({
                "plant": "Unknown",
                "disease": "Unknown",
                "confidence": float(confidence)
            })

        predicted_label = class_names[predicted_idx]

        if "___" in predicted_label:
            plant_raw, disease_raw = predicted_label.split("___")
        else:
            plant_raw, disease_raw = predicted_label, "Unknown"

        plant = plant_raw.replace("_", " ").replace("(", "").replace(")", "").strip()
        disease = disease_raw.replace("_", " ").replace("(", "").replace(")", "").strip()

        if disease.lower().startswith(plant.lower()):
            disease = disease[len(plant):].strip()

        disease = disease.lstrip("-–_ ").strip()

        return jsonify({
            "plant": plant,
            "disease": disease,
            "confidence": float(confidence)
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
