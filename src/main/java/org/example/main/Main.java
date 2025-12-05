package org.example.main;

// Diğer paketlerdeki sınıflarımızı kullanmak için içeri aktarıyoruz (Import)
import org.example.database.BookRepository;
import org.example.database.DatabaseManager;
import org.example.database.LoanRepository;
import org.example.database.StudentRepository;
import org.example.model.Book;
import org.example.model.Loan;
import org.example.model.Student;

import java.util.List;
import java.util.Scanner;

public class Main {

    // --- KOMPOZİSYON (Sınıfın Üyeleri) ---
    // Ana sınıfımız, işleri yapmak için bu "Usta" sınıfların örneklerine sahip.
    // 'static' yaptık çünkü main metodu da static ve onlara doğrudan erişmesi gerekiyor.
    private static final BookRepository bookRepo = new BookRepository();
    private static final StudentRepository studentRepo = new StudentRepository();
    private static final LoanRepository loanRepo = new LoanRepository();

    // Kullanıcıdan klavye girdisi almak için Scanner (static yapıyoruz ki her yerden erişelim)
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Sistem başlatılıyor...");
        // 1. ADIM: Veritabanı tablolarını hazırla (Yoksa oluşturur)
        DatabaseManager.initializeDatabase();

        System.out.println("\n=== SmartLibrary Kütüphane Sistemine Hoş Geldiniz ===");

        boolean running = true; // Döngüyü kontrol eden değişken
        while (running) {
            // Menüyü ekrana yazdır
            printMenu();

            // Kullanıcıdan seçim al (Güvenli bir şekilde sayı alma metodu kullanıyoruz)
            int choice = getIntInput("Seçiminiz: ");

            // Seçime göre ilgili işlemi yap
            switch (choice) {
                case 1:
                    uiAddBook(); // Kitap Ekleme Ekranı
                    break;
                case 2:
                    uiListBooks(); // Kitap Listeleme Ekranı
                    break;
                case 3:
                    uiAddStudent(); // Öğrenci Ekleme Ekranı
                    break;
                case 4:
                    uiListStudents(); // Öğrenci Listeleme Ekranı
                    break;
                case 5:
                    uiLendBook(); // Ödünç Verme Ekranı
                    break;
                case 6:
                    uiListLoans(); // Ödünçleri Listeleme Ekranı
                    break;
                case 7:
                    uiReturnBook(); // İade Alma Ekranı
                    break;
                case 0:
                    running = false; // Döngüyü bitir
                    System.out.println("Sistemden çıkılıyor. İyi günler!");
                    break;
                default:
                    System.out.println("Geçersiz bir seçim yaptınız, lütfen menüdeki numaralardan birini girin.");
            }
            System.out.println("--------------------------------------------------");
        }
        // Program biterken scanner'ı kapatmak iyi bir pratiktir.
        scanner.close();
    }

    // --- YARDIMCI METOT: Menüyü Yazdır ---
    private static void printMenu() {
        System.out.println("\n--- ANA MENÜ ---");
        System.out.println("1. Kitap Ekle");
        System.out.println("2. Kitapları Listele");
        System.out.println("3. Öğrenci Ekle");
        System.out.println("4. Öğrencileri Listele");
        System.out.println("5. Kitap Ödünç Ver");
        System.out.println("6. Ödünç Listesini Görüntüle");
        System.out.println("7. Kitap Geri Teslim Al");
        System.out.println("0. Çıkış");
    }

    // --- KULLANICI ARAYÜZÜ (UI) METOTLARI ---
    // Bu metotlar kullanıcı ile konuşur, verileri toplar ve ustalara iletir.

    // 1. Kitap Ekleme Ekranı
    private static void uiAddBook() {
        System.out.println("\n--- Yeni Kitap Ekle ---");
        // getStringInput ve getIntInput yardımcı metotlarını kullanıyoruz.
        String title = getStringInput("Kitap Başlığı: ");
        String author = getStringInput("Yazar: ");
        int year = getIntInput("Basım Yılı: ");

        // Verilerle yeni bir Kitap nesnesi oluştur
        Book newBook = new Book(title, author, year);
        // Kitap ustasına ver, veritabanına eklesin
        bookRepo.add(newBook);
    }

    // 2. Kitap Listeleme Ekranı
    private static void uiListBooks() {
        System.out.println("\n--- Kitap Listesi ---");
        // Ustadan tüm kitapları iste
        List<Book> books = bookRepo.getAll();
        if (books.isEmpty()) {
            System.out.println("Kayıtlı kitap yok.");
        } else {
            // For-each döngüsü ile listeyi gez ve yazdır
            for (Book book : books) {
                System.out.println(book); // Book sınıfındaki toString() metodu çalışır
            }
        }
    }

    // 3. Öğrenci Ekleme Ekranı
    private static void uiAddStudent() {
        System.out.println("\n--- Yeni Öğrenci Ekle ---");
        String name = getStringInput("Adı Soyadı: ");
        String department = getStringInput("Bölümü: ");

        Student newStudent = new Student(name, department);
        studentRepo.add(newStudent);
    }

    // 4. Öğrenci Listeleme Ekranı
    private static void uiListStudents() {
        System.out.println("\n--- Öğrenci Listesi ---");
        List<Student> students = studentRepo.getAll();
        if (students.isEmpty()) {
            System.out.println("Kayıtlı öğrenci yok.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }

    // 5. Kitap Ödünç Verme Ekranı
    private static void uiLendBook() {
        System.out.println("\n--- Kitap Ödünç Ver ---");

        // Kullanıcıya kolaylık olsun diye önce listeleri gösterelim
        System.out.println(">> Öğrenci Listesi:");
        uiListStudents();
        int studentId = getIntInput("\nÖğrenci ID'sini girin: ");

        // Validasyon (Doğrulama): Böyle bir öğrenci var mı?
        if (studentRepo.getById(studentId) == null) {
            System.out.println("Hata: Bu ID ile bir öğrenci bulunamadı.");
            return; // Metottan çık
        }

        System.out.println("\n>> Kitap Listesi:");
        uiListBooks();
        int bookId = getIntInput("\nVerilecek Kitap ID'sini girin: ");

        // Validasyon: Böyle bir kitap var mı?
        if (bookRepo.getById(bookId) == null) {
            System.out.println("Hata: Bu ID ile bir kitap bulunamadı.");
            return;
        }

        String date = getStringInput("Ödünç Tarihi (YYYY-MM-DD formatında): ");

        // Yeni bir ödünç nesnesi oluştur
        Loan newLoan = new Loan(bookId, studentId, date);
        // Ustaya ver (Kitabın müsaitlik kontrolünü usta kendi içinde yapacak)
        loanRepo.add(newLoan);
    }

    // 6. Ödünç Listeleme Ekranı
    private static void uiListLoans() {
        System.out.println("\n--- Ödünç Listesi ---");
        List<Loan> loans = loanRepo.getAll();
        if (loans.isEmpty()) {
            System.out.println("Kayıtlı işlem yok.");
        } else {
            for (Loan loan : loans) {
                // Daha şık bir çıktı için ID'leri kullanarak isimleri buluyoruz
                Student s = studentRepo.getById(loan.getStudentId());
                Book b = bookRepo.getById(loan.getBookId());

                // Eğer öğrenci veya kitap silinmişse "Bilinmiyor" yazalım
                String studentName = (s != null) ? s.getName() : "Bilinmiyor (Silinmiş)";
                String bookTitle = (b != null) ? b.getTitle() : "Bilinmiyor (Silinmiş)";

                // Durum bilgisini hazırla
                String status = (loan.getDateReturned() == null) ? "ÖDÜNÇTE" : "İade Edildi (" + loan.getDateReturned() + ")";

                System.out.println("İşlem ID: " + loan.getId() +
                        " | Öğrenci: " + studentName +
                        " | Kitap: " + bookTitle +
                        " | Tarih: " + loan.getDateBorrowed() +
                        " | Durum: " + status);
            }
        }
    }

    // 7. Kitap İade Alma Ekranı
    private static void uiReturnBook() {
        System.out.println("\n--- Kitap İade Al ---");
        uiListLoans(); // İşlemleri göster ki ID seçebilsin
        int loanId = getIntInput("\nİade edilecek İşlem ID'sini (Loan ID) girin: ");
        String date = getStringInput("İade Tarihi (YYYY-MM-DD): ");

        // Ustaya güncelleme emri ver
        loanRepo.returnBook(loanId, date);
    }

    // --- YARDIMCI METOTLAR (Girdi Alma) ---
    // Scanner'ın nextInt() sonrası nextLine() sorununu aşmak ve kod tekrarını önlemek için.

    // Kullanıcıdan metin (String) almak için
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Kullanıcıdan güvenli bir şekilde sayı (int) almak için
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        // Kullanıcı sayı girmeyip harf girerse döngüye girer
        while (!scanner.hasNextInt()) {
            System.out.println("Hata: Lütfen geçerli bir sayı giriniz.");
            System.out.print(prompt);
            scanner.next(); // Geçersiz girdiyi temizle
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // ÖNEMLİ: Satır sonu karakterini (Enter) temizle
        return input;
    }
}