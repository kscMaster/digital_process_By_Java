package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "encoder")
@ApiModel(value = "encoder 编码器 的Entity")
public class EncoderEntity extends AdminEntity{

    @Column(name = "prefix", length = 64)
    private String prefix;

    @Column(name = "length", length = 1)
    private Integer length;

    @Column(name = "max_serial_number", length = 10)
    private Long maxSerialNumber;

    @Column(name = "start_serial", length = 10)
    private Long startSerial;

    @Column(name = "end_serial", length = 10)
    private Long endSerial;
}
