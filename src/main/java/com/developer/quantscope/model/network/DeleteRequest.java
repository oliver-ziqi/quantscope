package com.developer.quantscope.model.network;

import java.io.Serializable;
import lombok.Data;

/**
 * Delete request
 *
 * @author ziqi
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

// https://www.code-nav.cn/