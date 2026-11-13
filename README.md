# Transporter Assignment on Lanes (Spring Boot Backend)

A Spring Boot backend application designed to optimize the allocation of
transporters to shipping lanes across India. It satisfies three core
constraints:

-   **Cost minimization**
-   **Maximum transporter usage**, up to a user-defined limit
-   **Full lane coverage**

------------------------------------------------------------------------

## How to Run the Application

1.  **Clone or open the project** in your preferred IDE, such as
    IntelliJ IDEA or Eclipse.

2.  **Add the H2 database dependency** to your `pom.xml` if it is not
    already present:

    ``` xml
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    ```

3.  **Run the application** using Maven:

    ``` bash
    mvn spring-boot:run
    ```

    Alternatively, run the main application class,
    `TransportAssignmentApplication.java`, directly from your IDE.

The server starts on port `8080`.

------------------------------------------------------------------------

## Algorithm and Logic

The application uses a combinatorial optimization approach in the
service layer.

1.  **Combination generation:** Generates combinations of available
    transporters, subject to the specified `maxTransporters` constraint.
2.  **Lane coverage and cost calculation:** For each transporter subset,
    evaluates the cheapest available quote for every required lane.
3.  **Constraint validation:** Checks whether the selected transporter
    subset collectively covers **all required lanes**.
4.  **Optimal selection:** Tracks and returns the valid combination with
    the minimum `totalCost`.

------------------------------------------------------------------------

## API Endpoints and Testing

### 1. Submit Input Data

-   **Method:** `POST`
-   **Endpoint:** `/api/v1/transporters/inputRequest`
-   **Content-Type:** `application/json`
-   **Description:** Saves lanes and transporter quote lists to the
    database.

**Sample request body:**

``` json
{
  "lanes": [
    { "id": 1, "origin": "Chandigarh", "destination": "Shimla" },
    { "id": 2, "origin": "Agra", "destination": "Kanpur" },
    { "id": 3, "origin": "Varanasi", "destination": "Gorakhpur" },
    { "id": 4, "origin": "Amritsar", "destination": "Ludhiana" },
    { "id": 5, "origin": "Coimbatore", "destination": "Madurai" }
  ],
  "transporters": [
    {
      "id": 1,
      "name": "Transporter X",
      "laneQuotes": [
        { "laneId": 1, "quote": 1000 },
        { "laneId": 2, "quote": 1500 }
      ]
    },
    {
      "id": 2,
      "name": "Transporter Y",
      "laneQuotes": [
        { "laneId": 3, "quote": 2000 },
        { "laneId": 4, "quote": 1800 }
      ]
    },
    {
      "id": 3,
      "name": "Transporter Z",
      "laneQuotes": [
        { "laneId": 5, "quote": 1200 }
      ]
    },
    {
      "id": 4,
      "name": "Transporter W",
      "laneQuotes": [
        { "laneId": 1, "quote": 1100 },
        { "laneId": 4, "quote": 1750 }
      ]
    }
  ]
}
```

### 2. Get Optimized Results

-   **Method:** `POST`
-   **Endpoint:** `/api/v1/transporters/assignment`
-   **Content-Type:** `application/json`
-   **Description:** Runs the optimization logic using the stored data
    and the specified transporter limit.

**Sample request body:**

``` json
{
  "maxTransporters": 3
}
```

**Expected response structure:**

``` json
{
  "status": "success",
  "totalCost": 7500,
  "assignments": [
    { "laneId": 1, "transporterId": 1 },
    { "laneId": 2, "transporterId": 1 },
    { "laneId": 3, "transporterId": 2 },
    { "laneId": 4, "transporterId": 2 },
    { "laneId": 5, "transporterId": 3 }
  ],
  "selectedTransporters": [1, 2, 3]
}
```
