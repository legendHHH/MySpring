package com.legend.entity;

/**
 * @author Create By legend
 * @date 2019/3/26 10:47
 */
public class Person {
    private Teacher teacher;
    private Student student;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
