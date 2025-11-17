package com.eq.mds.entity;

import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import com.eq.mds.entity.proxy.SysUserProxy;
import lombok.Data;

/**
 * create time 2025/11/17 16:21
 * 文件说明
 *
 * @author xuejiaming
 */
@Data
@Table("t_user")
@EntityProxy
public class SysUser implements ProxyEntityAvailable<SysUser , SysUserProxy> {
    @Column(primaryKey = true)
    private String id;

    private String name;
}
