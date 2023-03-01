package org.example.crypto.controller;


import lombok.AllArgsConstructor;
import org.example.crypto.dto.CourseDto;
import org.example.crypto.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/currency")
public class CourseController {

    private CourseService courseService;

    @PostMapping(value = "/getCourse", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> getCourse(@RequestBody CourseDto courseDto) {
        try {
            return ResponseEntity.ok(courseService.getCourse(courseDto));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
