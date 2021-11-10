package com.utn.bolsadetrabajo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "CATEGORY")
@SQLDelete(sql = "UPDATE CATEGORY SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull(message = "El campo Nombre de Categoria es obligatorio")
    @Column
    private String name;

    @NotNull(message = "El campo Descripciòn de Categoria es obligatorio")
    @Column
    private String description;

    @Column
    @CreationTimestamp
    private LocalDate createDay;

    @Column
    @CreationTimestamp
    private LocalDate modifiedDay;

    @Column
    @CreationTimestamp
    private LocalDate deletedDay;

    @Column
    private boolean deleted;
}
