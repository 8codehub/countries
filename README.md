# CountriesApp 🌍

A simple Android application that consumes the **Rest Countries API** and provides:

- A list of countries
- Country details screen
- Search functionality
- Offline persistence with Room
- Clean Architecture + modular design
- Unit tests for core business logic

---

## ✅ Features

### 🌍 Countries List Screen

- Displays a scrollable list of countries
- Shows:
    - Country flag
    - Country name

### 📌 Country Details Screen

- Displays detailed information including:
    - Flag
    - Name
    - Capital
    - Currencies

### 🔍 Search

- Users can search countries by name
- Filtering is performed locally on the already loaded list

### 🌐 Internationalization Ready

- The project structure makes it easy to add translations using Android string resources

### 💾 Offline Support

- Countries are cached locally after the first successful fetch
- Stored using Room persistence

---

## ✅ Offline & Caching Approach

- The full countries list is fetched once from the API
- Data is stored in the local database via Room
- UI always observes database updates through Flow

### Flags Note

There is **no special logic** to download and persist flag images as files.

Flags are displayed directly using the provided URL, relying on the image loading library cache.

---

## ✅ Architecture

The app follows **Clean Architecture + MVVM**, with a modular separation:

- **data** → API + Room + repository implementations
- **domain** → use cases + repository interfaces
- **ui** → ViewModels + screens + UI models
- **core** → shared utilities (dispatchers, base ViewModel, mappers)

---

## ✅ Modules Overview

| Module    | Responsibility                                          |
|-----------|---------------------------------------------------------|
| `core`    | Base architecture utilities and shared components       |
| `domain`  | Business logic, use cases, repository contracts         |
| `data`    | Room database, Retrofit API, repository implementations |
| `ui`      | Screens, ViewModels, UI state and models                |
| `content` | To store Strings                                        |

### ⏳ Note on Feature Modules

In a real production-scale application, the ideal approach would be to structure the project using
fully separated **feature-based modules** (e.g. `countries-list`, `country-details`, `search`,
etc.).

However, since this project was developed under a strict time limit as part of an interview
assignment, the implementation focuses on the required functionality and clean architecture
fundamentals rather than introducing additional feature module complexity.

The current modular setup still ensures clear separation between:

- UI layer
- Domain/business logic
- Data sources and persistence
- Shared core utilities

This keeps the codebase maintainable, scalable, and easy to extend further if needed.

---

## ✅ Testing

Unit tests were written for the most important layers:

### ViewModel Tests

- `CountriesListViewModelTest`
    - Query updates
    - Retry logic
    - Refresh triggering
    - Debounce + distinct filtering behavior

- `CountryDetailsViewModelTest`
    - Loading behavior
    - Null handling
    - Error handling
    - Restart logic

### Use Case Tests

- `ObserveCountriesListUseCaseTest`
- `ObserveCountryDetailsUseCaseTest`

Use cases are tested to ensure they correctly delegate to the repository layer.

---

## ✅ Tech Stack

- Kotlin
- Coroutines + Flow
- Jetpack ViewModel
- Room Database
- Retrofit
- Hilt Dependency Injection
- Turbine + MockK for testing
- Modular Clean Architecture

---
