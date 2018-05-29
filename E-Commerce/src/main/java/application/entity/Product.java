package application.entity;

import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "company", nullable = false)
    private String company;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "image", nullable = false)
    private String image;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() { return company; }

    public void setCompany(String company) {
        this.company = company;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type typeId) {
        this.type = typeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getId());
        jsonObject.put("name", getName());
        jsonObject.put("description", getDescription());
        jsonObject.put("company", getCompany());
        jsonObject.put("type", getType().toJSON());
        jsonObject.put("quantity", getQuantity());
        jsonObject.put("price", getPrice());
        jsonObject.put("image", getImage());
        return jsonObject;
    }
}


