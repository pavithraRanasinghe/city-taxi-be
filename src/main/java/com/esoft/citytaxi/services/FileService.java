package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.FileRequest;
import com.esoft.citytaxi.dto.response.FileResponse;
import com.esoft.citytaxi.enums.ImageCategory;
import com.esoft.citytaxi.exceptions.ExpectationFailedException;
import com.esoft.citytaxi.models.FileDetail;
import com.esoft.citytaxi.repository.FileRepository;
import com.esoft.citytaxi.util.FileUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Value("${app.file.upload.path}")
    private String fileUploadPath;

    @Transactional
    public FileDetail store(FileRequest newFile) throws IOException {

        log.info("Executing file saving service.");
        FileDetail existingFileDetail = null;
        String fullDirectoryName = fileUploadPath + "/"
                + newFile.getFileCategory() + "/";

        if (null != newFile.getUploadedFile()) {
            log.info("File attached to the request. Proceeding with the service.");
            FileDetail fileDetailToSave = new FileDetail(newFile);

            java.io.File directory = new java.io.File(fullDirectoryName);
            if (!directory.exists()) {
                log.info("File uploading directory not present in the server. creating directory.");
                directory.mkdirs();
            }

            fileDetailToSave.setFileType(newFile.getUploadedFile().getContentType());
            String fileName = UUID.randomUUID().toString()
                    + "." + FileUtil.getFileExtension(newFile.getUploadedFile());
            log.info("Rename the file and set name to the unique id." + fileName);
            fileDetailToSave.setName(fileName);
            String fileNameWithPath = fullDirectoryName.concat(Objects.requireNonNull(fileName));
            fileDetailToSave.setPath(fileNameWithPath);

            log.info("Writing files to the server.");
            byte[] bytes = newFile.getUploadedFile().getBytes();
            Path path = Paths.get(fileNameWithPath);
            Files.write(path, bytes);
            log.info("Files written successfully to the server. saving details to database.");
            return fileRepository.save(fileDetailToSave);
        }
        log.error("Request not contained any files. Sending error response.");
        return null;
    }

    public byte[] load(ImageCategory referenceType, long referenceId) throws IOException {
        log.info("Executing file retrieving service. Checking file exists in the server.");
        FileDetail savedFileDetail = fileRepository.findByReferenceIDAndReferenceType(referenceId, referenceType)
                .orElseThrow(()-> new EntityNotFoundException("File not found"));
        if (null != savedFileDetail) {
            log.info("Requesting file exists in the server. proceeding.");
            File existingFileDetail = new File(savedFileDetail.getPath());
            InputStream in = new FileInputStream(existingFileDetail);
            log.info("File retrieved successfully.");
            return StreamUtils.copyToByteArray(in);
        } else {
            log.error("Requested file not exists in the server.");
            return null;
        }
    }

    public List<FileResponse> loadAll(ImageCategory referenceType, long referenceId) throws IOException {
        log.info("Executing file retrieving service. Checking file exists in the server.");
        List<FileDetail> savedFileListDetail = fileRepository.findAllByReferenceIDAndReferenceType(referenceId, referenceType);

        log.info("Checking");
        List<FileResponse> fileList = new ArrayList<>();

        if (!savedFileListDetail.isEmpty()) {
            log.info("Requesting file exists in the server. proceeding.");
            for (FileDetail fileDetail :
                    savedFileListDetail) {
                File imageFileDetail = new File(fileDetail.getPath());
                if (imageFileDetail.exists()) {
                    InputStream in = new FileInputStream(imageFileDetail);
                    log.info("File retrieved successfully.");
                    byte[] bytes = StreamUtils.copyToByteArray(in);
                    fileList.add(new FileResponse(
                            fileDetail.getId(),
                            fileDetail.getDateTime(),
                            bytes
                    ));
                }
            }
            return fileList;

        } else {
            log.error("Requested file not exists in the server.");
            return null;
        }
    }

    public void deleteFile(Long id) throws IOException {
        Optional<FileDetail> file = fileRepository.findById(id);
        if (file.isPresent()) {
            Path path = Paths.get(file.get().getPath());
            Files.delete(path);
        } else {
            log.error("Requested file not exists in the server.");
            throw new ExpectationFailedException();
        }
    }
}
