package cn.gdiu.dto;

import lombok.Data;

@Data
public class MessageDTO {

    //来源ID
    private String sender;


    // 目标ID,
    private String receiver;

    // 时间
    private Long time;

    // 消息类型int类型(0:text、1:image、2:voice、3:video、4:music、5:news)
    private Integer messageType;

    //聊天类型int类型(0:未知,1:公聊,2:私聊)
    private Integer chatType;

    //群组id仅在chatType为(1)时需要,String类型
    private String groupId;

    //内容
    private String content;

    //扩展字段,JSON对象格式如：{'扩展字段名称':'扩展字段value'}
    private String extras;
}
