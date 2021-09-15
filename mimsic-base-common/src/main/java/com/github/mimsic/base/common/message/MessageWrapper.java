package com.github.mimsic.base.common.message;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class MessageWrapper<T, U> {

    private T data;
    private U metadata;
    private String msgType;

    public MessageWrapper(String msgType) {
        this.msgType = msgType;
    }

    public MessageWrapper(T data, String msgType) {
        this.data = data;
        this.msgType = msgType;
    }
}
