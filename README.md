# 📱 TriLoc-X: Object-Based Distance Estimation App

TriLoc-X is a **camera-based Android app** that allows users to **estimate the distance between themselves and a known object** in real-time, using only the device’s camera and the real-world height of the object.

## 🔍 What It Does

- Opens the **camera view** on launch.
- Allows the user to **manually draw a bounding box** around a visible object.
- Asks for the **real-world height** of the selected object (in meters).
- Calculates the **distance between the user and the object** using a pinhole camera model.

## 🎯 Who It's Made For

- Developed for **indoor or outdoor navigation** where GPS doesn’t work reliably (e.g. inside buildings, warehouses, etc.).
- Useful for **robotics, drone operators, or visually impaired navigation systems**.
- Built as part of an **internship-level demonstration project** to show skills in **camera APIs, math-based calculations, and Android development**.

## 🧠 How It Works (Logic)

We use the **pinhole camera model**:

> `Distance = (Real Height × Focal Length × Image Height in Pixels) / (Bounding Box Height in Pixels × Sensor Height)`

Where:
- **Real Height** = height of object entered by the user (in meters)
- **Focal Length** = extracted from the camera hardware (in mm)
- **Sensor Height** = assumed based on typical smartphone cameras (e.g., 4.8 mm)
- **Bounding Box Height** = selected by the user by drawing a box around the object

## 🛠️ How We Built It

- **Language**: Java (Android)
- **Camera API**: [CameraX](https://developer.android.com/training/camerax)
- **UI**: XML layouts with `PreviewView`, `Buttons`, `EditText`
- **Permission Handling**: Runtime camera permission check
- **Drawing Overlay**: Custom `OverlayView` for user interaction
- **Calculation**: Real-world trigonometry + focal length logic
- 
🚀 Features

- Real-time camera feed
- Manual object selection
- Accurate focal length extraction
- Distance output with visual overlay
- Clean and functional UI

 📈 Future Scope

- Automatic object detection (TensorFlow Lite or MLKit)
- Use of angle + multiple objects for **2D/3D triangulation**
- Indoor navigation and augmented reality integration


👨‍💻 Developed At
🎓 Aerial Delivery Research & Development Establishment (ADRDE), DRDO, Ministry Of Defence
🗓️ Internship Duration: June–July 2025  
👨‍🔬 Role: Android Developer Intern  
🧠 Focus Areas: Indoor localization, trigonometry-based calculation, user-centered app design

👨‍💻 Developer

Developed by Tapendra Verma
B.Tech 3rd Year Student 
Jaypee University Of Engineering & Technology, Guna Madhya Pradesh
Feel free to connect and explore more projects!



