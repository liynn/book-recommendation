package com.wy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wy on 2017/3/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String bookId;
    private String bookName;
    private String bookImg;
    private String bookTag;
}
