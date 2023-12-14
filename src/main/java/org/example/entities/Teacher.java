package org.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Teacher", schema = "languageCourseDatabase")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacherId", nullable = false)
    private Integer id;

    @Column(name = "teacherName", length = 50)
    private String teacherName;

    @Column(name = "teacherEmail", length = 50)
    private String teacherEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherCourseId")
    private LanguageCourse teacherCourse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public LanguageCourse getTeacherCourse() {
        return teacherCourse;
    }

    public void setTeacherCourse(LanguageCourse teacherCourse) {
        this.teacherCourse = teacherCourse;
    }

}