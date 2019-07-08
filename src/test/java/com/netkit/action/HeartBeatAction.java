package com.netkit.action;

import com.netkit.ServerMessageID;
import com.netkit.exception.ActionException;
import com.netkit.utils.MessageUtil;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午5:39:29
 *
 */
public class HeartBeatAction extends BaseAction {

    @Override
    public void execute() throws ActionException {
        sendMessage(MessageUtil.getMessage(ServerMessageID.HEART_BEAT, null));
    }

}
