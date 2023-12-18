package org.example.entities;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "School", schema = "languageCourseDatabase")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schoolId", nullable = false)
    private Integer id;

    @Column(name = "schoolName", length = 50)
    private String schoolName;

    @OneToMany(mappedBy = "courseSchool", fetch = FetchType.LAZY)
    private Set<LanguageCourse> languageCourses = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Set<LanguageCourse> getLanguageCourses() {
        return languageCourses;
    }

    public void setLanguageCourses(Set<LanguageCourse> languageCourses) {
        this.languageCourses = languageCourses;
    }

    public void addCourse(LanguageCourse course) {
        this.languageCourses.add(course);
    }
    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", schoolName='" + schoolName + '\'' +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        School school = (School) o;
        return getId() != null && Objects.equals(getId(), school.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}