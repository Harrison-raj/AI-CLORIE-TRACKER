# app.py (Flask service)

from flask import Flask, request, jsonify
from PIL import Image
import io

app = Flask(__name__)

# Simple calorie mapping (later can expand with API or ML model)
CALORIE_DB = {
    "fried_rice": 350,
    "pizza": 400,
    "burger": 500,
    "salad": 150,
    "dosa": 250
}

@app.route("/predict", methods=["POST"])
def predict():
    if "file" not in request.files:
        return jsonify({"error": "No file uploaded"}), 400
    
    file = request.files["file"]
    img = Image.open(io.BytesIO(file.read())).convert("RGB")

    # Dummy food label
    food_label = "pizza"  
    
    calories = CALORIE_DB.get(food_label, 200)  # default 200 if unknown
    
    return jsonify({
        "label": food_label,
        "confidence": 0.85,
        "calories": calories
    })

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
