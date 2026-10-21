# Smart Pantry Manager

An Android application (built entirely in Java) designed to reduce food waste. The app allows users to track the ingredients they currently have 
in their pantry and suggests recipes they can cook using **strictly** those leftover ingredients - no shopping trip required.

## Features
* **Pantry Management:** Full CRUD (Create, Read, Update, Delete) functionality for ingredients, including name, quantity, unit, and optional expiry date.
* **Recipe Collection:** Pre-loaded with 15 recipes stored locally on the device.
* **Strict-Matching Algorithm:** The app suggests recipes *only* if every required ingredient is present in the pantry in sufficient quantities. 
* It features normalization logic to handle plurals (e.g., "tomatoes" vs "tomato") and unit variations (e.g., "grams" vs "g").
* **Settings:** User preferences (like Expiring-Soon alerts) are saved persistently across sessions.

## Database Implementation
**Database Option Chosen: SQLite via Room Persistence Library.**
*Why?*
I chose Room because it provides a robust, local, offline-first solution. It eliminates the need for a backend server, ensuring the app is fast and 
the user's data remains private on their device. Room also provides compile-time verification of SQL queries and seamless integration with 
Java, making it the most reliable choice for a local inventory system.

## App Architecture & Tech Stack
* **Language:** Java
* **UI:** XML Layouts, RecyclerView, Material Design Components
* **Navigation:** Intents and Toolbars
* **Data Persistence:** Room (SQLite) for pantry/recipes, SharedPreferences for user settings.

## Setup & Run Instructions
1. Clone this repository to your local machine:
   `git clone https://github.com/Lesedi-9/SmartPantryManager.git`
2. Open the project in **Android Studio**.
3. Allow Gradle to sync all dependencies.
4. Run the app on an Android Emulator or a physical device (Minimum SDK: API 24).
5. The database will automatically seed itself with 20 recipes on the very first launch.

## Author
**Lesedi**
Mobile App Development 700