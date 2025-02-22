</head>
<body>

  <!-- Title Section -->
  <h1 align="center" id="title">Story of Bangkit</h1>
  <p align="center">
    <img src="https://socialify.git.ci/Wakbarr/Story-of-Bangkit-App/image?font=Inter&amp;name=1&amp;pattern=Circuit+Board&amp;theme=Auto" alt="project-image">
  </p>
  <p id="description">
    A mobile application that allows users to share stories interactively. The app integrates authentication, story listing, new story addition, maps integration, paging, testing, and animation features to provide a dynamic and responsive user experience.
  </p>

  <!-- Features Section -->
  <h1>🧐 Features</h1>
  <p>Here're some of the project's best features:</p>

  <h2>Core Features</h2>

  <h3>User Authentication</h3>
  <ul>
    <li>
      <strong>Login Page:</strong> Requires user inputs for email and password.
    </li>
    <li>
      <strong>Registration Page:</strong> Requires user inputs for name, email, and password (with the password input obscured for security).
    </li>
    <li>
      <strong>Custom EditText View:</strong> Implements real-time validation on the password field. An error message is displayed immediately if the password is shorter than 8 characters without needing to switch forms or click a button.
    </li>
    <li>
      <strong>Session and Token Management:</strong> User session data and tokens are securely stored. The app automatically navigates a logged-in user to the main page, while others are redirected to the login page.
    </li>
    <li>
      <strong>Logout Functionality:</strong> Available on the main page. Upon logout, both the token and session information are removed.
    </li>
  </ul>

  <h3>Story Listing</h3>
  <ul>
    <li>
      <strong>Main Story Listing:</strong> Displays a list of stories retrieved from the API, showing mandatory information such as the user name and associated photo.
    </li>
    <li>
      <strong>Story Detail View:</strong> Selecting a story item opens a detailed view containing the user name, photo, and description.
    </li>
  </ul>

  <h3>Add Story</h3>
  <ul>
    <li>
      <strong>Add Story Feature:</strong> A dedicated page for adding new stories is available. Users can select a photo from their gallery and provide a story description.
    </li>
    <li>
      <strong>Upload Process:</strong> After submitting the new story, the application returns to the story list page with the latest story appearing at the top.
    </li>
  </ul>

  <h3>Animations</h3>
  <ul>
    <li>
      <strong>Animation Implementation:</strong> The application incorporates one of the following animation types: Property Animation, Motion Animation, or Shared Element Transition. The specific animation type and its location within the app are documented in the Student Note.
    </li>
  </ul>

  <h3>Maps Integration</h3>
  <ul>
    <li>
      <strong>Display Maps:</strong> A dedicated page displays a map with markers or custom icons representing stories that include location data.
    </li>
    <li>
      <strong>Data Retrieval:</strong> Stories with location (latitude and longitude) are retrieved using an API parameter (e.g., <a href="https://story-api.dicoding.dev/v1/stories?location=1" target="_blank">?location=1</a>).
    </li>
  </ul>

  <h3>Paging List</h3>
  <ul>
    <li>
      <strong>Paging Implementation:</strong> The story list is implemented using Paging 3 to efficiently load data and handle large datasets.
    </li>
  </ul>

  <h3>Testing</h3>
  <ul>
    <li>
      <strong>Unit Testing:</strong> Unit tests are implemented for ViewModel functions that fetch paging data.
    </li>
    <li>
      <strong>Test Scenarios:</strong>
      <ol>
        <li>
          <em>When data is successfully loaded:</em>
          <ul>
            <li>Ensure that the data is not null.</li>
            <li>Ensure that the data count matches the expected number.</li>
            <li>Ensure that the first item in the returned data is correct.</li>
          </ul>
        </li>
        <li>
          <em>When there is no data:</em>
          <ul>
            <li>Ensure that the returned data count is zero.</li>
          </ul>
        </li>
      </ol>
    </li>
  </ul>

  <!-- Installation Steps -->
  <h2>🛠️ Installation Steps</h2>
  <ol>
    <li>
      <strong>Prerequisites:</strong>
      <ul>
        <li>Android Studio (latest version)</li>
        <li>Android SDK (API Level 21 or higher)</li>
        <li>Git</li>
      </ul>
    </li>
    <li>
      <strong>Clone the Repository:</strong>
      <pre><code>git clone https://github.com/yourusername/story-app.git</code></pre>
    </li>
    <li>
      <strong>Open the Project in Android Studio:</strong>
      <p>Launch Android Studio, select <em>File &gt; Open...</em>, and navigate to the cloned repository folder. Allow Android Studio to import the project and sync the Gradle files.</p>
    </li>
    <li>
      <strong>Sync and Build:</strong>
      <p>Click <em>File &gt; Sync Project with Gradle Files</em> to resolve dependencies, then build the project using <em>Build &gt; Make Project</em>.</p>
    </li>
    <li>
      <strong>Run the Application:</strong>
      <p>Connect an Android device or start an emulator from AVD Manager, then click the <em>Run</em> button to install and launch the app.</p>
    </li>
  </ol>

  <!-- Contribution Guidelines -->
  <h2>🍰 Contribution Guidelines</h2>
  <h3>Contribution Guidelines</h3>
  <p>Thank you for your interest in contributing to Story App! We welcome contributions from developers of all levels. Please follow the guidelines below to ensure a smooth collaboration.</p>
  <ol>
    <li>
      <strong>Fork the Repository:</strong> Click the <em>Fork</em> button on the top-right of this repository to create a copy under your GitHub account.
    </li>
    <li>
      <strong>Clone Your Forked Repository:</strong>
      <pre><code>git clone https://github.com/YOUR_GITHUB_USERNAME/story-app.git</code></pre>
      Replace <code>YOUR_GITHUB_USERNAME</code> with your actual GitHub username.
    </li>
    <li>
      <strong>Create a New Branch:</strong>
      <pre><code>git checkout -b feature/your-feature-name</code></pre>
      Use a descriptive branch name (e.g., <code>feature/user-authentication</code> or <code>bugfix/login-issue</code>).
    </li>
    <li>
      <strong>Make Your Changes:</strong>
      <p>Ensure your code follows best practices and the project structure. Keep code modular and reusable. Follow coding conventions for Java/Kotlin and XML.</p>
    </li>
    <li>
      <strong>Test Your Changes:</strong>
      <p>Run the app on an emulator or real device to verify that the changes work as expected. Write unit tests or UI tests if applicable.</p>
    </li>
    <li>
      <strong>Commit Your Changes:</strong>
      <pre><code>git commit -m "Add user authentication validation for login and register"</code></pre>
      Write meaningful commit messages following conventional commit message formats.
    </li>
    <li>
      <strong>Push Your Changes to GitHub:</strong>
      <pre><code>git push origin feature/your-feature-name</code></pre>
    </li>
    <li>
      <strong>Create a Pull Request (PR):</strong>
      <p>Go to the repository on GitHub, click the <em>New Pull Request</em> button, select your branch, and describe your changes in detail. Add screenshots or logs if relevant, then submit the PR and wait for review.</p>
    </li>
  </ol>

  <!-- Built With -->
  <h2>💻 Built with</h2>
  <p>Technologies used in the project:</p>
  <ul>
    <li><strong>Android SDK:</strong> The foundation for building and running the application on Android devices.</li>
    <li><strong>Kotlin/Java:</strong> Primary programming languages used for implementing the application logic, with Kotlin preferred for its concise syntax and modern features.</li>
    <li><strong>MVVM Architecture:</strong> Implemented using ViewModel, LiveData, and the Repository Pattern for a clear separation between UI and business logic.</li>
    <li><strong>Retrofit:</strong> A type-safe HTTP client for handling network operations and API calls.</li>
    <li><strong>Glide:</strong> A fast and efficient image loading library used for loading and caching images.</li>
    <li><strong>SharedPreferences:</strong> Utilized for secure storage and management of user sessions and authentication tokens.</li>
    <li><strong>ConstraintLayout &amp; Material Components:</strong> Used for building responsive and modern user interfaces that follow Material Design principles.</li>
    <li><strong>Coroutines (Kotlin):</strong> For handling asynchronous operations and background processing.</li>
    <li><strong>Custom Views:</strong> Custom EditText components for real-time validation feedback.</li>
    <li><strong>Animations:</strong> Enhancements using Shared Element Transitions, Property Animations, or Motion Animations for smooth UI transitions.</li>
    <li><strong>Paging 3:</strong> Used for efficiently loading and displaying large datasets in the story list.</li>
    <li><strong>Unit Testing:</strong> Implemented to validate ViewModel functions, ensuring data integrity and proper handling of paging scenarios.</li>
  </ul>

</body>
</html>
