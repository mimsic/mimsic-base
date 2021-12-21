package com.github.mimsic.base.persistence.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.mimsic.base.common.json.serializer.IsoTimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
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
@Table(name = "user_details",
        indexes = {
                @Index(name = "user_detail_user_id", columnList = "user_id")
        }
)
public class UserDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "title")
    private String title;

    @Column(name = "gender")
    private String gender;

    @Column(name = "add_1")
    private String address_1;

    @Column(name = "add_2")
    private String address_2;

    @Column(name = "add_3")
    private String address_3;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "mobile_no")
    private String mobileNumber;

    @Column(name = "phone_no")
    private String phoneNumber;

    @Column(name = "office_no")
    private String officeNumber;

    @Column(name = "created")
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime created;

    @Column(name = "birth_date")
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime birthDate;

    @Column(name = "last_update")
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime lastUpdate;

    @Column(name = "reg_date")
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime registrationDate;
}
