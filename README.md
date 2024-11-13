# TestApp About Random Users

This project is a TestApp that fetches random user data using the 
[Random User API](https://randomuser.me/). 
It leverages the following technologies and frameworks:

- **Kotlin** for Android development
- **Gradle** for build automation
- **Jetpack Compose** for UI
- **Dagger Hilt** for dependency injection
- **Coroutines and Flow** for asynchronous programming
- **Room** for local database management
- **Retrofit** for network requests

## Project Structure

The project is organized into the following modules:

- `app`: Handles the overall application setup, including navigation.
- `feature/user`: Contains the user-related features including presentation, view models, and UI components.
- `data`: Contains data handling logic including API services and local database.
- `domain`: Contains business logic and use cases.
- `common`: Contains shared utilities, resources, and components.


## Key Components

### UserListScreen

Displays a list of users with their details and allows marking users as favorites.

### UsersListViewModel

Handles the business logic for fetching and displaying the list of users.

### UserDetailsScreen

Displays detailed information about a selected user.

### UserDetailsViewModel

Handles the business logic for fetching and displaying user details.

## Backend Services

This project uses the [Random User API](https://randomuser.me/) to fetch user data. 
The API provides random user data for various purposes, such as testing and prototyping.

## Getting Started

### Prerequisites

- Android Studio Koala Feature Drop | 2024.1.2 Patch 1
- JDK 11 or higher

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/marin-hen/RandomUserApp.git
    ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.

### Running the App

1. Connect an Android device or start an emulator.
2. Click on the "Run" button in Android Studio.

### Testing

To run the tests, use the following command in the terminal:
```sh
./gradlew test
