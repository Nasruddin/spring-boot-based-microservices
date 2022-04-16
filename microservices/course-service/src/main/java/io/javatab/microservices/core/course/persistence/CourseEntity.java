package io.javatab.microservices.core.course.persistence;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

@Document(collection = "courses")
public class CourseEntity {

    @Id
    private String id;

    @Version
    private Integer version;

    @Indexed(unique = true)
    private int courseId;

    private String courseName;

    private String author;

    private int voteId;

    private String content;

    public CourseEntity() {
    }

    public CourseEntity(int courseId, String courseName, String author, int voteId, String content) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.author = author;
        this.voteId = voteId;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CourseEntity entity = (CourseEntity) o;

        return new EqualsBuilder().append(courseId, entity.courseId).append(voteId, entity.voteId).append(id, entity.id).append(version, entity.version).append(courseName, entity.courseName).append(author, entity.author).append(content, entity.content).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(version).append(courseId).append(courseName).append(author).append(voteId).append(content).toHashCode();
    }
}
