package org.example.crypto.repo;

import org.example.crypto.model.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepo extends CrudRepository<Course, Long> {

    Course findByCurrency1AndCurrency2(String currency1, String currency2);

    List<Course> findByCurrency1(String currency);
}
