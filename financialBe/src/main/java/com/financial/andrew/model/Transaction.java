package com.financial.andrew.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.financial.andrew.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profit_and_lose_id")
    @JsonBackReference
    private ProfitLose profitLose;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transaction transaction = (Transaction) o;
        return id != null && Objects.equals(id, transaction.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
