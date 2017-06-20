package uk.ac.ebi.eva.commons.mongodb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.eva.commons.core.models.VariantSource;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;
import uk.ac.ebi.eva.commons.mongodb.repositories.VariantSourceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VariantSourceService {

    @Autowired
    private VariantSourceRepository repository;

    public List<VariantSource> findAll() {
        return convert(repository.findAll());
    }

    private List<VariantSource> convert(List<VariantSourceMongo> variantSources) {
        return variantSources.stream().map(VariantSource::new).collect(Collectors.toList());
    }

    public List<VariantSource> findByStudyIdOrStudyName(String studyId, String studyName) {
        return convert(repository.findByStudyIdOrStudyName(studyId, studyName));
    }

    public List<VariantSource> findByStudyIdIn(List<String> studyIds, Pageable pageable) {
        return convert(repository.findByStudyIdIn(studyIds, pageable));
    }

    public long countByStudyIdIn(List<String> studyIds) {
        return repository.countByStudyIdIn(studyIds);
    }

    public List<VariantSource> findByFileIdIn(List<String> fileIds, Pageable pageable) {
        return convert(repository.findByFileIdIn(fileIds, pageable));
    }

    public long countByFileIdIn(List<String> fileIds) {
        return repository.countByFileIdIn(fileIds);
    }

}
