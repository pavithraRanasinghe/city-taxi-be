package com.esoft.citytaxi.dto;

import lombok.Data;

import java.util.Map;

@Data
public class EmailMapper {
    private String to;
    private String subject;
    private String templateName;
    private Map<String, Object> model;
}
