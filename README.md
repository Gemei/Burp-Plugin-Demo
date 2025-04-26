# Burp Suite Extension Demo with Montoya API & Java

This project demonstrates how to build a Burp Suite extension using the **Montoya API** and **Gradle** in **Java**.
 The Montoya API provides a modern interface for creating powerful and efficient extensions for Burp Suite.

## Prerequisites

- Java Development Kit (JDK) 17
- Gradle
- Burp Suite (Community or Professional Edition)
- Python3

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Gemei/burp-plugin-demo.git
```

### 2. Set Up the Burp Extension Project

Open your preferred Java IDE and import the directory `Request_Encryption_Handler_Demo` as a project3. Build the Extension

Use Gradle to build the project:

```bash
gradle clean build
```

The final JAR will be located under:

```
build/libs/Request_Encryption_Handler_Demo-1.0.jar
```

### 3. Load the Extension into Burp Suite

1. Open Burp Suite.
2. Go to the **Extender** tab → **Extensions** sub-tab.
3. Click **Add**.
4. Set **Extension Type** to **Java**.
5. Select your generated `.jar` file from the `build/libs/` directory.
6. Load the extension.

If everything is correct, Burp will show your extension as **Loaded Successfully**.

- **libs/**: Directory for external JARs like Montoya API (if needed).

### 4. Start the web application

Go to the `py_web` directory and run pip 

```bash
pip install requirements.txt
```

Then run the web application using `python3`

```bash
python3 main.py
```

## Resources

- [Official Montoya API Documentation](https://portswigger.net/burp/extender/api/montoya/)
- [Burp Suite Extension Development Guide](https://portswigger.net/burp/extender/writing-your-first-burp-extension)
- Original Guide by Hosam Gemei

## License

This project is licensed under the MIT License. See the [LICENSE](https://chatgpt.com/c/LICENSE) file for details.