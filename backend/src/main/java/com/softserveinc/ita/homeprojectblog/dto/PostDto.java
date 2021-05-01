package com.softserveinc.ita.homeprojectblog.dto;

import com.softserveinc.ita.homeprojectblog.generated.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private BigDecimal id; //+

    private List<Tag> tags; //+/TODO

    private OffsetDateTime createdOn; //+

    private Object user; //+

    private String text; //+/TODO

    private String title; //+/TODO

    private String previewAttachment; //+/TODO

    private OffsetDateTime updatedOn; //+

}