package com.increff.employee.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderPojo {
    @Id
    @SequenceGenerator(name = "sequ", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(generator = "sequ")
    private Integer id;
    private String time;

}
