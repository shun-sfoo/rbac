package com.matrix.rbac.model.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "info")
@Data
@ExcelIgnoreUnannotated
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "日期")
    private Date salesDate;

    @ExcelProperty(value = "收货客户")
    private String customer;

    @ExcelProperty(value = "开单员")
    private String operator;

    @ExcelProperty(value = "商品编号")
    private String productCode;

    @ExcelProperty(value = "商品名称")
    private String productName;

    @ExcelProperty(value = "商品规格")
    private String specification;

    @ExcelProperty(value = "商品产地")
    private String productLocation;


    @ExcelProperty(value = "单位")
    private String productUnit;

    @ExcelProperty(value = "供应商名称")
    private String provider;


    @ExcelProperty(value = "批号")
    private String batchNo;

    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "有效期至")
    private Date availableDate;

    @ExcelProperty(value = "数量")
    private Integer num;


    @ExcelProperty(value = "核算成本价")
    private BigDecimal price;
}
