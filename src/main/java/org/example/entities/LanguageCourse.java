package org.example.entities;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Member;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "LanguageCourse", schema = "languageCourseDatabase")
public class LanguageCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseId", nullable = false)
    private Integer id;

    @Column(name = "courseName", length = 50)
    private String courseName;

    @Column(name = "courseStartDate", length = 50)
    private String courseStartDate;

    @Column(name = "courseEndDate", length = 50)
    private String courseEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseSchoolId")
    private School courseSchool;

    @OneToMany(mappedBy = "examinationCourse")
    private Set<Examination> examinations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "studentCourse")
    private Set<Student> students = new LinkedHashSet<>();

    @OneToMany(mappedBy = "teacherCourse")
    private Set<Teacher> teachers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "courseLeaderCourse")
    private Set<CourseLeader> courseLeaders = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public School getCourseSchool() {
        return courseSchool;
    }

    public void setCourseSchool(School courseSchool) {
        this.courseSchool = courseSchool;
    }

    public Set<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<CourseLeader> getCourseLeaders() {
        return courseLeaders;
    }

    public void setCourseLeaders(Set<CourseLeader> courseLeaders) {
        this.courseLeaders = courseLeaders;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        LanguageCourse that = (LanguageCourse) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ID: "+ id+" Course: "+courseName+" Start date: "+courseStartDate+" End date: "+courseEndDate;
    }

    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
    }
    public void removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
    }
    public void addCourseLeader(CourseLeader courseLeader) {
        this.courseLeaders.add(courseLeader);
    }
    public void removeCourseLeader(CourseLeader courseLeader) {
        this.courseLeaders.remove(courseLeader);
    }
    public void addStudent(Student student) {
        this.students.add(student);
    }
    public void removeStudent(Student student) {
        this.students.remove(student);
    }
    public void addExamination(Examination examination) {
        this.examinations.add(examination);
    }
    public void removeExamination(Examination examination) {
        this.examinations.remove(examination);
    }
}