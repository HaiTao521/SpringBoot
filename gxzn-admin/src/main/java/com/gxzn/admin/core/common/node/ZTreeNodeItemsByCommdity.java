package com.gxzn.admin.core.common.node;

import java.util.List;


import lombok.Data;

@Data
public class ZTreeNodeItemsByCommdity {
    /**
     * 节点id
     */
    private String id;

    /**
     * 节点value
     */
    private String value;

    /**
     * 父节点id
     */
    private String pId;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 是否打开节点
     */
    private Boolean open;

    /**
     * 是否被选中
     */
    private Boolean checked = false;

    private List<ZTreeNodeItemsByCommdity> children = null;
}
