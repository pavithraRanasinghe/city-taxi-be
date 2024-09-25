package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.enums.ImageCategory;
import com.esoft.citytaxi.models.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileDetail, Long> {

    Optional<FileDetail> findByReferenceIDAndReferenceType(Long referenceId, ImageCategory imageCategory);
    List<FileDetail> findAllByReferenceIDAndReferenceType(Long referenceId, ImageCategory imageCategory);
}
