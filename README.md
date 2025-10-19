# 🧾 Java Swing POS Invoice Application

A desktop-based **Point of Sale (POS) Invoice Management System** developed using **Java Swing** and **MySQL**.  
This project allows users to manage items, generate invoices, view invoice history, and export invoices to PDF.

---

## 🎯 Project Overview

This application was developed as part of the **Java Swing Developer Test**.  
It follows the **MVC architecture** and uses **FlatLaf** for modern UI styling.

### Modules
1. **Item Management**
   - Add, edit, delete items
   - Search items by name, code, or category
   - Store item details including multiple price types

2. **Invoice Management**
   - Create new invoices (Retail or Wholesale)
   - Add multiple items dynamically
   - Calculate subtotal, discount, and grand total
   - Save invoices and invoice items to the database

3. **Invoice History**
   - View all invoices
   - Filter by date range or invoice number
   - View invoice details
   - Cancel invoices (mark as Cancelled, not deleted)
   - Export invoice as **PDF**

4. **User Login**
   - Simple login system with credentials stored in database
   - Redirects to the Dashboard upon successful login

---

## ⚙️ Technologies Used

| Component | Technology |
|------------|-------------|
| **Language** | Java 22 |
| **UI Framework** | Swing + FlatLaf |
| **Database** | MySQL 8+ |
| **Build Tool** | Maven |
| **PDF Generation** | iText 7 |
| **Calendar Picker** | JCalendar (toedter) |

---

## 📂 Project Structure
src/
├── main/java/com/pos/ <br/>
│ ├── controller/ → Business logic (ItemController, InvoiceController, etc.)<br/>
│ ├── dao/ → Database operations (JDBC)<br/>
│ ├── model/ → Data models (Item, Invoice, User, etc.)<br/>
│ ├── util/ → Utilities (DB connection, ThemeManager, PDFGenerator)<br/>
│ └── view/ → Swing forms (Dashboard, Invoice, Items, Login)<br/>
│<br/>
└── resources/ → Icons, UI resources (if any)<br/>

---

## 💡 How to Run the Application
### Prerequisites
<ul>
<li>JDK 22 or later</li>
<li>MySQL Server 8.0+</li>
<li>Maven 3.9+</li>
<li>IDE: IntelliJ IDEA / NetBeans / Eclipse</li>
</ul>

### Steps

**1. Clone or extract the project:** <br/>
    ```
    git clone <repository_url>
    ```<br/><br/>
**2. Import into your IDE as a Maven project.** <br/><br/>
**3. Configure database connection in:** <br/>
    ```
    src/main/java/com/pos/util/DBConnection.java
    ```<br/>
    ```
    private static final String URL = "jdbc:mysql://localhost:3306/pos_db";
    ```<br/>
    ```
    private static final String USER = "root";
    ```<br/>
    ```
    private static final String PASSWORD = "your_password_here";
    ```<br/><br/>

**4. Build project:** <br/>
    ```
    mvn clean install
    ```<br/>

**5. Run the app:** <br/>
    ```
    Main class: com.pos.Main
    ```<br/>
    ```
    Or directly run: Main.java
    ```
