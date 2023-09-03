package tech.noetzold.service;

import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.Image;
import tech.noetzold.repository.ImageRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@CacheResult(cacheName = "image")
public class ImageService {
    @Inject
    ImageRepository imageRepository;

    public List<Image> findAllImages(int page, int size, String sortBy){
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<Image> query = imageRepository.findAll(sort);
        return query.page(Page.of(page, size)).list();
    }

    public Image findImageById(Long id){
        Optional<Image> optionalImage = imageRepository.findByIdOptional(id);
        return optionalImage.orElseThrow(() -> new WebApplicationException("Image not found", Response.Status.NOT_FOUND));
    }

    @Transactional
    public Image saveImage(Image image){
        imageRepository.persist(image);
        return image;
    }

    @Transactional
    public void deleteImagem(Image image){
        imageRepository.delete(image);
    }
}
