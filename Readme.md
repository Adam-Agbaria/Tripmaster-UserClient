# TripMaster

TripMaster is a comprehensive travel management application designed to enhance your travel planning experience. It allows users to search for flights, hotels, and vacation packages, manage checklists, and receive notifications.

## Features

- **User Authentication**: Secure login and sign-up functionalities.
- **Flight Search**: Find and book flights easily.
- **Hotel Search**: Search for and book hotels.
- **Vacation Packages**: Browse and filter vacation packages.
- **Checklists**: Create and manage travel checklists.
- **Notifications**: Receive and view important notifications.
- **Navigation**: Seamless navigation through different activities with a bottom navigation bar.

## Getting Started

### Prerequisites

- Android Studio
- An Android device or emulator running Android 6.0 (Marshmallow) or higher

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/TripMaster.git
    ```
2. Open the project in Android Studio.
3. Build and run the project on an Android device or emulator.

## Usage

### User Authentication

- **Login**: Enter your email and password to log in. If your account's avatar starts with "D", you will be shown a dialog indicating that you are banned.
- **Sign Up**: Create a new account by providing the required details.

### Bottom Navigation

- **Home**: Navigate to the home screen.
- **Flights**: Search for flights.
- **Notifications**: View your notifications.
- **Checklists**: Manage your travel checklists.

### Search Flights

- Enter your travel details to search for flights.
- View and select from the list of available flights.

### Search Hotels

- Enter your destination and other preferences to search for hotels.
- View and select from the list of available hotels.

### Vacation Packages

- Browse through available vacation packages.
- Use the filter button to open a dialog and apply filters such as destination, stars, connection flights, price range, and date range.
- View and select from the list of filtered packages.

### Notifications

- View all your notifications in a list.

### Checklists

- Create and manage your travel checklists.
- Add items to your checklist and mark them as completed.

## Code Structure

### Activities

- `LoginActivity`: Handles user login.
- `SignupActivity`: Handles user sign-up.
- `HomeActivity`: Displays the home screen.
- `SearchFlightsActivity`: Allows users to search for flights.
- `SearchHotelsActivity`: Allows users to search for hotels.
- `DisplayAllVacationPackagesActivity`: Displays and filters vacation packages.
- `NotificationActivity`: Displays user notifications.
- `DisplayCheckListActivity`: Manages user checklists.

### Adapters

- `PackagesAdapter`: Binds vacation package data to the RecyclerView.
- `NotificationAdapter`: Binds notification data to the RecyclerView.

### Models

- `User`: Represents a user.
- `BoundaryObject`: Represents a boundary object.
- `BoundaryCommand`: Represents a command boundary.
- `CheckList`: Represents a checklist.
- `CheckListItem`: Represents an item in a checklist.
- `VacationPackage`: Represents a vacation package.
- `Notification`: Represents a notification.

### Network

- `RetrofitClient`: Configures Retrofit for network requests.
- `ObjectService`: Defines API endpoints for object-related operations.
- `CommandService`: Defines API endpoints for command-related operations.
- `UserService`: Defines API endpoints for user-related operations.
- `ObjectServiceImpl`: Implements object-related API calls.
- `CommandServiceImpl`: Implements command-related API calls.

## Scraper Server

TripMaster includes a scraper server to search for flight and hotel data. The scraper is implemented in Python using Flask.

### Flight Search Scraper

The flight search scraper accesses flight data from booking websites:

- **Flask**: The web framework used to handle HTTP requests.
- **Selenium**: A browser automation tool used to scrape data from dynamic web pages.

### Hotel Search Scraper

The hotel search scraper accesses hotel data from booking websites:

- **Flask**: The web framework used to handle HTTP requests.
- **BeautifulSoup**: A library used to parse HTML and extract data.

## Endpoints

### User Endpoints

- **Create User**
  - **URL**: `superapp/users`
  - **Method**: `POST`
  - **Description**: Creates a new user.
  - **Body**:
    ```json
    {
      "email": "user@example.com",
      "username": "username",
      "avatar": "avatar_url",
      "role": "user"
    }
    ```

- **Update User**
  - **URL**: `superapp/users/{superapp}/{email}`
  - **Method**: `PUT`
  - **Description**: Updates user information.
  - **Body**:
    ```json
    {
      "username": "new_username",
      "avatar": "new_avatar_url",
      "role": "user"
    }
    ```

- **Get User by ID**
  - **URL**: `superapp/users/login/{superapp}/{email}`
  - **Method**: `GET`
  - **Description**: Retrieves user information by email.

### Object Endpoints

- **Create Object**
  - **URL**: `superapp/objects`
  - **Method**: `POST`
  - **Description**: Creates a new object.
  - **Body**:
    ```json
    {
      "objectId": {
        "superapp": "superapp_name",
        "id": "object_id"
      },
      "type": "object_type",
      "alias": "object_alias",
      "location": {
        "lat": 0.0,
        "lng": 0.0
      },
      "active": true,
      "creationTimestamp": "timestamp",
      "createdBy": {
        "userId": {
          "superapp": "superapp_name",
          "email": "creator_email"
        }
      },
      "objectDetails": {}
    }
    ```

- **Update Object**
  - **URL**: `superapp/objects/{superapp}/{id}`
  - **Method**: `PUT`
  - **Description**: Updates an existing object.
  - **Body**:
    ```json
    {
      "type": "new_object_type",
      "alias": "new_object_alias",
      "location": {
        "lat": 0.0,
        "lng": 0.0
      },
      "active": true,
      "creationTimestamp": "new_timestamp",
      "createdBy": {
        "userId": {
          "superapp": "superapp_name",
          "email": "creator_email"
        }
      },
      "objectDetails": {}
    }
    ```

### Command Endpoints

- **Create Command**
  - **URL**: `superapp/miniapp/{miniAppName}`
  - **Method**: `POST`
  - **Description**: Creates a new command.
  - **Body**:
    ```json
    {
      "commandId": {
        "superapp": "superapp_name",
        "miniapp": "miniapp_name",
        "id": "command_id"
      },
      "command": "command_name",
      "targetObject": {
        "objectId": {
          "superapp": "superapp_name",
          "id": "target_object_id"
        }
      },
      "invocationTimestamp": "timestamp",
      "invokedBy": {
        "userId": {
          "superapp": "superapp_name",
          "email": "invoker_email"
        }
      },
      "commandAttributes": {}
    }
    ```

- **Find Objects by Creator Email and Type**
  - **URL**: `superapp/miniapp/userApp`
  - **Method**: `POST`
  - **Description**: Finds objects created by a specific email and type.
  - **Body**:
    ```json
    {
      "commandId": {
        "superapp": "tripMaster",
        "miniapp": "userApp",
        "id": "unique_command_id"
      },
      "command": "findObjectsByCreatorEmailAndType",
      "targetObject": {
        "objectId": {
          "superapp": "tripMaster",
          "id": "4893f3dd-ceec-4d9e-992a-fae8f08137eb"
        }
      },
      "invocationTimestamp": "timestamp",
      "invokedBy": {
        "userId": {
          "superapp": "tripMaster",
          "email": "invoker_email"
        }
      },
      "commandAttributes": {
        "email": "creator_email",
        "type": "object_type",
        "page": 0,
        "size": 10
      }
    }
    ```


## Contributing

1. Fork the repository.
2. Create your feature branch:
    ```sh
    git checkout -b feature/your-feature
    ```
3. Commit your changes:
    ```sh
    git commit -m 'Add some feature'
    ```
4. Push to the branch:
    ```sh
    git push origin feature/your-feature
    ```
5. Open a pull request.
