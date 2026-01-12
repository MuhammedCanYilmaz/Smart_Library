# SmartLibrary - Akıllı Kütüphane Yönetim Sistemi

Bu proje, bir üniversite senaryosu için geliştirilmiş, Java tabanlı, masaüstü konsol (CLI) kütüphane yönetim uygulamasıdır.

Proje, Nesne Yönelimli Programlama (OOP) prensiplerine uygun olarak tasarlanmış olup, veri kalıcılığı için yerel bir SQLite veritabanı ve Java Database Connectivity (JDBC) teknolojilerini kullanmaktadır.

---

## İçindekiler
- [Proje Hakkında](#proje-hakkında)
- [Özellikler](#özellikler)
- [Kullanılan Teknolojiler](#kullanılan-teknolojiler)
- [Kurulum ve Çalıştırma](#kurulum-ve-çalıştırma)
- [Veritabanı Yapısı](#veritabanı-yapısı)
- [Proje Yapısı](#proje-yapısı)

---

## Proje Hakkında

**SmartLibrary**, kütüphane envanterini ve ödünç alma süreçlerini dijitalleştirmek amacıyla geliştirilmiş bir otomasyon sistemidir. Sistem yöneticisi (kullanıcı), konsol menüsü üzerinden kitap ekleyebilir, öğrenci kaydedebilir ve kitap ödünç verme/iade alma işlemlerini gerçekleştirebilir.

Tüm veriler, uygulama dizininde oluşturulan yerel bir SQLite veritabanı dosyasında (`smartlibrary.db`) saklanır, bu sayede uygulama kapatılıp açıldığında veri kaybı yaşanmaz.

---

## Özellikler

Proje aşağıdaki temel fonksiyonları içermektedir:

* **Kitap Yönetimi:**
    * Sisteme yeni kitap ekleme (Başlık, Yazar ve Basım Yılı bilgileri ile).
    * Veritabanındaki kayıtlı tüm kitapları listeleme.
* **Öğrenci Yönetimi:**
    * Sisteme yeni öğrenci kaydetme (Ad Soyad ve Bölüm bilgileri ile).
    * Kayıtlı tüm öğrencileri listeleme.
* **Ödünç ve İade İşlemleri:**
    * Kayıtlı bir öğrenciye kitap ödünç verme.
    * **Müsaitlik Kontrolü:** Sistem, bir kitap başkasına ödünç verilmişse bunu tespit eder ve mükerrer ödünç işlemini engeller.
    * Ödünçteki bir kitabın iade işlemini gerçekleştirme (İade tarihi güncellenir).
    * Geçmiş ve aktif tüm ödünç işlemlerini durumlarıyla birlikte (Ödünçte/İade Edildi) listeleme.

---

## Kullanılan Teknolojiler

Projenin geliştirilmesinde kullanılan temel teknolojiler ve araçlar şunlardır:

* **Programlama Dili:** Java (JDK 17 veya üzeri önerilir)
* **Veritabanı:** SQLite (Gömülü veritabanı sistemi)
* **Veritabanı Bağlantısı:** JDBC (Java Database Connectivity)
* **Bağımlılık Yönetimi:** Maven
* **Kullanılan Kütüphane:** `sqlite-jdbc` (org.xerial)
* **Geliştirme Ortamı (IDE):** IntelliJ IDEA

---

## Kurulum ve Çalıştırma

Bu projeyi yerel makinenizde çalıştırmak için aşağıdaki adımları izleyin:

### Ön Gereksinimler
* Bilgisayarınızda **Java Development Kit (JDK)** yüklü olmalıdır.
* **Git** yüklü olmalıdır.

### Adımlar

1.  **Projeyi Klonlayın:**
    Terminalinizi açın ve aşağıdaki komutu çalıştırarak projeyi bilgisayarınıza indirin:
    ```bash
    git clone [https://github.com/KULLANICI_ADINIZ/SmartLibrary.git](https://github.com/KULLANICI_ADINIZ/SmartLibrary.git)
    ```
    *(Not: Linkteki kullanıcı adını kendi GitHub kullanıcı adınızla değiştirin)*

2.  **IDE ile Açın:**
    İndirdiğiniz `SmartLibrary` klasörünü kullandığınız Java IDE'si (Örn: IntelliJ IDEA) ile açın.

3.  **Maven Bağımlılıklarını Yükleyin:**
    Proje Maven tabanlıdır. IDE'niz genellikle açılışta `pom.xml` dosyasını algılar ve gerekli SQLite sürücüsünü otomatik olarak indirir. İndirmezse, IDE'nizin Maven panelinden projeyi yenileyin (Reload Project).

4.  **Uygulamayı Başlatın:**
    `src/main/java/org/example/main/Main.java` dosyasını açın ve içerisindeki `main` metodunu çalıştırın.

> **Not:** Uygulama ilk kez çalıştırıldığında, projenin ana dizininde `smartlibrary.db` adında bir veritabanı dosyası ve gerekli tablolar otomatik olarak oluşturulacaktır.

---

## Veritabanı Yapısı

Proje, ilişkisel bir veritabanı yapısına sahiptir ve 3 ana tablodan oluşmaktadır:

1.  **`books`**: Kitap bilgilerini tutar.
    * `id` (INTEGER, PRIMARY KEY, Auto Increment)
    * `title` (TEXT)
    * `author` (TEXT)
    * `year` (INTEGER)

2.  **`students`**: Öğrenci bilgilerini tutar.
    * `id` (INTEGER, PRIMARY KEY, Auto Increment)
    * `name` (TEXT)
    * `department` (TEXT)

3.  **`loans`**: Ödünç alma işlemlerini ve kitap-öğrenci ilişkilerini tutar.
    * `id` (INTEGER, PRIMARY KEY, Auto Increment)
    * `bookId` (INTEGER, FOREIGN KEY -> books.id)
    * `studentId` (INTEGER, FOREIGN KEY -> students.id)
    * `dateBorrowed` (TEXT)
    * `dateReturned` (TEXT, Null olabilir)

---

**Hazırlayan:** [Muhammed Can Yilmaz]
**Numara:** [20230108032]
**Ders:** Nesneye Dayalı Programlama 1
