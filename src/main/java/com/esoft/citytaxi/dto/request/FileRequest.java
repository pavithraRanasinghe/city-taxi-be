package com.esoft.citytaxi.dto.request;

import com.esoft.citytaxi.enums.ImageCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRequest {
    private long referenceId;
    private ImageCategory referenceType;
    private MultipartFile uploadedFile;
    private String fileCategory;
    private String dateTime;
}
