package flore.cristi.project.model.entity;

public class ClothesEntity {

    private String uid;
    private Season anotimp;
    private Material material;
    private ClothesType tip_haina;
    private String color;

    public ClothesEntity() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Season getAnotimp() {
        return anotimp;
    }

    public void setAnotimp(Season anotimp) {
        this.anotimp = anotimp;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ClothesType getTip_haina() {
        return tip_haina;
    }

    public void setTip_haina(ClothesType tip_haina) {
        this.tip_haina = tip_haina;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ClothesEntity{" +
                "uid='" + uid + '\'' +
                ", anotimp=" + anotimp +
                ", material=" + material +
                ", tip_haina=" + tip_haina +
                ", color='" + color + '\'' +
                '}';
    }
}
