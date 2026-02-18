# Spring Boot News Aggregator API

This is a secure, RESTful API built with **Spring Boot** that aggregates personalized news articles from multiple external sources. It includes robust features for user authentication, managing preferences, and fetching a personalized news feed.

---

## ✨ Features

* **User Authentication:** Secure user registration and login using **JWT** (JSON Web Tokens).
* **Personalized Preferences:** Users can manage their news topic preferences.
* **Multi-Source Aggregation:** Fetches news from various external APIs (like NewsAPI, GNews, etc.) based on user preferences.
* **In-Memory Database:** Uses **H2** for simple, in-memory data storage, making it easy to run and test without an external database.
* **Robust Error Handling:** Provides clear and consistent error responses for invalid requests and authentication issues.

---

## 🚀 Getting Started

### Prerequisites

* Java 17 or higher
* Maven
* API keys from news providers like **NewsAPI.org** or **GNews API**.

### Configuration

1.  Clone the repository:

    ```bash
    git clone [your-repository-url]
    ```

2.  Navigate to the project directory:

    ```bash
    cd news-aggregator
    ```

3.  Open the `src/main/resources/application.properties` file and add your API keys and a secure JWT secret.

    ```properties
    jwt.secret=yourVerySecretKeyForJWTGenerationAndValidation
    newsapi.org.api-key=YOUR_NEWSAPI_ORG_KEY
    gnews.io.api-key=YOUR_GNEWS_IO_KEY
    ```

### Run the Application

```bash
mvn spring-boot:run
