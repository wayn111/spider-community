package com.wayn.spider.community.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.spider.community.core.entity.Community;
import com.wayn.spider.community.core.mapper.CommunityMapper;
import com.wayn.spider.community.core.service.CommunityService;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {
}
