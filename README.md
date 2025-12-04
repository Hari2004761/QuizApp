# QuizApp

This project contains two Spring Boot services and a React frontend. The quickest way to run everything from IntelliJ is outlined below.

## 1) Run the dashboard/backend service (Spring Boot)
- Open the repo in IntelliJ and import the Maven project from the root `pom.xml`.
- Run the main class `src/main/java/com/hari/quizappdashboard/QuizAppDashboardApplication.java`.
- The app starts on port **8080** by default and serves the quiz dashboard pages and APIs that the frontend links to.

## 2) Run the authentication API service (Spring Boot)
- In IntelliJ, also open the secondary Maven module under `backend/pom.xml`.
- Run `backend/src/main/java/com/example/QuizAppApplication.java` to start the auth API that the React login/signup forms call at `http://localhost:8080/api/auth`.
- By default it uses environment variables for the SQL Server connection; set `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD` in your run configuration or `.env`.

> If both Spring Boot apps use the same port, start only the one you need or change one of them via `server.port` in the corresponding `application.properties`.

## 3) Run the React frontend
- In a terminal (inside IntelliJ or external), go to `frontend/` and run `npm install` followed by `npm start`.
- The React dev server runs on port **3000**; it calls the auth API at `http://localhost:8080/api/auth` and links to the dashboard pages on `http://localhost:8080/`.

## 4) Logging in and using the app
1. Start the auth API (step 2) so login/signup works.
2. Start the dashboard service (step 1) so the dashboard pages are available.
3. Start the React app (step 3), open `http://localhost:3000`, and sign up or log in. On success you are sent to the dashboard links served by the Spring Boot app.

## Troubleshooting
- **Port already in use:** Stop other services or set a unique `server.port` in `src/main/resources/application.properties` for each Spring Boot app.
- **Database connection errors:** The auth API expects a reachable SQL Server with the credentials you configure. For quick testing you can point it to a local or containerized SQL Server instance.
- **Frontend API errors:** Ensure the auth API is running on `localhost:8080` (or update the frontend URLs in `frontend/src/components/LoginForm.jsx` and `frontend/src/components/SignupForm.jsx`).
