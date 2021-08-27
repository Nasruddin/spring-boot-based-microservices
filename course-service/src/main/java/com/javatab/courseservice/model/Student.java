package com.javatab.courseservice.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student implements Serializable {

    @Id
    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "semster", nullable = false)
    private String semster;

    @Column(name = "join_in", nullable = false)
    private String yearOfJoin;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "student_course",
            joinColumns = { @JoinColumn(name = "username") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") })
    private Set<Course> courses = new HashSet<>();


}
