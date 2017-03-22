package com.wy.vo;

import com.wy.domain.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wy on 2017/3/15.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookVO {
    private Book book;
    private boolean isSuccess;

    public static BookVO ok(Book book){
        return new BookVO(book,true);
    }

    public static BookVO fail(){
        return new BookVO(new Book(),false);
    }
}
