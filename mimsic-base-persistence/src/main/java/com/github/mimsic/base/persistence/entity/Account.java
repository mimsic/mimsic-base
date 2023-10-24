package com.github.mimsic.base.persistence.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.mimsic.base.common.json.deserializer.IsoTimestampDeserializer;
import com.github.mimsic.base.common.json.serializer.IsoTimestampSerializer;
import com.github.mimsic.base.persistence.converter.ZonedDateTimeAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@Table(name = "accounts",
        indexes = {
                @Index(name = "account_agent_id", columnList = "agent_id"),
                @Index(name = "account_branch_id", columnList = "branch_id"),
                @Index(name = "account_group_id", columnList = "group_id"),
                @Index(name = "account_user_id", columnList = "user_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"client_code", "branch_id", "source_code"})
        }
)
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "account_type")
    private Long accTypeId;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "branch_id")
    private Long brhId;

    @Column(name = "group_id")
    private Long grpId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "client_code")
    private String cliCode;

    @Column(name = "client_name")
    private String cliName;

    @Column(name = "cds_no")
    private String cdsNo;

    @Column(name = "email")
    private String email;

    @Column(name = "id_no")
    private String idNo;

    @Column(name = "id_no_2")
    private String idNo2;

    @Column(name = "id_type")
    private String idType;

    @Column(name = "source_code")
    private String srcCode;

    @Column(name = "status")
    private String status;

    @Column(name = "associable", nullable = false)
    private boolean associable;

    @Column(name = "buy_suspend", nullable = false)
    private boolean buySuspend;

    @Column(name = "sell_suspend", nullable = false)
    private boolean sellSuspend;

    @Column(name = "create_date")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonDeserialize(using = IsoTimestampDeserializer.class)
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime createDate;

    @Column(name = "last_submit")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonDeserialize(using = IsoTimestampDeserializer.class)
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime lastSubmit;

    @Column(name = "last_update")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonDeserialize(using = IsoTimestampDeserializer.class)
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime lastUpdate;
}
