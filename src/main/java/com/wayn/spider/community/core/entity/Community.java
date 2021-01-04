package com.wayn.spider.community.core.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Builder
public class Community {

    @TableId(type = IdType.AUTO)
    private Long communityId;

    private Long communityPid;

    private String communityName;

    private Integer areaId;

    private Integer cityCode;

    private String cityName;

    private BigDecimal communityLongitude;

    private BigDecimal communityLatitude;

    private String communityLetter;

    private Boolean isDelete;
}
