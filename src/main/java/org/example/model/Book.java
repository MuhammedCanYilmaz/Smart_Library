package org.example.model;

// Bu sınıf bir "Kitap" kalıbıdır.
public class Book {
    // Kitabın özellikleri (Veritabanındaki sütunlar)
    // 'private' yaparak dışarıdan doğrudan erişimi engelliyoruz (Kapsülleme).
    private int id;
    private String title;
    private String author;
    private int year;

    // 1. Boş Constructor (Kurucu Metot)
    // Bazen içi boş bir kitap nesnesi oluşturup sonradan doldurmak gerekir.
    public Book() {
    }

    // 2. ID'siz Constructor (Yeni kitap eklerken kullanılır)
    // Yeni kitap eklerken ID'yi biz bilmeyiz, veritabanı otomatik verir.
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // 3. Tam Constructor (Veritabanından okurken kullanılır)
    // Veritabanından okuduğumuzda ID dahil her bilgiyi biliriz.
    public Book(int id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // --- Getter ve Setter Metotları ---
    // private olan özelliklere güvenli bir şekilde erişmek ve değiştirmek için kullanılır.

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    // --- toString Metodu ---
    // Bu nesneyi ekrana yazdırdığımızda (System.out.println(kitap))
    // anlamsız bir hafıza adresi yerine, okunaklı bir metin çıkmasını sağlar.
    @Override
    public String toString() {
        return "Kitap [ID=" + id + ", Başlık='" + title + "', Yazar='" + author + "', Yıl=" + year + "]";
    }
}
