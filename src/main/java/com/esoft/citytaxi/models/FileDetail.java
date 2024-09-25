package com.esoft.citytaxi.models;

import com.esoft.citytaxi.dto.request.FileRequest;
import com.esoft.citytaxi.enums.ImageCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileDetail extends Audit implements Serializable {

    public FileDetail(FileRequest fileRequest) {
        this.referenceID = fileRequest.getReferenceId();
        this.referenceType = fileRequest.getReferenceType();
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(name = "file_type")
    private String fileType;

    private String path;

    @Column(name = "reference_type")
    @Enumerated(EnumType.STRING)
    private ImageCategory referenceType;

    @Column(name = "reference_id")
    private Long referenceID;

    @Column(name = "date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateTime;

}
