package org.example.database;

import org.example.model.Book; // Modelimizi kullanacağımız için import ettik

import java.sql.*; // SQL işlemleri için gerekli kütüphaneler
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    // --- 1. Kitap Ekleme Metodu (INSERT) ---
    public void add(Book book) {
        // SQL Sorgusu: Değerlerin yerine soru işareti (?) koyuyoruz.
        // Buna "Yer Tutucu" (Placeholder) denir. Güvenlik için çok önemlidir.
        String sql = "INSERT INTO books(title, author, year) VALUES(?,?,?)";

        // Try-with-resources ile bağlantıyı ve ifadeyi açıyoruz (İş bitince otomatik kapanacaklar)
        try (Connection conn = DatabaseManager.connect();
             // PreparedStatement: Soru işaretlerini güvenli bir şekilde doldurmak için kullanılır.
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Soru işaretlerinin yerini sırayla (1'den başlayarak) dolduruyoruz.
            pstmt.setString(1, book.getTitle());  // 1. soru işareti -> Başlık
            pstmt.setString(2, book.getAuthor()); // 2. soru işareti -> Yazar
            pstmt.setInt(3, book.getYear());      // 3. soru işareti -> Yıl

            // Komutu çalıştır (Ekleme/Silme/Güncelleme için executeUpdate kullanılır)
            pstmt.executeUpdate();
            System.out.println("Kitap başarıyla eklendi: " + book.getTitle());

        } catch (SQLException e) {
            System.err.println("Kitap ekleme hatası: " + e.getMessage());
        }
    }

    // --- 2. Tüm Kitapları Listeleme Metodu (SELECT ALL) ---
    public List<Book> getAll() {
        List<Book> booksList = new ArrayList<>(); // Sonuçları koyacağımız boş bir liste
        String sql = "SELECT * FROM books"; // Tüm kitapları getir

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             // executeQuery: Okuma işlemleri için kullanılır. Sonuç 'ResultSet' olarak döner.
             ResultSet rs = stmt.executeQuery(sql)) {

            // rs.next(): "Sırada başka kayıt var mı?" diye bakar. Varsa true döner ve o kayda geçer.
            // Tüm kayıtlar bitene kadar bu döngü döner.
            while (rs.next()) {
                // O anki satırdaki sütun verilerini al
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");

                // Bu verilerle bir Java Nesnesi (Book) oluştur
                // (Burada ID'li constructor'ı kullanıyoruz)
                Book book = new Book(id, title, author, year);

                // Oluşan nesneyi listeye ekle
                booksList.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Kitapları listeleme hatası: " + e.getMessage());
        }
        // Dolu listeyi geri döndür
        return booksList;
    }

    // --- 3. ID ile Tek Kitap Bulma Metodu (SELECT BY ID) ---
    public Book getById(int idToFind) {
        String sql = "SELECT * FROM books WHERE id = ?"; // Sadece o ID'ye sahip olanı getir
        Book foundBook = null; // Başlangıçta bulamadık varsayıyoruz (null)

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idToFind); // Soru işaretini aradığımız ID ile doldur

            // Try-with-resources içinde ResultSet'i de açabiliriz, böylece o da otomatik kapanır.
            try (ResultSet rs = pstmt.executeQuery()) {
                // if (rs.next()): Sadece tek bir kayıt beklediğimiz için 'while' yerine 'if' kullandık.
                // Eğer kayıt varsa içeri girer.
                if (rs.next()) {
                    foundBook = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("year")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Kitap bulma hatası: " + e.getMessage());
        }
        // Eğer bulunduysa kitabı, bulunamadıysa null döndürür.
        return foundBook;
    }
}