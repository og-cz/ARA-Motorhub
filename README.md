<div align="center">
  <img src="sql/readme/main-logo.png" alt="ARA Motorhub Logo"/>
</div>

---

> **find** your  
> motor **needs.**

<!--
## Table of Contents

-  [Project Overview](#project-overview)
-  [How to Run](#how-to-run)
-  [Technologies Used](#technologies-used)
-  [Features](#features)
  -  [Admin Side](#admin-side)
  -  [Customer Side](#customer-side)
  -  [Seller Side](#seller-side)
  -  [User Settings](#user-settings)
-  [Developer](#developer)
-->

---

## Project Overview

**ARA Motorhub** is a full-featured desktop application designed to streamline motorcycle parts inventory and order management. Built using **JavaFX** and managed with **Maven**, it offers customers a modern UI to browse and request parts, while providing sellers and admins with robust tools for managing products, users, and store data. The system runs on a **local MySQL database** powered by **XAMPP** and phpMyAdmin.

>  Created as a final requirement for my Advanced Database Systems class

---

<br>

<div align="center">
  <img src="sql/readme/aragif.gif" alt="ARA Motorhub Animated Logo" width="300"/>
</div>

<br>
<br>

<div align="center">
  <img src="sql/readme/2.png" alt="ARA Motorhub Seller Showcase" width="800"/>
</div>

<br>
<br>

<div align="center">
  <img src="sql/readme/3.png" alt="ARA Motorhub Admin Showcase" width="800"/>
</div>

<br>
<br>
<br>

<div align="center">
  <img src="sql/readme/1.png" alt="ARA Motorhub UI/UX Showcase" width="800"/>
</div>

---

## How to Run

> **Note:** Make sure Java 17+ and Maven are installed. JavaFX libraries are automatically handled by Maven.

### Prerequisites:

- Java JDK 17+
- Apache Maven (i have maven 8.9.11)
- MySQL Server / XAMPP

### Steps to Run:

1. **Clone the repository:**

   ```bash
   git clone https://github.com/OG-CZ/aramotorhub.git
   cd aramotorhub
   ```

2. **Setup Database:**

- Import the SQL file from /sql-database/ into phpMyAdmin or MySQL Workbench.

3. **Configure Database Connection:**

- Open DatabaseConnection.java under:
  ```bash
  src/com/ogcz/app/database/DatabaseConnection.java
  ```
- Update with your local database credentials (host, db name, user, password).

4. **Run the app using Maven:**

   ```bash
    mvn clean javafx:run
   ```

5. ** Enjoy!**

---

## Technologies Used

- Java Programming Language
- JavaFX (UI Framework)
- Maven (Build & Dependency Management)
- FXML (Declarative UI)
- CSS for Styling
- SceneBuilder (for designing UI visually)
- MySQL (Database)
- XAMPP with phpMyAdmin (Local DB Server)

---

## Features

### Admin Side

- Full CRUD operations:
  - Manage Admins, Users, Stores
  - Manage Inventory, Products, Categories
- Stock Management: No Stock, Available, Cart, etc.

### Customer Side

- Step 1 & 2 Registration
- Customer Home Pages
- Buying Process:
  - Search & Filter Products
  - Add to Cart (with Spinner Quantity)
  - View Seller Shops
  - Checkout with Card Payment

### Seller Side

- Search & Filter Own Products
- Sales Dashboard (Informative Analytics)
- Manage Inventory
- Upload & Edit Products
- View Other Products & Shops (for reference only)
- Cannot Purchase Products (seller account restricted from buying)

### User Settings

- Edit Profile Information
- Delete Account
- Log Out

---



## Developer

<div align="center">
  <table>
    <tr>
      <td width="200" align="center" valign="top">
        <img src="sql/readme/ogcz.png" alt="Carl Zeus Anastacio" width="120" style="border-radius:50%;"/>
        <p>
          <strong>OGCZ</strong><br/>
          <em>Project Lead • Project Manager • UI/UX Designer • Front-end Developer • Back-end Developer • Documentation • Database Designer</em>
        </p>
      </td>
  </table>
</div>

