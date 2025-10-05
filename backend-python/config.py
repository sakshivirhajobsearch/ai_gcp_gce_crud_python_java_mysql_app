import os
from dotenv import load_dotenv

load_dotenv()

DB_HOST = os.getenv("DB_HOST", "localhost")
DB_USER = os.getenv("DB_USER", "root")
DB_PASS = os.getenv("DB_PASS", "")
DB_NAME = os.getenv("DB_NAME", "gce_db")

GCP_PROJECT = os.getenv("GCP_PROJECT")
GCP_ZONE = os.getenv("GCP_ZONE")
