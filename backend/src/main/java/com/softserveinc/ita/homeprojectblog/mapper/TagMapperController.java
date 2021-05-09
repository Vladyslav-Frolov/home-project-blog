package com.softserveinc.ita.homeprojectblog.mapper;

import com.softserveinc.ita.homeprojectblog.dto.TagDto;
import com.softserveinc.ita.homeprojectblog.model.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapperController {

    Tag toTag(TagDto tagDto);
}
