package me.study.studyjpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import me.study.studyjpashop.domain.Category;
import me.study.studyjpashop.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비지니스 로직을 entity에 넣으면 응집도가 올라간다.
    // setter 를 사용하지 않고 stockquantity에 대한 로직을 entity에 구현하여 가장 객체지향적 코드가 된다.

    /**
     * stock 증가
     * @param quantity
     */
    public void addStockQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     * @param quantity
     */
    public void removeStockQuantity(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
