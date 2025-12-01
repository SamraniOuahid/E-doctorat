package com.example.demo.candidat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/candidat/")
public class CandidatController {

    @Autowired
    private CandidatService candidatService;

    @GetMapping
    public List<CandidatModel> getAll() {
        return candidatService.getAll();
    }

    @GetMapping("/{id}")
    public CandidatModel getById(@PathVariable int id) {
        return candidatService.getById(id);
    }
    @PostMapping
    public CandidatModel create(@RequestBody CandidatModel candidat) {
        return candidatService.create(candidat);

    }
    @PutMapping("/{id}")
    public CandidatModel update(@PathVariable int id,  @RequestBody CandidatModel candidat) {
        return candidatService.update(id, candidat);
    }


//    public void delete(@PathVariable int id) {
//        candidatService.deleteById(id);
//
//    }
    @DeleteMapping("/{id}")
    public List<CandidatModel> delete(@PathVariable int id) {
        return candidatService.deleteById(id);
    }




}
