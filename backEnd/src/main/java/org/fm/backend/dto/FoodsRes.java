package org.fm.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fm.backend.model.Foods;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodsRes {
    private List<Foods> foodsInfo;
}
