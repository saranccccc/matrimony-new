package com.matrimony.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_id_sequence ")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileIdSequenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
