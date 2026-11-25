# RAVLINE — Car Selling Management System

Beginner console project using Core Java, JDBC and SQLite. **MySQL and Workbench are not used.** SQLite stores the database in a local file; there is no database server to install or start. Exactly four Java source files and one SQL file are included. Use JDK 17 or newer.

## Project files

```text
RAVLINE/
├── database.sql
├── ravline.db
├── lib/sqlite-jdbc-3.54.1.0.jar
├── .vscode/settings.json
├── README.md
└── src/ravline/
    ├── Main.java
    ├── Car.java
    ├── Customer.java
    └── Database.java
```

Complete source: [Main.java](src/ravline/Main.java), [Car.java](src/ravline/Car.java), [Customer.java](src/ravline/Customer.java), [Database.java](src/ravline/Database.java), [database.sql](database.sql).

## SQLite and JDBC setup

The project ZIP includes `ravline.db` with 25 sample cars, 10 sample customers, and 4 sample completed sales already loaded. View All Cars and View Sold Cars show data immediately; several cars remain available to demonstrate search, update and sale options. At any prompt, enter **B** to cancel the current action and return to the main menu. In View All Cars and Search Car, enter **N** for next and **P** for previous car. Menu option 10, **Purchase Car**, lets you confirm a purchase for a registered customer. You do not need to install SQLite or run `database.sql` just to start the project. The SQL file also includes the sample rows and is safe to run again because it uses `INSERT OR IGNORE`.

The ZIP includes `lib/sqlite-jdbc-3.54.1.0.jar`, the JDBC driver Java needs to connect to the included database file. VS Code is configured to load JAR files from `lib`.

Optional: if you want the SQLite command-line tool to inspect or recreate the database, download the Windows command-line tools ZIP from the [official SQLite downloads page](https://www.sqlite.org/download.html), then run from the project directory:

```cmd
cd /d "C:\path\to\RAVLINE"
sqlite3 ravline.db ".read database.sql"
sqlite3 ravline.db ".tables"
```

The tables shown should be `cars`, `customers`, `sales`. This creates the database file if it does not exist. The `CREATE TABLE IF NOT EXISTS` commands do not erase existing data.

## Compile and run with CMD

Simplest: open the RAVLINE folder in VS Code and run `.\run.bat` in the PowerShell terminal. It compiles the files and starts the program without the Java native-access warning. You can also click Run in VS Code.

To compile manually from the RAVLINE project folder:

```cmd
mkdir out
javac -cp ".;lib\sqlite-jdbc-3.54.1.0.jar" -d out src\ravline\*.java
java --enable-native-access=ALL-UNNAMED -cp "out;lib\sqlite-jdbc-3.54.1.0.jar" ravline.Main
```

Windows uses `;` between classpath entries. Start the program from the project folder so it finds `ravline.db` there.

## Open and run in Apache NetBeans

1. In NetBeans choose **File → New Project → Java with Ant → Java Application**. Name the project `RAVLINE` and select JDK 17 or newer. Clear the generated main-class option if offered.
2. Under **Source Packages**, create package `ravline` if needed. Copy the four Java source files into that package, replacing any generated `Main.java`.
3. Keep the included `ravline.db` in the project folder alongside `database.sql`.
4. Right-click **Libraries → Add JAR/Folder**, select `lib/sqlite-jdbc-3.54.1.0.jar` and confirm.
5. Right-click `Main.java` → **Run File**. If asked, choose `ravline.Main`.

## File explanations

- **Main.java:** prints the nine-option menu, reads/validates input, calls database methods and displays friendly errors.
- **Car.java:** car model with private fields, constructors, getters/setters and `toString()`.
- **Customer.java:** customer model and contact information.
- **Database.java:** SQLite connection, prepared SQL operations, queries, transaction for sale, and resource closing.
- **database.sql:** creates cars, customers and sales tables with primary/foreign keys, checks, and sample data. `ravline.db` is the ready-to-use populated database file.

## Java syllabus concept map

| Topic | Where used/shown |
|---|---|
| Classes, objects, constructors, methods, `this` | `Car` and `Customer`; field assignment in their constructors |
| `static` | Shared `Main.INPUT`; static menu/database methods |
| Inheritance, abstraction, interfaces, polymorphism | Not needed for this simple CRUD program; explain these separately in your viva. |
| Arrays, String pool, StringBuilder/StringBuffer, wrappers | Not needed for the car workflow; prepare these as syllabus concepts for viva. |
| Exceptions | `SQLException` with `throws`, `try/catch` in the menu, parsing catches and unchecked `IllegalArgumentException` |
| Collections, iterators and sorting | Not needed for the database workflow; SQL rows are read through `ResultSet`. Explain the collection use cases separately. |
| Multithreading | Not needed in this single-user console program; explain thread concepts separately in viva. |
| JDBC | Database.java uses DriverManager, Connection, Statement, PreparedStatement, ResultSet, executeQuery/executeUpdate and parameter binding |

Thread lifecycle viva: a thread can be NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING or TERMINATED. `start()` creates scheduled work and the JVM calls `run()`; calling `run()` directly does not start a new thread. A synchronized block protects a critical section. `wait()` releases its monitor; `notify()` wakes one waiter and `notifyAll()` wakes all, which must reacquire the monitor and check the condition again (usually in a loop).

## Viva questions and answers

1. **What is JDBC?** Java's standard API for communicating with a database.
2. **What does DriverManager do?** Opens a connection using a JDBC URL and registered driver.
3. **Why PreparedStatement?** Binds values safely and separately from SQL text.
4. **executeQuery vs executeUpdate?** SELECT returns a ResultSet; INSERT/UPDATE/DELETE return an affected-row count.
5. **Why try-with-resources?** It closes JDBC objects automatically, even on errors.
6. **What is a foreign key?** A rule requiring a referenced car/customer row to exist.
7. **How is duplicate car sale prevented?** In a transaction the program conditionally changes AVAILABLE to SOLD before inserting the sale; SQLite serializes writers, and UNIQUE on `sales.car_id` blocks a second sale. Any error rolls back.
8. **Why a transaction?** Sale insertion and status update should succeed or be undone together.
9. **`==` vs `equals()` for strings?** Reference identity versus text content.
10. **What is encapsulation?** Keeping model fields private and exposing controlled methods.
11. **What is polymorphism?** Calling an overridden method using a parent/interface reference.
12. **Checked vs unchecked exception?** Checked exceptions must be caught/declared; unchecked exceptions do not require it at compile time.
13. **What is a SQLite database file?** A local file containing the database; this project uses `ravline.db`.
14. **What are commit and rollback?** Commit keeps transaction changes; rollback cancels uncommitted changes.

Price is recorded as the car's listed price after user confirmation. Email validation is intentionally a basic beginner-level check.



