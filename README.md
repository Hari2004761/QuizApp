🌟 QuizApp — Full-Stack Learning & Quiz Platform

A modern learning platform built with:

-   React (Login, Signup, Dashboard UI)
-   Spring Boot Authentication Service (SQL Server)
-   Spring Boot Quiz Dashboard Service (H2 Database)

This project includes two backend services + one frontend working
together.

📛 Tech Stack

Java, Spring Boot, React, SQL Server, H2, Maven

📁 Project Structure

```
QuizApp/
│
├── auth-backend/              # Spring Boot Login/Signup API (SQL Server)
│   └── src/main/java/com/example/QuizAppApplication.java
│
├── quiz-backend/              # Spring Boot Dashboard + Quiz API (H2)
│   └── src/main/java/com/hari/quizappdashboard/QuizAppDashboardApplication.java
│
├── frontend/                  # React application (port 3000)
│
├── pom.xml                    # Root Maven configuration
└── README.md
```


🚀 How to Run the Project

You must run three apps:

1)  Authentication Backend (Spring Boot + SQL Server)
2)  Quiz Dashboard Backend (Spring Boot + H2)
3)  React Frontend

🔹 1) Start the Quiz Dashboard Backend (quiz-backend)

Run:
quiz-backend/src/main/java/com/hari/quizappdashboard/QuizAppDashboardApplication.java

Port: 8081
H2 Console: http://localhost:8081/h2-console

🔹 2) Start the Authentication Backend (auth-backend)

Run: auth-backend/src/main/java/com/example/QuizAppApplication.java

Required environment variables:

DB_HOST=localhost
DB_PORT=1433
DB_NAME=UserLoginDB
DB_USERNAME=yourUsername
DB_PASSWORD=yourPassword

🔹 3) Start the React Frontend

cd frontend
npm install
npm start

Frontend URL → http://localhost:3000
Auth API → http://localhost:8080/api/auth
Dashboard API → http://localhost:8081/

🔐 4) Workflow for Logging In

1.  Start auth-backend
2.  Start quiz-backend
3.  Start frontend
4.  Open http://localhost:3000
5.  Login
6.  Use dashboard

🛠 Troubleshooting

Ports, database errors, CORS issues, login failures.
