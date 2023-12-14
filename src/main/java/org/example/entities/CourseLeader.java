package org.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "courseLeader", schema = "languageCourseDatabase")
public class CourseLeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseLeaderId", nullable = false)
    private Integer id;

    @Column(name = "courseLeaderName", length = 50)
    private String courseLeaderName;

    @Column(name = "courseLeaderEmail", length = 50)
    private String courseLeaderEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseLeaderCourseId")
    private LanguageCourse courseLeaderCourse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseLeaderName() {
        return courseLeaderName;
    }

    public void setCourseLeaderName(String courseLeaderName) {
        this.courseLeaderName = courseLeaderName;
    }

    public String getCourseLeaderEmail() {
        return courseLeaderEmail;
    }

    public void setCourseLeaderEmail(String courseLeaderEmail) {
        this.courseLeaderEmail = courseLeaderEmail;
    }

    public LanguageCourse getCourseLeaderCourse() {
        return courseLeaderCourse;
    }

    public void setCourseLeaderCourse(LanguageCourse courseLeaderCourse) {
        this.courseLeaderCourse = courseLeaderCourse;
    }

}