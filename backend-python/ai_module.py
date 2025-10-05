
import joblib
import os

model_path = os.path.join("ai_model", "model.pkl")

cpu_map = {"Intel": 1, "AMD": 2, "ARM": 3}
status_map = {"RUNNING": 1, "TERMINATED": 0, "STOPPED": 0}

if not os.path.exists(model_path):
    raise FileNotFoundError("❌ model.pkl not found. Run train_model.py to create it.")

try:
    model = joblib.load(model_path)
    print("✅ AI model loaded successfully.")
except Exception as e:
    raise RuntimeError(f"❌ Failed to load model: {e}")

def analyze_instance(instance):
    try:
        cpu = instance.get("cpuPlatform", "Intel")
        status = instance.get("status", "TERMINATED")
        tags = instance.get("tags", {}).get("items", [])

        cpu_encoded = cpu_map.get(cpu, 1)
        status_encoded = status_map.get(status, 0)
        tags_encoded = 1 if tags else 0

        features = [[cpu_encoded, status_encoded, tags_encoded]]

        prediction = model.predict(features)[0]
        return {
            "anomaly_detected": bool(prediction),
            "ai_comment": "⚠️ Possible issue detected" if prediction else "✅ Instance is normal"
        }
    except Exception as e:
        print(f"⚠️ Error analyzing instance: {e}")
        return {
            "anomaly_detected": True,
            "ai_comment": f"⚠️ AI prediction failed: {e}"
        }

if __name__ == "__main__":
    test_instance = {
        "cpuPlatform": "Intel",
        "status": "TERMINATED",
        "tags": {}
    }
    print("🔍 AI Test Result:", analyze_instance(test_instance))
