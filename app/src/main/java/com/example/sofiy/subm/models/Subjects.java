package com.example.sofiy.subm.models;

public class Subjects {
    private String subject_name;
    private String no_of_experiments;
    private String no_of_assignments;

    public Subjects(){

    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getNo_of_experiments() {
        return no_of_experiments;
    }

    public void setNo_of_experiments(String no_of_experiments) {
        this.no_of_experiments = no_of_experiments;
    }

    public String getNo_of_assignments() {
        return no_of_assignments;
    }

    public void setNo_of_assignments(String no_of_assignments) {
        this.no_of_assignments = no_of_assignments;
    }

    public Subjects(String subject_name, String no_of_experiments, String no_of_assignments) {
        this.subject_name = subject_name;
        this.no_of_experiments = no_of_experiments;
        this.no_of_assignments = no_of_assignments;
    }
}
