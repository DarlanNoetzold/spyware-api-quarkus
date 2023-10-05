package tech.noetzold.service;

import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import tech.noetzold.model.Image;
import tech.noetzold.repository.ImageRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ImageService {
    @Inject
    ImageRepository imageRepository;

    @CacheResult(cacheName = "listimage")
    @Transactional
    public List<Image> findAllImages(int page, int size, String sortBy){
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<Image> query = imageRepository.findAll(sort);

        int offset = (page - 1) * size;
        return query.range(offset, (size-1)*page).list();
    }
    @CacheResult(cacheName = "image")
    @Transactional
    public Image findImageById(Long id){
        Optional<Image> optionalImage = imageRepository.findByIdOptional(id);
        return optionalImage.orElse(null);
    }

    @Transactional
    public Image saveImage(Image image){
        imageRepository.persist(image);
        return image;
    }

    @Transactional
    public void deleteImage(Long id){
        imageRepository.deleteById(id);
    }
}
