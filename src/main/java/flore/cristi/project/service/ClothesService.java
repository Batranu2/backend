package flore.cristi.project.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import flore.cristi.project.model.entity.ClothesEntity;
import flore.cristi.project.model.entity.ClothesType;
import flore.cristi.project.model.entity.WeatherEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ClothesService {

    private final GenericRepo repo;
    final String CLOTHES_TABLE = "Clothes";
    @Value("${api.apiKey}")
    private String apiKey;

    public ClothesService(GenericRepo repo) {
        this.repo = repo;
    }

    public void saveCloth(ClothesEntity clothesEntity) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(CLOTHES_TABLE);
        database.child(clothesEntity.getUid()).setValueAsync(clothesEntity);
    }

    public ClothesEntity getClothByUid(String uid) throws InterruptedException {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(CLOTHES_TABLE);
        return repo.getEntityByUid(uid, database, ClothesEntity.class);
    }

    public WeatherEntity getWeather(String city) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> weatherEntity = restTemplate.getForObject("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + apiKey, Map.class);
       // String main = (String) ((Map<String, Object>) weatherEntity.get("weather")).get(1);
        //String description = (String)((Map<String , Object>) weatherEntity.get("weather")).get("description");
        Integer clouds = (Integer)((Map<Long , Object>) weatherEntity.get("clouds")).get("all");
        Double temperature = (Double) ((Map<String, Object>) weatherEntity.get("main")).get("temp");
        Double feels_like = (Double) ((Map<String, Object>) weatherEntity.get("main")).get("feels_like");
        Random rand = new Random();
        if(feels_like > 28) {
            List<ClothesEntity> haina = getClothesByType(ClothesType.TRICOU);
            if(!haina.isEmpty()){
                int index = rand.nextInt(haina.size());
                ClothesEntity clothesEntity = haina.get(index);
                System.out.println(clothesEntity);
                System.out.println("tricou");
            }
            else {
                ResponseEntity.notFound();
            }
        }
        else {
            List<ClothesEntity> haina = getClothesByType(ClothesType.BLUZA);
            if(!haina.isEmpty()) {
                int index = rand.nextInt(haina.size());
                ClothesEntity clothesEntity = haina.get(index);
                System.out.println(clothesEntity);
                System.out.println("bluza");
            }
            else {
                ResponseEntity.notFound();
            }
        }
        return new WeatherEntity(null, null, clouds, temperature, feels_like);
    }

    public List<ClothesEntity> getAllClothes() throws InterruptedException {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(CLOTHES_TABLE);
        return repo.getAllEntities(database, ClothesEntity.class);
    }

    public List<ClothesEntity> getClothesByType(ClothesType clothesType) throws InterruptedException {
        return getAllClothes().stream()
                .filter(clothesEntity -> clothesEntity != null && clothesEntity.getTip_haina() != null)
                .filter(clothesEntity -> clothesEntity.getTip_haina().equals(clothesType))
                .collect(Collectors.toList());
    }

    public void deleteCloth(String uid) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(CLOTHES_TABLE).child(uid);
        database.removeValue(null);
    }
}
