package com.rookie.rookiemeeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto implements Serializable {

    //会议室ID
    private Integer roomId;

    //会议室名称
    private String roomName;
}
