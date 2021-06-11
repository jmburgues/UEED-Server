package edu.utn.UEEDServer.utils;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

public class Response{

    public static ResponseEntity response(Page page){

        return ResponseEntity.status(page.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .header("X-Total-Count",Integer.toString(page.getTotalPages()))
                .header("X-Total-Elements",Long.toString(page.getTotalElements()))
                .body(page.getContent());
    }
    public static ResponseEntity response(List list){

        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)

                .body(list);
    }
    
    public static ResponseEntity response(Object element){
        return ResponseEntity.status(HttpStatus.OK).body(element);
    }
}
