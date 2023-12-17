package org.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Examination", schema = "languageCourseDatabase")
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examinationId", nullable = false)
    private Integer id;

    @Column(name = "examinationName", length = 50)
    private String examinationName;

    @Column(name = "examinationMinScore")
    private Integer examinationMinScore;

    @Column(name = "examinationMaxScore")
    private Integer examinationMaxScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examinationCourseId")
    private LanguageCourse examinationCourse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExaminationName() {
        return examinationName;
    }

    public void setExaminationName(String examinationName) {
        this.examinationName = examinationName;
    }

    public Integer getExaminationMinScore() {
        return examinationMinScore;
    }

    public void setExaminationMinScore(Integer examinationMinScore) {
        this.examinationMinScore = examinationMinScore;
    }

    public Integer getExaminationMaxScore() {
        return examinationMaxScore;
    }

    public void setExaminationMaxScore(Integer examinationMaxScore) {
        this.examinationMaxScore = examinationMaxScore;
    }

    public LanguageCourse getExaminationCourse() {
        return examinationCourse;
    }

    public void setExaminationCourse(LanguageCourse examinationCourse) {
        this.examinationCourse = examinationCourse;
    }
    @Override
    public String toString() {
        return "ID: "+ id+" Examination: "+examinationName+" Min score: "+examinationMinScore + " Max score: "+examinationMaxScore;
    }

}