package com.developer.quantscoperagservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.developer.quantscoperagservice.mapper.RagJobEventMapper;
import com.developer.quantscoperagservice.service.RagJobEventService;
import com.developer.quantscopecommon.entity.RagJobEvent;
import org.springframework.stereotype.Service;

/**
 * Database operation Service implementation for table rag_job_event
 */
@Service
public class RagJobEventServiceImpl extends ServiceImpl<RagJobEventMapper, RagJobEvent>
    implements RagJobEventService {

}
