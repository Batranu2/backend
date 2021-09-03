package flore.cristi.project.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import flore.cristi.project.model.entity.ClothesEntity;
import flore.cristi.project.model.entity.ClothesType;
import flore.cristi.project.model.entity.Stats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Stats getHotOrCold(String uid, String city) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        ClothesEntity clothesEntity = getClothByUid(uid);
        Map<String, Object> weatherEntity = restTemplate.getForObject("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + apiKey, Map.class);
        Double feels_like = (Double) ((Map<String, Object>) weatherEntity.get("main")).get("feels_like");

        if (clothesEntity.getTip_haina() == ClothesType.TRICOU && feels_like > 15) {
            return new Stats("GOOD");
        } else if (clothesEntity.getTip_haina() == ClothesType.TRICOU && feels_like < 15) {
            return new Stats("COLD");
        }

        if (clothesEntity.getTip_haina() == ClothesType.FUSTA && feels_like > 22) {
            return new Stats("GOOD");
        } else if (clothesEntity.getTip_haina() == ClothesType.FUSTA && feels_like < 22)
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.PANTALONI_LUNGI && feels_like > 20) {
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.PANTALONI_LUNGI && feels_like < 20)
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.PANTALONI_SCURTI && feels_like > 20) {
            return new Stats("GOOD");
        } else if (clothesEntity.getTip_haina() == ClothesType.PANTALONI_SCURTI && feels_like < 20)
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.BLUZA && feels_like > 15) {
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.BLUZA && feels_like < 15)
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.GEACA && feels_like > 15) {
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.GEACA && feels_like < 15)
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.ADIDASI && feels_like > 7) {
            return new Stats("GOOD");
        } else if (clothesEntity.getTip_haina() == ClothesType.ADIDASI && feels_like < 7)
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.HANORAC && feels_like > 18) {
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.HANORAC && feels_like < 18 && feels_like > 10)
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.HANORAC && feels_like < 10)
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.CARDIGAN && feels_like > 20) {
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.CARDIGAN && feels_like < 20 && feels_like > 13)
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.CARDIGAN && feels_like < 13)
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.GHETE && feels_like > 5) {
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.GHETE && feels_like < 5)
            return new Stats("GOOD");

        return new Stats("Default");
    }

    public ClothesEntity getRandomClothByType(ClothesType type, Random rand) {
        List<ClothesEntity> haina = new ArrayList<>();
        try {
            haina = getClothesByType(type);
        } catch (InterruptedException ignored) {
            ignored.printStackTrace();
        }

        if (!haina.isEmpty()) {
            return haina.get(rand.nextInt(haina.size()));
        }
        return null;
    }

    public ResponseEntity getClothesByWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> weatherEntity = restTemplate.getForObject("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + apiKey, Map.class);
        Double feels_like = (Double) ((Map<String, Object>) weatherEntity.get("main")).get("feels_like");
        Random rand = new Random();
        List<ClothesEntity> lista = new ArrayList<>();

        if (feels_like > 20) {
            ClothesType[] types = {ClothesType.TRICOU, ClothesType.PANTALONI_SCURTI, ClothesType.ADIDASI};
            lista = Stream.of(types).map((ClothesType type) -> getRandomClothByType(type, rand)).collect(Collectors.toList());

        }

        if (feels_like < 20 && feels_like > 15) {
            ClothesType[] types = {ClothesType.TRICOU, ClothesType.CARDIGAN, ClothesType.PANTALONI_SCURTI, ClothesType.ADIDASI};
            lista = Stream.of(types).map((ClothesType type) -> getRandomClothByType(type, rand)).collect(Collectors.toList());
        }

        if (feels_like < 15 && feels_like > 10) {
            ClothesType[] types = {ClothesType.TRICOU, ClothesType.HANORAC, ClothesType.PANTALONI_LUNGI, ClothesType.ADIDASI};
            lista = Stream.of(types).map((ClothesType type) -> getRandomClothByType(type, rand)).collect(Collectors.toList());
        }

        if (feels_like < 10 && feels_like > 7) {
            ClothesType[] types = {ClothesType.TRICOU, ClothesType.GEACA, ClothesType.PANTALONI_LUNGI, ClothesType.ADIDASI};
            lista = Stream.of(types).map((ClothesType type) -> getRandomClothByType(type, rand)).collect(Collectors.toList());
        }

        if (feels_like < 7) {
            ClothesType[] types = {ClothesType.GEACA, ClothesType.BLUZA, ClothesType.PANTALONI_LUNGI, ClothesType.GHETE};
            lista = Stream.of(types).map((ClothesType type) -> getRandomClothByType(type, rand)).collect(Collectors.toList());
        }

        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lista);
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
