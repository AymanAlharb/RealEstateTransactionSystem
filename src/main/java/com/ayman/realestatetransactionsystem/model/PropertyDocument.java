package com.ayman.realestatetransactionsystem.model;

import com.ayman.realestatetransactionsystem.model.enums.TransectionStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "propertyindex")
public class PropertyDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Double, name = "price")
    private double price;

    @Field(type = FieldType.Text, name = "status")
    private String status;

    @Field(type = FieldType.Text, name = "location")
    private String location;

    @Field(type = FieldType.Text, name = "city")
    private String city;

    @Field(type = FieldType.Text, name = "owner")
    private String ownerName;
}
