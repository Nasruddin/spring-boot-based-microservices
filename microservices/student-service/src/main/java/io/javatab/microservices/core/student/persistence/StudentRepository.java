package io.javatab.microservices.core.student.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudentRepository extends PagingAndSortingRepository<StudentEntity, Integer> {

}
