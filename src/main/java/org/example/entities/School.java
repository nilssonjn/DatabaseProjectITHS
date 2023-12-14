package org.example.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
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

    @OneToMany(mappedBy = "courseSchool")
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

}