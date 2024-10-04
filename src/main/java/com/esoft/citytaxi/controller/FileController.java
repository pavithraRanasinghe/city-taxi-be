package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.FileRequest;
import com.esoft.citytaxi.dto.response.BaseResponse;
import com.esoft.citytaxi.dto.response.FileResponse;
import com.esoft.citytaxi.enums.ImageCategory;
import com.esoft.citytaxi.models.FileDetail;
import com.esoft.citytaxi.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


@RestController
@Slf4j
@RequestMapping(value = "v1/files")
public class FileController {

    private final FileService fileService;

    /**
     * Instantiates a new File controller.
     *
     * @param fileService the storage service
     */
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Gets image with media type.
     *
     * @param referenceType the reference type
     * @param referenceId   the reference id
     * @return the image with media type
     * @throws IOException the io exception
     */
    @GetMapping(
            value = "/images/get/{referenceType}/{referenceId}",
            produces = MediaType.IMAGE_JPEG_VALUE) 
    public ResponseEntity<byte[]> getImageWithMediaType(@PathVariable ImageCategory referenceType, @PathVariable long referenceId) throws IOException {
        log.info("File requested from the front end.");
        byte[] fileStream = fileService.load(referenceType, referenceId);
        if (null != fileStream) {
            log.info("File request executed successfully.");
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileStream);
        } else {
            log.error("File request execution failed.");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    /**
     * Handle file upload response entity.
     *
     * @param fileRequest the new file
     * @return the response entity
     * @throws IOException the io exception
     */
    @PostMapping("/images/upload") 
    public ResponseEntity<?> handleFileUpload(FileRequest fileRequest) throws IOException {
        log.info("Incoming file receiving to upload.");
        if (null != fileRequest.getUploadedFile()) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileRequest.getUploadedFile().getBytes());
            if (ImageIO.read(byteArrayInputStream) != null) {
                fileRequest.setReferenceId(fileRequest.getReferenceId());
                FileDetail savedFile = fileService.store(fileRequest);
                if (savedFile != null) {
                    log.info("File successfully uploaded to the server.");
                    return ResponseEntity.ok(new BaseResponse("success"));
                } else {
                    log.error("File uploading request failed.");
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
                }
            } else {
                log.error("Uploaded file is not an Image.");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        } else {
            log.error("Image file not found in the request.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * Gets image list with media type.
     *
     * @param referenceType the reference type
     * @param referenceId   the reference id
     * @return the image list with media type
     * @throws IOException the io exception
     */
    @GetMapping(value = "/images/get-all/{referenceType}/{referenceId}") 
    public ResponseEntity<?> getImageAllWithMediaType(@PathVariable ImageCategory referenceType, @PathVariable long referenceId) throws IOException {
        log.info("File requested from the front end.");
        List<FileResponse> fileStreamList = fileService.loadAll(referenceType, referenceId);
        if (fileStreamList != null) {
            log.info("File list request executed successfully.");
            return ResponseEntity.ok().body(fileStreamList);
        } else {
            log.error("File list request execution failed.");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    @GetMapping(
            value = "/pdf/get/{referenceType}/{referenceId}",
            produces = MediaType.APPLICATION_PDF_VALUE) 
    public ResponseEntity<byte[]> getPDFWithMediaType(@PathVariable ImageCategory referenceType, @PathVariable long referenceId) throws IOException {
        log.info("File requested from the front end.");
        byte[] file = fileService.load(referenceType, referenceId);
        if (file != null) {
            log.info("File list request executed successfully.");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(file);
        } else {
            log.error("File list request execution failed.");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .build();
        }
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<?> deleteFile(@PathVariable Long id) throws IOException {
        fileService.deleteFile(id);
        log.info("File delete successfully.");
        return ResponseEntity.ok(new BaseResponse("success"));
    }
}
