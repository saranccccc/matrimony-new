package com.matrimony.user.mapper;

import com.matrimony.user.dto.PartnerPreferenceResponse;
import com.matrimony.user.entity.PartnerPreference;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PartnerPreferenceMapper {
    PartnerPreferenceResponse toResponse(PartnerPreference entity);

}