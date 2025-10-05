import os
from dotenv import load_dotenv
from googleapiclient.discovery import build
from google.auth.exceptions import DefaultCredentialsError

# Load .env file
load_dotenv()

# Set GOOGLE_APPLICATION_CREDENTIALS env variable
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = os.getenv("GOOGLE_APPLICATION_CREDENTIALS")

# GCP project and zone
PROJECT = os.getenv("GCP_PROJECT_ID", "your-gcp-project-id")
ZONE = os.getenv("GCP_ZONE", "us-central1-a")

def get_gce_service():
    try:
        return build('compute', 'v1')
    except DefaultCredentialsError as e:
        raise RuntimeError("⚠️ Google credentials error: " + str(e))

def get_instances():
    try:
        service = get_gce_service()
        request = service.instances().list(project=PROJECT, zone=ZONE)
        response = request.execute()
        instances = response.get('items', [])
        return instances
    except Exception as e:
        print(f"❌ Failed to fetch instances: {e}")
        return []

def start_instance(name):
    try:
        service = get_gce_service()
        request = service.instances().start(project=PROJECT, zone=ZONE, instance=name)
        response = request.execute()
        return f"✅ Start request sent for instance: {name}"
    except Exception as e:
        return f"❌ Failed to start instance '{name}': {e}"

def stop_instance(name):
    try:
        service = get_gce_service()
        request = service.instances().stop(project=PROJECT, zone=ZONE, instance=name)
        response = request.execute()
        return f"✅ Stop request sent for instance: {name}"
    except Exception as e:
        return f"❌ Failed to stop instance '{name}': {e}"

# For quick testing
if __name__ == "__main__":
    print("🔍 Fetching GCE instances:")
    instances = get_instances()
    for ins in instances:
        print(f"➡️ {ins['name']} - {ins['status']}")
