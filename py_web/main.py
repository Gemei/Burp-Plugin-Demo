import sqlite3
import base64
from flask import Flask, request, redirect, url_for, render_template, jsonify
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

app = Flask(__name__)

# Symmetric encryption key (must be 32 bytes for AES-256)
encryption_key = b'H3lloW0rldH3lloW0rldH3lloW0rld!!'

# Initialize SQLite database
def init_db():
    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL,
            password TEXT NOT NULL
        )
    ''')
    # Insert default user if not exists
    cursor.execute('SELECT * FROM users WHERE username=?', ('gemei',))
    if cursor.fetchone() is None:
        cursor.execute('INSERT INTO users (username, password) VALUES (?, ?)', ('gemei', 'P@ssw0rd'))
    conn.commit()
    conn.close()

init_db()

@app.route("/", methods=["GET"])
def main():
    return render_template("login.html", encryption_key=base64.urlsafe_b64encode(encryption_key).decode())

@app.route('/login', methods=["POST"])
def login():
    from cryptography.hazmat.primitives.ciphers.aead import AESGCM
    import json

    try:
        body = request.get_json()
        encrypted_b64 = body['enc_payload']
        decoded_json = base64.b64decode(encrypted_b64).decode()
        data = json.loads(decoded_json)

        iv = bytes(data['iv'])
        ciphertext = bytes(data['ciphertext'])

        aesgcm = AESGCM(encryption_key)
        decrypted_data = aesgcm.decrypt(iv, ciphertext, None)
        credentials = json.loads(decrypted_data.decode())
        username = credentials.get("username")
        password = credentials.get("password")
    except Exception:
        return "Decryption failed."

    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM users WHERE username=? AND password=?", (username, password))
    user = cursor.fetchone()
    conn.close()

    if user:
        return "Logged in successfully for user {}".format(username)
    else:
        return "Invalid credentials for user {}".format(username)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8070)
