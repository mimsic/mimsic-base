package com.github.mimsic.base.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.mimsic.base.common.json.deserializer.IsoTimestampDeserializer;
import com.github.mimsic.base.common.json.serializer.IsoTimestampSerializer;
import com.github.mimsic.base.persistence.converter.ZonedDateTimeAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "user_login_id", columnList = "login_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"login_id"}),
        }
)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "login_id", nullable = false)
    @QuerySqlField(index = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "roles", nullable = false)
    private String[] roles;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonDeserialize(using = IsoTimestampDeserializer.class)
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime created;

    @Column(name = "last_update")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonDeserialize(using = IsoTimestampDeserializer.class)
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime lastUpdate;
}
