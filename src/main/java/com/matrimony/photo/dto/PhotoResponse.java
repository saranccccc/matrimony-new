package com.matrimony.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoResponse {
    private Long photoId;
    private Boolean isPrimary;
    private Boolean isDeleted;
}
