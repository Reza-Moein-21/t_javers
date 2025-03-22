package org.example.event_field;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Amount {
    private BigDecimal value;
    private String ccy;

    @Override
    public String toString() {
        return value.toString() + " " + ccy;
    }

}
