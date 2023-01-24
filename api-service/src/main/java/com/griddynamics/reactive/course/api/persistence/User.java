package com.griddynamics.reactive.course.api.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String _id;
    private String name;
    private String phone;
}
