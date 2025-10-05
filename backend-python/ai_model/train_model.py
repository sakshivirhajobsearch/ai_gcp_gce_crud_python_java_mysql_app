
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
import joblib
import os

data = {
    "cpu_platform": [1, 2, 1, 3, 2, 1],
    "status": [1, 1, 0, 0, 1, 0],
    "tagged": [1, 0, 1, 0, 1, 0],
    "anomaly": [0, 1, 0, 1, 0, 1]
}

df = pd.DataFrame(data)
X = df[["cpu_platform", "status", "tagged"]]
y = df["anomaly"]

model = RandomForestClassifier()
model.fit(X, y)

os.makedirs("ai_model", exist_ok=True)
joblib.dump(model, "ai_model/model.pkl")
print("✅ AI model trained and saved to ai_model/model.pkl")
