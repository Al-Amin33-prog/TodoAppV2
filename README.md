# TodoAppV2 - Production-Ready Task Management App

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

## 📁 Project Structure
