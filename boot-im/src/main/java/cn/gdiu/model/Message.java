package cn.gdiu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_message")
@Data
public class Message {

    @Id
    @GenericGenerator(name = "snowflakeId", strategy = "cn.gdiu.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "snowflakeId")
    private Long id;

    // 接收入
    private Long receiver;

    //发送人
    private Long sender;

    private Long time;


    private Integer messageType;

    private Integer chatType;

    private Long groupId;


    private String content;


    private String extras;

}
