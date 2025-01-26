package com.example.animal_feed.item;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String type;
    @NonNull
    private String code;
    @NonNull
    @Builder.Default
    private String img = "";
    @Builder.Default
    private int weight = 0;
    private int price;
    @NonNull
    @Builder.Default
    private String state = "Available";
    @NonNull
    @Builder.Default
    private String description = "";
}
