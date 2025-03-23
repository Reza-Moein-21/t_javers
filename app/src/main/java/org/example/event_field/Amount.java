package org.example.event_field;

import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Amount {
    private BigDecimal value;
    private String ccy;

    public String print() {
        if (value == null || ccy == null) return "";
        return value + " " + ccy;
    }

}
