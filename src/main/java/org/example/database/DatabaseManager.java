package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // Veritabanı dosyasının bağlantı adresi (URL).
    // "jdbc:sqlite:" kısmı Java'ya SQLite sürücüsünü kullanmasını söyler.
    // "smartlibrary.db" ise dosyanın adıdır. Bu dosya projenin ana klasöründe oluşacak.
    private static final String DB_URL = "jdbc:sqlite:smartlibrary.db";

    /**
     * Veritabanına bir bağlantı (connection) açar.
     * @return Connection nesnesi döndürür. İşimiz bitince bu bağlantıyı kapatmayı unutmamalıyız.
     * @throws SQLException Bağlantı kurulamazsa hata fırlatır.
     */
    public static Connection connect() throws SQLException {
        // DriverManager, verdiğimiz URL'e bakar ve uygun sürücüyü (bizim eklediğimiz sqlite-jdbc) bulup bağlanır.
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Uygulama ilk açıldığında çalışır. Gerekli tablolar veritabanında yoksa oluşturur.
     */
    public static void initializeDatabase() {
        // --- SQL Tablo Oluşturma Komutları ---
        // IF NOT EXISTS: "Eğer yoksa oluştur" demektir. Varsa hata vermez, pas geçer.

        // 1. Kitaplar Tablosu
        String sqlBooks = """
                CREATE TABLE IF NOT EXISTS books (
                 id INTEGER PRIMARY KEY AUTOINCREMENT,
                 title TEXT NOT NULL,
                 author TEXT NOT NULL,
                 year INTEGER
                );
                """;

        // 2. Öğrenciler Tablosu
        String sqlStudents = """
                CREATE TABLE IF NOT EXISTS students (
                 id INTEGER PRIMARY KEY AUTOINCREMENT,
                 name TEXT NOT NULL,
                 department TEXT
                );
                """;

        // 3. Ödünç İşlemleri Tablosu
        // FOREIGN KEY kısımları, bu tablodaki kitap ve öğrenci ID'lerinin,
        // diğer tablolardaki gerçek kayıtlara bağlı olduğunu belirtir (Veri tutarlılığı için).
        String sqlLoans = """
                CREATE TABLE IF NOT EXISTS loans (
                 id INTEGER PRIMARY KEY AUTOINCREMENT,
                 bookId INTEGER NOT NULL,
                 studentId INTEGER NOT NULL,
                 dateBorrowed TEXT NOT NULL,
                 dateReturned TEXT,
                 FOREIGN KEY (bookId) REFERENCES books (id),
                 FOREIGN KEY (studentId) REFERENCES students (id)
                );
                """;

        // --- Try-with-resources Yapısı (Çok Önemli) ---
        // Parantez () içinde açılan Connection ve Statement nesneleri,
        // süslü parantez {} içindeki işleri bitince OTOMATİK olarak kapatılır.
        // Bu sayede açık bağlantı kalmaz ve bilgisayar kaynakları boşuna tüketilmez.
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // SQL komutlarını veritabanına gönder ve çalıştır.
            stmt.execute(sqlBooks);
            stmt.execute(sqlStudents);
            stmt.execute(sqlLoans);
            // System.out.println("Veritabanı tabloları hazır."); // İstersen bu yorumu açabilirsin.

        } catch (SQLException e) {
            // Bir hata olursa konsola kırmızı bir şekilde yazdır.
            System.err.println("Veritabanı başlatma hatası: " + e.getMessage());
        }
    }
}