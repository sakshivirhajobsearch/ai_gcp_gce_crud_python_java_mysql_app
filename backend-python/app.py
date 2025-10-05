from flask import Flask, jsonify, request
from flask_cors import CORS
from gce_module import get_instances, start_instance, stop_instance
from ai_module import analyze_instance

app = Flask(__name__)
CORS(app)

@app.route("/")
def index():
    return jsonify({
        "message": "✅ AI + GCP Compute Engine Flask backend is running",
        "endpoints": [
            "/instances",
            "/instances/analyze",
            "/instances/start",
            "/instances/stop"
        ]
    })

# ✅ GET: List all instances
@app.route("/instances", methods=["GET"])
def get_all_instances():
    try:
        instances = get_instances()
        return jsonify(instances)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# ✅ GET: Analyze with AI
@app.route("/instances/analyze", methods=["GET"])
def analyze_instances():
    try:
        instances = get_instances()
        results = []
        for instance in instances:
            analysis = analyze_instance(instance)
            combined = {**instance, **analysis}
            results.append(combined)
        return jsonify(results)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# ✅ POST: Start a GCE instance
@app.route("/instances/start", methods=["POST"])
def start_instance_route():
    try:
        data = request.get_json()
        name = data.get("name")
        if not name:
            return jsonify({"error": "Missing 'name'"}), 400
        result = start_instance(name)
        return jsonify({"status": result})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# ✅ GET fallback (optional but friendly)
@app.route("/instances/start", methods=["GET"])
def start_instance_info():
    return jsonify({
        "error": "❌ This endpoint only accepts POST requests with JSON body: {\"name\": \"instance-name\"}"
    }), 405

# ✅ POST: Stop a GCE instance
@app.route("/instances/stop", methods=["POST"])
def stop_instance_route():
    try:
        data = request.get_json()
        name = data.get("name")
        if not name:
            return jsonify({"error": "Missing 'name'"}), 400
        result = stop_instance(name)
        return jsonify({"status": result})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# ✅ GET fallback for /stop
@app.route("/instances/stop", methods=["GET"])
def stop_instance_info():
    return jsonify({
        "error": "❌ This endpoint only accepts POST requests with JSON body: {\"name\": \"instance-name\"}"
    }), 405

if __name__ == "__main__":
    app.run(debug=True)
