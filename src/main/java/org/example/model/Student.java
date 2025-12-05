package org.example.model;

public class Student {
    private int id;
    private String name;       // Öğrencinin Adı Soyadı
    private String department; // Bölümü

    // Boş Constructor
    public Student() { }

    // Yeni öğrenci eklerken (ID'siz)
    public Student(String name, String department) {
        this.name = name;
        this.department = department;
    }

    // Veritabanından okurken (ID'li)
    public Student(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    // --- Getter ve Setter Metotları ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    // --- toString Metodu ---
    @Override
    public String toString() {
        return "Öğrenci [ID=" + id + ", İsim='" + name + "', Bölüm='" + department + "']";
    }
}