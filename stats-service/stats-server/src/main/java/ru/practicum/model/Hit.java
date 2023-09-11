package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "app_name")
    private String app;
    @Column(name = "user_ip")
    private String ip;
    @Column(name = "created")
    private LocalDateTime timestamp;
    @Column(name = "uri")
    private String uri;
}
