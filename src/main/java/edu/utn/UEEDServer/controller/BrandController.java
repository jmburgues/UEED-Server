package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Brand;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public PostResponse add(@RequestBody Brand brand)
    {
        return brandService.add(brand);
    }

    @GetMapping()
    public List<Brand>getAll()
    {
        return brandService.getAll();
    }

    @GetMapping("/{id}")
    public Brand getById(@PathVariable Integer id)
    {
        return brandService.getById(id);
    }

}
