package org.example.entities;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "Grade", schema = "languageCourseDatabaseTEST")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gradeId", nullable = false)
    private Integer id;

    @Column(name = "gradeValue", length = 50)
    private String gradeValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeStudentId")
    private Student gradeStudent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeTeacherId")
    private Teacher gradeTeacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeCourseId")
    private LanguageCourse gradeCourse;

    public LanguageCourse getGradeCourse() {
        return gradeCourse;
    }

    public void setGradeCourse(LanguageCourse gradeCourse) {
        this.gradeCourse = gradeCourse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGradeValue() {
        return this.gradeValue;
    }

    public void setGradeValue(String gradeValue) {
        this.gradeValue = gradeValue;
    }

    public Student getGradeStudent() {
        return gradeStudent;
    }

    public void setGradeStudent(Student gradeStudent) {
        this.gradeStudent = gradeStudent;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", gradeValue='" + gradeValue + '\'' +
                ", gradeStudent=" + gradeStudent +
                ", gradeTeacher=" + gradeTeacher +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Grade grade = (Grade) o;
        return getId() != null && Objects.equals(getId(), grade.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}