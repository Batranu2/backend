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

import java.util.ArrayList;
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

    public List<ClothesEntity> getWeather(String city) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> weatherEntity = restTemplate.getForObject("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + apiKey, Map.class);
        Double feels_like = (Double) ((Map<String, Object>) weatherEntity.get("main")).get("feels_like");
        Random rand = new Random();
        List<ClothesEntity> lista= new ArrayList<>();
        if(feels_like > 17) {
            List<ClothesEntity> haina = getClothesByType(ClothesType.TRICOU);
            List<ClothesEntity> haina1 = getClothesByType(ClothesType.PANTALONI_SCURTI);
            List<ClothesEntity> haina2 = getClothesByType(ClothesType.ADIDASI);
            if(!haina.isEmpty() || !haina1.isEmpty() || !haina2.isEmpty()){
                int index = rand.nextInt(haina.size());
                int index1 = rand.nextInt(haina1.size());
                int index2 = rand.nextInt(haina2.size());
                ClothesEntity clothesEntity = haina.get(index);
                ClothesEntity clothesEntity1 = haina1.get(index1);
                ClothesEntity clothesEntity2 = haina2.get(index2);
                lista.add(clothesEntity);
                lista.add(clothesEntity1);
                lista.add(clothesEntity2);
                return lista;
            }
            else {
                ResponseEntity.notFound();
            }
        }
        else {
            List<ClothesEntity> haina = getClothesByType(ClothesType.BLUZA);
            List<ClothesEntity> haina1 = getClothesByType(ClothesType.PANTALONI_LUNGI);
            List<ClothesEntity> haina2 = getClothesByType(ClothesType.ADIDASI);
            if(!haina.isEmpty()) {
                int index = rand.nextInt(haina.size());
                int index1 = rand.nextInt(haina1.size());
                int index2 = rand.nextInt(haina2.size());
                ClothesEntity clothesEntity = haina.get(index);
                ClothesEntity clothesEntity1 = haina1.get(index1);
                ClothesEntity clothesEntity2 = haina2.get(index2);
                lista.add(clothesEntity);
                lista.add(clothesEntity1);
                lista.add(clothesEntity2);
                return lista;
            }
            else {
                ResponseEntity.notFound();
            }
        }
        return lista;
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
