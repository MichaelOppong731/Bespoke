package com.bespoke.Bespoke.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@Entity
@NoArgsConstructor
@Table(name = "Videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private String filePath;
    private LocalDateTime dateCreated;


    public Video(String title, String description, String filePath){
        this.title = title;
        this.description = description;
        this.filePath = filePath;
    }
    @PrePersist
    private void onClick(){
        this.dateCreated = LocalDateTime.now();
    }
}
