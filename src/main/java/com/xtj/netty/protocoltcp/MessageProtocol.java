package com.xtj.netty.protocoltcp;

/**
 * 自定义协议包
 * @author 涓
 * @date 2021/2/15
 */
public class MessageProtocol {
    private int len; // 关键
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
