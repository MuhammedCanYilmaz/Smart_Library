package org.example.database;

import org.example.model.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {

    // --- 1. Ödünç Verme Metodu (Ekleme ve Kontrol) ---
    public void add(Loan loan) {
        // KURAL: Kitap şu anda başkasında mı?
        // Önce aşağıdaki yardımcı metodu çağırıp kontrol ediyoruz.
        if (isBookCurrentlyBorrowed(loan.getBookId())) {
            System.out.println("HATA: Bu kitap şu anda başkasına ödünç verilmiş durumda! İşlem iptal edildi.");
            // Metodu burada bitiriyoruz, ekleme yapmıyoruz.
            return;
        }

        // Kuraldan geçtiyse, ekleme işlemini yap.
        String sql = "INSERT INTO loans(bookId, studentId, dateBorrowed) VALUES(?,?,?)";
        // Not: dateReturned kısmına bir şey yazmıyoruz, veritabanı otomatik NULL yapacak.

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loan.getBookId());
            pstmt.setInt(2, loan.getStudentId());
            pstmt.setString(3, loan.getDateBorrowed());

            pstmt.executeUpdate();
            System.out.println("Kitap başarıyla ödünç verildi.");

        } catch (SQLException e) {
            System.err.println("Ödünç verme hatası: " + e.getMessage());
        }
    }

    // --- 2. Tüm İşlemleri Listeleme Metodu ---
    public List<Loan> getAll() {
        List<Loan> loansList = new ArrayList<>();
        String sql = "SELECT * FROM loans";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Veritabanından okurken dateReturned NULL gelebilir, bu normaldir.
                Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getInt("bookId"),
                        rs.getInt("studentId"),
                        rs.getString("dateBorrowed"),
                        rs.getString("dateReturned") // İade edilmemişse burası null gelir
                );
                loansList.add(loan);
            }
        } catch (SQLException e) {
            System.err.println("Ödünç listesi hatası: " + e.getMessage());
        }
        return loansList;
    }

    // --- 3. Kitap İade Alma Metodu (GÜNCELLEME - UPDATE) ---
    public void returnBook(int loanId, String dateReturned) {
        // Mantık: ID'si verilen işlemin iade tarihini güncelle.
        // KURAL: Sadece henüz iade edilmemiş (dateReturned IS NULL) olanları güncelle.
        // Bu sayede aynı işlemi iki kere iade almaya çalışırsak ikincisi başarısız olur.
        String sql = "UPDATE loans SET dateReturned = ? WHERE id = ? AND dateReturned IS NULL";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dateReturned); // 1. soru işareti: İade tarihi
            pstmt.setInt(2, loanId);         // 2. soru işareti: İşlem ID'si

            // executeUpdate metodu, bu işlemden kaç satırın etkilendiğini (değiştiğini) döndürür.
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Kitap başarıyla iade alındı. İşlem ID: " + loanId);
            } else {
                System.out.println("HATA: İade işlemi başarısız. ID yanlış olabilir veya kitap zaten iade edilmiş.");
            }

        } catch (SQLException e) {
            System.err.println("İade alma hatası: " + e.getMessage());
        }
    }

    // --- YARDIMCI METOT (Private) ---
    // Bu metot sadece bu sınıfın içinde kullanılır (add metodu tarafından).
    // Verilen kitap ID'sinin şu anda ödünçte olup olmadığını kontrol eder.
    private boolean isBookCurrentlyBorrowed(int bookId) {
        // Sorgu: Bu kitap ID'sine sahip VE iade tarihi NULL olan kaç kayıt var?
        String sql = "SELECT COUNT(*) FROM loans WHERE bookId = ? AND dateReturned IS NULL";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // İlk sütundaki sayıyı al (COUNT sonucu)
                    return count > 0; // Eğer sayı 0'dan büyükse, evet, ödünçtedir (true döner).
                }
            }
        } catch (SQLException e) {
            System.err.println("Kontrol hatası: " + e.getMessage());
        }
        return false; // Bir hata olursa veya kayıt yoksa ödünçte değil sayalım.
    }
}