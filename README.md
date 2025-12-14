# SmartShop - Stock and Sales Management App

## Project Overview

SmartShop is a modern Android application designed for efficient stock and sales management. It serves as a comprehensive final semester project, demonstrating a real-world application built with cutting-edge Android development technologies and best practices. The app allows users to manage a product inventory, with all data seamlessly synchronized between a local database and a cloud backend.

### Core Features

- **Product Management**: Full CRUD (Create, Read, Update, Delete) functionality for products.
- **Local Persistence**: A robust local database ensures the app is fully functional offline.
- **Cloud Sync**: Real-time, two-way data synchronization with Firebase Firestore.
- **Statistics Dashboard**: A real-time dashboard displaying key inventory metrics like total product count and total stock value.

## Architecture & Technical Stack

This project strictly follows the official Android recommended architecture, ensuring a clean, scalable, and maintainable codebase.

- **Language**: 100% [Kotlin](https://kotlinlang.org/)
- **UI**: Built entirely with [Jetpack Compose](https://developer.android.com/jetpack/compose), Android's modern declarative UI toolkit.
- **Architecture**: Model-View-ViewModel (MVVM)
- **Local Database**: [Room](https://developer.android.com/jetpack/androidx/releases/room) for robust, offline-first data persistence.
- **Cloud Database**: [Firebase Firestore](https://firebase.google.com/docs/firestore) for real-time, cloud-based data storage and synchronization.
- **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/flow.html) for managing background tasks and reactive data streams.
- **Dependency Injection**: Manual DI via an `AppContainer` for simplicity and clarity.

### MVVM Architecture Explained

The application is divided into three primary layers:

1.  **UI Layer (Compose Screens)**: The UI is composed of stateless composables that observe state from the ViewModels. It is responsible for displaying data and forwarding user events (`ProductListScreen`, `ProductDetailScreen`, `DashboardScreen`).

2.  **ViewModel Layer (`ViewModel`s)**: The ViewModels (`ProductListViewModel`, etc.) act as the bridge between the UI and the data layer. They hold the UI state (using `StateFlow`), handle user events, and call the repository to perform business logic. They are completely unaware of the UI.

3.  **Data Layer (`Repository`)**: The `ProductRepository` is the single source of truth for all application data. It abstracts the data sources from the rest of the app. The ViewModels only interact with the repository, without knowing whether the data is coming from a local cache or a remote server.

### Room & Firestore Two-Way Synchronization

The repository (`ProductRepositoryImpl`) implements a sophisticated two-way data sync strategy:

- **Remote to Local (Listening)**: The repository launches a coroutine that listens to a real-time snapshot of the `products` collection in Firestore. Whenever data changes in the cloud, the repository automatically updates the local Room database using an `upsert` operation. Since the UI observes a `Flow` from Room, any remote changes are instantly reflected on the screen.

- **Local to Remote (Writing)**: When a user saves or deletes a product, the repository follows a "remote-first" approach. It first sends the create, update, or delete operation to Firestore. Once that is complete, it performs the same operation on the local Room database. This ensures data consistency and provides an immediate UI update.

This architecture makes the application **offline-first**. All UI components read directly from the fast, local Room database. The repository handles all the complexity of background synchronization, providing a seamless and responsive user experience, whether online or offline.

## How to Set Up and Run

To build and run this project, you will need to set up your own Firebase project.

### Step 1: Clone the Repository

```sh
git clone <your-repository-url>
```

### Step 2: Set Up a Firebase Project

1.  Go to the [Firebase Console](https://console.firebase.google.com/).
2.  Click **"Add project"** and follow the on-screen instructions to create a new project.
3.  Once your project is created, register your Android app:
    - Click the **Android icon** (</>) to start the setup workflow.
    - **Package Name**: Enter `com.omar.smartshop`. This is critical and must match exactly.
    - **App Nickname**: (Optional) e.g., "SmartShop App"
    - **Debug Signing Certificate SHA-1**: (Optional for now) You can skip this.
4.  **Download `google-services.json`**: After registering the app, Firebase will provide a `google-services.json` file to download. 
5.  **Move the File**: Place the downloaded `google-services.json` file into the `app/` directory of your project (e.g., `SmartShop/app/google-services.json`).

### Step 3: Configure Firestore Database

1.  In the Firebase Console, go to the **"Firestore Database"** section from the left-hand menu.
2.  Click **"Create database"**.
3.  Choose to start in **"Test mode"**. This will allow open read/write access for development. **Note: For a production app, you would need to set up security rules.**
4.  Select a location for your database.
5.  Click **"Enable"**.

### Step 4: Build and Run

Open the project in Android Studio. It should sync and build automatically. You can now run the app on an emulator or a physical device.

Any products you add, edit, or delete in the app will now appear in your Firestore console in real-time, and any changes you make directly in the Firestore console will reflect in the app instantly.
