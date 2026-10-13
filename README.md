# History Explorer — NIT3213 Assignment 2

An Android application that authenticates users through the NIT3213 API,
retrieves historical events, and displays information about a selected event.

## Student and Project

- Student ID: 8161359
- Project name: 8161359Assignment2
- Package: com.example.a8161359assignment2
- Class location: Sydney

## Features

### Login
- Student ID and case-sensitive first-name password inputs.
- Input validation.
- Authentication through the Sydney login endpoint.
- Loading indicator and error messages.
- Navigation to the Dashboard after successful authentication.

### Dashboard
- Fetches events using the keypass returned by login.
- Displays events in a RecyclerView.
- Each card shows the event name, years, location, and key figure.
- Loading, empty, and error states.
- Retry button after a failed request.
- Selecting a card opens the Details screen.

### Details
- Displays all information about the selected event:
  event name, start year, end year, location, key figure, and description.
- Scrollable layout.
- Back button to return to the Dashboard.
- Uses the selected event data without making another API request.

## Technology and Dependencies

- Kotlin
- Android Views and XML layouts
- View Binding
- AndroidX ViewModel and Lifecycle
- Kotlin coroutines and StateFlow
- Hilt dependency injection
- Retrofit and Gson converter
- RecyclerView, ListAdapter, and DiffUtil
- Material Components
- JUnit 4 and kotlinx-coroutines-test

Dependency versions are declared in:
- gradle/libs.versions.toml
- app/build.gradle.kts

## Architecture

The application uses an MVVM-style structure with a repository layer:

UI → ViewModel → AppRepository → ApiService → API

Hilt supplies the Retrofit client, API service, and repository.
ViewModels expose loading, success, and error states through StateFlow.
Activities collect those states and update their views.

The Details screen displays data passed from the Dashboard.

## Project Structure

```text
app/src/main/java/com/example/a8161359assignment2/
├── AssignmentApplication.kt
├── MainActivity.kt
├── data/
│   ├── model/
│   ├── remote/
│   └── repository/
├── di/
└── ui/
    ├── login/
    ├── dashboard/
    └── details/

app/src/test/java/com/example/a8161359assignment2/testing/
├── MainDispatcherRule.kt
├── FakeAppRepository.kt
├── LoginViewModelTest.kt
└── DashboardViewModelTest.kt
```

## Requirements

- Android Studio compatible with this project's Android Gradle Plugin.
- Android SDK Platform 37.
- Android device or emulator running Android 7.0 (API 24) or later.
- Internet access for dependency downloads and API requests.
- Use Android Studio's compatible bundled Gradle JDK.

The project includes the Gradle wrapper; a separate Gradle installation is
not required.

## Build and Run

1. Clone the repository:

   ```bash
   git clone https://github.com/nashibrana25-code/8161359Assignment2.git
   ```

2. Open the cloned 8161359Assignment2 folder in Android Studio.
3. Allow Gradle sync to finish.
4. Install SDK components requested by Android Studio.
5. Select an emulator or connected Android device.
6. Run the app configuration.

To build the debug APK from the project root:

Windows:

```powershell
.\gradlew.bat assembleDebug
```

macOS/Linux:

```bash
bash ./gradlew assembleDebug
```

## Login Instructions

Use credentials registered with the assignment API:

- Username: student ID without the letter s.
- Password: the registered first name, using the correct case.

Credentials are entered at runtime and are not included in this README.
A successful login returns a keypass used for the Dashboard request.

## API

Base URL:

```text
[https://nit3213apinew.onrender.com/](https://nit3213apinew.onrender.com/)
```

Authentication:

```http
POST /sydney/auth
Content-Type: application/json
```

Request format:

```json
{
  "username": "YOUR_STUDENT_ID",
  "password": "YourRegisteredFirstName"
}
```

Dashboard:

```http
GET /dashboard/{keypass}
```

The history response contains an entities array and entityTotal.
Each history entity contains:

- event
- startYear
- endYear
- location
- keyFigure
- description

## Unit Tests

The ViewModel tests use a fake AppRepository and controlled coroutine
dispatchers. They do not require the live API or an emulator.

### LoginViewModel — 7 tests
- Empty inputs.
- Student ID containing a letter.
- Successful login and preservation of password case.
- Network failure.
- Empty keypass response.
- Duplicate login attempts while loading.
- Clearing the keypass after navigation is handled.

### DashboardViewModel — 8 tests
- Successful data retrieval.
- Missing keypass.
- Network failure.
- Avoiding duplicate loads after success.
- Forced refresh.
- Duplicate requests while loading.
- Successful retry after failure.
- Successful empty response.

All 15 ViewModel tests passed in the local development run.

Run local unit tests from the project root:

Windows:

```powershell
.\gradlew.bat testDebugUnitTest
```

macOS/Linux:

```bash
bash ./gradlew testDebugUnitTest
```

Alternatively, right-click the testing package in Android Studio and select
Run Tests.

## Manual Checks

- Login with valid and invalid credentials.
- Dashboard loading and event display.
- Opening different events in Details.
- Returning from Details to Dashboard.
- Screen rotation.
- Network failure and retry.

## Limitations

- Login and Dashboard depend on the external assignment API being available.
- There is no persistent offline cache.
- The app does not persist the login session across a fresh launch.
- Unit tests cover ViewModel logic, not automated end-to-end UI interaction.