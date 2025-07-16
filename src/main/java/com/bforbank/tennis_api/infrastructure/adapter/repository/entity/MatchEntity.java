package com.bforbank.tennis_api.infrastructure.adapter.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "matches")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity {

    @Id
    private String id;

    private String playerAName;
    private int playerAPoints;
    private boolean playerAAdvantage;

    private String playerBName;
    private int playerBPoints;
    private boolean playerBAdvantage;

    private String winner;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Europe/Paris"));

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MatchEntity that = (MatchEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
