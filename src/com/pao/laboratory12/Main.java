package com.pao.laboratory12;

import com.pao.laboratory12.model.*;
import com.pao.laboratory12.repository.*;
import com.pao.laboratory12.service.AuditService;
import com.pao.laboratory12.service.LibraryService;
import com.pao.laboratory12.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class Main {

    private static void initDb() throws Exception {
        String schema = """
            DROP TABLE IF EXISTS loan;
            DROP TABLE IF EXISTS book;
            DROP TABLE IF EXISTS reader;
            DROP TABLE IF EXISTS author;
            
            CREATE TABLE author (
                id      INTEGER PRIMARY KEY AUTOINCREMENT,
                name    VARCHAR(200) NOT NULL,
                country VARCHAR(100)
            );
            
            CREATE TABLE book (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                title     VARCHAR(300) NOT NULL,
                author_id INTEGER NOT NULL,
                available INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY (author_id) REFERENCES author(id)
            );
            
            CREATE TABLE reader (
                id    INTEGER PRIMARY KEY AUTOINCREMENT,
                name  VARCHAR(200) NOT NULL,
                email VARCHAR(200)
            );
            
            CREATE TABLE loan (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                book_id     INTEGER NOT NULL,
                reader_id   INTEGER NOT NULL,
                loan_date   VARCHAR(20) NOT NULL,
                return_date VARCHAR(20),
                FOREIGN KEY (book_id)   REFERENCES book(id),
                FOREIGN KEY (reader_id) REFERENCES reader(id)
            );
        """;
        
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            for (String sql : schema.split(";")) {
                if (!sql.trim().isEmpty()) {
                    stmt.execute(sql.trim());
                }
            }
        }
        System.out.println("Baza de date a fost initializata cu succes.");
    }

    public static void main(String[] args) throws Exception {
        // Initializare baza de date ca sa functioneze totul direct din cod fara server sql
        initDb();
        
        AuditService audit = AuditService.getInstance();
        AuthorRepository authorRepo = new AuthorRepository();
        BookRepository bookRepo     = new BookRepository();
        ReaderRepository readerRepo = new ReaderRepository();
        LoanRepository loanRepo     = new LoanRepository();
        LibraryService libraryService = LibraryService.getInstance();

        System.out.println("=== BIBLIOTECA JDBC — Demo Lab12 ===\n");

        // ---- Actiunea 1: Adauga autor ----
        Author author = new Author("Gabriel Garcia Marquez", "CO");
        authorRepo.save(author);
        audit.log("add_author");
        System.out.println("1. Autor adaugat: " + author);

        // ---- Actiunea 2: Adauga carte ----
        Book book1 = new Book("100 de ani de singuratate", author.getId());
        Book book2 = new Book("Dragostea in vremea holerei", author.getId());
        bookRepo.save(book1);
        bookRepo.save(book2);
        audit.log("add_book");
        System.out.println("2. Carti adaugate: " + book1 + ", " + book2);

        // ---- Actiunea 3: Adauga cititor ----
        Reader reader = new Reader("Ion Popescu", "ion.popescu@email.com");
        readerRepo.save(reader);
        audit.log("add_reader");
        System.out.println("3. Cititor adaugat: " + reader);

        // ---- Actiunea 4: Listeaza toate cartile ----
        List<Book> allBooks = bookRepo.findAll();
        audit.log("list_books");
        System.out.println("4. Toate cartile (" + allBooks.size() + "):");
        allBooks.forEach(b -> System.out.println("   " + b));

        // ---- Actiunea 5: Cauta carte dupa id ----
        bookRepo.findById(book1.getId()).ifPresentOrElse(
            b -> System.out.println("5. Carte gasita: " + b),
            () -> System.out.println("5. Carte negasita.")
        );
        audit.log("find_book_by_id");

        // ---- Actiunea 6: Actualizeaza carte ----
        book1.setTitle("100 de ani de singuratate (Ed. speciala)");
        bookRepo.update(book1);
        audit.log("update_book");
        System.out.println("6. Carte actualizata: " + book1);

        // ---- Actiunea 7: Imprumuta carte (TRANZACTIE) ----
        long loanId = libraryService.borrowBook(reader.getId(), book1.getId());
        audit.log("borrow_book");
        System.out.println("7. Imprumut creat cu ID=" + loanId);
        
        // Demo Rollback (daca incerci sa imprumuti aceeasi carte nedisponibila)
        try {
            libraryService.borrowBook(reader.getId(), book1.getId());
        } catch (Exception e) {
            System.out.println("[ROLLBACK demonstrat] " + e.getMessage());
        }

        // ---- Actiunea 8: Returneaza carte (TRANZACTIE) ----
        libraryService.returnBook(loanId);
        audit.log("return_book");
        System.out.println("8. Carte returnata.");

        // ---- Actiunea 9: Raport imprumuturi active cu JOIN ----
        List<String> activeLoans = libraryService.getActiveLoansWithDetails();
        audit.log("report_active_loans");
        System.out.println("9. Imprumuturi active: " + (activeLoans.isEmpty() ? "niciun" : ""));
        activeLoans.forEach(s -> System.out.println("   " + s));

        // ---- Actiunea 10: Sterge cititor ----
        readerRepo.delete(reader.getId());
        audit.log("delete_reader");
        System.out.println("10. Cititor sters cu ID=" + reader.getId());

        System.out.println("\n=== Demo finalizat. Verifica audit.csv ===");
        DatabaseConnection.getInstance().close();
    }
}
