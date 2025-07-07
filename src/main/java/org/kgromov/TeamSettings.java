package org.kgromov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamSettings {
    private Integer id;
    private String name;
    private int boardId;
}