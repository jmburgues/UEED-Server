package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Model;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.repository.ModelRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ModelService {

    private static final String MODEL_PATH = "model";
    ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<Model> getAll() {

        List<Model> list = modelRepository.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Model list is empty");
        return list;
    }

    public Model getById(Integer modelId) {

        return modelRepository.findById(modelId).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND, "No models found under id: " + modelId));
    }

    public void delete(Integer modelId) {
        if(!modelRepository.existsById(modelId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No model found under id: " + modelId);
        modelRepository.deleteById(modelId);
    }

    public PostResponse update(Model model) {

        this.getById(model.getId());

        Model saved = modelRepository.save(model);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(MODEL_PATH,saved.getId().toString()))
                .build();
    }
}
