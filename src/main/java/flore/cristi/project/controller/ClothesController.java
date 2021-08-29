package flore.cristi.project.controller;

import flore.cristi.project.model.entity.ClothesEntity;
import flore.cristi.project.model.entity.ClothesType;
import flore.cristi.project.model.entity.WeatherEntity;
import flore.cristi.project.service.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ClothesController {

    private final ClothesService clothesService;

    @Autowired
    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @GetMapping("/weather/{city}")
    @CrossOrigin
    public ResponseEntity getApi(@PathVariable("city") String city) throws InterruptedException {
        return ResponseEntity.ok(clothesService.getWeather(city));
    }

    @PutMapping("/save-clothes")
    @CrossOrigin
    public void saveCloth(@RequestBody ClothesEntity clothesEntity) {
        clothesService.saveCloth(clothesEntity);
    }

    @GetMapping("/clothes/{uid}")
    @CrossOrigin
    public ClothesEntity getClothes(@PathVariable("uid") String uid) throws InterruptedException {
        return clothesService.getClothByUid(uid);
    }

    @GetMapping("/clothes-type/{tip}")
    @CrossOrigin
    public List<ClothesEntity> getClothesByType(@PathVariable("tip") String type) throws InterruptedException {
        return clothesService.getClothesByType(ClothesType.valueOf(type));
    }

    @GetMapping("/clothes")
    @CrossOrigin
    public List<ClothesEntity> getAllClothes() throws InterruptedException {
        return clothesService.getAllClothes();
    }

    @DeleteMapping("/clothes/remove/{uid}")
    @CrossOrigin
    public void removeClothes(@PathVariable("uid") String uid) {
        clothesService.deleteCloth(uid);
    }
}
