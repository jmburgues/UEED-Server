package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Model;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.repository.ModelRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class ModelService {

    private static final String MODEL_PATH = "model";
    ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public PostResponse add(Model model) {

        Model m =modelRepository.save(model);

        return PostResponse.builder().status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(MODEL_PATH,m.getId()))
                .build();
    }

    public List<Model> getAll() {

        return modelRepository.findAll();
    }

    public Model getById(Integer id) {

        return modelRepository.findById(id).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }
}
