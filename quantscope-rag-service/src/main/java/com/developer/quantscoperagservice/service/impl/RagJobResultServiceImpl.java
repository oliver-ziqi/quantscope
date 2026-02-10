package com.developer.quantscoperagservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.developer.quantscoperagservice.mapper.RagJobResultMapper;
import com.developer.quantscoperagservice.service.RagJobResultService;
import com.developer.quantscopecommon.entity.RagJobResult;
import org.springframework.stereotype.Service;

/**
 * Database operation Service implementation for table rag_job_result
 */
@Service
public class RagJobResultServiceImpl extends ServiceImpl<RagJobResultMapper, RagJobResult>
    implements RagJobResultService {

}
