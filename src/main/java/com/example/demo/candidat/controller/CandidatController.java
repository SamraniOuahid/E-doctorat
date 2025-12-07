package com.example.demo.candidat.controller;

import com.example.demo.candidat.CandidatService;
import com.example.demo.candidat.model.Candidat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidat/")
public class CandidatController {

    @Autowired
    private CandidatService candidatService;

    @GetMapping
    public List<Candidat> getAll() {
        return candidatService.getAll();
    }

    @GetMapping("/{id}")
    public Candidat getById(@PathVariable int id) {
        return candidatService.getById(id);
    }
    @PostMapping
    public Candidat create(@RequestBody Candidat candidat) {
        return candidatService.create(candidat);

    }
    @PutMapping("/{id}")
    public Candidat update(@PathVariable int id, @RequestBody Candidat candidat) {
        return candidatService.update(id, candidat);
    }


//    public void delete(@PathVariable int id) {
//        candidatService.deleteById(id);
//
//    }
    @DeleteMapping("/{id}")
    public List<Candidat> delete(@PathVariable int id) {
        return candidatService.deleteById(id);
    }




}
