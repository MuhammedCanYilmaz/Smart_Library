package org.example.model;

public class Loan {
    private int id;
    private int bookId;       // Hangi Kitap? (Kitabın ID'si)
    private int studentId;    // Hangi Öğrenci? (Öğrencinin ID'si)
    private String dateBorrowed; // Ödünç Alma Tarihi (Metin olarak: "2023-10-27")
    private String dateReturned; // İade Tarihi (Başlangıçta boş olabilir)

    // Boş Constructor
    public Loan() { }

    // Yeni ödünç verme işlemi oluştururken kullanılır.
    // Dikkat: İade tarihi (dateReturned) henüz olmadığı için onu 'null' yapıyoruz.
    public Loan(int bookId, int studentId, String dateBorrowed) {
        this.bookId = bookId;
        this.studentId = studentId;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = null; // Henüz iade edilmedi
    }

    // Veritabanından okurken kullanılır (Tüm bilgiler bellidir).
    public Loan(int id, int bookId, int studentId, String dateBorrowed, String dateReturned) {
        this.id = id;
        this.bookId = bookId;
        this.studentId = studentId;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
    }

    // --- Getter ve Setter Metotları ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getDateBorrowed() { return dateBorrowed; }
    public void setDateBorrowed(String dateBorrowed) { this.dateBorrowed = dateBorrowed; }

    public String getDateReturned() { return dateReturned; }
    public void setDateReturned(String dateReturned) { this.dateReturned = dateReturned; }

    // --- toString Metodu ---
    @Override
    public String toString() {
        // İade durumuna göre ekrana yazılacak metni belirliyoruz.
        String durumMetni = (dateReturned == null) ? "ÖDÜNÇTE" : "İade Edildi (" + dateReturned + ")";

        return "İşlem [ID=" + id +
                ", KitapID=" + bookId +
                ", ÖğrenciID=" + studentId +
                ", Alış Tarihi='" + dateBorrowed + "'" +
                ", Durum=" + durumMetni + "]";
    }
}