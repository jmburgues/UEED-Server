package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Model;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/model")
public class ModelController {

    ModelService modelService;

    @Autowired
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping()
    public PostResponse add (@RequestBody Model model){

       return modelService.add(model);
    }

    @GetMapping()
    public List<Model> getAll(){

        return modelService.getAll();
    }

    @GetMapping
    public Model getById(@PathVariable Integer id)
    {
        return modelService.getById(id);
    }
}
