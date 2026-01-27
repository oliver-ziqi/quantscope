package com.developer.quantscope.model.network;



import java.io.Serializable;
import lombok.Data;

/**
 * Delete request
 *
 * @author ziqi
 */
@Data
public class IdRequest implements Serializable {
    /**
     * ID
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}