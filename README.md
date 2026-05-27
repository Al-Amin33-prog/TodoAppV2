![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-brightgreen)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange)
# TodoAppV2 - Modern Android Task Management App

A **modern, production-grade** Android todo management application built with **Kotlin** and **Jetpack Compose**. This project demonstrates industry best practices in architecture, reactive programming, and user-centric design.

## 🎯 Key Features

- **Multi-Subject Task Organization** - Organize tasks by subjects with custom colors
- **Smart Task Management** - Filter, search, group, and categorize tasks efficiently
- **Firebase Authentication** - Secure user authentication with email verification & password reset
- **Real-Time Statistics** - Track productivity metrics and weekly completion trends
- **Task Reminders** - Scheduled notifications for upcoming and overdue tasks
- **Offline-First Architecture** - Full functionality with local database (Room)
- **Pull-to-Refresh** - Seamless data synchronization
- **Reactive UI** - Real-time updates with StateFlow-based state management

## 🛠️ Architecture & Tech Stack

### Architecture Pattern
- **MVVM** with **Single-Source-of-Truth Repository Pattern**
- Clear separation of concerns: Data → Domain → Presentation layers
- Event-driven state management with Kotlin Flow & StateFlow

### Technology Stack
| Component | Technology |
|-----------|-----------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Declarative) |
| **Database** | Room (Local, Offline-first) |
| **Backend Services** | Firebase (Auth + Firestore) |
| **Dependency Injection** | Hilt |
| **Async Programming** | Kotlin Coroutines & Flow |
| **Navigation** | Jetpack Compose Navigation |

## 🧪 Testing Strategy

- **Unit Tests** - ViewModels with `kotlinx-coroutines-test` for deterministic state verification
- **Fake Repositories** - Comprehensive testing with dependency mocking
- **StateFlow Verification** - Reactive state management validation
- **Integration Tests** - End-to-end authentication flows with real Firebase

## 📊 Project Highlights

✨ **Production-Ready Code**
- Proper error handling with custom exceptions
- Session management & secure token storage
- Validation logic across all features

✨ **Scalable Architecture**
- Modular feature organization (auth, task, subject, dashboard, statistics)
- Dependency injection for testability
- Reusable composition patterns

✨ **User Experience**
- 24 minimum SDK compatibility (Android 7.0+)
- Target Android 15 (SDK 35)
- Material Design 3 components
- Loading states & error handling

  ## 💡 Why I Built This

I built TodoAppV2 to deepen my understanding of modern Android development practices beyond basic CRUD applications.

The project helped me explore:
- scalable MVVM architecture
- reactive state management with StateFlow
- navigation complexity in Jetpack Compose
- offline-first data handling
- Firebase authentication workflows
- clean dependency injection with Hilt

A major focus of the project was learning how to structure Android applications in a way that remains maintainable as features grow.

## ⚡ Challenges Faced

Some notable engineering challenges solved during development:

- Refactoring nested navigation graphs between root and app shell navigation
- Managing selection state and batch task deletion
- Preventing recomposition-related UI inconsistencies
- Designing reusable Compose UI components
- Handling task restoration with Snackbar undo flows
- Synchronizing reactive UI state with Room + Flow

  ## 🚀 Getting Started

### Prerequisites

Before running the project, make sure you have:

- Android Studio Hedgehog or newer
- Android SDK 24+
- Kotlin support enabled
- A Firebase project configured

---

### Installation

1. Clone the repository

```bash
git clone https://github.com/your-username/TodoAppV2.git
```

2. Open the project in Android Studio

3. Create a Firebase project from Firebase Console

4. Add an Android app to Firebase using your package name:

```text
com.example.todoappv2
```

5. Download the `google-services.json` file

6. Place the file inside the app module:

```text
app/google-services.json
```

7. Sync Gradle and run the app

---

### Firebase Features Used

- Firebase Authentication
- Cloud Firestore

---

### Build Configuration

```gradle
minSdk = 24
targetSdk = 35
```

## 📦 APK

If you'd like to test the app quickly without building from source, install the latest APK from the releases section.

## 📁 Project Structure
