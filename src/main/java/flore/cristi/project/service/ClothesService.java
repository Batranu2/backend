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
            //"Acest " + clothesEntity.getTip_haina().toString().toLowerCase() + " este potrivit pentru temperatura mediului exterior!";
            return new Stats("GOOD");
        } else if (clothesEntity.getTip_haina() == ClothesType.TRICOU && feels_like < 15) {
            //String mesaj1 = "Acest " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu este potrivit pentru temperatura mediului exterior!";
            return new Stats("COLD");
        }
        if (clothesEntity.getTip_haina() == ClothesType.FUSTA && feels_like > 22) {
            // "Această " + clothesEntity.getTip_haina().toString().toLowerCase() + " este potrivită pentru temperatura mediului exterior!";
            return new Stats("GOOD");
        } else if (clothesEntity.getTip_haina() == ClothesType.FUSTA && feels_like < 22)
            // "Această " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu este potrivită pentru temperatura mediului exterior!";
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.PANTALONI_LUNGI && feels_like > 20) {
            // "Acești " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu sunt potriviți pentru temperatura mediului exterior!";
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.PANTALONI_LUNGI && feels_like < 20)
            // "Acești " + clothesEntity.getTip_haina().toString().toLowerCase() + " sunt potriviți pentru temperatura mediului exterior!";
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.PANTALONI_SCURTI && feels_like > 20) {
            // "Acești " + clothesEntity.getTip_haina().toString().toLowerCase() + " sunt potriviți pentru temperatura mediului exterior!";
            return new Stats("GOOD");
        } else if (clothesEntity.getTip_haina() == ClothesType.PANTALONI_SCURTI && feels_like < 20)
            // "Acești " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu sunt potriviți pentru temperatura mediului exterior!";
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.BLUZA && feels_like > 15) {
            // "Această " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu este potrivită pentru temperatura mediului exterior!";
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.BLUZA && feels_like < 15)
            // "Această " + clothesEntity.getTip_haina().toString().toLowerCase() + " este potrivită pentru temperatura mediului exterior!";
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.GEACA && feels_like > 15) {
            //return "Această " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu este potrivită pentru temperatura mediului exterior!";
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.GEACA && feels_like < 15)
            //return "Această " + clothesEntity.getTip_haina().toString().toLowerCase() + " este potrivită pentru temperatura mediului exterior!";
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.ADIDASI && feels_like > 7) {
            //return "Acești " + clothesEntity.getTip_haina().toString().toLowerCase() + " sunt potriviți pentru temperatura mediului exterior!";
            return new Stats("GOOD");
        } else if (clothesEntity.getTip_haina() == ClothesType.ADIDASI && feels_like < 7)
            //return "Acești " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu sunt potriviți pentru temperatura mediului exterior!";
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.HANORAC && feels_like > 18) {
            //return "Acest " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu este potrivit pentru temperatura mediului exterior!";
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.HANORAC && feels_like < 18 && feels_like > 10)
            //return "Acest " + clothesEntity.getTip_haina().toString().toLowerCase() + " este potrivit pentru temperatura mediului exterior!";
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.HANORAC && feels_like < 10)
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.CARDIGAN && feels_like > 20) {
            //return "Acest " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu este potrivit pentru temperatura mediului exterior!";
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.CARDIGAN && feels_like < 20 && feels_like > 13)
            //return "Acest " + clothesEntity.getTip_haina().toString().toLowerCase() + " este potrivit pentru temperatura mediului exterior!";
            return new Stats("GOOD");

        if (clothesEntity.getTip_haina() == ClothesType.CARDIGAN && feels_like < 13)
            return new Stats("COLD");

        if (clothesEntity.getTip_haina() == ClothesType.GHETE && feels_like > 5) {
            //return "Acest " + clothesEntity.getTip_haina().toString().toLowerCase() + " nu sunt potrivite pentru temperatura mediului exterior!";
            return new Stats("HOT");
        } else if (clothesEntity.getTip_haina() == ClothesType.GHETE && feels_like < 5)
            //return "Aceste " + clothesEntity.getTip_haina().toString().toLowerCase() + " sunt potrivite pentru temperatura mediului exterior!";
            return new Stats("GOOD");

        return new Stats("Default");
    }

    public List<ClothesEntity> getWeather(String city) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> weatherEntity = restTemplate.getForObject("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + apiKey, Map.class);
        Double feels_like = (Double) ((Map<String, Object>) weatherEntity.get("main")).get("feels_like");
        Random rand = new Random();
        List<ClothesEntity> lista = new ArrayList<>();
        if (feels_like > 17) {
            List<ClothesEntity> haina = getClothesByType(ClothesType.TRICOU);
            List<ClothesEntity> haina1 = getClothesByType(ClothesType.PANTALONI_SCURTI);
            List<ClothesEntity> haina2 = getClothesByType(ClothesType.ADIDASI);
            if (!haina.isEmpty() || !haina1.isEmpty() || !haina2.isEmpty()) {
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
            } else {
                ResponseEntity.notFound();
            }
        } else {
            List<ClothesEntity> haina = getClothesByType(ClothesType.BLUZA);
            List<ClothesEntity> haina1 = getClothesByType(ClothesType.PANTALONI_LUNGI);
            List<ClothesEntity> haina2 = getClothesByType(ClothesType.ADIDASI);
            if (!haina.isEmpty()) {
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
            } else {
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
