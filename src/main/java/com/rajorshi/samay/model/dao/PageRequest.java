package com.rajorshi.samay.model.dao;

import com.rajorshi.samay.common.Constants;
import lombok.Getter;

@Getter
public class PageRequest {
    final private int offset;
    final private int limit;

    public PageRequest(int offset, int limit)
    {
        this.offset = offset;
        this.limit = limit;
    }

    public static PageRequest DEFAULT_PAGE()
    {
        return new PageRequest(Constants.OFFSET,Constants.BATCH_SIZE);
    }


}
