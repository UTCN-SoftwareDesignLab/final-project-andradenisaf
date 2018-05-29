package application.entity;

import org.json.simple.JSONObject;
import org.springframework.security.core.GrantedAuthority;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = dateFormat.format(getDate());
        jsonObject.put("id", getId());
        jsonObject.put("user", getUser().toJSON());
        jsonObject.put("date", dateString);
        jsonObject.put("total", getTotal());
        return jsonObject;
    }

}
