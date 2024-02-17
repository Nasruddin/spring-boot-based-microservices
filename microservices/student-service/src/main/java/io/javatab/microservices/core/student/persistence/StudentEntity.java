package io.javatab.microservices.core.student.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@Entity
@Table(name = "student")
public class StudentEntity {

    @Id
    @GeneratedValue
    private int id;

    private int studentId;
    private String studentName;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StudentEntity() {
    }

    public StudentEntity(int studentId, String studentName, String email) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StudentEntity that = (StudentEntity) o;

        return new EqualsBuilder().append(studentId, that.studentId).append(studentName, that.studentName).append(email, that.email).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(studentId).append(studentName).append(email).toHashCode();
    }
}
