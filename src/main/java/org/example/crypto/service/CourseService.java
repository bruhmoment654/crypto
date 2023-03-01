package org.example.crypto.service;


import lombok.AllArgsConstructor;
import org.example.crypto.dto.CourseDto;
import org.example.crypto.exception.CourseException;
import org.example.crypto.model.Course;
import org.example.crypto.repo.CourseRepo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CourseService {

    private CourseRepo courseRepo;

    public Course findCourse(String currency1, String currency2) {
        return courseRepo.findByCurrency1AndCurrency2(currency1, currency2);
    }

    public Double convertCurrency(String from, String to, Double sum) throws CourseException {
        if (from.equals(to)) {
            return sum;
        }
        Double course = courseRepo.findByCurrency1AndCurrency2(from, to).getCourse();
        if (course == null) {
            throw new CourseException("course not found");
        }
        return sum / course;
    }

    public Map<String, String> setCourse(String currency, Map<String, String> converts) {
        for (String currency2 : converts.keySet()) {
            Course curr1ToCurr2 = courseRepo.findByCurrency1AndCurrency2(currency, currency2);
            curr1ToCurr2 = curr1ToCurr2 == null ? new Course(currency, currency2) : curr1ToCurr2;
            curr1ToCurr2.setCourse(Double.parseDouble(converts.get(currency2)));

            Course curr2ToCurr1 = courseRepo.findByCurrency1AndCurrency2(currency2, currency);
            curr2ToCurr1 = curr2ToCurr1 == null ? new Course(currency2, currency) : curr2ToCurr1;
            curr2ToCurr1.setCourse(1 / Double.parseDouble(converts.get(currency2)));

            courseRepo.save(curr1ToCurr2);
            courseRepo.save(curr2ToCurr1);
        }
        return converts;
    }


    public Map<String, String> getCourse(CourseDto courseDto) throws CourseException {

        List<Course> courses = courseRepo.findByCurrency1(courseDto.getCurrency());
        if (courses.size() == 0) {
            throw new CourseException("currency not found");
        }
        Map<String, String> course = new HashMap<>();
        for (Course currency : courses) {
            course.put(currency.getCurrency2(), String.valueOf(currency.getCourse()));
        }

        return course;
    }


}
