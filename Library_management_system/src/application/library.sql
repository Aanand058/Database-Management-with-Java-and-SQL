-- Ensure you're using the correct database
USE LibraryDB;

-- Drop tables if they exist (to avoid conflicts when re-running the script)
DROP TABLE IF EXISTS Loans;
DROP TABLE IF EXISTS Borrowers;
DROP TABLE IF EXISTS Books;

-- Create Books table
CREATE TABLE Books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL
);

-- Create Borrowers table
CREATE TABLE Borrowers (
    borrower_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Create Loans table
CREATE TABLE Loans (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    borrower_id INT NOT NULL,
    loan_date DATE NOT NULL,
    return_date DATE DEFAULT NULL,
    FOREIGN KEY (book_id) REFERENCES Books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (borrower_id) REFERENCES Borrowers(borrower_id) ON DELETE CASCADE
);

-- Insert sample data into Books
INSERT INTO Books (title, author, available) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', TRUE),
('1984', 'George Orwell', TRUE),
('To Kill a Mockingbird', 'Harper Lee', TRUE),
('Pride and Prejudice', 'Jane Austen', TRUE),
('The Catcher in the Rye', 'J.D. Salinger', TRUE);

-- Insert sample data into Borrowers
INSERT INTO Borrowers (name, email) VALUES
('Alice Johnson', 'alice@example.com'),
('Bob Smith', 'bob@example.com'),
('Charlie Brown', 'charlie@example.com'),
('Diana Prince', 'diana@example.com'),
('Edward Elric', 'edward@example.com');

-- Insert sample data into Loans
INSERT INTO Loans (book_id, borrower_id, loan_date, return_date) VALUES
(1, 1, '2025-06-01', '2025-06-10'),
(2, 2, '2025-06-03', NULL),
(3, 3, '2025-06-05', '2025-06-12'),
(4, 4, '2025-06-07', NULL),
(5, 5, '2025-06-09', '2025-06-15');

-- ✅ Verify table structures
DESCRIBE Books;
DESCRIBE Borrowers;
DESCRIBE Loans;

-- ✅ Verify data
SELECT * FROM Books;
SELECT * FROM Borrowers;
SELECT * FROM Loans;
