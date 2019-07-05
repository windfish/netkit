package com.netkit;

import com.netkit.exception.ActionException;
import com.netkit.utils.Message;

/**
 * 业务处理过滤器
 * @author xuliang
 * @since 2019年7月4日 下午2:01:12
 *
 */
public interface ActionFilter {

    void doFilter(Action action, Message message, ActionChain chain) throws ActionException;
    
}
