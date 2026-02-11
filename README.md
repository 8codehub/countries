# CountriesApp 🌍

A simple Android application that consumes the **REST Countries API** and demonstrates clean architecture, offline persistence, and modern Android development practices.

This project was implemented as part of an **Android interview assignment** under a **strict 3-hour time limit**.

---

## ✅ Features

### 🌍 Countries List
- Displays a scrollable list of countries
- Shows:
  - Country flag
  - Country name

### 📌 Country Details
- Displays detailed country information:
  - Flag
  - Name
  - Capital
  - Currencies

### 🔍 Search
- Local search by country name
- Filtering is performed on the already loaded dataset
- Debounced and distinct query handling

### 💾 Offline Support
- Country data is fetched once from the API
- Persisted locally using **Room**
- UI always observes local database updates via **Flow**

### 🌐 Internationalization Ready
- UI strings are externalized and separated
- A dedicated module exists for content and string resources
- The project is structured to support localization without architectural changes

---

## 🌐 Localization & Translations (Note)

The **REST Countries API (v3.1)** does not provide a locale-based query mechanism (for example `?lang=es`).

Although country name translations are available in the API response (`nativeName`, translation maps), integrating full localization requires additional mapping and fallback logic.

Due to the **strict 3-hour time constraint**, full country name localization was **intentionally not implemented**.

However:
- A dedicated **`content` module** was created to host string resources
- UI text is fully decoupled from business logic
- Localization can be added later without refactoring existing layers

This demonstrates awareness of internationalization concerns while keeping the implementation focused on the assignment’s core requirements.

---

## 🧠 Architecture

The application follows **Clean Architecture + MVVM**, with clear separation of concerns:

- **data**  
  - Retrofit API
  - Room database
  - Repository implementations

- **domain**  
  - Use cases
  - Repository interfaces
  - Business logic

- **ui**  
  - ViewModels
  - Compose screens
  - UI state

- **core**  
  - Shared utilities (dispatchers, base components)

- **content**  
  - String resources
  - Localization-ready module

---

## 🧩 Modules Overview

| Module    | Responsibility                                          |
|-----------|---------------------------------------------------------|
| `core`    | Shared utilities and base architectural components      |
| `domain`  | Business logic, use cases, repository contracts         |
| `data`    | API, Room database, repository implementations          |
| `ui`      | Screens, ViewModels, UI state                           |
| `content` | Strings and localization-ready resources                |

---

## ⏳ Note on Feature Modules

In a production-scale application, the preferred approach would be to introduce fully separated **feature-based modules** such as:

- `countries-list`
- `country-details`
- `search`

Due to the limited scope and time constraints of this assignment, the implementation focuses on demonstrating clean architecture fundamentals rather than additional modular complexity.

The current setup still ensures:
- Clear layer separation
- High maintainability
- Easy future extensibility

---

## 🧪 Testing

Unit tests were written for the most important layers.

### ViewModel Tests
- `CountriesListViewModelTest`
  - Query updates
  - Retry logic
  - Refresh triggering
  - Debounce and distinct filtering behavior

- `CountryDetailsViewModelTest`
  - Loading state handling
  - Null safety
  - Error handling
  - Restart logic

### Use Case Tests
- `ObserveCountriesUseCaseTest`
- `ObserveCountryDetailsUseCaseTest`

Use cases are tested to verify correct delegation to the repository layer.

---

## 🧰 Tech Stack

- Kotlin
- Coroutines + Flow
- Jetpack Compose
- Jetpack ViewModel
- Room Database
- Retrofit + Moshi
- Hilt Dependency Injection
- Turbine + MockK for testing
- Modular Clean Architecture

---

## 🚀 Future Improvements

Given more time, the following enhancements could be added seamlessly:

- Localized country names based on device locale
- Feature-based UI modules
- Improved offline strategy (cache invalidation, background refresh)
- Enhanced UI animations and transitions
- Better empty and error state visuals

The current architecture supports all of the above without requiring refactoring.

---

## 📌 Notes

- Flag images are **not persisted manually**
- Flags are displayed using the provided image URL
- Image caching is handled by the image loading library

---

## 👤 Author

Developed by **Hovsep Arakelyan**  
Senior Android Developer

---