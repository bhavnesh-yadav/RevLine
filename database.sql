-- Run this file while connected to ravline.db using the sqlite3 command-line tool.
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS cars (
    car_id INTEGER PRIMARY KEY AUTOINCREMENT,
    brand TEXT NOT NULL,
    model TEXT NOT NULL,
    year INTEGER NOT NULL CHECK (year BETWEEN 1886 AND 2100),
    price NUMERIC NOT NULL CHECK (price > 0),
    status TEXT NOT NULL DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'SOLD'))
);

CREATE TABLE IF NOT EXISTS customers (
    customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    phone TEXT NOT NULL,
    email TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS sales (
    sale_id INTEGER PRIMARY KEY AUTOINCREMENT,
    car_id INTEGER NOT NULL UNIQUE,
    customer_id INTEGER NOT NULL,
    sale_price NUMERIC NOT NULL CHECK (sale_price > 0),
    sale_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (car_id) REFERENCES cars(car_id) ON DELETE RESTRICT,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT
);

-- Sample inventory makes the project ready to demonstrate as soon as it opens.
-- INSERT OR IGNORE keeps existing rows safe when this setup script is run again.
INSERT OR IGNORE INTO cars (car_id, brand, model, year, price, status) VALUES
(1, 'Maruti Suzuki', 'Swift', 2022, 650000, 'AVAILABLE'),
(2, 'Hyundai', 'i20', 2021, 720000, 'AVAILABLE'),
(3, 'Tata', 'Nexon', 2023, 1050000, 'SOLD'),
(4, 'Honda', 'City', 2020, 950000, 'AVAILABLE'),
(5, 'Toyota', 'Innova Crysta', 2022, 1850000, 'AVAILABLE'),
(6, 'Mahindra', 'Thar', 2023, 1450000, 'AVAILABLE'),
(7, 'Kia', 'Seltos', 2022, 1320000, 'SOLD'),
(8, 'Renault', 'Kwid', 2021, 430000, 'AVAILABLE'),
(9, 'Volkswagen', 'Polo', 2019, 610000, 'AVAILABLE'),
(10, 'Skoda', 'Slavia', 2023, 1380000, 'AVAILABLE'),
(11, 'Maruti Suzuki', 'Baleno', 2022, 690000, 'AVAILABLE'),
(12, 'Hyundai', 'Creta', 2021, 1190000, 'SOLD'),
(13, 'Tata', 'Punch', 2023, 780000, 'AVAILABLE'),
(14, 'Honda', 'Amaze', 2020, 670000, 'AVAILABLE'),
(15, 'Toyota', 'Glanza', 2022, 820000, 'AVAILABLE'),
(16, 'Mahindra', 'XUV300', 2021, 890000, 'AVAILABLE'),
(17, 'Kia', 'Sonet', 2022, 980000, 'AVAILABLE'),
(18, 'MG', 'Hector', 2021, 1550000, 'AVAILABLE'),
(19, 'Nissan', 'Magnite', 2023, 760000, 'SOLD'),
(20, 'Maruti Suzuki', 'Brezza', 2023, 1020000, 'AVAILABLE'),
(21, 'Hyundai', 'Venue', 2022, 940000, 'AVAILABLE'),
(22, 'Tata', 'Altroz', 2021, 720000, 'AVAILABLE'),
(23, 'Toyota', 'Fortuner', 2020, 3250000, 'AVAILABLE'),
(24, 'Renault', 'Triber', 2022, 680000, 'AVAILABLE'),
(25, 'Skoda', 'Kushaq', 2023, 1420000, 'AVAILABLE');

INSERT OR IGNORE INTO customers (customer_id, name, phone, email) VALUES
(1, 'Aarav Sharma', '9876500001', 'aarav@example.com'),
(2, 'Diya Patel', '9876500002', 'diya@example.com'),
(3, 'Arjun Singh', '9876500003', 'arjun@example.com'),
(4, 'Ananya Gupta', '9876500004', 'ananya@example.com'),
(5, 'Vivaan Kumar', '9876500005', 'vivaan@example.com'),
(6, 'Isha Mehta', '9876500006', 'isha@example.com'),
(7, 'Reyansh Verma', '9876500007', 'reyansh@example.com'),
(8, 'Saanvi Rao', '9876500008', 'saanvi@example.com'),
(9, 'Aditya Nair', '9876500009', 'aditya@example.com'),
(10, 'Myra Joshi', '9876500010', 'myra@example.com');

INSERT OR IGNORE INTO sales (sale_id, car_id, customer_id, sale_price, sale_date) VALUES
(1, 3, 1, 1050000, '2026-01-12 10:30:00'),
(2, 7, 2, 1320000, '2026-02-04 14:15:00'),
(3, 12, 3, 1190000, '2026-03-19 11:00:00'),
(4, 19, 4, 760000, '2026-04-08 16:45:00');
