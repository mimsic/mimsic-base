package com.github.mimsic.base.persistence.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.mimsic.base.common.json.deserializer.IsoTimestampDeserializer;
import com.github.mimsic.base.common.json.serializer.IsoTimestampSerializer;
import com.github.mimsic.base.persistence.converter.ZonedDateTimeAttributeConverter;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * id       (equals to clientOrderId)
 */

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "orders",
        indexes = {
                @Index(name = "order_account_id", columnList = "account_id"),
                @Index(name = "order_exchange_id", columnList = "exchange_id")
        }
)
public class Order implements Serializable {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "account_id", nullable = false)
    private Long accId;

    @Column(name = "created", nullable = false)
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonDeserialize(using = IsoTimestampDeserializer.class)
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime created;

    @Column(name = "last_update", nullable = false)
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonDeserialize(using = IsoTimestampDeserializer.class)
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime lastUpdate;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode data;
}
