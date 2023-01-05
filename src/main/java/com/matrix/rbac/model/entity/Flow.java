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
@Table(name = "flow")
@Data
@ExcelIgnoreUnannotated
public class Flow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "日期")
    private Date salesDate;

    @ExcelProperty(value = "往来单位名称")
    private String customer;

    @ExcelProperty(value = "商品名称")
    private String productName;

    @ExcelProperty(value = "商品规格")
    private String specification;

    @ExcelProperty(value = "包装单位")
    private String productUnit;

    @ExcelProperty(value = "生产厂家")
    private String provider;


    @ExcelProperty(value = "数量")
    private Integer num;


    @ExcelProperty(value = "含税价")
    private BigDecimal price;


    @ExcelProperty(value = "批号")
    private String batchNo;


    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    @ExcelProperty(value = "有效期至")
    private Date availableDate;
    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "生产日期")
    private Date productDate;
    @ExcelProperty(value = "开票员")
    private String operator;

    @ExcelProperty(value = "权限名称")
    private String authName;
}
