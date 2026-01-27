package com.developer.quantscope.model.network;


import com.developer.quantscope.constant.CommonConstant;
import lombok.Data;

/**
 * Pagination Request
 *
 * @author ziqi
 */
@Data
public class PageRequest {

    /**
     * Current page number
     */
    private long current = 1;

    /**
     * Page size
     */
    private long pageSize = 10;

    /**
     * Sort field
     */
    private String sortField;

    /**
     * Sort order (default ascending)
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
