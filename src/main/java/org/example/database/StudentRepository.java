package org.example.database;

import org.example.model.Student; // Öğrenci modelini import et
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    // --- 1. Öğrenci Ekleme Metodu ---
    public void add(Student student) {
        String sql = "INSERT INTO students(name, department) VALUES(?,?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());

            pstmt.executeUpdate();
            System.out.println("Öğrenci başarıyla eklendi: " + student.getName());

        } catch (SQLException e) {
            System.err.println("Öğrenci ekleme hatası: " + e.getMessage());
        }
    }

    // --- 2. Tüm Öğrencileri Listeleme Metodu ---
    public List<Student> getAll() {
        List<Student> studentsList = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Sütun isimlerinin veritabanındakiyle aynı olduğuna emin olmalıyız.
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department")
                );
                studentsList.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Öğrencileri listeleme hatası: " + e.getMessage());
        }
        return studentsList;
    }

    // --- 3. ID ile Öğrenci Bulma Metodu ---
    // Ödünç verirken "Böyle bir öğrenci var mı?" diye kontrol etmek için.
    public Student getById(int idToFind) {
        String sql = "SELECT * FROM students WHERE id = ?";
        Student foundStudent = null;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idToFind);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    foundStudent = new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("department")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Öğrenci bulma hatası: " + e.getMessage());
        }
        return foundStudent;
    }
}