package org.example.entities;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "Teacher name = " + teacherName + ", " +
                "Email = " + teacherEmail + ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Teacher teacher = (Teacher) o;
        return getId() != null && Objects.equals(getId(), teacher.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}