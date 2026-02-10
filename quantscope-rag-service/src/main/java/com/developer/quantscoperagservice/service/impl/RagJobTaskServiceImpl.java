package com.developer.quantscoperagservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.developer.quantscoperagservice.mapper.RagJobTaskMapper;
import com.developer.quantscoperagservice.service.RagJobTaskService;
import com.developer.quantscopecommon.entity.RagJobTask;
import org.springframework.stereotype.Service;

/**
 * Database operation Service implementation for table rag_job_task
 */
@Service
public class RagJobTaskServiceImpl extends ServiceImpl<RagJobTaskMapper, RagJobTask>
    implements RagJobTaskService {

}
