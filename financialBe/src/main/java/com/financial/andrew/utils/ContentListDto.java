package com.financial.andrew.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ContentListDto <T>{

    private List<T> content;
}
