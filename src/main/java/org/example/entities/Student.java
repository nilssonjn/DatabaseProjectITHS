package org.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Student", schema = "languageCourseDatabase")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId", nullable = false)
    private Integer id;

    @Column(name = "studentName", length = 50)
    private String studentName;

    @Column(name = "studentEmail", length = 50)
    private String studentEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentCourseId")
    private LanguageCourse studentCourse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public LanguageCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(LanguageCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

}