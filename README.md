<!-- Title Section -->
  <h1 align="center" id="title">Story of Bangkit</h1>
  <p align="center">
    <img src="https://socialify.git.ci/Wakbarr/Story-of-Bangkit-App/image?font=Inter&amp;name=1&amp;pattern=Circuit+Board&amp;theme=Auto" alt="project-image">
  </p>
  <p id="description">
    A mobile application that allows users to share stories interactively. The app integrates authentication, story listing, new story addition, and animation features to provide a dynamic and responsive user experience.
  </p>

  <!-- Features Section -->
  <h1>🧐 Features</h1>
  <p>Here're some of the project's best features:</p>

  <h2>Core Features</h2>

  <h3>User Authentication</h3>
  <ul>
    <li>
      <strong>Login Page</strong> 
    </li>
    <li>
      <strong>Registration Page</strong> </em>
    </li>
    <li>
      <strong>Custom EditText View</strong> 
    </li>
    <li>
      <strong>Session and Token Management</strong>
    </li>
    <li>
      <strong>Logout Functionality</strong> 
    </li>
  </ul>

  <h3>Story Listing (List Story)</h3>
  <ul>
    <li>
      <strong>Main Story Listing:</strong> Displays a list of stories retrieved from the API.
    </li>
    <li>
      <strong>Story Detail View:</strong> Tapping on a story item brings up a detailed view containing: User Name, Photo , and Description .
    </li>
  </ul>

  <h3>Add Story</h3>
  <ul>
    <li>
      <strong>Add Story Feature:</strong> A dedicated page for adding new stories accessible from the story list. Required inputs include: Photo file (sourced from the gallery) and Story description 
    </li>
    <li>
      <strong>Upload Process:</strong> A button is provided to upload the story data to the server. Upon successful upload, the application returns to the story list page with the latest story appearing at the top.
    </li>
  </ul>

  <h3>Animations</h3>
  <ul>
    <li>
      <strong>Animation Implementation:</strong> The application incorporates one of the following animation types: Property Animation, Motion Animation, or Shared Element Transition. The specific animation type and its location within the app are documented in the Student Note.
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
      <p>Click <em>File &gt; Sync Project with Gradle Files</em> to resolve dependencies. Build the project using <em>Build &gt; Make Project</em>.</p>
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
    <li><strong>Kotlin/Java:</strong> Primary programming languages used for implementing the application logic. Kotlin is preferred for its concise syntax and modern features, while Java is also supported.</li>
    <li><strong>MVVM Architecture:</strong> Implemented using ViewModel, LiveData, and the Repository Pattern for a clear separation between UI and business logic.</li>
    <li><strong>Retrofit:</strong> A type-safe HTTP client for handling network operations and API calls.</li>
    <li><strong>Glide:</strong> A fast and efficient image loading library used for loading and caching images.</li>
    <li><strong>SharedPreferences:</strong> Utilized for secure storage and management of user sessions and authentication tokens.</li>
    <li><strong>ConstraintLayout &amp; Material Components:</strong> Used for building responsive and modern user interfaces that follow Material Design principles.</li>
    <li><strong>Coroutines (Kotlin):</strong> For handling asynchronous operations and background processing.</li>
    <li><strong>Custom Views:</strong> Custom EditText components for real-time validation feedback.</li>
    <li><strong>Animations:</strong> Enhancements using Shared Element Transitions, Property Animations, or Motion Animations for smooth UI transitions.
    </li>
  </ul>

</body>
</html>
