package info.stepanoff.trsis.samples.model;

import javax.persistence.*;

@Entity
@Table(name = "car", uniqueConstraints = @UniqueConstraint(columnNames = "id")) // Добавлена уникальность для столбца id
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int mileage;

    @Column(nullable = false)
    private int price;

    // Конструктор без параметров (обязателен для JPA)
    public Car() {}

    // Конструктор с параметрами
    public Car(String make, String model, int mileage, int price) {
        this.make = make;
        this.model = model;
        this.mileage = mileage;
        this.price = price;
    }

    // Getters и Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Переопределение метода toString для корректного отображения
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", mileage=" + mileage +
                ", price=" + price +
                '}';
    }
}
