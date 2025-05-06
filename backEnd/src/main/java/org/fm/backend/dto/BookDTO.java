package org.fm.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {
    public int [] bookIDs;
    public String message;
}
